package com.formax.pexelsgallery.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.formax.pexelsgallery.AppExecutors;
import com.formax.pexelsgallery.database.PhotoDao;
import com.formax.pexelsgallery.database.PhotoDatabase;
import com.formax.pexelsgallery.models.Photo;

import java.util.List;

public class PhotoDatabaseRepository {

    private PhotoDao photoDao;
    private LiveData<List<Photo>> allPhotos;

    public PhotoDatabaseRepository(Application application){
        PhotoDatabase database = PhotoDatabase.getInstance(application);
        photoDao = database.photoDao();
        allPhotos = photoDao.getAllPhotosFromDatabase();
    }

    public LiveData<List<Photo>> getAllPhotos() {
        return allPhotos;
    }

    public void insert(Photo photo){
        AppExecutors.get().getDatabaseWriteExecutor().execute(new Runnable() {
            @Override
            public void run() {
                photoDao.insert(photo);
            }
        });
    }

    public void delete(Photo photo){
        AppExecutors.get().getDatabaseWriteExecutor().execute(new Runnable() {
            @Override
            public void run() {
                photoDao.delete(photo);
            }
        });
    }

    public void deleteAll(){
        AppExecutors.get().getDatabaseWriteExecutor().execute(new Runnable() {
            @Override
            public void run() {
                photoDao.deleteAll();
            }
        });
    }
}
