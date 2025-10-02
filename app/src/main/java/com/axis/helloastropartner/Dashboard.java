package com.axis.helloastropartner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    CircleImageView profileImage;
    TextView repo;
    TextView ord1;
    ImageView cardImage6;
    FirebaseFirestore db;
    BottomNavigationView bottomNavigationView;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        profileImage = findViewById(R.id.profileImage);
        repo = findViewById(R.id.repo);

        ord1 = findViewById(R.id.ord1);
        db = FirebaseFirestore.getInstance();
        uid = getIntent().getStringExtra("uid");


        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_restaurants:
                    selectedFragment = new BookingListnerFragment();
                    break;

                case R.id.nav_search:
                    selectedFragment = new AstroMartFragment();
                    break;

                case R.id.nav_favourites:
                    selectedFragment = new FavouriteFragment();
                    break;

                case R.id.nav_profile:
                    selectedFragment = new MyProfileFragment();
                    break;

              //  case R.id.nav_home: // if you have a home/dashboard item
              //      findViewById(R.id.scrollableContent).setVisibility(View.VISIBLE);
              //      findViewById(R.id.fragment_container).setVisibility(View.GONE);
              //      return true;
            }

            if (selectedFragment != null) {
                // Hide dashboard, show fragment container
                findViewById(R.id.scrollableContent).setVisibility(View.GONE);
                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });




        // bottomNavigationView.setOnItemSelectedListener(item -> {
       //     switch (item.getItemId()) {
                //   case R.id.nav_home:
                // Optional: Load a home fragment (if exists)
                //       return true;

             //   case R.id.nav_restaurants:
                    // 🔔 Start BookingListenerService
            //        Intent serviceIntent = new Intent(this, BookingListenerService.class);
            //        startService(serviceIntent);

                    // 📋 Show BookingActivity
                    //  startActivity(new Intent(this, BookingActivity.class));
                    // return true;

            //    case R.id.nav_profile:
                    // Optional: Load profile fragment (if exists)
            //        return true;
       //     }
       //     return false;
       // });



        fetchTodaysOrderCount();
        astroImage();
        repo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,BusinessReportActivity.class));
            }
        });

        findViewById(R.id.cardImage6).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewsListActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        findViewById(R.id.cardImage5).setOnClickListener(v -> {
            Intent intent = new Intent(this, PayoutActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        findViewById(R.id.cardImage2).setOnClickListener(v -> {
            Intent intent = new Intent(this, RemediesActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        findViewById(R.id.cardImage1).setOnClickListener(v -> {
            Intent intent = new Intent(this, SetTimingActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        findViewById(R.id.cardImage3).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowComplaintsActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        findViewById(R.id.cardImage4).setOnClickListener(v -> {
            Intent intent = new Intent(this, DiscountActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });
        findViewById(R.id.cardImage7).setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageProfileImagesActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });
        findViewById(R.id.cardImage8).setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminDashboardActivity.class);
           // intent.putExtra("uid", uid);
            intent.putExtra("userId", uid);
            startActivity(intent);

        });

        findViewById(R.id.cardImage).setOnClickListener(v -> {
            Intent intent = new Intent(this, POCActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });


    }

    private void astroImage() {
        db.collection("astrologers")  // or "users" if that's your collection
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String imageUrl = documentSnapshot.getString("selfieUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.ic_baseline_image_24) // fallback
                                    .error(R.drawable.ic_baseline_image_24)       // error icon
                                    .into(profileImage);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Dashboard", "Failed to load profile image", e);
                });

    }

    private void fetchTodaysOrderCount() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        db.collection("bookings")
                .whereEqualTo("bookingDate", today)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int todaysOrders = queryDocumentSnapshots.size();
                    ord1.setText(String.valueOf(todaysOrders));
                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE", "Failed to fetch today's orders", e);
                    ord1.setText("0");
                });
    }
}