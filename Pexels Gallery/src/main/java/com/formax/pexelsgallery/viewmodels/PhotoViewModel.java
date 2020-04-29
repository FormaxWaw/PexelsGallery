package com.formax.pexelsgallery.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.repositories.PexelsRepository;
import com.formax.pexelsgallery.repositories.PhotoDatabaseRepository;

import java.util.List;

public class PhotoViewModel extends AndroidViewModel {

    private PexelsRepository repository;
    private boolean isShowingPhoto;
    private PhotoDatabaseRepository photoDatabaseRepository;
    private LiveData<List<Photo>> allPhotosFromDatabase;
    private boolean isSavedInDatabase;

    public PhotoViewModel(Application application) {
        super(application);
        repository = PexelsRepository.getInstance();
        isShowingPhoto = false;
        photoDatabaseRepository = new PhotoDatabaseRepository(application);
        allPhotosFromDatabase = photoDatabaseRepository.getAllPhotos();
        isSavedInDatabase = false;
    }

    public LiveData<Photo> getPhoto() {
        return repository.getPhoto();
    }

    public void getRandomPhoto(int perPage, int page) {
        repository.getRandomPhoto(perPage, page);
    }

    public LiveData<List<Photo>> getAllPhotosFromDatabase() {
        return allPhotosFromDatabase;
    }

    public void insert(Photo photo){
        photoDatabaseRepository.insert(photo);
    }

    public void delete(Photo photo){
        photoDatabaseRepository.delete(photo);
    }

    public boolean isShowingPhoto() {
        return isShowingPhoto;
    }

    public void setShowingPhoto(boolean showingPhoto) {
        isShowingPhoto = showingPhoto;
    }

    public boolean isSavedInDatabase() {
        return isSavedInDatabase;
    }

    public void setSavedInDatabase(boolean savedInDatabase) {
        isSavedInDatabase = savedInDatabase;
    }
}
