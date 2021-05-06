package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AppCompatActivity {
            private   ImageView imageView;
            private EditText addCaption;
            private EditText addLocation;
            private LinearLayout post;
            private byte [] arr;
            private Button tag_people;

            

            private ProgressBar progressBar;



    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        tag_people = findViewById(R.id.tag_peple_btn);
        tag_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePost.this , TagActivity.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.createPost_imageView);
        progressBar = findViewById(R.id.create_progress_bar);
          Uri uri = null ;
          try {

              uri = Uri.parse(getIntent().getStringExtra("ImageUri"));
          }catch (NullPointerException e){

          }




        if(uri!= null){
           imageView.setImageURI(uri);
            Bitmap bitmap = null;

            try {
                bitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

            arr = byteArrayOutputStream.toByteArray();


        }else {

            String filePath = getIntent().getStringExtra("filepath");

            File file = new File(filePath);

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            } catch (Exception e) {

            }
            arr = baos.toByteArray();
        }



        addCaption = findViewById(R.id.createPost_caption);
        addLocation = findViewById(R.id.createPost_location);
        post = findViewById(R.id.createPost_post);




        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            sendPost();
            }
        });




    }


    private void sendPost(){
        if(arr != null  && !TextUtils.isEmpty(addCaption.getText().toString()) && !TextUtils.isEmpty(addLocation.getText().toString())){
            progressBar.setVisibility(View.VISIBLE);


            final Date date = Calendar.getInstance().getTime();
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts");
          final String  key = databaseReference.push().getKey();
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("MyPosts").child(key);
            UploadTask uploadTask = storageReference.putBytes(arr);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String,Object> map = new  HashMap<>();
                                map.put("postImageUri", uri.toString());
                                map.put("postCaption",addCaption.getText().toString());
                                map.put("postTime", date.toString());

                                map.put("totalLikes",0);
                                map.put("TagPeople", true);
                                map.put("postLocation", addLocation.getText().toString());
                                 databaseReference.child(key).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful()){
                                             getCheck(key);
                                             Toast.makeText(CreatePost.this, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show();

                                         }else{

                                             Toast.makeText(CreatePost.this, "Error: " +  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                         }

                                     }
                                 });
                            }
                        });
                    }
                }
            });

            progressBar.setVisibility(View.INVISIBLE);

        }else{
            Toast.makeText(this, "Fields are Empty", Toast.LENGTH_SHORT).show();
        }
     //

    }

    private void getCheck(String key){
        for(int i  = 0; i < TagActivity.tagObjectList.size() ; i++) {
            if (TagActivity.tagObjectList.get(i).getCheck()) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts").child(key).child("TagPosts").child(TagActivity.tagObjectList.get(i).getUid()).setValue(true);

            }
        }
    }


}