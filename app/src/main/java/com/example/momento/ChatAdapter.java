package com.example.momento;

import android.content.Intent;
import android.os.Bundle;
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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<OnlyChatObject> chatObjectList;


    public ChatAdapter(List<OnlyChatObject> chatObjectList) {
        this.chatObjectList = chatObjectList;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.only_chat_layoout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        String image = chatObjectList.get(position).getProfileImage();
        String name = chatObjectList.get(position).getName();
        if(chatObjectList.get(position).getCheck()) {
            holder.check.setImageResource(R.drawable.ic_baseline_supervisor_account_24);
        }

        holder.setData(image, name);
    }

    @Override
    public int getItemCount() {
        return chatObjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView, check;
        private TextView textView;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chat_person_image);
            textView = itemView.findViewById(R.id.chat_user_name);
            check = itemView.findViewById(R.id.check_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(itemView.getContext(),ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserchatId",chatObjectList.get(getLayoutPosition()).getUid());
                    bundle.putString("chat_user_name", chatObjectList.get(getLayoutPosition()).getName());
                    if(chatObjectList.get(getLayoutPosition()).getCheck()) {
                        bundle.putString("check", "group");
                    }else{
                        bundle.putString("check","single");
                    }
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

        public void setData(String resources, String name){
            Glide.with(itemView.getContext()).load(resources).apply(new RequestOptions().placeholder(R.drawable.user)).into(imageView);
                textView.setText(name);
        }
    }
}
