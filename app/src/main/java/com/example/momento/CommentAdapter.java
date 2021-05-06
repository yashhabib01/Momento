package com.example.momento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<CommentObject> commentObjects;

    public CommentAdapter(List<CommentObject> commentObjects) {
        this.commentObjects = commentObjects;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_comment_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        String image = commentObjects.get(position).getImage();
        String name = commentObjects.get(position).getName();
        String time = commentObjects.get(position).getTime();
        String comment = commentObjects.get(position).getComment();

        holder.setData(image,name,time,comment);
    }

    @Override
    public int getItemCount() {
        return commentObjects.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView name;
        private TextView time;
        private TextView comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.feed_comment_image);
            name = itemView.findViewById(R.id.feed_comment_name);
            time = itemView.findViewById(R.id.feed_comment_time);
            comment = itemView.findViewById(R.id.feed_comment);
        }

        public void setData(String resources, String user, String ago, String about){
            Glide.with(itemView.getContext()).load(resources).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).into(imageView);
            name.setText(user);
            time.setText(ago);
            comment.setText(about);
        }
    }
}
