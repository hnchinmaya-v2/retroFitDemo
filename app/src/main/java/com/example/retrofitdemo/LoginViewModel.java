package com.example.retrofitdemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private MutableLiveData<List<UserData>> userMutableLiveData;

    public LoginViewModel(@NonNull @NotNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        userMutableLiveData = dataRepository.getUserMutableLiveData();
    }

    public void getUser(String username, String password) {
        dataRepository.getUser(username, password);
    }

    public MutableLiveData<List<UserData>> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    // TODO: Implement the ViewModel
}