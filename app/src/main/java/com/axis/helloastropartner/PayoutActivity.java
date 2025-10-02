package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PayoutActivity extends AppCompatActivity {
    private TextView totalEarningsText, withdrawableText;
    private EditText amountInput;
    private Button requestPayoutBtn;
    private FirebaseFirestore db;
    private String astrologerId;
    private double totalEarnings = 0;
    private double withdrawnAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        totalEarningsText = findViewById(R.id.totalEarningsText);
        withdrawableText = findViewById(R.id.withdrawableText);
        amountInput = findViewById(R.id.amountInput);
        requestPayoutBtn = findViewById(R.id.requestPayoutBtn);

        db = FirebaseFirestore.getInstance();
        astrologerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchEarnings();

        requestPayoutBtn.setOnClickListener(v -> {
            String amountStr = amountInput.getText().toString().trim();
            if (!amountStr.isEmpty()) {
                double requestAmount = Double.parseDouble(amountStr);
                if (requestAmount <= (totalEarnings - withdrawnAmount)) {
                    requestPayout(requestAmount);
                } else {
                    Toast.makeText(this, "Not enough balance", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchEarnings() {
        // Total earnings
        db.collection("bookings")
                .whereEqualTo("astrologerId", astrologerId)
                .whereEqualTo("status", "completed")
                .get()
                .addOnSuccessListener(snapshot -> {
                    totalEarnings = 0;
                    for (DocumentSnapshot doc : snapshot) {
                        Number price = doc.getDouble("price");
                        if (price != null) totalEarnings += price.doubleValue();
                    }
                    totalEarningsText.setText("Total Earnings: ₹" + totalEarnings);
                    fetchWithdrawn();
                });
    }

    private void fetchWithdrawn() {
        // Sum of already withdrawn amount
        db.collection("payoutRequests")
                .whereEqualTo("astrologerId", astrologerId)
                .whereIn("status", java.util.Arrays.asList("pending", "approved"))
                .get()
                .addOnSuccessListener(snapshot -> {
                    withdrawnAmount = 0;
                    for (DocumentSnapshot doc : snapshot) {
                        Number amt = doc.getDouble("amount");
                        if (amt != null) withdrawnAmount += amt.doubleValue();
                    }

                    double withdrawable = totalEarnings - withdrawnAmount;
                    withdrawableText.setText("Withdrawable: ₹" + withdrawable);
                });
    }

    private void requestPayout(double amount) {
       // Timestamp now = Timestamp.now();
        Map<String, Object> payoutData = new HashMap<>();
        payoutData.put("astrologerId", astrologerId);
        payoutData.put("amount", amount);
        payoutData.put("status", "pending");
        payoutData.put("requestedAt", FieldValue.serverTimestamp());

        db.collection("payoutRequests")
                .add(payoutData)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Payout request submitted!", Toast.LENGTH_SHORT).show();
                    fetchEarnings(); // refresh
                    amountInput.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}