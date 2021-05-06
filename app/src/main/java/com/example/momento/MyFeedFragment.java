package com.example.momento;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.momento.FeedActivity.layout_title;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private  List<MyFeedObject> myFeedObjectList;
    private  MyFeedAdapter myFeedAdapter;

    public MyFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFeedFragment newInstance(String param1, String param2) {
        MyFeedFragment fragment = new MyFeedFragment();
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
        View view  =  inflater.inflate(R.layout.fragment_my_feed, container, false);
        recyclerView = view.findViewById(R.id.Myfeed_recylerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        layout_title.setText("My Feed");
        myFeedObjectList  = new ArrayList<>();



        myFeedAdapter = new MyFeedAdapter(myFeedObjectList);




        DatabaseReference foll = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        foll.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists()){
                    String uid  = snapshot.getRef().getKey();
                    myFeedObjectList.clear();
                    myFeedAdapter.notifyDataSetChanged();

                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {



                            String email = snapshot.child("email").getValue().toString();
                            String uid  = snapshot.getRef().getKey();
                            String name  = snapshot.child("Name").getValue().toString();

                            String user_image = snapshot.child("ProfileImage").getValue().toString();

                            for(DataSnapshot dataSnapshot : snapshot.child("posts").getChildren()) {
                                String image = dataSnapshot.child("postImageUri").getValue().toString();
                                String  geTtime ="";


                                       MyFeedObject myFeedObject = new MyFeedObject(image, name, geTtime, user_image, uid, dataSnapshot.getRef().getKey());
                                       if(!myFeedObjectList.contains(myFeedObject)){
                                           myFeedObjectList.add(myFeedObject);
                                           myFeedAdapter.notifyDataSetChanged();
                                       }







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







        recyclerView.setAdapter(myFeedAdapter);
        myFeedAdapter.notifyDataSetChanged();



        return view;
    }

    public void clear(){
        this.myFeedObjectList.size();
        this.myFeedObjectList.clear();
        myFeedAdapter.notifyItemChanged(0,myFeedObjectList.size());


    }
}