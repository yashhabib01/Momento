package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class OthersProfile extends AppCompatActivity {

    private GridView gridView;
    private TextView name;
    private TextView address;
    private ImageView userImage;
    private TextView about_me ;
    private Button follow;
    private  TextView follow_text;
    private TextView followers_text,profile_Layout_title;
    private TextView post_num;
    private List<String> list = new ArrayList<>();
    private OtherProfileGridAdapter otherProfileGridAdapter;
    private DatabaseReference mFollowUsers;

private String userUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        gridView = findViewById(R.id.gridView_moments);
        userUid = getIntent().getStringExtra("UserUid");
        name = findViewById(R.id.my_profile_name);
        address = findViewById(R.id.my_profile_country);
        profile_Layout_title = findViewById(R.id.profile_layout_title);
        userImage = findViewById(R.id.my_profile_userimage);
        follow_text = findViewById(R.id.other_following);
        followers_text = findViewById(R.id.other_followers);
        post_num = findViewById(R.id.other_posts);
        about_me = findViewById(R.id.my_profile_about_me);
        follow = findViewById(R.id.follow_btn);
        otherProfileGridAdapter = new OtherProfileGridAdapter(list);
        gridView.setAdapter(otherProfileGridAdapter);

        mFollowUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
       final String user  = FirebaseAuth.getInstance().getCurrentUser().getUid();


        getInformation();
        getProfiles();

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserINformation.listFollowing.contains(userUid)){
                    follow.setText("Follow");
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(userUid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                    UserINformation.listFollowing.remove(userUid);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).child("followers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").child(userUid).setValue(true);
                    DatabaseReference check  = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).child("following");
                    check.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(user)){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Chats");
                                String key  = databaseReference.push().getKey();

                                FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).child("following").child(user).child("chatId").setValue(key);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("following").child(userUid).child("chatId").setValue(key);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(user).child("getChat").child(userUid).setValue(true);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).child("getChat").child(user).setValue(true);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    follow.setText("Following");

                }
            }
        });

        if(UserINformation.listFollowing.contains(userUid)){
            follow.setText("Following");
        }else{
            follow.setText("Follow");
        }





    }

    private void getInformation() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String follow =  String.valueOf(snapshot.child("following").getChildrenCount());
                String followers = String.valueOf(snapshot.child("followers").getChildrenCount());
                String posts =  String.valueOf(snapshot.child("posts").getChildrenCount());

                follow_text.setText(follow);
                followers_text.setText(followers);
                post_num.setText(posts);


                if(snapshot.exists() && snapshot.getChildrenCount() > 0 ){
                    Map<String,Object> map = (Map<String,Object>) snapshot.getValue();

                    if(map.get("Name") != null){
                        name.setText(map.get("Name").toString());
                        profile_Layout_title.setText(map.get("Name").toString());
                    }
                    if(map.get("Location") != null){
                        address.setText(map.get("Location").toString());
                    }
                    if(map.get("AboutMe") != null){
                        about_me.setText(map.get("AboutMe").toString());
                    }if(map.get("ProfileImage") != null){
                        Glide.with(getApplicationContext()).load(map.get("ProfileImage").toString()).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).into(userImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void getProfiles(){
        DatabaseReference data  = FirebaseDatabase.getInstance().getReference().child("Users").child(userUid).child("posts");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){

                   String image =  snapshot.child("postImageUri").getValue().toString();
                   list.add(image);
                   otherProfileGridAdapter.notifyDataSetChanged();

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