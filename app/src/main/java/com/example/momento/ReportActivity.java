package com.example.momento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private CheckBox sexual , violent, harmful, hateful, spam,terrorism;
    private Button report_btn;
    private TextView report_name;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Bundle bundle = getIntent().getExtras();
        final String userId = bundle.get("userID").toString();
        final String followId =  bundle.get("postID").toString();
        final String username =  bundle.get("username").toString();
        sexual = findViewById(R.id.radio_sexual);
        violent = findViewById(R.id.radio_violent);
        harmful = findViewById(R.id.radio_harmful);
        hateful = findViewById(R.id.radio_hateful);
        spam = findViewById(R.id.radio_spam);
        terrorism = findViewById(R.id.radio_terrisom);
        report_btn  = findViewById(R.id.Report_btn);
        report_name = findViewById(R.id.report_name);
        progressBar = findViewById(R.id.report_proogressbar);




        report_name.setText("Report "+ username );


        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sexual.isChecked() || violent.isChecked() || harmful.isChecked() || hateful.isChecked() || spam.isChecked() || terrorism.isChecked()){
                    progressBar.setVisibility(View.VISIBLE);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Reports").child(userId).child(followId).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    if(spam.isChecked()){
                       databaseReference.child("spam").setValue(true);

                    }
                    if(sexual.isChecked()){
                        databaseReference.child("sexual").setValue(true);
                    }
                    if(terrorism.isChecked()){
                        databaseReference.child("terrorism").setValue(true);
                    }
                    if(hateful.isChecked()){
                        databaseReference.child("hateful").setValue(true);
                    }
                    if(harmful.isChecked()){
                        databaseReference.child("harmful").setValue(true);
                    }
                    if(violent.isChecked()){
                        databaseReference.child("violent").setValue(true);
                    }
                    Toast.makeText(ReportActivity.this, "Reported Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);

                    finish();


                }else{
                    Toast.makeText(ReportActivity.this, "Select a Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}