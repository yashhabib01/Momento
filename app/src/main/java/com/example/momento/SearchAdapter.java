package com.example.momento;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<ChatObject> searchList;

    public SearchAdapter(List<ChatObject> searchList) {
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_itemlayout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ViewHolder holder,  int position) {
        String image = searchList.get(position).getUser_image();
        String name = searchList.get(position).getUser_name();

        if(UserINformation.listFollowing.contains(searchList.get(holder.getLayoutPosition()).getUid())){
            holder.btn.setText("Following");
        }else{
            holder.btn.setText("Follow");
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user  = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(!UserINformation.listFollowing.contains(searchList.get(holder.getLayoutPosition()).getUid())) {
                    holder.btn.setText("Following");
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("following").child(searchList.get(holder.getLayoutPosition()).getUid()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(searchList.get(holder.getLayoutPosition()).getUid()).child("followers").child(user).setValue(true);
                    DatabaseReference check  = FirebaseDatabase.getInstance().getReference().child("Users").child(searchList.get(holder.getLayoutPosition()).getUid()).child("following");
                    check.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(user)){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
                                String key  = databaseReference.push().getKey();

                                FirebaseDatabase.getInstance().getReference().child("Users").child(searchList.get(holder.getLayoutPosition()).getUid()).child("following").child(user).child("chatId").setValue(key);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("following").child(searchList.get(holder.getLayoutPosition()).getUid()).child("chatId").setValue(key);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("getChat").child(searchList.get(holder.getLayoutPosition()).getUid()).setValue(true);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(searchList.get(holder.getLayoutPosition()).getUid()).child("getChat").child(user).setValue(true);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }else{
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("following").child(searchList.get(holder.getLayoutPosition()).getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(searchList.get(holder.getLayoutPosition()).getUid()).child("followers").child(user).removeValue();

                    holder.btn.setText("Follow");
                }
            }
        });

        holder.setData(image, name);
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        public Button btn;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chat_person_image);
            textView = itemView.findViewById(R.id.chat_user_name);
            btn = itemView.findViewById(R.id.new_folow_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), OthersProfile.class);
                    intent.putExtra("UserUid", searchList.get(getLayoutPosition()).getUid());
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
