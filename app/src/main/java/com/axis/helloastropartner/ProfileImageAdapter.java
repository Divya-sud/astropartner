package com.axis.helloastropartner;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProfileImageAdapter extends RecyclerView.Adapter<ProfileImageAdapter.ViewHolder> {
    private List<String> imageUrls;
    public ProfileImageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    @NonNull
    @Override
    public ProfileImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new RecyclerView.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileImageAdapter.ViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
                .load(imageUrls.get(position))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ViewHolder(ImageView view) {
            super(view);
            imageView = view;
        }
    }
}
