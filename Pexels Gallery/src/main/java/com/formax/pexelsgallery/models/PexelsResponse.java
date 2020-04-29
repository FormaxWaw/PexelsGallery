package com.formax.pexelsgallery.models;

import java.util.List;

public class PexelsResponse {

    private int page;
    private int per_page;
    private int total_results;
    private String url;
    private String next_page;
    private List<Photo> photos;

    public PexelsResponse(int page, int per_page, int total_results, String url, String next_page, List<Photo> photos) {
        this.page = page;
        this.per_page = per_page;
        this.total_results = total_results;
        this.url = url;
        this.next_page = next_page;
        this.photos = photos;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "PexelsResponse{" +
                "page=" + page +
                ", per_page=" + per_page +
                ", total_results=" + total_results +
                ", url='" + url + '\'' +
                ", next_page='" + next_page + '\'' +
                ", photos=" + photos +
                '}';
    }
}
