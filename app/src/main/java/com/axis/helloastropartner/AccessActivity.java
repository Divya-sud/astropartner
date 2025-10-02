package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccessActivity extends AppCompatActivity {

    private EditText etMobile, etPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        etMobile = findViewById(R.id.etMobile);
       // etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> loginWithMobile());
    }

    private void loginWithMobile() {
        String mobile = etMobile.getText().toString().trim();

        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Step 1: Find astrologer with this mobile
        db.collection("astrologers")
                .whereEqualTo("mobile", mobile)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // User exists
                        String uid = queryDocumentSnapshots.getDocuments().get(0).getString("uid");

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Step 2: Go to main screen
                        Intent intent = new Intent(AccessActivity.this, Dashboard.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "No astrologer found with this mobile", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}