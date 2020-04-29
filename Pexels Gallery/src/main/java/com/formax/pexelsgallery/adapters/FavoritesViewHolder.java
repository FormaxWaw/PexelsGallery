package com.formax.pexelsgallery.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.formax.pexelsgallery.R;

public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imagePhoto, imageBookmark;
    TextView textPhotographer;
    private OnBookmarkListener onBookmarkListener;

    FavoritesViewHolder(@NonNull View itemView, OnBookmarkListener onBookmarkListener) {
        super(itemView);

        this.onBookmarkListener = onBookmarkListener;

        imagePhoto = itemView.findViewById(R.id.img_image);
        imageBookmark = itemView.findViewById(R.id.img_bookmark);
        textPhotographer = itemView.findViewById(R.id.tv_photographer);

        imageBookmark.setOnClickListener(this);
        imagePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == imageBookmark) {
            onBookmarkListener.onBookmarkClick(getAdapterPosition());
        } else if (v == imagePhoto) {
            onBookmarkListener.onPhotoClick(getAdapterPosition());
        }
    }
}
