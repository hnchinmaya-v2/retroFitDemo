package com.example.retrofitdemo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class, UserData.class}, version = 1)
public abstract class PostDatabase extends RoomDatabase {
    private static PostDatabase instance;

    public abstract PostDao postDao();

    public abstract UserDataDao userDataDao();

    public static synchronized PostDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    PostDatabase.class, "post_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
