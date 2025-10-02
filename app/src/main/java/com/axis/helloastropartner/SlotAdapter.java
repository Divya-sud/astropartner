package com.axis.helloastropartner;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder>{

    private final List<TimeSlot> slotList;

    public SlotAdapter(List<TimeSlot> slotList) {
        this.slotList = slotList;
    }
    @NonNull
    @Override
    public SlotAdapter.SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotAdapter.SlotViewHolder holder, int position) {
        TimeSlot slot = slotList.get(position);
        holder.fromText.setText(slot.from);
        holder.toText.setText(slot.to);

        holder.fromText.setOnClickListener(v -> {
            showTimePicker(v.getContext(), time -> {
                slot.from = time;
                holder.fromText.setText(time);
            });
        });

        holder.toText.setOnClickListener(v -> {
            showTimePicker(v.getContext(), time -> {
                slot.to = time;
                holder.toText.setText(time);
            });
        });

    }

    @Override
    public int getItemCount() {
        return slotList.size();
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView fromText, toText;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            fromText = itemView.findViewById(R.id.tvFrom);
            toText = itemView.findViewById(R.id.tvTo);
        }
    }

    private void showTimePicker(Context context, TimePickedCallback callback) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(context, (view, hourOfDay, minute1) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
            callback.onTimePicked(time);
        }, hour, minute, true).show();
    }

    interface TimePickedCallback {
        void onTimePicked(String time);
    }
}
