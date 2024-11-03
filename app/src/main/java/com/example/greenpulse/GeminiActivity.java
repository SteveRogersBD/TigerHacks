package com.example.greenpulse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.greenpulse.adapters.ArticleAdapter;
import com.example.greenpulse.adapters.ImageAdapter;
import com.example.greenpulse.apiInterfaces.VideoApi;
import com.example.greenpulse.databinding.ActivityGeminiBinding;
import com.example.greenpulse.responses.ArticleResponse;
import com.example.greenpulse.responses.ImageResponse;
import com.example.greenpulse.retrofit.RetrofitInstance;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeminiActivity extends AppCompatActivity {

    ActivityGeminiBinding binding;
    private final int REASON = 1;
    private final int SOLUTION = 2;
    String reason = "";
    String solution = "";
    ImageAdapter imageAdapter;
    ArticleAdapter articleAdapter;
    VideoApi videoApi;

    List<ImageResponse.ImagesResult>imageList;
    List<ArticleResponse.NewsResult>newsResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeminiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String disease = intent.getStringExtra("disease");
        String name = intent.getStringExtra("crop");
        binding.diseaseName.setText("Analyzed Disease: "+disease);
        callGemini(createPrompt(disease, name, REASON), REASON);
        callGemini(createPrompt(disease, name, SOLUTION), SOLUTION);

        //logic for google image
        videoApi = RetrofitInstance.seraFit.create(VideoApi.class);
        String imageGPrompt = name+" "+disease;
        getGoogleImages(imageGPrompt);

        String articlePrompt = name+"and "+disease+" and "+"fertilizer and prevention";
        getArticles(articlePrompt);


    }

    private void getArticles(String articlePrompt) {
        videoApi.getNews("google_news",articlePrompt,"us","en",
                getString(R.string.video_api_key)).enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if(response.body()!=null && response.isSuccessful())
                {
                    newsResultList = response.body().news_results;
                    articleAdapter = new ArticleAdapter(GeminiActivity.this,newsResultList);
                    binding.newsRecyclerView.setAdapter(articleAdapter);
                    binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(GeminiActivity.this,
                            LinearLayoutManager.HORIZONTAL,false));

                }
                else{
                    Toast.makeText(GeminiActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable throwable) {
                Toast.makeText(GeminiActivity.this, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getGoogleImages(String imageGPrompt) {
        videoApi.searchImages("google_images",imageGPrompt,"United+States",
                getString(R.string.video_api_key)).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if(response.isSuccessful() && response.body()!=null)
                {
                    imageList = response.body().images_results;
                    imageAdapter = new ImageAdapter(GeminiActivity.this,imageList);
                    binding.imageRecycler.setAdapter(imageAdapter);
                    binding.imageRecycler.setLayoutManager(new LinearLayoutManager(GeminiActivity.this,
                            LinearLayoutManager.HORIZONTAL,false));
                }
                else{
                    Toast.makeText(GeminiActivity.this, response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable throwable) {
                Toast.makeText(GeminiActivity.this, throwable.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callGemini(String prompt, int flag) {
        // Initialize the GenerativeModel with the model name and API key.
        GenerativeModel gm = new GenerativeModel(
                "gemini-1.5-flash", getString(R.string.apiKey));
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder().addText(prompt).build();

        // Use a single-thread executor or consider a shared thread pool for multiple requests.
        Executor executor = Executors.newSingleThreadExecutor();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        String resultText = result.getText();
                        // Run handleResponse on the main thread
                        runOnUiThread(() -> handleResponse(resultText, flag));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Print the error and update UI to inform the user.
                        t.printStackTrace();
                        runOnUiThread(() -> binding.reasonsTv.setText("Error fetching response. Please try again."));
                    }
                },
                executor
        );
    }


    private void handleResponse(String resultText, int flag) {
        if (flag == REASON) {
            binding.reasonsTv.setText(resultText);
        } else if (flag == SOLUTION) {
            binding.solutionsTv.setText(resultText);
        }
    }

    private String createPrompt(String disease, String name, int flag) {
        if (flag == REASON) {
            return String.format(
                    "My crops (%s) are affected by %s. " +
                            "Write a simple paragraph without bullet points or numbers, " +
                            "about the possible specific reasons for this disease on my crop, within 50 words.",
                    name, disease
            );
        } else if (flag == SOLUTION) {
            return String.format(
                    "My crops (%s) are affected by %s. " +
                            "Write a simple paragraph without bullet points or numbers, " +
                            "about specific solutions for this disease on my crop, within 200 words. " +
                            "Include specific medicines or fertilizers that can help.",
                    name, disease
            );
        }
        return "";
    }
}
