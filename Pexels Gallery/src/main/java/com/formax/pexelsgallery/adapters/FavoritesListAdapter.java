package com.formax.pexelsgallery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.formax.pexelsgallery.R;
import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.viewmodels.FavoritesViewModel;

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private List<Photo> photos;
    private Context context;
    private OnBookmarkListener onBookmarkListener;

    public FavoritesListAdapter(Context context, OnBookmarkListener onBookmarkListener) {
        this.context = context;
        this.onBookmarkListener = onBookmarkListener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites_photo, parent, false);
        return new FavoritesViewHolder(view, onBookmarkListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.lightGrey);
        Glide.with(holder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(photos.get(position).getSrc().getLarge())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.imagePhoto);

        holder.imageBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        holder.textPhotographer.setText(context.getResources().getString(R.string.photo_by, photos.get(position).getPhotographer()));
    }

    @Override
    public int getItemCount() {
        if (photos != null) {
            return photos.size();
        } else return 0;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public Photo getPhotoPosition(int position) {
        if (photos != null) {
            if (photos.size() > 0) {
                return photos.get(position);
            }
        }
        return null;
    }
}
