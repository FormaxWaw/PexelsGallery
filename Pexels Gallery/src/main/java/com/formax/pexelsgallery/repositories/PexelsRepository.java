package com.formax.pexelsgallery.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.requests.PexelsAPIClient;

import java.util.List;

public class PexelsRepository {

    private static final String TAG = "PexelsRepository";

    private static PexelsRepository instance;
    private PexelsAPIClient pexelsAPIClient;
    private int mPage;
    private int mPerPage;
    private String mQuery;
    private MutableLiveData<Boolean> isQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Photo>> mediatorLiveData = new MediatorLiveData<>();

    private PexelsRepository() {
        pexelsAPIClient = PexelsAPIClient.getInstance();
        initMediators();
    }

    public static PexelsRepository getInstance() {
        if (instance == null) {
            instance = new PexelsRepository();
        }
        return instance;
    }

    public LiveData<List<Photo>> getPhotos() {
        return mediatorLiveData;
    }

    private void initMediators() {
        LiveData<List<Photo>> photoListApiSource = pexelsAPIClient.getPhotos();
        mediatorLiveData.addSource(photoListApiSource, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                if (photos != null) {
                    mediatorLiveData.setValue(photos);
                    doneQuery(photos);
                } else {
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Photo> list) {
        if (list != null) {
            if (list.size() % 20 != 0) {
                isQueryExhausted.setValue(true);
            }
        } else {
            isQueryExhausted.setValue(true);
        }
    }

    public LiveData<Photo> getPhoto() {
        return pexelsAPIClient.getRandomPhoto();
    }

    public LiveData<Boolean> isQueryExhausted() {
        return isQueryExhausted;
    }

    public void getCuratedPhotos(int perPage, int page) {
        if (page == 0) {
            page = 1;
        }
        mPerPage = perPage;
        mPage = page;
        isQueryExhausted.setValue(false);
        pexelsAPIClient.getCuratedPhotos(perPage, page);
    }

    public void getCuratedPhotosNextPage() {
        getCuratedPhotos(mPerPage, mPage + 1);
    }

    public void searchPhotos(String query, int perPage, int page) {
        if (page == 0) {
            page = 1;
        }
        mPerPage = perPage;
        mPage = page;
        mQuery = query;
        isQueryExhausted.setValue(false);
        pexelsAPIClient.searchPhotos(query, perPage, page);
    }

    public void searchPhotosNextPage() {
        searchPhotos(mQuery, mPerPage, mPage + 1);
    }

    public void getRandomPhoto(int perPage, int page) {
        pexelsAPIClient.getRandomPhoto(perPage, page);
    }

    public void cancelRequest() {
        pexelsAPIClient.cancelRequest();
    }

}
