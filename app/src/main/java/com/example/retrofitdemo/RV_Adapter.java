package com.example.retrofitdemo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RV_Adapter extends RecyclerView.Adapter<RV_Adapter.ExampleViewHolder> {

    @NonNull
    private ArrayList<Post> mExampleList = new ArrayList<>();
    private onButtonClickListener onButtonClickListener;

    public void setPost(Post post, int position) {
        mExampleList.set(position, post);
        notifyItemChanged(position);
    }

    public interface onButtonClickListener {
        void insertImage(Post post, int position);
    }

    public void setOnButtonClickListener(onButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewID;
        public TextView textViewUserID;
        public TextView textViewTitle;
        public Button imageButton;
        public ImageView imageView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            textViewID = itemView.findViewById(R.id.tv_id);
            textViewUserID = itemView.findViewById(R.id.tv_user_id);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            imageButton = itemView.findViewById(R.id.button_add_image);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public void setExampleList(ArrayList<Post> results) {
        this.mExampleList.clear();
        this.mExampleList.addAll(results);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Post currentItem = mExampleList.get(position);
        holder.textViewTitle.setText((currentItem.getTitle()));
        holder.textViewID.setText(((Integer) currentItem.getId()).toString());
        holder.textViewUserID.setText(((Integer) currentItem.getUserId()).toString());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onButtonClickListener != null) {
                    onButtonClickListener.insertImage(mExampleList.get(position), position);
                }

            }
        });
        byte[] postImage = mExampleList.get(position).getImage();
        if (postImage != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            Bitmap b1 = BitmapFactory.decodeByteArray(postImage, 0, postImage.length);
            holder.imageView.setImageBitmap(b1);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
