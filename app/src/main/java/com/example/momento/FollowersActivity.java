package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FollowersActivity extends AppCompatActivity {

    private List<OnlyChatObject> onlyChatObjectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private  FollowersAdapter followersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        recyclerView = findViewById(R.id.followers_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        followersAdapter = new FollowersAdapter(onlyChatObjectList);
        recyclerView.setAdapter(followersAdapter);


        getFollowers();

    }


    private void getFollowers() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("followers");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    final  String ref = snapshot.getRef().getKey();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(ref);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String,Object> map = (Map<String, Object>) snapshot.getValue();
                            String name  = null;
                            String image = null;
                            if(map.get("Name")!= null){
                                name = map.get("Name").toString();
                            } if(map.get("ProfileImage")!= null){
                                image = map.get("ProfileImage").toString();
                            }
                            OnlyChatObject onlyChatObject = new OnlyChatObject(image, name,ref,false);
                                if(!onlyChatObjectList.contains(onlyChatObject)) {

                                    onlyChatObjectList.add(onlyChatObject);
                                    followersAdapter.notifyDataSetChanged();
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