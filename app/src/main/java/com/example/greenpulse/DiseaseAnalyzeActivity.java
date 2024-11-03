package com.example.greenpulse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.greenpulse.databinding.ActivityDiseaseAnalyzeBinding;
import com.example.greenpulse.models.DiseasedCrop;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiseaseAnalyzeActivity extends AppCompatActivity {

    ActivityDiseaseAnalyzeBinding binding;
    private final int REQUEST_STORAGE_PERMISSION = 1000;
    private final int REQUEST_IMAGE_PICK = 2000;
    DiseasedCrop diseasedCrop;
    Dialog dialog;
    String resultText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiseaseAnalyzeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Remove the local declaration and initialize the instance variable instead
        diseasedCrop = new DiseasedCrop();  // Initialize the instance variable

        binding.imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission();
            }
        });

        binding.promptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.promptBtn.setVisibility(View.INVISIBLE);
                setTheDialog();
                diseasedCrop.setWeather("60 degree F, pretty cold");
                diseasedCrop.setLocation("Columbia,MO 63501");
            }
        });

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGemini();
            }
        });
    }


    private void askPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
        else{
            callImagePicker();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callImagePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                binding.imagePicker.setImageURI(selectedImageUri);
            }
        }
    }

    private void setTheDialog() {
        dialog = new Dialog(DiseaseAnalyzeActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_up_prompt,null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        //initializing the three spinners
        Spinner cropTypeSp = dialog.findViewById(R.id.crop_type_spinner);
        Spinner growthStageSp = dialog.findViewById(R.id.growth_stage_spinner);
        Spinner affectedAreaSp = dialog.findViewById(R.id.affected_area_spinner);
        EditText cropNameET = dialog.findViewById(R.id.crop_name);


        //cropName spinner
        String cropName = cropNameET.getText().toString();
        AppCompatButton doneBtn = dialog.findViewById(R.id.done);
        String[] cropTypes = {"Cereal", "Legume", "Vegetable", "Fruit", "OilSeed", "Tube",
                "Root", "Pulse", "Nut", "Spice & Herb", "Beverage Crop"};
        ArrayAdapter<String> ad1 = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cropTypes);
        ad1.setDropDownViewResource(com.google.android.material.
                R.layout.support_simple_spinner_dropdown_item);
        cropTypeSp.setAdapter(ad1);

        //growthStage spinner
        String[] growthStage = {"Seedling","Vegetative","Reproductive"};
        ArrayAdapter<String> ad2 = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, growthStage);
        ad2.setDropDownViewResource(com.google.android.material.
                R.layout.support_simple_spinner_dropdown_item);
        growthStageSp.setAdapter(ad2);


        //affectedAreas Spinner
        String[] affectedAreas = {"Leaf","Root","Stem","Flower","Fruit","Seed","Bark"};

        ArrayAdapter<String> ad3 = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, affectedAreas);

        ad3.setDropDownViewResource(com.google.android.material.
                R.layout.support_simple_spinner_dropdown_item);
        affectedAreaSp.setAdapter(ad3);

        cropTypeSp.setOnItemSelectedListener(sharedListener);
        growthStageSp.setOnItemSelectedListener(sharedListener);
        affectedAreaSp.setOnItemSelectedListener(sharedListener);

        dialog.show();
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.sendBtn.setVisibility(View.VISIBLE);
                diseasedCrop.setName(cropNameET.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private final AdapterView.OnItemSelectedListener sharedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Identify which spinner triggered the callback
            if (parent.getId() == R.id.crop_type_spinner) {
                String type = parent.getItemAtPosition(position).toString(); // Set directly
                diseasedCrop.setType(type);
            } else if (parent.getId() == R.id.growth_stage_spinner) {
                String gStage = parent.getItemAtPosition(position).toString(); // Set directly
                diseasedCrop.setGrowthStage(gStage);
            } else if (parent.getId() == R.id.affected_area_spinner) {
                String areaAffected = parent.getItemAtPosition(position).toString(); // Set directly
                diseasedCrop.setAffectedArea(areaAffected);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Handle the case where no selection is made, if necessary
        }
    };

    private void callGemini() {
        // Specify a Gemini model appropriate for your use case
        GenerativeModel gm = new GenerativeModel(
                "gemini-1.5-flash", getString(R.string.apiKey));
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        String prompt = generatePrompt();
        binding.imagePicker.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(binding.imagePicker.getDrawingCache());
        Content content = new Content.Builder().addText(prompt).addImage(bitmap).build();

        // For illustrative purposes only. You should use an executor that fits your needs.
        Executor executor = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        // Update resultText and call a method to handle/display it
                        resultText = resultText.concat(result.getText());
                        handleResponse(resultText);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                },
                executor);
    }

    private String generatePrompt() {
        String prompt = "This is a " + diseasedCrop.getName() + " of type " + diseasedCrop.getType() +
                " currently at " + diseasedCrop.growthStage + " which I am growing in " +
                diseasedCrop.getLocation() + " where the weather is " + diseasedCrop.getWeather() +
                ". "+ "Whose "+diseasedCrop.getAffectedArea()+"got affected recently, as you can see in the picture. "
                +"Can you analyze and tell me the name of the disease based on the information given?";
        return prompt;
    }

    private void handleResponse(String responseText) {
        // Display resultText in the UI, for example
        runOnUiThread(() -> {
            Intent intent = new Intent(DiseaseAnalyzeActivity.this, GeminiActivity.class);
            intent.putExtra("disease",responseText);
            intent.putExtra("crop",diseasedCrop.name);
            startActivity(intent);
        });
    }




}