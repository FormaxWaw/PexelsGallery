package com.formax.pexelsgallery.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.formax.pexelsgallery.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageView;
    private OnPhotoListener onPhotoListener;

    PhotoViewHolder(@NonNull View itemView, OnPhotoListener onPhotoListener) {
        super(itemView);
        this.onPhotoListener = onPhotoListener;

        imageView = itemView.findViewById(R.id.img_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onPhotoListener.onPhotoClick(getAdapterPosition());
    }
}
