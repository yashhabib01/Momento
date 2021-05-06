package com.example.momento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class RegistrationActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    public static boolean singIn = false;
    public static boolean forgotpass = false;
    public static boolean SingUP = false;
    public static boolean ProfileSet = false;
    private FirebaseAuth firebaseAuth;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        frameLayout = findViewById(R.id.registration_fragment);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


            setFragment(new SplashFragment());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_BACK){

            if(singIn){
                setFragment(new SplashFragment());
                singIn = false;

            }else if(forgotpass){
                setFragment(new SignInFragment());
                singIn = true;
                forgotpass = false;
            }else if(SingUP){
                setFragment(new SplashFragment());
                SingUP = false;

            }else if(ProfileSet){
                ProfileSet = true;
                finish();
            }
            else{
                finish();
            }
        }

        return false;
    }

    private  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.setCustomAnimations(R.anim.fromleft,R.anim.slide_out_fragment);
        fragmentTransaction.commit();
    }


}