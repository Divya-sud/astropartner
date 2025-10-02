package com.axis.helloastropartner;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class RemediesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RemedyAdapter adapter;
    private List<Remedy> remedyList = new ArrayList<>();
    private FirebaseFirestore db;
    private FloatingActionButton addRemedyFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies);

        recyclerView = findViewById(R.id.recyclerView);
        addRemedyFab = findViewById(R.id.addRemedyFab);

        db = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RemedyAdapter(remedyList, this, new RemedyAdapter.OnRemedyActionListener() {
            @Override
            public void onEdit(Remedy remedy) {
                Intent intent = new Intent(RemediesActivity.this, UploadRemedyActivity.class);
                intent.putExtra("remedyId", remedy.getId());
                intent.putExtra("name", remedy.getName());
                intent.putExtra("description", remedy.getDescription());
                intent.putExtra("price", remedy.getPrice());
                intent.putExtra("imageUrl", remedy.getImageUrl());
                startActivity(intent);
            }

            @Override
            public void onDelete(Remedy remedy) {
                new AlertDialog.Builder(RemediesActivity.this)
                        .setTitle("Delete Remedy")
                        .setMessage("Are you sure you want to delete " + remedy.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.collection("remedies").document(remedy.getId())
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(RemediesActivity.this, "Remedy deleted", Toast.LENGTH_SHORT).show();
                                        fetchRemedies();
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);

        addRemedyFab.setOnClickListener(v -> {
            Intent intent = new Intent(RemediesActivity.this, UploadRemedyActivity.class);
            startActivity(intent);
        });

        fetchRemedies();
    }

    private void fetchRemedies() {
        db.collection("remedies").get().addOnSuccessListener(snapshot -> {
            remedyList.clear();
            for (DocumentSnapshot doc : snapshot) {
                Remedy r = new Remedy(
                        doc.getId(),
                        doc.getString("name"),
                        doc.getString("description"),
                        doc.getString("imageUrl"),
                        doc.getDouble("price")
                );
                remedyList.add(r);
            }
            adapter.notifyDataSetChanged();
        });
    }
}
