package com.axis.helloastropartner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BusinessReportActivity extends AppCompatActivity {
    private TextView totalBookingsText, totalEarningsText, dailyBookingsText, monthlyBookingsText;
    private PieChart pieChart;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_report);

        totalBookingsText = findViewById(R.id.totalBookingsText);
        totalEarningsText = findViewById(R.id.totalEarningsText);
        dailyBookingsText = findViewById(R.id.dailyBookingsText);
        monthlyBookingsText = findViewById(R.id.monthlyBookingsText);
        pieChart = findViewById(R.id.pieChart);

        db = FirebaseFirestore.getInstance();
        loadReportData();
    }

    private void loadReportData() {
        CollectionReference bookingsRef = db.collection("bookings");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);

        bookingsRef.get().addOnSuccessListener(querySnapshot -> {
            int totalBookings = 0;
            double totalEarnings = 0;
            int dailyCount = 0;
            int monthlyCount = 0;
            int completed = 0;
            int cancelled = 0;

            for (QueryDocumentSnapshot doc : querySnapshot) {
                totalBookings++;

                String date = doc.getString("bookingDate");
                Double price = doc.getDouble("price");
                String status = doc.getString("status");

                if (price != null) totalEarnings += price;

                if (today.equals(date)) dailyCount++;

                if (date != null && date.length() >= 7) {
                    String[] parts = date.split("-");
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);

                    if (year == currentYear && month == currentMonth) {
                        monthlyCount++;
                    }
                }

                if ("completed".equalsIgnoreCase(status)) completed++;
                else if ("cancelled".equalsIgnoreCase(status)) cancelled++;
            }

            totalBookingsText.setText("Total Bookings: " + totalBookings);
            totalEarningsText.setText("Total Earnings: ₹" + totalEarnings);
            dailyBookingsText.setText("Today's Bookings: " + dailyCount);
            monthlyBookingsText.setText("This Month's Bookings: " + monthlyCount);

            setupPieChart(completed, cancelled);
        });
    }

    private void setupPieChart(int completed, int cancelled) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(completed, "Completed"));
        entries.add(new PieEntry(cancelled, "Cancelled"));

        PieDataSet dataSet = new PieDataSet(entries, "Booking Status");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(14f);

        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh chart
    }
}