package com.axis.helloastropartner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.Manifest;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etMobile, price,specification,detail;
    private Button btnRegister, btnUploadAadhaar, btnUploadPan, btnUploadSelfie;
    private ImageView imgAadhaar, imgPan, imgSelfie;

    private Spinner spinnerLanguage, spinnerExperince;

    private Uri aadhaarUri, panUri, selfieUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private StorageReference storageRef;

    private static final int PICK_AADHAAR = 101;
    private static final int PICK_PAN = 102;
    private static final int PICK_SELFIE = 103;

  //  private List<TimeSlot> slots = new ArrayList<>();
  //  private SlotAdapter slotAdapter;

    private Map<String, List<TimeSlot>> weeklySlots = new HashMap<>();
    private Map<String, SlotAdapter> slotAdapters = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etMobile = findViewById(R.id.etMobile);
        btnRegister = findViewById(R.id.btnRegister);
        btnUploadAadhaar = findViewById(R.id.btnUploadAadhaar);
        btnUploadPan = findViewById(R.id.btnUploadPan);
        btnUploadSelfie = findViewById(R.id.btnUploadSelfie);

        imgAadhaar = findViewById(R.id.imgAadhaar);
        imgPan = findViewById(R.id.imgPan);
        imgSelfie = findViewById(R.id.imgSelfie);

        specification = findViewById(R.id.specification);
        detail = findViewById(R.id.detail);

        price = findViewById(R.id.price);

        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        String[] languages = {"English", "Hindi"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                languages
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set adapter to spinner
        spinnerLanguage.setAdapter(adapter);

// Optional: Handle selection
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // You can leave this empty
            }
        });



        spinnerExperince = findViewById(R.id.spinnerExperince);

// Generate experience options from 0 to 30
        List<String> experienceList = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            experienceList.add(i + " years");
        }

// Create adapter
        ArrayAdapter<String> experienceAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                experienceList
        );
        experienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set adapter to spinner
        spinnerExperince.setAdapter(experienceAdapter);

// Optional: Handle selection
        spinnerExperince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedExperience = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Selected: " + selectedExperience, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional
            }
        });



        storageRef = FirebaseStorage.getInstance().getReference();

        btnUploadAadhaar.setOnClickListener(v -> pickImage(PICK_AADHAAR));
        btnUploadPan.setOnClickListener(v -> pickImage(PICK_PAN));
        btnUploadSelfie.setOnClickListener(v -> pickImage(PICK_SELFIE));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(v -> registerAstrologer());



       // Spinner spinnerDay = findViewById(R.id.spinnerDay);
       // RecyclerView recyclerSlots = findViewById(R.id.recyclerSlots);
       // Button btnAddSlot = findViewById(R.id.btnAddSlot);
       // Button btnSaveSlots = findViewById(R.id.btnSaveSlots);


      //  String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
      //  ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
      //  dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      //  spinnerDay.setAdapter(dayAdapter);

// Setup slot list
     //   recyclerSlots.setLayoutManager(new LinearLayoutManager(this));
     //   slotAdapter = new SlotAdapter(slots);
     //   recyclerSlots.setAdapter(slotAdapter);

// Add new slot
     //   btnAddSlot.setOnClickListener(v -> {
      //      slots.add(new TimeSlot("00:00", "00:00"));
      //      slotAdapter.notifyItemInserted(slots.size() - 1);
      //  });

// Save slots to Firestore
      //  btnSaveSlots.setOnClickListener(v -> {
      //      String day = spinnerDay.getSelectedItem().toString();
      //      String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Assumes user is logged in

      //      Map<String, Object> slotMap = new HashMap<>();
      //      slotMap.put("slots." + day, slots);

      //      FirebaseFirestore.getInstance()
      //              .collection("astrologers")
      //              .document(uid)
      //              .set(slotMap, SetOptions.merge())
      //              .addOnSuccessListener(aVoid ->
      //                      Toast.makeText(this, "Slots saved for " + day, Toast.LENGTH_SHORT).show()
      //              )
      //              .addOnFailureListener(e ->
      //                      Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
      //              );
     //   });

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (String day : days) {
            int recyclerId = getResources().getIdentifier("recycler" + day, "id", getPackageName());
            int buttonId = getResources().getIdentifier("btnAdd" + day + "Slot", "id", getPackageName());

            RecyclerView dayRecycler = findViewById(recyclerId);
            Button btnAdd = findViewById(buttonId);

            List<TimeSlot> daySlots = new ArrayList<>();
            SlotAdapter slotAdapter = new SlotAdapter(daySlots);

            dayRecycler.setLayoutManager(new LinearLayoutManager(this));
            dayRecycler.setAdapter(slotAdapter);

            weeklySlots.put(day, daySlots);
            slotAdapters.put(day, slotAdapter);

            btnAdd.setOnClickListener(v -> {
                daySlots.add(new TimeSlot("00:00", "00:00"));
                slotAdapter.notifyItemInserted(daySlots.size() - 1);
            });
        }
    }

    private void pickImage(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            switch (requestCode) {
                case PICK_AADHAAR:
                    aadhaarUri = uri;
                    imgAadhaar.setImageURI(uri);
                    break;
                case PICK_PAN:
                    panUri = uri;
                    imgPan.setImageURI(uri);
                    break;
                case PICK_SELFIE:
                    selfieUri = uri;
                    imgSelfie.setImageURI(uri);
                    break;
            }
        }
    }

    private void registerAstrologer() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String pr = price.getText().toString().trim();
        String sl = spinnerLanguage.getSelectedItem().toString();
        String se = spinnerExperince.getSelectedItem().toString();
        String specifi = specification.getText().toString().trim();
        String deta = detail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || pr.isEmpty()
                || aadhaarUri == null || panUri == null || selfieUri == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();

                        uploadImage("aadhaar", uid, aadhaarUri, aadhaarUrl -> {
                            uploadImage("pan", uid, panUri, panUrl -> {
                                uploadImage("selfie", uid, selfieUri, selfieUrl -> {

                                    Map<String, Object> astrologer = new HashMap<>();
                                    astrologer.put("uid", uid);
                                    astrologer.put("price",pr);
                                    astrologer.put("experince",se);
                                    astrologer.put("language", sl);
                                    astrologer.put("name", name);
                                    astrologer.put("email", email);
                                    astrologer.put("mobile", mobile);
                                    astrologer.put("aadhaarUrl", aadhaarUrl);
                                    astrologer.put("panUrl", panUrl);
                                    astrologer.put("specification", specifi);
                                    astrologer.put("detail", deta);
                                    astrologer.put("selfieUrl", selfieUrl);
                                    astrologer.put("createdAt", FieldValue.serverTimestamp());
                                    Map<String, Object> slotMap = new HashMap<>();

                                    for (Map.Entry<String, List<TimeSlot>> entry : weeklySlots.entrySet()) {
                                        slotMap.put("slots." + entry.getKey(), entry.getValue());
                                    }
                                    astrologer.putAll(slotMap);


                                    db.collection("astrologers").document(uid)
                                            .set(astrologer)
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(this, LoginActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e ->
                                                    Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                            );

                                });
                            });
                        });

                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void uploadImage(String type, String uid, Uri uri, OnUrlUploaded callback) {
        StorageReference fileRef = storageRef.child("documents/" + uid + "/" + type + ".jpg");
        fileRef.putFile(uri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                })
                .addOnSuccessListener(callback::onUploaded)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to upload " + type + ": " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    interface OnUrlUploaded {
        void onUploaded(Uri downloadUrl);
    }


}
