package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FeedDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView username;
    private ImageView userImage , goBack;
    private TextView addCaption;
    private ImageView postImage;
    private TextView location , feed_comment , postTime;
    private TextView likes_num  , tag_people_list;
    private TextView feedTitle;
    private EditText user_comments;
    private ImageView options_image;

    private RecyclerView tagRecyclerview;
    private FeedTagAdapter feedTagAdapter;
    private List<FeedTagObject> feedTagObjectList = new ArrayList<>();



    private ImageView send_btn;
    private boolean thisActivity = false;
    Bundle bundle;
    String followUid;
    String postId,post_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        recyclerView = findViewById(R.id.feed_comment_recycler);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        bundle = getIntent().getExtras();
        followUid =  bundle.get("userId").toString();
        postId  = bundle.get("postID").toString();
        tag_people_list = findViewById(R.id.tag_people_list_text);
        options_image = findViewById(R.id.options_image);

        feed_comment = findViewById(R.id.feed_comment);
        feedTagAdapter = new FeedTagAdapter(feedTagObjectList);
        recyclerView.setLayoutManager(linearLayoutManager);
        thisActivity = true;


        options_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FeedDetailActivity.this);
                dialog.setContentView(R.layout.select);
                dialog.setCancelable(true);
                final LinearLayout save = dialog.findViewById(R.id.saved_moment);
                final LinearLayout  report =dialog.findViewById(R.id.report_moment);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                 DatabaseReference db   =  FirebaseDatabase.getInstance().getReference().child("Users");

                 db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saved").child(followUid).child(postId).setValue(true)
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                             }
                         });

                    }
                });

                report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String[] name = {""};
                        DatabaseReference db1   =  FirebaseDatabase.getInstance().getReference().child("Users").child(followUid);
                              db1.addValueEventListener(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         name[0] = snapshot.child("Name").getValue().toString();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        Bundle bundle = new Bundle();
                        bundle.putString("userID",followUid);
                        bundle.putString("postID",postId);
                        bundle.putString("username", name[0]);

                        Intent intent = new Intent(FeedDetailActivity.this, ReportActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });



                dialog.show();

            }
        });



        tag_people_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FeedDetailActivity.this);
                dialog.setContentView(R.layout.tag_user_sub_layout);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.text_background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                tagRecyclerview = dialog.findViewById(R.id.tag_user_list_recylerView);
              final  LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FeedDetailActivity.this);
                linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
                tagRecyclerview.setLayoutManager(linearLayoutManager1);
                tagRecyclerview.setAdapter(feedTagAdapter);


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(followUid).child("posts").child(postId).child("TagPosts");
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Toast.makeText(FeedDetailActivity.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.getKey());
                data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String image = "";
                            String name = "";
                            if(snapshot.child("ProfileImage") != null){
                                image = snapshot.child("ProfileImage").getValue().toString();
                            }
                            if(snapshot.child("Name") != null){
                                name = snapshot.child("Name").getValue().toString();
                            }
                            FeedTagObject feedTagObject = new FeedTagObject(image,snapshot.getKey(),name);
                            if(!feedTagObjectList.contains(feedTagObject)){
                                feedTagObjectList.add(feedTagObject);
                                feedTagAdapter.notifyDataSetChanged();
                            }



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

                dialog.show();
            }
        });
        userImage = findViewById(R.id.feed_userImage);
        addCaption = findViewById(R.id.feed_postCaption);
        username = findViewById(R.id.feed_UserName);




        postImage = findViewById(R.id.feed_postImage);
        likes_num = findViewById(R.id.feed_total_likes);
        postTime = findViewById(R.id.feed_postTime);
        location = findViewById(R.id.feed_postLocation);
        feedTitle  = findViewById(R.id.post_userName);
        user_comments = findViewById(R.id.users_comments);
        send_btn  = findViewById(R.id.send_comments_btn);
        goBack = findViewById(R.id.post_GoBak);


        getComments();


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComments();
            }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });











        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(followUid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (thisActivity) {
                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                        if (map.get("Name") != null) {
                            username.setText(map.get("Name").toString());
                            feedTitle.setText(map.get("Name").toString() + "'s Moment");

                        }
                        if (map.get("ProfileImage") != null) {
                            Glide.with(FeedDetailActivity.this).load(map.get("ProfileImage").toString()).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).into(userImage);

                        }

                        databaseReference.child("posts").child(postId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               String likes = String.valueOf(snapshot.child("postLikes").getChildrenCount());
                                Map<String, Object> pstMap = (Map<String, Object>) snapshot.getValue();
                                Glide.with(getApplicationContext()).load(pstMap.get("postImageUri").toString()).centerCrop().into(postImage);

                                addCaption.setText(pstMap.get("postCaption").toString());

                                likes_num.setText(likes);

                                location.setText(pstMap.get("postLocation").toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        thisActivity = !thisActivity;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    private void sendComments(){
        if(!TextUtils.isEmpty(user_comments.getText().toString())) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(followUid).child("posts").child(postId).child("comments");
            Map<String,Object> map = new HashMap<>();
            map.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),user_comments.getText().toString());
            databaseReference.updateChildren(map);
            user_comments.setText(null);

        }else{
            Toast.makeText(this, "Null pinter Exception", Toast.LENGTH_SHORT).show();
        }


    }

  //
    //              //  T
   //

    private void getComments() {
        final List<CommentObject> commentObjectList = new ArrayList<>();
        final CommentAdapter commentAdapter = new CommentAdapter(commentObjectList);

        final DatabaseReference comments = FirebaseDatabase.getInstance().getReference().child("Users").child(followUid).child("posts").child(postId).child("comments");
        comments.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String comments_uid  = snapshot.getRef().getKey();
                String comments = String.valueOf(snapshot.getChildrenCount());
                feed_comment.setText(comments);

                final String comments_string  = snapshot.getValue().toString();


                DatabaseReference user =  FirebaseDatabase.getInstance().getReference().child("Users").child(comments_uid);
                user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            String comm_name = null;
                            String comm_profile = null;
                            Map<String, Object> map = (Map<String, Object>) snapshot.getValue();

                            if (map.get("Name") != null) {
                                comm_name = map.get("Name").toString();

                            }
                            if (map.get("ProfileImage") != null) {
                                comm_profile = map.get("ProfileImage").toString();


                            }
                            CommentObject commentObject =  new CommentObject(comm_profile, comm_name, "2 hours ago ", comments_string);
                            if(comm_name != null && comm_profile != null) {
                                if(!commentObjectList.contains(commentObject)){
                                    commentObjectList.add(commentObject);
                                    commentAdapter.notifyDataSetChanged();
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

        recyclerView.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();

    }



}