package com.formax.pexelsgallery.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.formax.pexelsgallery.models.Photo;
import com.formax.pexelsgallery.repositories.PhotoDatabaseRepository;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {

    private PhotoDatabaseRepository photoDatabaseRepository;
    private LiveData<List<Photo>> allPhotosFromDatabase;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        photoDatabaseRepository = new PhotoDatabaseRepository(application);
        allPhotosFromDatabase = photoDatabaseRepository.getAllPhotos();
    }

    public LiveData<List<Photo>> getAllPhotosFromDatabase() {
        return allPhotosFromDatabase;
    }

    public void delete(Photo photo){
        photoDatabaseRepository.delete(photo);
    }

}
