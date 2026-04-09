package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.doctor.ApiService;
import com.example.app.doctor.ChatRequest;
import com.example.app.doctor.ChatResponse;
import com.example.app.doctor.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotActivity extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText etMessage;
    private ImageView btnSend;
    private ScrollView chatScrollView;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        // Initialize Views
        chatContainer = findViewById(R.id.chat_container);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        chatScrollView = findViewById(R.id.chat_scroll_view);
        apiService = RetrofitClient.getApiService();

        // Back Button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // Send Button Logic
        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                addUserMessage(message);
                etMessage.setText("");
                sendMessageToAI(message);
            }
        });
    }

    private void addUserMessage(String message) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_chat_user, chatContainer, false);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        chatContainer.addView(view);
        scrollToBottom();
    }

    private void addAIMessage(String message) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_chat_ai, chatContainer, false);
        TextView tvMessage = view.findViewById(R.id.tv_message);
        tvMessage.setText(message);
        chatContainer.addView(view);
        scrollToBottom();
    }

    private void sendMessageToAI(String message) {
        apiService.chat(new ChatRequest(message)).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addAIMessage(response.body().response);
                } else {
                    addAIMessage("I'm sorry, I encountered an error. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                addAIMessage("Connection error. Please check your internet.");
            }
        });
    }

    private void scrollToBottom() {
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }
}
