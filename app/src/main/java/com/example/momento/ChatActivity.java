package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private ImageView send_btn, chat_report;
    private String chatId , check;
    private String uid, username;
    private TextView layout_title;
    private List<ChatViewObject> chatViewObjects = new ArrayList<>();
    private ChatViewAdapter chatViewAdapter;
     DatabaseReference databaseReference, chatDataBase , groupChat, groupData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chatViewRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatViewAdapter = new ChatViewAdapter(chatViewObjects);
        recyclerView.setAdapter(chatViewAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        Bundle bundle = getIntent().getExtras();
        chat_report = findViewById(R.id.chat_report);
        layout_title = findViewById(R.id.chat_layout_name);
        username = bundle.getString("chat_user_name");
        check = bundle.getString("check");
        layout_title.setText(username);
        editText = findViewById(R.id.chat_getText);
        send_btn = findViewById(R.id.sen_chat_img);
        String currentId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        layout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   =  new Intent(ChatActivity.this,GroupViewActivity.class);
                Bundle b = new Bundle();
                b.putString("groupUid", uid);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        chat_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ChatActivity.this);
                dialog.setContentView(R.layout.chat_report);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.text_background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView name = dialog.findViewById(R.id.chat_report_name);
                name.setText("Report "  + username);
                Button cancel = dialog.findViewById(R.id.chat_cancel);
                Button report  = dialog.findViewById(R.id.chat_user_report);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Reports").child("Chat");
                        String key = databaseReference.push().getKey();

                        Map<String ,Object> str   = new HashMap<>();
                       str.put("ReportedId", uid);
                       str.put("ReporterId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                       databaseReference.child(key).setValue(str).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                               if(task.isSuccessful()){
                                   Toast.makeText(ChatActivity.this, "Reported Successfully", Toast.LENGTH_SHORT).show();
                               }else{
                                   Toast.makeText(ChatActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                               }
                               dialog.cancel();
                           }
                       });
                    }
                });

                dialog.show();
            }
        });

         uid  = bundle.getString("UserchatId");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentId).child("following").child(uid).child("chatId");
        chatDataBase = FirebaseDatabase.getInstance().getReference().child("Chat");
        groupChat = FirebaseDatabase.getInstance().getReference().child("Chat-Group").child(uid).child("chat");
       // Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();


        getChatId();



        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sendChat();
            }
        });

    }

    private void sendChat(){
      String sendMessage =  editText.getText().toString();


      if(!sendMessage.isEmpty()){
            if(check.equals("single")) {
                DatabaseReference newMessage = chatDataBase.push();
                Map<String, Object> map = new HashMap<>();
                map.put("createdByUser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                map.put("text", sendMessage);

                newMessage.setValue(map);
            }else{
                DatabaseReference newMessage = groupChat.push();
                Map<String, Object> map = new HashMap<>();
                map.put("createdByUser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                map.put("text", sendMessage);
                newMessage.setValue(map);
            }

      }else {
          Toast.makeText(this, "field is empty", Toast.LENGTH_SHORT).show();
      }

      editText.setText(null);
    }


    private void getChatId(){
        if(check.equals("single")) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        chatId = snapshot.getValue().toString();
                        chatDataBase = chatDataBase.child(chatId);
                        getChatMessage();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }else{
            getGroupChat();
        }

    }

    private void getChatMessage() {

            chatDataBase.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String name = null;
                    String createdByUser = null;
                    if (snapshot.exists())
                        if (snapshot.child("text").getValue() != null) {
                            name = snapshot.child("text").getValue().toString();
                        }
                    if (snapshot.child("createdByUser").getValue() != null) {
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }

                    if (name != null && createdByUser != null) {
                        Boolean user = false;
                        if (createdByUser.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            user = true;
                        }

                        chatViewObjects.add(new ChatViewObject(name, uid, user));
                        chatViewAdapter.notifyDataSetChanged();


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


    private void getGroupChat(){


            groupChat.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {



                   DatabaseReference data  = FirebaseDatabase.getInstance().getReference().child("Chat-Group").child(uid).child("chat").child(snapshot.getKey());
                   data.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot1) {

                           Toast.makeText(ChatActivity.this, snapshot1.getKey(), Toast.LENGTH_SHORT).show();
                           String name = null;
                           String createdByUser = null;
                           if (snapshot1.child("text").getValue() != null) {
                               name = snapshot1.child("text").getValue().toString();
                           }if (snapshot1.child("createdByUser").getValue()  != null) {
                               createdByUser = snapshot1.child("createdByUser").getValue().toString();
                           }


                           if (name != null && createdByUser != null) {
                               Boolean user = false;
                               if (createdByUser.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                   user = true;
                               }

                               chatViewObjects.add(new ChatViewObject(name, uid, user));
                               chatViewAdapter.notifyDataSetChanged();


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

        }

}