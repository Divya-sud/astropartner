package com.axis.helloastropartner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RemedyAdapter extends RecyclerView.Adapter<RemedyAdapter.ViewHolder> {
    private List<Remedy> remedyList;
    private Context context;

    public interface OnRemedyActionListener {
        void onEdit(Remedy remedy);
        void onDelete(Remedy remedy);
    }

    private OnRemedyActionListener listener;

    public RemedyAdapter(List<Remedy> remedyList, Context context, OnRemedyActionListener listener) {
        this.remedyList = remedyList;
        this.context = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, description, price;
        Button editBtn, deleteBtn;

        public ViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.remedyImage);
            name = view.findViewById(R.id.remedyName);
            description = view.findViewById(R.id.remedyDescription);
            price = view.findViewById(R.id.remedyPrice);
            editBtn = view.findViewById(R.id.editBtn);
            deleteBtn = view.findViewById(R.id.deleteBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_remedy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remedy remedy = remedyList.get(position);
        holder.name.setText(remedy.getName());
        holder.description.setText(remedy.getDescription());
        holder.price.setText("₹" + remedy.getPrice());

        Glide.with(context).load(remedy.getImageUrl()).into(holder.image);

        holder.editBtn.setOnClickListener(v -> listener.onEdit(remedy));
        holder.deleteBtn.setOnClickListener(v -> listener.onDelete(remedy));
    }

    @Override
    public int getItemCount() {
        return remedyList.size();
    }
}

