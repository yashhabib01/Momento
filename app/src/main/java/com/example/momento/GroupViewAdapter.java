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

public class GroupViewAdapter extends RecyclerView.Adapter<GroupViewAdapter.ViewHolder> {

    List<ChatObject> chatObjectList;


    public GroupViewAdapter(List<ChatObject> chatObjectList) {
        this.chatObjectList = chatObjectList;
    }

    @NonNull
    @Override
    public GroupViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.only_chat_layoout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewAdapter.ViewHolder holder, int position) {

        holder.setData(chatObjectList.get(position).getUser_image(),chatObjectList.get(position).getUser_name());
    }

    @Override
    public int getItemCount() {
        return chatObjectList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView, check;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chat_person_image);
            textView = itemView.findViewById(R.id.chat_user_name);
            check = itemView.findViewById(R.id.check_img);
        }
        public void setData(String resources, String name){
            Glide.with(itemView.getContext()).load(resources).apply(new RequestOptions().placeholder(R.drawable.user)).into(imageView);
            textView.setText(name);
        }
    }
}
