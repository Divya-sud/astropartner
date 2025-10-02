package com.axis.helloastropartner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetTimingActivity extends AppCompatActivity {

    private TimePicker fromTimePicker, toTimePicker;
    private Button addTimeBtn, saveBtn;
    private ListView timeListView;
    private List<String> timeSlots = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;
    private String astrologerId;
    private int editIndex = -1; // -1 means not editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timing);

        fromTimePicker = findViewById(R.id.fromTimePicker);
        toTimePicker = findViewById(R.id.toTimePicker);
        addTimeBtn = findViewById(R.id.addTimeBtn);
        saveBtn = findViewById(R.id.saveBtn);
        timeListView = findViewById(R.id.timeListView);

        db = FirebaseFirestore.getInstance();
        astrologerId = FirebaseAuth.getInstance().getUid();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timeSlots);
        timeListView.setAdapter(adapter);

        addTimeBtn.setOnClickListener(v -> {
            String from = formatTime(fromTimePicker);
            String to = formatTime(toTimePicker);
            String slot = from + " - " + to;

            if (editIndex != -1) {
                timeSlots.set(editIndex, slot);
                editIndex = -1;
                addTimeBtn.setText("Add Time Slot");
            } else {
                timeSlots.add(slot);
            }
            adapter.notifyDataSetChanged();
        });

        timeListView.setOnItemClickListener((parent, view, position, id) -> showEditDeleteDialog(position));

        saveBtn.setOnClickListener(v -> saveTimingsToFirestore());
    }

    private void showEditDeleteDialog(int index) {
        String selectedSlot = timeSlots.get(index);
        new AlertDialog.Builder(this)
                .setTitle("Modify Time Slot")
                .setMessage(selectedSlot)
                .setPositiveButton("Edit", (dialog, which) -> {
                    String[] times = selectedSlot.split(" - ");
                    setTimePicker(fromTimePicker, times[0]);
                    setTimePicker(toTimePicker, times[1]);
                    editIndex = index;
                    addTimeBtn.setText("Update Slot");
                })
                .setNegativeButton("Delete", (dialog, which) -> {
                    timeSlots.remove(index);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private String formatTime(TimePicker picker) {
        int hour = picker.getHour();
        int minute = picker.getMinute();
        String ampm = hour >= 12 ? "PM" : "AM";
        hour = hour % 12;
        if (hour == 0) hour = 12;
        return String.format("%02d:%02d %s", hour, minute, ampm);
    }

    private void setTimePicker(TimePicker picker, String timeStr) {
        try {
            String[] parts = timeStr.split(" ");
            String[] hm = parts[0].split(":");
            int hour = Integer.parseInt(hm[0]);
            int minute = Integer.parseInt(hm[1]);
            String ampm = parts[1];

            if (ampm.equals("PM") && hour < 12) hour += 12;
            if (ampm.equals("AM") && hour == 12) hour = 0;

            picker.setHour(hour);
            picker.setMinute(minute);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to parse time", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTimingsToFirestore() {
        if (astrologerId == null || timeSlots.isEmpty()) {
            Toast.makeText(this, "No timings to save", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("timings", timeSlots);

        db.collection("astrologers").document(astrologerId)
                .update(updateData)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Timings saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
