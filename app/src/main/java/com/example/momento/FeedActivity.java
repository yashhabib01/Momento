package com.example.momento;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

public class FeedActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;

    public static boolean myFeed = false;
    public static boolean myProfile = false;
    private ImageView nav_userImage;
    private TextView nav_userName;
    private TextView nav_userEmail;
    public static  TextView layout_title;

    private  Toolbar toolbar;

    private View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
          toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayShowTitleEnabled(false);
       frameLayout = findViewById(R.id.main_frameLayout);

        final FloatingActionButton fab = findViewById(R.id.fab);
        layout_title = findViewById(R.id.layout_title);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(FeedActivity.this,CameraActivity.class);
               startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        final NavigationView navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nav_userEmail = header.findViewById(R.id.Nav_MyProfleEmail);
        nav_userName = header.findViewById(R.id.nav_MyProfileName);
        nav_userImage = header.findViewById(R.id.nav_myProfleView);
        getUserInfo();

        UserINformation userINformation  = new UserINformation();
        userINformation.gatherInformation();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.myProfileFragment){



                    navigationView.getMenu().getItem(1).setChecked(true);
                    myFeed = true;

                    return true;
                }else if(item.getItemId() == R.id.myFeedFragment){


                    navigationView.getMenu().getItem(0).setChecked(true);

                }else if(item.getItemId() == R.id.chatFragment){

                    navigationView.getMenu().getItem(2).setChecked(true);
                    myFeed = true;
                }else if(item.getItemId() == R.id.aboutMomento){


                    navigationView.getMenu().getItem(5).setChecked(true);

                }else if(item.getItemId() == R.id.settingFragment){
                    
                    navigationView.getMenu().getItem(4).setChecked(true);

                }else  if(item.getItemId() == R.id.savedFragment){
                    navigationView.getMenu().getItem(3).setChecked(true);
                }

                return true;
            }
        });




        navigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.myFeedFragment, R.id.myProfileFragment, R.id.chatFragment,R.id.aboutMomento,R.id.settingFragment,R.id.savedFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
      //  setFragment(new MyFeedFragment());

      //  navigationView.getMenu().getItem(0).setChecked(true);



    }



    private void getUserInfo() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 1){
                    Map<String , Object> map = (Map<String,Object>) snapshot.getValue();
                    if(map.get("Name")!= null){
                        nav_userName.setText(map.get("Name").toString());

                    }
                    if(map.get("email")!= null){
                        nav_userEmail.setText(map.get("email").toString());

                    } if(map.get("ProfileImage")!= null){
                    //    Glide.with(header).load(map.get("ProfileImage").toString()).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_24)).into(nav_userImage);
                        Glide.with(getApplication()).load(map.get("ProfileImage").toString()).placeholder(R.drawable.ic_account_circle_24).dontAnimate().into(nav_userImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.

            getMenuInflater().inflate(R.menu.feed, menu);





        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if(item.getItemId() == R.id.searchFragment){

          Intent intent = new Intent(FeedActivity.this, SearchActivity.class);
          startActivity(intent);
          return  true;
      }
        return super.onOptionsItemSelected(item);
    }
}