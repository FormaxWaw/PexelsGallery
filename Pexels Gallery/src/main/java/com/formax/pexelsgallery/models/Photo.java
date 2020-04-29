package com.formax.pexelsgallery.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class Photo implements Parcelable {

    @NonNull
    @PrimaryKey
    private String url;
    private String photographer;
    @Embedded
    private PhotoSource src;

    @Ignore
    public Photo() {
    }

    public Photo(@NonNull String url, String photographer, PhotoSource src) {
        this.url = url;
        this.photographer = photographer;
        this.src = src;
    }

    @Ignore
    protected Photo(Parcel in) {
        url = in.readString();
        photographer = in.readString();
        src = in.readParcelable(PhotoSource.class.getClassLoader());
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotographer() {
        return photographer;
    }

    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    public PhotoSource getSrc() {
        return src;
    }

    public void setSrc(PhotoSource src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "Photo{" +
                ", url='" + url + '\'' +
                ", photographer='" + photographer + '\'' +
                ", photoSource=" + src +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(photographer);
        dest.writeParcelable(src, flags);
    }
}
