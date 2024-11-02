package com.example.greenpulse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.greenpulse.adapters.OnBoardAdapter;
import com.example.greenpulse.databinding.ActivityOnBoardBinding;
import com.example.greenpulse.models.OnBoardItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnBoardActivity extends AppCompatActivity {

    ActivityOnBoardBinding binding;
    OnBoardAdapter adapter;
    List<OnBoardItem> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        itemList = new ArrayList<>();
        List<String> desc = new ArrayList<>(Arrays.asList(
                getString(R.string.disease_analyzer),
                getString(R.string.map),
                getString(R.string.bot),
                getString(R.string.trends),
                getString(R.string.social)
        ));
        List<Integer>images = new ArrayList<>(Arrays.asList(
                R.drawable.plant,
                R.drawable.location,
                R.drawable.chatbot,
                R.drawable.trend,
                R.drawable.geofencewithoubg

        ));
        for(int i=0;i<desc.size();i++) itemList.add(new OnBoardItem(images.get(i),desc.get(i)));


        adapter = new OnBoardAdapter(this,itemList);

        binding.viewPager2.setAdapter(adapter);
        binding.dot.setSelectedDotColor(R.color.white);
        binding.dot.setViewPager2(binding.viewPager2);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = binding.viewPager2.getCurrentItem();
                if(currentItem<binding.viewPager2.getAdapter().getItemCount()-1)
                {
                    //do something later
                    binding.viewPager2.setCurrentItem(currentItem + 1);

                }
                else{
                    startActivity(new Intent(OnBoardActivity.this,StartingActivity.class));
                }
            }
        });






    }
}