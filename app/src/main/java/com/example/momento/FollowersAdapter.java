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

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private List<OnlyChatObject> onlyChatObjects;

    public FollowersAdapter(List<OnlyChatObject> onlyChatObjects) {
        this.onlyChatObjects = onlyChatObjects;
    }

    @NonNull
    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.only_chat_layoout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersAdapter.ViewHolder holder, int position) {
        String image = onlyChatObjects.get(position).getProfileImage();
        String name = onlyChatObjects.get(position).getName();

        holder.setData(image, name);
    }

    @Override
    public int getItemCount() {
        return onlyChatObjects.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder  {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chat_person_image);
            textView = itemView.findViewById(R.id.chat_user_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), OthersProfile.class);
                    intent.putExtra("UserUid", onlyChatObjects.get(getLayoutPosition()).getUid());

                    itemView.getContext().startActivity(intent);
                }
            });

            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserchatId", onlyChatObjects.get(getLayoutPosition()).getUid());
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }
            });

             */

        }

        public void setData(String resources, String name) {
            Glide.with(itemView.getContext()).load(resources).apply(new RequestOptions().placeholder(R.drawable.user)).into(imageView);
            textView.setText(name);
        }
    }
}
