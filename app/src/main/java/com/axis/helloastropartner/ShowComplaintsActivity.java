package com.axis.helloastropartner;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class ShowComplaintsActivity extends AppCompatActivity {

  //  private RecyclerView recyclerView;
  //  private List<Complaint> complaintList = new ArrayList<>();
  //  private ComplaintAdapter adapter;
  //  private FirebaseFirestore db;
  //  private String astrologerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complaints);

    //    recyclerView = findViewById(R.id.recyclerView);
    //    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    //    adapter = new ComplaintAdapter(complaintList);
    //    recyclerView.setAdapter(adapter);

     //   db = FirebaseFirestore.getInstance();
     //   astrologerId = FirebaseAuth.getInstance().getUid();

      //  fetchComplaints();
    }

  //  private void fetchComplaints() {
  //      db.collection("complaints")
  //              .whereEqualTo("astrologerId", astrologerId)
  //              .orderBy("timestamp", Query.Direction.DESCENDING)
  //              .get()
   //             .addOnSuccessListener(snapshot -> {
   //                 complaintList.clear();
   //                 for (DocumentSnapshot doc : snapshot) {
   //                     Complaint complaint = new Complaint(
   //                             doc.getId(),
   //                             doc.getString("userId"),
    //                            doc.getString("astrologerId"),
    //                            doc.getString("complaintText"),
   //                             doc.getString("timestamp")
    //                    );
    //                    complaintList.add(complaint);
    //                }
    //                adapter.notifyDataSetChanged();
     //           })
     //           .addOnFailureListener(e ->
      //                  Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
  //  }
}
