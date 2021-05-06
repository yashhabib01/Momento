package com.example.momento;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {
    @NonNull

    private List<ChatViewObject> chatViewObjects;

    public ChatViewAdapter(@NonNull List<ChatViewObject> chatViewObjects) {
        this.chatViewObjects = chatViewObjects;
    }

    @Override
    public ChatViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout_item,parent,false);
       RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       view.setLayoutParams(lp);
       return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ChatViewAdapter.ViewHolder holder, final int position) {




        String image     = null;

        holder.setTextView( chatViewObjects.get(position).getChat(),image);
        if(chatViewObjects.get(position).isCheckCreate() ){
            holder.container1.setGravity(Gravity.END);



        }else{
            holder.container1.setGravity(Gravity.START);
            holder.textView.setBackgroundTintList(ColorStateList.valueOf(R.color.textColor));


        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(chatViewObjects.get(position).getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                  if(snapshot.child("ProfileImage").getValue() != null){


                    String image     = snapshot.child("ProfileImage").getValue().toString();

                        holder.setTextView( chatViewObjects.get(position).getChat(),image);
                        if(chatViewObjects.get(position).isCheckCreate() ){
                            holder.container1.setGravity(Gravity.END);



                        }else{
                            holder.container1.setGravity(Gravity.START);
                            holder.textView.setBackgroundTintList(ColorStateList.valueOf(R.color.textColor));


                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return chatViewObjects.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        public TextView textView;
        private ImageView imageView;
        public LinearLayout container1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.chat_get_message);
            container1 = itemView.findViewById(R.id.chat_container);
            imageView = itemView.findViewById(R.id.chat_view_person);
        }

        public  void setTextView(String text , String img){
            textView.setText(text);
            Glide.with(itemView.getContext()).load(img).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).into(imageView);
        }
    }
}
