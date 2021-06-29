package com.example.retrofitdemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

public class SignUpViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private MutableLiveData<Boolean> usernameTaken;

    public LiveData<Boolean> getUsernameTaken() {
        return this.usernameTaken;
    }

    public SignUpViewModel(@NonNull @NotNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        usernameTaken = dataRepository.getUsernameTakenLiveData();
    }

    public void insertUserData(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        dataRepository.insertUser(userData);
    }


}