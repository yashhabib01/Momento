package com.example.momento;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetUpProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetUpProfileFragment extends Fragment  implements  DatePickerDialog.OnDateSetListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button setUPDate;
    private RadioGroup radioGroup;
    private EditText name;
    private EditText location;
    private Uri  resultUri, downloadUri;
    private ImageView userImage;
    private DatabaseReference mUserReferences;

    private EditText about_me;
    private Button proceed;
    private FirebaseAuth firebaseAuth;
    private int selected;
    private RadioButton radioButton;
    private FirebaseDatabase firebaseDatabase;
    private ProgressBar progressBar;

    private FrameLayout frameLayout;
    public SetUpProfileFragment() {
        // Required empty public constructor
    }

    public void showPickerDate(){
        DatePickerDialog datePicker  = new DatePickerDialog(getActivity()
        ,this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

        datePicker.show();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetUpProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetUpProfileFragment newInstance(String param1, String param2) {
        SetUpProfileFragment fragment = new SetUpProfileFragment();
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
        View view =    inflater.inflate(R.layout.fragment_set_up_profile, container, false);
        setUPDate  = view.findViewById(R.id.setUp_dateofBirth);
        radioGroup = view.findViewById(R.id.setUp_radioGroup);
        name = view.findViewById(R.id.setUp_name);
        frameLayout = view.findViewById(R.id.main_frameLayout);
        about_me = view.findViewById(R.id.setUp_AboutMe);
        location = view.findViewById(R.id.setUp_Country);
        proceed = view.findViewById(R.id.setUp_btn);
        userImage =  (ImageView) view.findViewById(R.id.setUp_userImage);
        firebaseAuth  = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressBar  = view.findViewById(R.id.setUP_progressBar);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUPDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerDate();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfile();
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(getActivity(),SetUpProfileFragment.this);


            }
        });
    }




    private void setProfile() {
        selected = radioGroup.getCheckedRadioButtonId();
        radioButton = getView().findViewById(selected);
        if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(location.getText().toString()) && !TextUtils.isEmpty(radioButton.getText().toString()) && !TextUtils.isEmpty(radioButton.getText().toString()) && !TextUtils.isEmpty(setUPDate.getText().toString()) &&
            !TextUtils.isEmpty(radioButton.getText().toString())){

            progressBar.setVisibility(View.VISIBLE);


            final Map<String, Object> map = new HashMap();
            map.put("Name", name.getText().toString());
            map.put("DateOfBirth", setUPDate.getText().toString());
            map.put("Gender", radioButton.getText());
            map.put("Location", location.getText().toString());
            map.put("AboutMe", about_me.getText().toString());
            map.put("email",firebaseAuth.getCurrentUser().getEmail());




            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
            databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> map1 = new HashMap();
                        map1.put("setUpProfile", true);
                        Toast.makeText(getContext(), "Profile SetUp Successfully ", Toast.LENGTH_SHORT).show();
                        firebaseDatabase.getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).updateChildren(map1);

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();

                    }

                    progressBar.setVisibility(View.INVISIBLE);

                }
            });

            if(resultUri!= null){
                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Bitmap bitmap  = null;
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplication().getContentResolver(),resultUri);
                }catch(Exception e){

                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
                byte [] data  = baos.toByteArray();
                UploadTask uploadTask = storageReference.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("Uri" , uri.toString());
                                Map newmap = new HashMap();
                                newmap.put("ProfileImage", uri.toString());
                                databaseReference.updateChildren(newmap);

                            }
                        });
                    }
                });






            }

        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth+ "/" + month + "/" +  year;
        setUPDate.setText(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                final Uri uri = result.getUri();
                resultUri = uri;
                userImage.setImageURI(resultUri);
            }
        }
    }
    private  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fromright,R.anim.slide_out_left);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}