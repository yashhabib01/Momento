package com.example.momento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EidtProfile extends AppCompatActivity {
    private Button setUPDate;
    private RadioGroup radioGroup;
    private EditText name;
    private EditText location;
    private Uri resultUri, downloadUri;
    private ImageView userImage;
    private DatabaseReference mUserReferences;
    private String ImageUri;

    private EditText about_me;
    private Button proceed;
    private FirebaseAuth firebaseAuth;
    private int selected;
    private RadioButton radioButton;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eidt_profile);
        setUPDate  = findViewById(R.id.setUp_dateofBirth);
        radioGroup =findViewById(R.id.setUp_radioGroup);
        name = findViewById(R.id.setUp_name);

        about_me = findViewById(R.id.setUp_AboutMe);
        location = findViewById(R.id.setUp_Country);
        proceed =findViewById(R.id.setUp_btn);
        userImage =  (ImageView) findViewById(R.id.setUp_userImage);
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        if(firebaseAuth.getCurrentUser() != null){
            mUserReferences  = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
            mUserReferences.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists() && snapshot.getChildrenCount() > 2){
                        Map<String,Object> map = (Map<String,Object>) snapshot.getValue();
                        if(map.get("Name") != null){
                            name.setText(map.get("Name").toString());
                        } if(map.get("DateOfBirth") != null){
                            setUPDate.setText(map.get("DateOfBirth").toString());
                        } if(map.get("AboutMe") != null){
                            about_me.setText(map.get("AboutMe").toString());
                        }if(map.get("Location") != null){
                            location.setText(map.get("Location").toString());
                        }


                        if(map.get("ProfileImage") != null){
                            ImageUri = map.get("ProfileImage").toString();
                            Glide.with(EidtProfile.this).load(map.get("ProfileImage").toString()).into(userImage);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selected);
                final Map<String, Object> map1 = new HashMap();
                map1.put("Name", name.getText().toString());
                map1.put("DateOfBirth", setUPDate.getText().toString());
                map1.put("Gender", radioButton.getText());
                map1.put("Location", location.getText().toString());
                map1.put("AboutMe", about_me.getText().toString());
                map1.put("ProfileImage", ImageUri);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
                databaseReference.updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EidtProfile.this, "ProfileUpdated Successfully", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


    }
}