package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.Set;

public class AdminDashboardActivity extends AppCompatActivity {

    private LinearLayout userListLayout;
    private FirebaseFirestore db;
    private String adminId; // astrologer UID (current logged in)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        userListLayout = findViewById(R.id.userListLayout);
        db = FirebaseFirestore.getInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            adminId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            Log.e("AdminDashboard", "No admin UID found (not logged in)");
            return;
        }

        loadUserChats();
    }

    private void loadUserChats() {
        CollectionReference chatsRef = db.collection("astrologers").document(adminId).collection("messages");

        chatsRef.get().addOnSuccessListener(querySnapshot -> {
            Set<String> userIds = new HashSet<>();

            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                String chatId = doc.getId(); // e.g., user123_admin456

                String[] parts = chatId.split("_");
                if (parts.length == 2) {
                    if (parts[0].equals(adminId)) {
                        userIds.add(parts[1]); // other participant
                    } else if (parts[1].equals(adminId)) {
                        userIds.add(parts[0]); // other participant
                    }
 //                    else: skip, chat doesn’t belong to this admin
                }
            }

            for (String userId : userIds) {
                addUserToList(userId);
            }

        }).addOnFailureListener(e -> Log.e("AdminDashboard", "Failed to load chats", e));
    }

    private void addUserToList(String userId) {
        TextView userTextView = new TextView(this);
        userTextView.setText("Chat with: " + userId);
        userTextView.setTextSize(18);
        userTextView.setPadding(16, 16, 16, 16);

        userTextView.setOnClickListener(v -> {
          //   Build chatId consistently
            String chatId = buildChatId(userId, adminId);
            openChat(chatId, userId);
        });

        userListLayout.addView(userTextView,
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void openChat(String chatId, String userId) {
        Intent intent = new Intent(this, AdminChatActivity.class);
        intent.putExtra("chatId", chatId);   // ✅ Pass chatId directly
        intent.putExtra("userId", userId);   // still pass userId for display
        startActivity(intent);
    }

    // ✅ Same buildChatId logic everywhere
    private String buildChatId(String uid1, String uid2) {
        return (uid1.compareTo(uid2) < 0) ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }
}
