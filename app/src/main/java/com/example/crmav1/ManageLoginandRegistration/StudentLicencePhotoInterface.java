package com.example.crmav1.ManageLoginandRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class StudentLicencePhotoInterface extends AppCompatActivity {

    private ImageView licencePhoto;
    private Button register;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private StorageReference storageRef, storageRef1, storageRef2;

    private String fullName, emailAddress, phoneNo, stdIC, newPassword, image_uriID, stdID, stdCampus, image_uriIC;
    ProgressDialog progressDialog;

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
    private Uri ICfile, IDfile, Licencefile, image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_licence_photo_interface);

        this.setTitle("Registration");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Your registration is in progress. Please wait for a few seconds.");

        register = findViewById(R.id.registerS);
        licencePhoto = findViewById(R.id.stdLicencePhoto);

        auth = FirebaseAuth.getInstance();

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        licencePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialogLicence();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                inputData();
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
        String image_path = getIntent().getStringExtra("image_uriIC");
        ICfile = Uri.parse(image_path);
        String image_path1= getIntent().getStringExtra("image_uriID");
        IDfile = Uri.parse(image_path1);


        if(licencePhoto.getDrawable() == null){
            Toast.makeText(getApplicationContext(), "Please insert your Matric ID Photo", Toast.LENGTH_SHORT).show();
        }

        createAccount();
    }

    private void createAccount() {
        auth.createUserWithEmailAndPassword(emailAddress, newPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                saveAccountData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentLicencePhotoInterface.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAccountData(){

        String filePathAndName = "StdLicence/" + "" + auth.getUid();
        String filePathAndName1 = "StdIC/" + "" + auth.getUid();
        String filePathAndName2 = "StdID/" + "" + auth.getUid();
        storageRef = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageRef1 = FirebaseStorage.getInstance().getReference(filePathAndName1);
        storageRef2 = FirebaseStorage.getInstance().getReference(filePathAndName2);

        storageRef.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //get uri of uploaded image
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadImageUriLicence = uriTask.getResult();

                storageRef1.putFile(ICfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadImageUriIC = uriTask.getResult();

                        storageRef2.putFile(IDfile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful());
                                Uri downloadImageUriID = uriTask.getResult();

                                if (uriTask.isSuccessful()){
                                    //set data to save
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("sId", "" + auth.getUid());
                                    hashMap.put("userType", "Student");
                                    hashMap.put("sName", "" + fullName);
                                    hashMap.put("sEmail", "" + emailAddress);
                                    hashMap.put("sPhone", "" + phoneNo);
                                    hashMap.put("sIC", "" + stdIC);
                                    hashMap.put("sPassword", "" + newPassword);
                                    hashMap.put("sMatricID", "" + stdID);
                                    hashMap.put("sCampus", "" + stdCampus);
                                    hashMap.put("sIDUri", "" + downloadImageUriID);
                                    hashMap.put("sICUri", "" + downloadImageUriIC);
                                    hashMap.put("sLicenceUri", "" + downloadImageUriLicence);

                                    //save to database
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                    databaseReference.child(auth.getUid()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(StudentLicencePhotoInterface.this, "Register Successfully!", Toast.LENGTH_SHORT).show();
                                            if (auth.getCurrentUser().isEmailVerified()) {
                                                Intent intent2std_main = new Intent(StudentLicencePhotoInterface.this, StudentMainInterface.class);
                                                startActivity(intent2std_main);
                                                finish();
                                            } else {
                                                startActivity(new Intent(getApplicationContext(), EmailVerificationInterface.class));
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(StudentLicencePhotoInterface.this, "Register Unsuccessfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showImageDialogLicence() {
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
                    if (checkCameraPermissionLicence()){
                        //allowed, open camera
                        pickFromCameraLicence();
                    }
                    else {
                        requestCameraPermissionLicence();
                    }
                }
                else {
                    //gallery clicked
                    if (checkStoragePermissionLicence()){
                        //allowed, open gallery
                        pickFromGalleryLicence();
                    }
                    else
                        requestStoragePermissionLicence();
                }
            }
        }).show();
    }



    private void pickFromGalleryLicence() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK);
        intentToGallery.setType("image/*");
        startActivityForResult(intentToGallery, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCameraLicence() {
        //intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intentToCamera, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissionLicence() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissionLicence() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissionLicence() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermissionLicence() {
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

                        pickFromCameraLicence();
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
                        pickFromGalleryLicence();
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
                licencePhoto.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                licencePhoto.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}