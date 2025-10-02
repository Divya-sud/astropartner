package com.axis.helloastropartner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class POCAdapter extends RecyclerView.Adapter<POCAdapter.ViewHolder> {

    private List<POCModel> list;

    public POCAdapter(List<POCModel> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public POCAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POCAdapter.ViewHolder holder, int position) {
        POCModel item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.pocTitle);
            description = itemView.findViewById(R.id.pocDescription);
        }
    }
}
