package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchName;
    private ImageView search_img;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private List<ChatObject> chatObjectList;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.search_fragment_RecyclerView);


        // search users
        search_img = findViewById(R.id.search_btn);
        searchName = findViewById(R.id.search_editText);
        firebaseAuth = FirebaseAuth.getInstance();

        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clear();
                listentoData();

            }
        });
    }




    private void listentoData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatObjectList = new ArrayList<>();

        searchAdapter  = new SearchAdapter(chatObjectList);
        recyclerView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

        DatabaseReference db  = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = db.orderByChild("Name").startAt(searchName.getText().toString()).endAt(searchName.getText().toString()+"\uff8f");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String name  = "";
                String email = "";
                String user_profile = "";
                String uid = snapshot.getRef().getKey();
                if(snapshot.child("Name").getValue() != null){
                    name = snapshot.child("Name").getValue().toString();
                } if(snapshot.child("ProfileImage").getValue() != null){
                    user_profile = snapshot.child("ProfileImage").getValue().toString();
                }
                if(snapshot.child("email").getValue() != null){
                    email = snapshot.child("email").getValue().toString();
                }

                if(!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    ChatObject chatObject = new ChatObject(user_profile,name,uid);
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