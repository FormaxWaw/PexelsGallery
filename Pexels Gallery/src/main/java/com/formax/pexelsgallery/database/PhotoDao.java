package com.formax.pexelsgallery.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.formax.pexelsgallery.models.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Photo photo);

    @Delete
    void delete(Photo photo);

    @Query("DELETE FROM photo_table")
    void deleteAll();

    @Query("SELECT * FROM photo_table")
    LiveData<List<Photo>> getAllPhotosFromDatabase();
}
