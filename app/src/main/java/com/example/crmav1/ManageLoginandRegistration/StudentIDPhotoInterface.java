package com.example.crmav1.ManageLoginandRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.crmav1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class StudentIDPhotoInterface extends AppCompatActivity {

    private ImageView idPhoto;
    private Button next;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private StorageReference storageRef, storageRef1;

    private String fullName, emailAddress, phoneNo, stdIC, newPassword, image_uriID, stdID, stdCampus, image_uriIC;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //permission array
    private String[] cameraPermission;
    private String[] storagePermission;
    //image picked uri
    private Uri ICfile, IDfile, image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_idphoto_interface);

        this.setTitle("Registration");

        next = findViewById(R.id.nextS2);
        idPhoto = findViewById(R.id.stdIDPhoto);

        auth = FirebaseAuth.getInstance();

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        idPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialogID();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
                Intent intent2next = new Intent(StudentIDPhotoInterface.this, StudentLicencePhotoInterface.class);
                intent2next.putExtra("fullName", fullName);
                intent2next.putExtra("emailAddress", emailAddress);
                intent2next.putExtra("phoneNo", phoneNo);
                intent2next.putExtra("stdIC", stdIC);
                intent2next.putExtra("newPassword", newPassword);
                intent2next.putExtra("campusS", stdCampus);
                intent2next.putExtra("stdID", stdID);
                intent2next.putExtra("image_uriIC", image_uriIC);
                intent2next.putExtra("image_uriID", image_uriID);
                startActivity(intent2next);

            }
        });

    }

    private void inputData(){
        fullName = getIntent().getStringExtra("fullName");
        emailAddress = getIntent().getStringExtra("emailAddress");
        phoneNo = getIntent().getStringExtra("phoneNo");
        stdIC = getIntent().getStringExtra("stdIC");
        newPassword = getIntent().getStringExtra("newPassword");
        stdID = getIntent().getStringExtra("stdID");
        stdCampus = getIntent().getStringExtra("campusS");
        String image_path= getIntent().getStringExtra("image_uriIC");
        ICfile = Uri.parse(image_path);
        image_uriID = image_uri.toString();
        image_uriIC = ICfile.toString();

        if(idPhoto.getDrawable() == null){
            Toast.makeText(getApplicationContext(), "Please insert your Matric ID Photo", Toast.LENGTH_SHORT).show();
        }

    }

    private void showImageDialogID() {
        //options to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if (which==0){
                    //camera clicked
                    if (checkCameraPermissionID()){
                        //allowed, open camera
                        pickFromCameraID();
                    }
                    else {
                        requestCameraPermissionID();
                    }
                }
                else {
                    //gallery clicked
                    if (checkStoragePermissionID()){
                        //allowed, open gallery
                        pickFromGalleryID();
                    }
                    else
                        requestStoragePermissionID();
                }
            }
        }).show();
    }



    private void pickFromGalleryID() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK);
        intentToGallery.setType("image/*");
        startActivityForResult(intentToGallery, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCameraID() {
        //intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intentToCamera, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissionID() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissionID() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissionID() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermissionID() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {

                        pickFromCameraID();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Camera permissions are necessary...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGalleryID();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Storage permissions are necessary...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //get chosen image
                image_uri = data.getData();
                //set to image view
                idPhoto.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                idPhoto.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}