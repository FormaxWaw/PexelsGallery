package com.formax.pexelsgallery.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.repositories.PexelsRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = "PexelsViewModel";

    private PexelsRepository pexelsRepository;
    private boolean isPerformingQuery;
    private boolean isShowingCuratedPhotos;
    private boolean isShowingFirstTime;

    public MainViewModel() {
        pexelsRepository = PexelsRepository.getInstance();
        isShowingCuratedPhotos = true;
        isShowingFirstTime = true;
    }

    public LiveData<List<Photo>> getPhotos() {
        return pexelsRepository.getPhotos();
    }

    public LiveData<Boolean> isQueryExhausted() {
        return pexelsRepository.isQueryExhausted();
    }

    public boolean isPerformingQuery() {
        return isPerformingQuery;
    }

    public void setPerformingQuery(boolean performingQuery) {
        isPerformingQuery = performingQuery;
    }

    public void getCuratedPhotos(int perPage, int page) {
        isPerformingQuery = true;
        pexelsRepository.getCuratedPhotos(perPage, page);
    }

    public void getCuratedPhotosNextPage() {
        if (!isQueryExhausted().getValue()) {
            pexelsRepository.getCuratedPhotosNextPage();
        }
    }

    public void searchPhotos(String query, int perPage, int page) {
        isPerformingQuery = true;
        pexelsRepository.searchPhotos(query, perPage, page);
    }

    public void searchPhotosNextPage() {
        if (!isQueryExhausted().getValue()) {
            pexelsRepository.searchPhotosNextPage();
        }
    }

    public boolean isShowingFirstTime() {
        return isShowingFirstTime;
    }

    public void setShowingFirstTime(boolean showingFirstTime) {
        isShowingFirstTime = showingFirstTime;
    }

    public boolean isShowingCuratedPhotos() {
        return isShowingCuratedPhotos;
    }

    public void setShowingCuratedPhotos(boolean showingCuratedPhotos) {
        isShowingCuratedPhotos = showingCuratedPhotos;
    }

    public boolean onBackPressed() {
        if (isPerformingQuery) {
            pexelsRepository.cancelRequest();
            isPerformingQuery = false;
        }
        if (!isShowingCuratedPhotos) {
            isShowingCuratedPhotos = true;
            return true;
        }
        return false;
    }
}
