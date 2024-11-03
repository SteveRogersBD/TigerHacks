package com.example.greenpulse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.greenpulse.adapters.ChatAdapter;
import com.example.greenpulse.databinding.ActivityBotBinding;
import com.example.greenpulse.models.ChatModel;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BotActivity extends AppCompatActivity {

    List<ChatModel> messageList;
    ChatAdapter chatAdapter;
    ActivityBotBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(BotActivity.this,messageList);
        binding.mainRecycler.setAdapter(chatAdapter);
        binding.mainRecycler.setLayoutManager(new LinearLayoutManager(BotActivity.this));

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.textEt.getText().equals(""))
                {
                    String query = binding.textEt.getText().toString();
                    ChatModel newMessage = new ChatModel(query,ChatModel.SENT_BY_ME);
                    messageList.add(newMessage);
                    chatAdapter.notifyDataSetChanged();
                    binding.mainRecycler.smoothScrollToPosition(messageList.size());
                    binding.textEt.setText("");
                    callGemini(query);
                }
            }
        });
    }


    private void callGemini(String query){
        String apiKey = getString(R.string.apiKey);
        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash",apiKey);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        query = filtered(query);
        Content content =
                new Content.Builder().addText(query).build();

// For illustrative purposes only. You should use an executor that fits your needs.
        Executor executor = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //textView.setText(result.getText());
                                ChatModel replyMessage = new ChatModel(result.getText(), ChatModel.SENT_BY_BOT);
                                messageList.add(replyMessage);
                                chatAdapter.notifyDataSetChanged();
                                binding.mainRecycler.smoothScrollToPosition(messageList.size());

                            }
                        });
                    }


                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                },
                executor);
    }

    private String filtered(String query) {
        String prompt= query+" If this query isn't related to agriculture and farming, then" +
                "don't reply saying that I am AgriBot and I only can help with agriculture. " +
                "But if someone tries to greet you or introduce, then reply. But dont reply anything" +
                "that is not related to agriculture.";
        return prompt;
    }
}