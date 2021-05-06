package com.example.momento;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private EditText searchName;
    private ImageView search_img;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<ChatObject> chatObjectList;
    private SearchAdapter searchAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.search_fragment_RecyclerView);


        // search users
        search_img = view.findViewById(R.id.search_btn);
        searchName = view.findViewById(R.id.search_editText);
            firebaseAuth = FirebaseAuth.getInstance();

        // search users

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clear();
                listentoData();

            }
        });
    }


    private void clear(){
        this.chatObjectList.size();
        this.chatObjectList.clear();
        searchAdapter.notifyItemChanged(0,chatObjectList.size());

    }
    private void listentoData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
         chatObjectList = new ArrayList<>();
        final String user_image = "https://firebasestorage.googleapis.com/v0/b/momento-5c79e.appspot.com/o/Oval.png?alt=media&token=ced1ca4a-19e4-4fdb-a9dc-e170aced39fc";

         searchAdapter  = new SearchAdapter(chatObjectList);
        recyclerView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

        DatabaseReference db  = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = db.orderByChild("Name").startAt(searchName.getText().toString()).endAt(searchName.getText().toString()+"\uff8f");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String email  = "";
                String user_profile = "";
                String uid = snapshot.getRef().getKey();
                if(snapshot.child("Name").getValue() != null){
                    email = snapshot.child("Name").getValue().toString();
                } if(snapshot.child("ProfileImage").getValue() != null){
                    user_profile = snapshot.child("ProfileImage").getValue().toString();
                }

                if(!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    ChatObject chatObject = new ChatObject(user_profile,email,uid);
                    chatObjectList.add(chatObject);
                    searchAdapter.notifyDataSetChanged();
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