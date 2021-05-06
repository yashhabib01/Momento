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

public class FeedTagAdapter extends RecyclerView.Adapter<FeedTagAdapter.ViewHolder> {

    List<FeedTagObject> feedTagObjectList;

    public FeedTagAdapter(List<FeedTagObject> feedTagObjectList) {
        this.feedTagObjectList = feedTagObjectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.tagusersfeed,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setImage(feedTagObjectList.get(holder.getLayoutPosition()).getImage() , feedTagObjectList.get(holder.getLayoutPosition()).getName());

    }

    @Override
    public int getItemCount() {
        return feedTagObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView imageView;
       TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView  = itemView.findViewById(R.id.taguserfeed_image);
            textView = itemView.findViewById(R.id.tag_user_list_name);
        }

        public void setImage(String image,String Name) {
            Glide.with(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).centerCrop().into(imageView);
            textView.setText(Name);
        }
    }
}
