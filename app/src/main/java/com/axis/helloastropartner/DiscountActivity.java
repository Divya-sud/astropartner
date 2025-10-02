package com.axis.helloastropartner;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscountActivity extends AppCompatActivity {

    private Spinner productSpinner;
    private EditText discountInput;
    private Button applyBtn;

    private FirebaseFirestore db;
    private List<String> productNames = new ArrayList<>();
    private List<String> productIds = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private String selectedProductId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        productSpinner = findViewById(R.id.productSpinner);
        discountInput = findViewById(R.id.discountInput);
        applyBtn = findViewById(R.id.applyBtn);

        db = FirebaseFirestore.getInstance();

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productNames);
        productSpinner.setAdapter(spinnerAdapter);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProductId = productIds.get(position);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        fetchProducts();

        applyBtn.setOnClickListener(v -> applyDiscount());
    }

    private void fetchProducts() {
        db.collection("remedies").get().addOnSuccessListener(snapshot -> {
            productNames.clear();
            productIds.clear();
            for (DocumentSnapshot doc : snapshot) {
                productNames.add(doc.getString("name"));
                productIds.add(doc.getId());
            }
            spinnerAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Error fetching products", Toast.LENGTH_SHORT).show());
    }

    private void applyDiscount() {
        String discountStr = discountInput.getText().toString().trim();
        if (discountStr.isEmpty()) {
            discountInput.setError("Enter discount");
            return;
        }

        double discountValue = Double.parseDouble(discountStr);
        if (discountValue < 0 || discountValue > 100) {
            discountInput.setError("Enter valid discount (0-100%)");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("discount", discountValue);

        db.collection("remedies").document(selectedProductId)
                .update(updates)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Discount applied", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
