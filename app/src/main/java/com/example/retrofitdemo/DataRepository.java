package com.example.retrofitdemo;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {
    private PostDao postDao;
    private UserDataDao userDataDao;
    private LiveData<List<Post>> allPosts;
    private MutableLiveData<List<UserData>> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> usernameTakenLiveData = new MutableLiveData<>();

    public DataRepository(Application application) {
        PostDatabase postDatabase = PostDatabase.getInstance(application);
        postDao = postDatabase.postDao();
        userDataDao = postDatabase.userDataDao();
        allPosts = postDao.getAllPosts();
    }

    private JsonPlaceHolderAPI jsonPlaceHolderAPI = RetroFitImpl.getRetrofit().create(JsonPlaceHolderAPI.class);

    public void getPosts() {
        if (allPosts.getValue().isEmpty()) {
            Call<List<Post>> call = jsonPlaceHolderAPI.getPosts();
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    assert response.body() != null;
                    for (Post post : response.body()) {
                        insert(post);
                    }
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {

                }
            });
        } else {
            Log.i("getPosts", "Posts already in DB");
        }
    }

    public void postData(Post post, MutableLiveData<Post> postMutableLiveData) {

        Call<Post> call = jsonPlaceHolderAPI.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                postMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<List<UserData>> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void insert(Post post) {
        new InsertPostAsyncTask(postDao).execute(post);
    }

    public void update(Post post) {
        new UpdatePostAsyncTask(postDao).execute(post);
    }

    public void deleteAllPosts() {
        new DeleteAllPostsAsyncTask(postDao).execute();
    }

    public LiveData<List<Post>> getAllPosts() {
        return allPosts;
    }

    public void insertUser(UserData userData) {
        new InsertUserDataAsyncTask(userDataDao,usernameTakenLiveData).execute(userData);
    }

    public void getUser(String username, String password) {
        new GetUserAsyncTask(userDataDao,userMutableLiveData).execute(username, password);
    }

    public MutableLiveData<Boolean> getUsernameTakenLiveData() {
        return usernameTakenLiveData;
    }


    private static class InsertPostAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao postDao;

        private InsertPostAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Post... posts) {
            postDao.insert(posts[0]);
            return null;
        }
    }

    private static class UpdatePostAsyncTask extends AsyncTask<Post, Void, Void> {

        private PostDao postDao;

        private UpdatePostAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Post... posts) {
            postDao.update(posts[0]);
            return null;
        }
    }

    private static class DeleteAllPostsAsyncTask extends AsyncTask<Void, Void, Void> {

        private PostDao postDao;

        private DeleteAllPostsAsyncTask(PostDao postDao) {
            this.postDao = postDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            postDao.deleteAllPosts();
            return null;
        }
    }

    private static class InsertUserDataAsyncTask extends AsyncTask<UserData, Void, Void> {

        private UserDataDao userDataDao;
        private MutableLiveData<Boolean> usernameTakenLiveData;
        boolean success;

        private InsertUserDataAsyncTask(UserDataDao userDataDao, MutableLiveData<Boolean> usernameTakenLiveData) {
            this.userDataDao = userDataDao;
            this.usernameTakenLiveData = usernameTakenLiveData;
        }

        @Override
        protected Void doInBackground(UserData... userData) {
            try {
                userDataDao.insertUser(userData[0]);
                success = true;
            } catch (Exception e) {
                success = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            usernameTakenLiveData.setValue(!success);
        }
    }

    private static class GetUserAsyncTask extends AsyncTask<String, Void, Void> {

        private UserDataDao userDataDao;
        private MutableLiveData<List<UserData>> userMutableLiveData;
        private List<UserData> user;

        private GetUserAsyncTask(UserDataDao userDataDao, MutableLiveData<List<UserData>> userMutableLiveData) {
            this.userDataDao = userDataDao;
            this.userMutableLiveData = userMutableLiveData;
        }

        @Override
        protected Void doInBackground(String... strings) {
            user = userDataDao.getUser(strings[0], strings[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            userMutableLiveData.setValue(user);
        }
    }

}
