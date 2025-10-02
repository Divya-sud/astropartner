package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReviewsListActivity extends AppCompatActivity {

 //   RecyclerView reviewRecyclerView;
 //   ReviewAdapter reviewAdapter;
 //   List<Review> reviewList = new ArrayList<>();
 //   FirebaseFirestore db;

 //   String astrologerId = "astro123"; // Pass this dynamically if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_list);

     //   reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
     //   reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

     //   reviewAdapter = new ReviewAdapter(reviewList);
     //   reviewRecyclerView.setAdapter(reviewAdapter);

      //  db = FirebaseFirestore.getInstance();

      //  loadReviews();
    }

  //  private void loadReviews() {
   //     db.collection("reviews")
  //              .whereEqualTo("astrologerId", astrologerId)
  //              .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
  //              .get()
  //             .addOnSuccessListener(queryDocumentSnapshots -> {
  //                  reviewList.clear();
  //                  for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
  //                     Review review = doc.toObject(Review.class);
  //                      reviewList.add(review);
  //                  }
  //                  reviewAdapter.notifyDataSetChanged();
  //              })
  //              .addOnFailureListener(e -> Log.e("FIRESTORE", "Failed to fetch reviews", e));
  //  }
}