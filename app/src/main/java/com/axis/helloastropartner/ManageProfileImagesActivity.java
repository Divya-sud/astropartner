package com.axis.helloastropartner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ManageProfileImagesActivity extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST = 2;

    ImageView addImageBtn;
    RecyclerView recyclerView;
    ProfileImageAdapter adapter;

    List<String> uploadedImageUrls = new ArrayList<>();

    FirebaseFirestore db;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile_images);

        addImageBtn = findViewById(R.id.addImageBtn);
        recyclerView = findViewById(R.id.recyclerViewProfileImages);

        db = FirebaseFirestore.getInstance();
        uid = getIntent().getStringExtra("uid");

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ProfileImageAdapter(uploadedImageUrls);
        recyclerView.setAdapter(adapter);

        loadProfileImages();

        addImageBtn.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uploadImageToFirebase(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        String fileName = "profileImages/" + uid + "/" + System.currentTimeMillis() + ".jpg";

        FirebaseStorage.getInstance().getReference(fileName)
                .putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        FirebaseStorage.getInstance().getReference(fileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    saveImageUrlToFirestore(downloadUrl);
                                }))
                .addOnFailureListener(e -> Log.e("UPLOAD_ERROR", "Upload failed", e));
    }

    private void saveImageUrlToFirestore(String url) {
        db.collection("astrologers").document(uid)
                .collection("profileimage").document(uid)
                .set(
                        new java.util.HashMap<String, Object>() {{
                            put("image", FieldValue.arrayUnion(url));
                        }},
                        com.google.firebase.firestore.SetOptions.merge()
                )
                .addOnSuccessListener(aVoid -> {
                    uploadedImageUrls.add(url);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FIRESTORE", "Failed to save URL", e));
    }


    private void loadProfileImages() {
        db.collection("astrologers").document(uid).collection("profileimage").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<String> images = (List<String>) documentSnapshot.get("image");
                    if (images != null) {
                        uploadedImageUrls.clear();
                        uploadedImageUrls.addAll(images);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
