package com.example.momento;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class SingUpFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FrameLayout framelayout;
    private ImageView goBack;
    private Button button;
    private EditText email;
    private EditText password;
    private EditText confirm_pass;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;

    private ProgressBar progressBar;



    private String mParam1;
    private String mParam2;

    public SingUpFragment() {

    }


    public static SingUpFragment newInstance(String param1, String param2) {
        SingUpFragment fragment = new SingUpFragment();
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
        View view  =  inflater.inflate(R.layout.fragment_sing_up, container, false);
        framelayout = getActivity().findViewById(R.id.registration_fragment);
        goBack = view.findViewById(R.id.signIn_goBack);
        button = view.findViewById(R.id.SignIn_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.SignIn_email);

        password = view.findViewById(R.id.SignIn_password);
        confirm_pass = view.findViewById(R.id.singUP_confirmPass);

        progressBar  = view.findViewById(R.id.singUp_progress);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SplashFragment());
                RegistrationActivity.SingUP = false;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();

            }
        });
    }

    private  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fromright,R.anim.slide_out_left);
        fragmentTransaction.replace(framelayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    public void createUser(){

        if(!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirm_pass.getText().toString())){
            if(TextUtils.equals(password.getText().toString(),confirm_pass.getText().toString())){
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Map<String , Object > map  = new HashMap<>();
                            map.put("email", email.getText().toString());
                            map.put("setUpProfile",false);

                  DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("Users").child(task.getResult().getUser().getUid());
                  databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        setFragment(new SetUpProfileFragment());
                                        RegistrationActivity.ProfileSet = true;
                                        RegistrationActivity.SingUP = false;
                                        Toast.makeText(getContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();

                                    }else{
                                        Toast.makeText(getContext(), "Error"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });

                        }else{
                            Toast.makeText(getContext(), "Error"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });



            }else{
                Toast.makeText(getActivity(), "Fields InMatch", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Fields are Empty", Toast.LENGTH_SHORT).show();
        }

    }


}