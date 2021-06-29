package com.example.retrofitdemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDataDao {

    @Insert
    void insertUser(UserData userData);

    @Query("SELECT * FROM user_data_table WHERE username = :inputUsername AND password = :inputPassword ")
    List<UserData> getUser(String inputUsername, String inputPassword);
}
