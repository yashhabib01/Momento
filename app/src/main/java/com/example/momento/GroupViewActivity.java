package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ChatObject> chatObjectList = new ArrayList<>();
    private String uid ;
    private ImageView imageView;
    private TextView text;
    private GroupViewAdapter groupViewAdapter = new GroupViewAdapter(chatObjectList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("groupUid");
        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();


        recyclerView = findViewById(R.id.view_group_recycler);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        linear.setOrientation(RecyclerView.VERTICAL);
        imageView = findViewById(R.id.view_group_img);
        text = findViewById(R.id.view_group_name);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(groupViewAdapter);
        groupViewAdapter.notifyDataSetChanged();

        DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Chat-Group").child(uid);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    String tit = "";
                    String img = "";

                    if(snapshot.child("name").getValue() != null){
                        tit = snapshot.child("name").getValue().toString();
                    } if(snapshot.child("groupImg").getValue() != null){
                        img = snapshot.child("groupImg").getValue().toString();
                    }

                    text.setText(tit);
                    Glide.with(getApplicationContext()).load(img).apply(new RequestOptions().placeholder(R.drawable.sd)).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getUsersInfo();
    }

    private void getUsersInfo() {
        DatabaseReference data  = FirebaseDatabase.getInstance().getReference().child("Chat-Group").child(uid).child("users");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot snapshot, @Nullable String previousChildName) {
               if(snapshot.exists()) {

                   DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.getKey());
                   userInfo.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot1) {

                           if(snapshot1.exists()){
                               String name = "";
                               String image="";

                               if(snapshot1.child("ProfileImage").getValue() != null){
                                   image = snapshot1.child("ProfileImage").getValue().toString();
                               }if(snapshot1.child("Name").getValue() != null){
                                   name = snapshot1.child("Name").getValue().toString();
                               }

                               chatObjectList.add(new ChatObject(image,name,snapshot.getKey()));
                               groupViewAdapter.notifyDataSetChanged();
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}