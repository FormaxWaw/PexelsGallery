package com.formax.pexelsgallery.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

public class PhotoSource implements Parcelable {

    private String original;
    private String large;
    private String large2x;

    public PhotoSource(String original, String large, String large2x) {
        this.original = original;
        this.large = large;
        this.large2x = large2x;
    }

    @Ignore
    public PhotoSource() {
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getLarge2x() {
        return large2x;
    }

    public void setLarge2x(String large2x) {
        this.large2x = large2x;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original);
        dest.writeString(large);
        dest.writeString(large2x);
    }

    @Override
    public String toString() {
        return "PhotoSource{" +
                "original='" + original + '\'' +
                ", large='" + large + '\'' +
                ", large2x='" + large2x + '\'' +
                '}';
    }

    @Ignore
    protected PhotoSource(Parcel in) {
        original = in.readString();
        large = in.readString();
        large2x = in.readString();
    }

    public static final Creator<PhotoSource> CREATOR = new Creator<PhotoSource>() {
        @Override
        public PhotoSource createFromParcel(Parcel in) {
            return new PhotoSource(in);
        }

        @Override
        public PhotoSource[] newArray(int size) {
            return new PhotoSource[size];
        }
    };
}
