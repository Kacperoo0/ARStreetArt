package com.example.arstreetart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    public interface OnGraffitiClickListener {
        void onGraffitiClick(Graffiti graffiti);
    }

    private final List<Graffiti> graffitiList;
    private final OnGraffitiClickListener listener;

    public GalleryAdapter(List<Graffiti> graffitiList, OnGraffitiClickListener listener) {
        this.graffitiList = graffitiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_graffiti, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Graffiti graffiti = graffitiList.get(position);
        byte[] imageBytes = Base64.decode(graffiti.getBitmapBase64(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.imageView.setImageBitmap(bitmap);

        holder.itemView.setOnClickListener(v -> listener.onGraffitiClick(graffiti));
    }

    @Override
    public int getItemCount() {
        return graffitiList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}