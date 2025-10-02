package com.axis.helloastropartner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadRemedyActivity extends AppCompatActivity {

    private EditText remedyNameInput, remedyDescInput, remedyPriceInput;
    private ImageView remedyImage;
    private Button uploadBtn;

    private Uri selectedImageUri = null;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_remedy);

        remedyNameInput = findViewById(R.id.remedyNameInput);
        remedyDescInput = findViewById(R.id.remedyDescInput);
        remedyPriceInput = findViewById(R.id.remedyPriceInput);
        remedyImage = findViewById(R.id.remedyImage);
        uploadBtn = findViewById(R.id.uploadRemedyBtn);

        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("remedy_images");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        remedyImage.setOnClickListener(v -> pickImage());

        uploadBtn.setOnClickListener(v -> {
            String name = remedyNameInput.getText().toString().trim();
            String desc = remedyDescInput.getText().toString().trim();
            String priceStr = remedyPriceInput.getText().toString().trim();

            if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            uploadRemedy(name, desc, price, selectedImageUri);
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            remedyImage.setImageURI(selectedImageUri);
        }
    }

    private void uploadRemedy(String name, String desc, double price, Uri imageUri) {
        progressDialog.show();

        String imageId = UUID.randomUUID().toString();
        storageReference.child(imageId).putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.child(imageId).getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();

                                Map<String, Object> remedy = new HashMap<>();
                                remedy.put("name", name);
                                remedy.put("description", desc);
                                remedy.put("price", price);
                                remedy.put("imageUrl", imageUrl);

                                firestore.collection("remedies")
                                        .add(remedy)
                                        .addOnSuccessListener(documentReference -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, "Remedy uploaded successfully!", Toast.LENGTH_SHORT).show();
                                            finish(); // close activity
                                        })
                                        .addOnFailureListener(e -> {
                                            progressDialog.dismiss();
                                            Toast.makeText(this, "Failed to upload remedy: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
