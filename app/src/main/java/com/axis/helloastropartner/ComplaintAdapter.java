package com.axis.helloastropartner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder>{
    private List<Complaint> complaintList;

    public ComplaintAdapter(List<Complaint> complaintList) {
        this.complaintList = complaintList;
    }
    @NonNull
    @Override
    public ComplaintAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintAdapter.ViewHolder holder, int position) {
        Complaint c = complaintList.get(position);
        holder.textView.setText(c.getComplaintText());
        holder.timestampView.setText(c.getTimestamp());

    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, timestampView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.complaintText);
            timestampView = itemView.findViewById(R.id.complaintTime);
        }
    }
}
