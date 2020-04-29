package com.formax.pexelsgallery.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.formax.pexelsgallery.models.Photo;

@Database(entities = Photo.class, version = 1, exportSchema = false)
public abstract class PhotoDatabase extends RoomDatabase {

    public abstract PhotoDao photoDao();
    private static volatile PhotoDatabase instance;

    public static PhotoDatabase getInstance(Context context){
        if (instance == null){
            synchronized (PhotoDatabase.class){
                if (instance==null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), PhotoDatabase.class, "photo_database.db").build();
                }
            }
        }
        return instance;
    }


}
