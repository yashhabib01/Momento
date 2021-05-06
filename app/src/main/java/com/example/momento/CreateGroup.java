package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroup extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button submit;
    private ImageView imageView;
    private EditText title;
    private Uri resultUri , ImageUri;

    private List<TagObject> tagObjectList = new ArrayList<>();
    private CreateGroupAdapter createGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        recyclerView = findViewById(R.id.recyclerView_creat_group);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        createGroupAdapter = new CreateGroupAdapter(tagObjectList);
        title = findViewById(R.id.create_group_title);
        submit = findViewById(R.id.create_group_btn);
        imageView = findViewById(R.id.create_group_img);


        imageView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(CreateGroup.this);


            }
        });





        final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Chat-Group");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(title.getText().toString()) && resultUri != null){
              ;
               final DatabaseReference reference = data.push();
               FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("groups").child(reference.getKey()).setValue(true);

                    for(int i = 0 ; i< tagObjectList.size() ;i++){
                        if(tagObjectList.get(i).getCheck()){
                            FirebaseDatabase.getInstance().getReference().child("Users").child(tagObjectList.get(i).getUid()).child("groups").child(reference.getKey()).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("Chat-Group").child(reference.getKey().toString()).child("users").child(tagObjectList.get(i).getUid()).setValue(true);
                        }
                    }


                    final StorageReference storage  = FirebaseStorage.getInstance().getReference().child("Groups").child(reference.getKey());
                    Bitmap bitmap = null;

                    try {

                        bitmap  = MediaStore.Images.Media.getBitmap(getContentResolver(),resultUri);

                    }catch (Exception e){

                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
                    byte [] bytes = baos.toByteArray();

                    UploadTask up  = storage.putBytes(bytes);
                    up.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(CreateGroup.this, "Successfully", Toast.LENGTH_SHORT).show();
                                    Map<String , Object> newmap = new HashMap();
                                    newmap.put("groupImg" , uri.toString());
                                    newmap.put("name", title.getText().toString());
                                    reference.updateChildren(newmap);
                                    finish();


                                }
                            });
                        }
                    });





                }
            }
        });
        recyclerView.setAdapter(createGroupAdapter);
        createGroupAdapter.notifyDataSetChanged();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("getChat");
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

                            TagObject tagObject = new TagObject(name,image,ref,false);
                            if(!tagObjectList.contains(tagObject)) {
                                tagObjectList.add(tagObject);
                                createGroupAdapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                final Uri uri = result.getUri();
                resultUri = uri;
                imageView.setImageURI(resultUri);

            }
        }

    }
}