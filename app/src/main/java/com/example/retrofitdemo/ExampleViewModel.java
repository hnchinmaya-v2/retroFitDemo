package com.example.retrofitdemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExampleViewModel extends AndroidViewModel {  //Main Activity View Model


    private MutableLiveData<Post> postMutableLiveData = new MutableLiveData<>();

    private LiveData<List<Post>> allPosts;

    private DataRepository dataRepository;

    public ExampleViewModel(@NonNull @NotNull Application application) {
        super(application);
        dataRepository = new DataRepository(application);
        allPosts = dataRepository.getAllPosts();
    }

    public void updatePost(Post post) {
        dataRepository.update(post);
    }

    public LiveData<Post> getLiveDataPost() {
        return this.postMutableLiveData;
    }

    public LiveData<List<Post>> getAllPosts() {
        return this.allPosts;
    }

    public void fetchData() {
        dataRepository.getPosts();
    }

    public void postData(int userId, String title, String text) {
        Post post = new Post(userId, title, text);
        dataRepository.postData(post, postMutableLiveData);
    }

    public void deleteAllPosts() {
        dataRepository.deleteAllPosts();
    }

}
