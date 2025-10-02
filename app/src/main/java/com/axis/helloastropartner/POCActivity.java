package com.axis.helloastropartner;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class POCActivity extends AppCompatActivity {

    RecyclerView pocRecyclerView;
    List<POCModel> pocList;
    POCAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocactivity);

        pocRecyclerView = findViewById(R.id.pocRecyclerView);
        pocRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pocList = new ArrayList<>();
        adapter = new POCAdapter(pocList);
        pocRecyclerView.setAdapter(adapter);

        loadDemoData();
    }

    private void loadDemoData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("poc_demo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pocList.clear();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                POCModel item = doc.toObject(POCModel.class);
                                pocList.add(item);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(POCActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
