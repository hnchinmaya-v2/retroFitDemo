package com.example.retrofitdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RV_Adapter.onButtonClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ExampleViewModel mViewModel;
    private TextView postTextView;
    private Button getButton;
    private Button postButton;
    private Post updatedPost;
    private int positionToUpdate;
    private RV_Adapter mAdapter;

    private final int REQUEST_CODE_LOGIN = 1;
    private final int REQUEST_CODE_CAMERA = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("login", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        }

        mViewModel = new ViewModelProvider(this).get(ExampleViewModel.class);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RV_Adapter();
        mAdapter.setOnButtonClickListener(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        postTextView = findViewById(R.id.tv_post);
        getButton = findViewById(R.id.button_get);
        postButton = findViewById(R.id.button_post);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.fetchData();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.postData(20, "abc", "def");
            }
        });

        mViewModel.getAllPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                if (posts != null) {
                    mAdapter.setExampleList((ArrayList<Post>) posts);
                }
            }
        });


        mViewModel.getLiveDataPost().observe(this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                postTextView.setText(post.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.button_logout) {
            mViewModel.deleteAllPosts();
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("login", false);
            editor.apply();
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == MainActivity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap imageBitmap = (Bitmap) bundle.get("data");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();
                update(bArray);
            }
        }
    }


    private void chooseImage() {
        if (checkAndRequestPermission()) {
            imageFromCamera();
        }


    }

    private void imageFromCamera() {
        Intent fromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(fromCamera, 2);

    }


    private boolean checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imageFromCamera();
        } else
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void insertImage(Post post, int position) {
        updatedPost = post;
        positionToUpdate = position;
        chooseImage();
    }

    private void update(byte[] image) {
        updatedPost.setImage(image);
        mViewModel.updatePost(updatedPost);
        mAdapter.setPost(updatedPost, positionToUpdate);
    }
}