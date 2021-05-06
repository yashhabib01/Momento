package com.example.momento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class CameraActivity extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isCameraInislized ;
    private ImageView flashB;
    private Camera camera = null ;
    private static SurfaceHolder mySurfaceHolder ;

    private FrameLayout mPreview;
    private CameraPreview cameraPreview;
    private Button gallery_btn;
    public final int GALLERY = 3;
    private Context context;

    public static OrientationEventListener orientationEventListener = null;

    private static final  String [] Permission_String = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static final int requestCode1 = 34;
    public static final int Permission_count= 3;
    private boolean fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        gallery_btn = findViewById(R.id.create_gallery_btn);



        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGalleryPics();
            }
        });


    }
    private void getGalleryPics() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
       startActivityForResult(intent,GALLERY);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean Permission(){

        for(int i = 0; i< Permission_count; i++){
            if(this.checkSelfPermission(Permission_String[i]) != PackageManager.PERMISSION_GRANTED){

                return true;
            }
        }
        return false;
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == requestCode1 && grantResults.length> 0){
            ((ActivityManager)(this.getSystemService(ACCESSIBILITY_SERVICE))).clearApplicationUserData();
            this.recreate();
        }else{
            onResume();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Permission()){

            requestPermissions(Permission_String, requestCode1);
            return;
        }

        if(!isCameraInislized){
            camera = Camera.open();
            cameraPreview = new CameraPreview(this,camera);
            mPreview = findViewById(R.id.camera_layout);
            mPreview.addView(cameraPreview);
            rotateCamera();
            flashB = findViewById(R.id.flash);
            if(hasflash()){
                flashB.setVisibility(View.VISIBLE);
            }else{
                flashB.setVisibility(View.GONE);
            }

            final ImageView switch_btn = findViewById(R.id.switch_btn);
            switch_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    camera.release();
                    switchCamera();
                    rotateCamera();
                    try{
                        camera.setPreviewDisplay(mySurfaceHolder);
                        camera.startPreview();
                    }catch(Exception e){

                    }

                    if(hasflash()){
                        flashB.setVisibility(View.VISIBLE);
                    }else{
                        flashB.setVisibility(View.GONE);
                    }

                }
            });

            final ImageView take = findViewById(R.id.take_photo);
            take.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        camera.takePicture(null, null,mPicture);
                        camera.startPreview();
                    }catch (Exception e){

                    }


                }
            });

            orientationEventListener = new OrientationEventListener(this) {
                @Override
                public void onOrientationChanged(int orientation) {
                    rotateCamera();
                }
            };
            orientationEventListener.enable();
            mPreview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(whichCamera){
                        if(fm){
                            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        }else{
                            p.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
                        }

                        try{
                            camera.setParameters(p);
                        }catch(Exception e) {

                        }
                        fm = !fm;
                    }
                    return true;
                }
            });

        }
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmap =  BitmapFactory.decodeByteArray(data,0,data.length);

            String filepath = tempFileImage(CameraActivity.this,bitmap,random().toString());


                Intent intent = new Intent(CameraActivity.this, CreatePost.class);
                 intent.putExtra("filepath",filepath);
                 startActivity(intent);




        }
    };
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(6);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }




    public void switchCamera(){
        if(whichCamera){
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }else{
            camera = Camera.open();
        }
        whichCamera = !whichCamera;
    }

    @Override
    public void onPause() {
        super.onPause();
        camerrelease();
    }
    public void camerrelease(){
        if(camera != null){
            mPreview.removeView(cameraPreview);
            camera.release();
            orientationEventListener.disable();
            camera = null;
            whichCamera = !whichCamera;
        }
    }

    private static List<String> codeeffect;
    public static boolean hasflash(){
        codeeffect = p.getSupportedColorEffects();
        final List<String> flashMode= p.getSupportedFlashModes();
        if(flashMode == null){
            return false;
        }

        for(String stering : flashMode){
            if(Camera.Parameters.FLASH_MODE_ON.equals(stering)){
                return true;
            }
        }
        return false;
    }

    public static int rotation;
    public static boolean whichCamera = true;
    public static Camera.Parameters  p ;
    public void rotateCamera(){

        if(camera != null){
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if(rotation == 0){
                rotation = 90;
            }else if(rotation == 1){
                rotation = 0;
            }else if(rotation == 2){
                rotation = 270;
            }else{
                rotation  = 180;
            }

            camera.setDisplayOrientation(rotation);
            if(!whichCamera){
                if(rotation == 90){
                    rotation = 270;
                }else if (rotation == 270){
                    rotation = 90;
                }

            }
            p = camera.getParameters();
            p.setRotation(rotation);
            camera.setParameters(p);
        }
    }
    public static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

        private static SurfaceHolder mHolder;
        private static Camera camera;


        private CameraPreview(Context context, Camera camera1){
            super(context);
            camera = camera1;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        }



        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mySurfaceHolder = holder;

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }catch (Exception e){

            }

        }

        public void surfaceChanged(SurfaceHolder holder , int format, int w, int n){

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }


    }
    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY && resultCode == Activity.RESULT_OK){
            Uri uri  = data.getData();
            Intent intent = new Intent(CameraActivity.this, CreatePost.class);
            intent.putExtra("ImageUri", uri.toString());
            startActivity(intent);
        }
    }




}