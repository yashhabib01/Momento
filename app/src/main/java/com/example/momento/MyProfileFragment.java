package com.example.momento;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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


public class MyProfileFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView name;
    private TextView address, followers, following ;
    private ImageView userImage;
    private LinearLayout edit_layout;
    private FrameLayout frameLayout;
    private TextView about_me ;
    private MyProfileGirdAdapter myProfileGirdAdapter;
    private  List<MyProfilePostObject> list = new ArrayList<>();;
    public static boolean fromprofile = false;
    private  TextView follow_text;
    private TextView followers_text;
    private TextView post_num;

    private String mParam1;
    private String mParam2;
    private GridView gridView;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);
        gridView = view.findViewById(R.id.gridView_moments);
        name = view.findViewById(R.id.my_profile_name);
        address = view.findViewById(R.id.my_profile_country);
        userImage = view.findViewById(R.id.my_profile_userimage);
        edit_layout = view.findViewById(R.id.edit_profile_layout);
        about_me = view.findViewById(R.id.my_profile_about_me);
        followers = view.findViewById(R.id.followers_getFollowers);
        following = view.findViewById(R.id.fpowing_getFollowing);
        follow_text = view.findViewById(R.id.other_following);
        FeedActivity.layout_title.setText("My Profile");
        followers_text = view.findViewById(R.id.other_followers);
        post_num = view.findViewById(R.id.other_posts);
        frameLayout = getActivity().findViewById(R.id.main_frameLayout);
        myProfileGirdAdapter = new MyProfileGirdAdapter(list);
        gridView.setAdapter(myProfileGirdAdapter);


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FollowersActivity.class);
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FollowingActivity.class);
                startActivity(intent);

            }
        });







        getProfiles();

        return  view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    edit_layout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(),EidtProfile.class);
            getActivity().startActivity(intent);
        }
    });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int follow = (int) (snapshot.child("following").getChildrenCount());
                int followers = (int) (snapshot.child("followers").getChildrenCount());
                int posts = (int) (snapshot.child("posts").getChildrenCount() );

                follow_text.setText(String.valueOf(follow));
                followers_text.setText(String.valueOf(followers));
                post_num.setText(String.valueOf(posts));

                if(snapshot.exists() && snapshot.getChildrenCount() > 0 ){
                    Map<String,Object> map = (Map<String,Object>) snapshot.getValue();

                    if(map.get("Name") != null){
                        name.setText(map.get("Name").toString());
                    }
                    if(map.get("Location") != null){
                        address.setText(map.get("Location").toString());
                    }
                    if(map.get("AboutMe") != null){
                        about_me.setText(map.get("AboutMe").toString());
                    }if(map.get("ProfileImage") != null){
                        Glide.with(getActivity()).load(map.get("ProfileImage").toString()).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).into(userImage);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fromright,R.anim.slide_out_left);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }



    private  void getProfiles(){
        DatabaseReference data  = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts");
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){

                    String key  = snapshot.getKey();


                   String image = snapshot.child("postImageUri").getValue().toString();

                   list.add(new MyProfilePostObject(image,FirebaseAuth.getInstance().getCurrentUser().getUid(),key));
                    myProfileGirdAdapter.notifyDataSetChanged();

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