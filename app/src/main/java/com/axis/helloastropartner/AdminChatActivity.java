package com.axis.helloastropartner;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;

    private FirebaseFirestore db;
    private String astrologerId;   // logged-in astrologer UID
    private String userId;         // comes from AdminDashboardActivity Intent
    private String chatId;

    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        db = FirebaseFirestore.getInstance();
        astrologerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userId = getIntent().getStringExtra("userId");

        if (userId == null || userId.isEmpty()) {
            Log.e("AdminChatActivity", "No userId provided!");
            finish();
            return;
        }

        // ✅ Build chatId same way as UserChatActivity
        if (userId.compareTo(astrologerId) < 0) {
            chatId = userId + "_" + astrologerId;
        } else {
            chatId = astrologerId + "_" + userId;
        }

        Log.d("AdminChatActivity", "ChatId = " + chatId);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageList, astrologerId);
        recyclerView.setAdapter(messageAdapter);

        // Load + realtime updates
        listenForMessages();

        // Send button
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void listenForMessages() {
        CollectionReference messagesRef = db.collection("chats")
                .document(chatId)
                .collection("messages");

        messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("AdminChatActivity", "Listen failed.", error);
                        return;
                    }
                    if (value == null) return;

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        Message msg = dc.getDocument().toObject(Message.class);
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            messageList.add(msg);
                            messageAdapter.notifyItemInserted(messageList.size() - 1);
                            recyclerView.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        Map<String, Object> message = new HashMap<>();
        message.put("senderId", astrologerId);
        message.put("receiverId", userId);
        message.put("message", text);
        message.put("timestamp", System.currentTimeMillis());

        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(doc -> {
                    Log.d("AdminChatActivity", "Message sent: " + text);
                    messageInput.setText("");
                })
                .addOnFailureListener(e -> Log.e("AdminChatActivity", "Send failed", e));
    }
}
