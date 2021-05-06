package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static  List<TagObject> tagObjectList = new ArrayList<>();
    private TagAdapter tagAdapter = new TagAdapter(tagObjectList);
    private Button tag_confirm;
    private EditText search_text;
    private ImageView search_img;
    private ImageView tag_user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        recyclerView = findViewById(R.id.recycerView_tagUsers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(tagAdapter);
        tag_confirm = findViewById(R.id.tag_confirm);
        recyclerView.setLayoutManager(linearLayoutManager);
        tag_user = findViewById(R.id.tag_users_img);

        search_img = findViewById(R.id.tag_search_img);
        search_text = findViewById(R.id.tag_search_text);
        tag_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });


        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();

            }
        });

    }



    private void searchUsers() {

        if (!TextUtils.isEmpty(search_text.getText().toString())) {
            tag_user.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            tag_confirm.setVisibility(View.VISIBLE);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
            Query q = databaseReference.orderByChild("Name").startAt(search_text.getText().toString()).endAt(search_text.getText().toString() + "\uff8f");

            q.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()) {


                        String name = "";
                        String email = "";
                        String user_profile = "";
                        String uid = snapshot.getRef().getKey();
                        if (snapshot.child("Name").getValue() != null) {
                            name = snapshot.child("Name").getValue().toString();
                        }
                        if (snapshot.child("ProfileImage").getValue() != null) {
                            user_profile = snapshot.child("ProfileImage").getValue().toString();
                        }
                        if (snapshot.child("email").getValue() != null) {
                            email = snapshot.child("email").getValue().toString();
                        }

                        if (!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) && !TagAdapter.checkTagUsers.contains(uid)) {
                            TagObject chatObject = new TagObject(name, user_profile, uid, false);
                            if (!tagObjectList.contains(chatObject)) {
                                tagObjectList.add(chatObject);
                                tagAdapter.notifyDataSetChanged();
                            }
                        }
                    }else{
                        tag_user.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        tag_confirm.setVisibility(View.GONE);
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

        }else{
            tag_user.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tag_confirm.setVisibility(View.GONE);
            Toast.makeText(this, "Field is Empty", Toast.LENGTH_SHORT).show();
        }
    }


}