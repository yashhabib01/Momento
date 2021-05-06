package com.example.momento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.ViewHolder> {

    private List<MyFeedObject> myFeedObjectsList;
    public static  boolean  isLike = false;


    public MyFeedAdapter(List<MyFeedObject> myFeedObjectsList) {
        this.myFeedObjectsList = myFeedObjectsList;
    }

    @NonNull
    @Override
    public MyFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_feed_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyFeedAdapter.ViewHolder holder, int position) {
        String image =  myFeedObjectsList.get(position).getImageResources();
        String name =  myFeedObjectsList.get(position).getName();
        String time =  myFeedObjectsList.get(position).getTime();
        String user =  myFeedObjectsList.get(position).getUserImageResources();
        holder.setData(image, name, time, user);
        holder.name.setTag(myFeedObjectsList.get(position).getUid());
        holder.checkLikes();




        holder.like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLike = true;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(myFeedObjectsList.get(holder.getLayoutPosition()).getUid()).child("posts").child(myFeedObjectsList.get(holder.getLayoutPosition()).getPostRef()).child("postLikes");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(isLike) {
                                   if (snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                FirebaseDatabase.getInstance().getReference().child("Users").child(myFeedObjectsList.get(holder.getLayoutPosition()).getUid()).child("posts").child(myFeedObjectsList.get(holder.getLayoutPosition()).getPostRef()).child("postLikes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                isLike = !isLike;


                            }else{
                                FirebaseDatabase.getInstance().getReference().child("Users").child(myFeedObjectsList.get(holder.getLayoutPosition()).getUid()).child("posts").child(myFeedObjectsList.get(holder.getLayoutPosition()).getPostRef()).child("postLikes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                                isLike = !isLike;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });



    }

    @Override
    public int getItemCount() {
        return myFeedObjectsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder   {
        private ImageView imageView, user_image1;
        private TextView name, time;
        private ImageView like_image;
        private long postLikes = 0;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.myfeed_imageVIew);

            name = itemView.findViewById(R.id.myfeed_user_name);

            user_image1 = itemView.findViewById(R.id.myfeed_user_image);
            like_image = itemView.findViewById(R.id.likethepost);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent   =  new Intent(itemView.getContext(), FeedDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("userId",name.getTag().toString());
                    bundle.putString("postID", myFeedObjectsList.get(getLayoutPosition()).getPostRef());
                    bundle.putString("post_time",myFeedObjectsList.get(getLayoutPosition()).getTime());
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }
            });

            user_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), OthersProfile.class);
                    intent.putExtra("UserUid", myFeedObjectsList.get(getLayoutPosition()).getUid());
                    itemView.getContext().startActivity(intent);
                }
            });


        }




        public void checkLikes(){
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(myFeedObjectsList.get(getLayoutPosition()).getUid()).child("posts").child(myFeedObjectsList.get(getLayoutPosition()).getPostRef()).child("postLikes");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists() && snapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                            postLikes = snapshot.getChildrenCount();
                           Log.i("postLikes",String.valueOf(postLikes));
                             }else {

                        postLikes = snapshot.getChildrenCount();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Map<String,Object> map = new HashMap<>();
            map.put("totalLikes", postLikes);
     DatabaseReference data =    FirebaseDatabase.getInstance().getReference().child("Users").child(myFeedObjectsList.get(getLayoutPosition()).getUid()).child("posts").child(myFeedObjectsList.get(getLayoutPosition()).getPostRef());
     data.updateChildren(map);


        }


        public void setData(String image, String nameString , String time1, String user_image){
            Glide.with(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.loadref)).centerCrop().into(imageView);
            Glide.with(itemView.getContext()).load(user_image).apply(new RequestOptions().placeholder(R.drawable.userdefaultimage)).centerCrop().into(user_image1);
            name.setText(nameString);

        }



    }


}
