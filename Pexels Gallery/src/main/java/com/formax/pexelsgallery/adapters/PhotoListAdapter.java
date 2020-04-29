package com.formax.pexelsgallery.adapters;

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

import java.util.ArrayList;
import java.util.List;

public class PhotoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PhotoListAdapter";

    private static final int PHOTO_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int NO_RECORDS_TYPE = 3;
    private static final int NO_MORE_RECORDS_TYPE = 4;

    private List<Photo> photos;
    private OnPhotoListener onPhotoListener;

    public PhotoListAdapter(OnPhotoListener onPhotoListener) {
        this.onPhotoListener = onPhotoListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view);
            case NO_RECORDS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_records, parent, false);
                return new NoRecordsViewHolder(view);
            case NO_MORE_RECORDS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_more_results, parent, false);
                return new ExhaustedViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
                return new PhotoViewHolder(view, onPhotoListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if (itemViewType == PHOTO_TYPE) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.color.lightGrey);

            Glide.with(holder.itemView.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(photos.get(position).getSrc().getLarge())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(((PhotoViewHolder) holder).imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (photos.get(position).getPhotographer().equals("loading")) {
            return LOADING_TYPE;
        } else if (photos.get(position).getPhotographer().equals("no more records")) {
            return NO_MORE_RECORDS_TYPE;
        } else if (photos.get(position).getPhotographer().equals("no records")) {
            return NO_RECORDS_TYPE;
        } else if (position == photos.size() - 1 && position != 0 && !photos.get(position).getPhotographer().equals("no more records")) {
            return LOADING_TYPE;
        } else {
            return PHOTO_TYPE;
        }
    }

    public void setExhausted() {
        hideLoading();
        Photo photo = new Photo();
        photo.setPhotographer("no more records");
        photos.add(photo);
        notifyDataSetChanged();
    }

    private void hideLoading() {
        if (isLoading()) {
            for (Photo p : photos) {
                if (p.getPhotographer().equals("loading")) {
                    photos.remove(p);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading() {
        if (!isLoading()) {
            Photo photo = new Photo();
            photo.setPhotographer("loading");
            List<Photo> currentList = new ArrayList<>();
            currentList.add(photo);
            photos = currentList;
            notifyDataSetChanged();
        }
    }

    public void displayNoMorePhotosImage() {
        Photo photo = new Photo();
        photo.setPhotographer("no records");
        List<Photo> currentList = new ArrayList<>();
        currentList.add(photo);
        photos = currentList;
        notifyDataSetChanged();
    }

    private boolean isLoading() {
        if (photos.size() > 0) {
            return photos.get(photos.size() - 1).getPhotographer().equals("loading");
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (photos != null) {
            return photos.size();
        }
        return 0;
    }

    public Photo getSelectedPhoto(int position) {
        if (photos != null) {
            if (photos.size() > 0) {
                return photos.get(position);
            }
        }
        return null;
    }

    public void setPhotos(List<Photo> photoList) {
        photos = photoList;
        notifyDataSetChanged();
    }
}
