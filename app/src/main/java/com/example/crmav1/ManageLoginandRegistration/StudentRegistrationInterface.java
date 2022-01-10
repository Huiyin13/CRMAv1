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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.crmav1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class StudentRegistrationInterface extends AppCompatActivity {

    private EditText name, ic, phone, email, password, confirmPass, id;
    private RadioButton gambang, pekan;
    private RadioGroup campus;
    private ImageView icPhoto;
    private Button next;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private StorageReference storageRef;

    private String fullName, emailAddress, campusS, phoneNo, stdIC, stdID, newPassword, confirmPassword, image_uriIC;

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
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration_interface);

        this.setTitle("Registration");

        name = findViewById(R.id.stdName);
        ic = findViewById(R.id.stdIC);
        phone = findViewById(R.id.stdPhone);
        email = findViewById(R.id.stdEmail);
        password = findViewById(R.id.stdPassword);
        confirmPass = findViewById(R.id.stdConfirm);
        id = findViewById(R.id.stdID);
        campus = findViewById(R.id.stdCampus);
        gambang = findViewById(R.id.rbGambang);
        pekan = findViewById(R.id.rbPekan);
        icPhoto = findViewById(R.id.stdICPhoto);
        next = findViewById(R.id.nextS);

        auth = FirebaseAuth.getInstance();

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        icPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialogIC();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
                Intent intent2next = new Intent(StudentRegistrationInterface.this, StudentIDPhotoInterface.class);
                if(!fullName.isEmpty() && !emailAddress.isEmpty() && !phoneNo.isEmpty() && !stdIC.isEmpty()  && !newPassword.isEmpty()  && !confirmPassword.isEmpty()
                        && !stdID.isEmpty()  && !phoneNo.isEmpty() && icPhoto.getDrawable()!=null && gambang.isChecked() == true || pekan.isChecked() == true){
                    if (password.getText().length()> 5){
                        if (newPassword.equals(confirmPassword)){
                            intent2next.putExtra("fullName", fullName);
                            intent2next.putExtra("emailAddress", emailAddress);
                            intent2next.putExtra("phoneNo", phoneNo);
                            intent2next.putExtra("stdIC", stdIC);
                            intent2next.putExtra("newPassword", newPassword);
                            intent2next.putExtra("campusS", campusS);
                            intent2next.putExtra("stdID", stdID);
                            intent2next.putExtra("image_uriIC", image_uriIC);
                            startActivity(intent2next);
                        }
                    }
                    else{
                        Toast.makeText(StudentRegistrationInterface.this, "Minimum password length is 6.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(StudentRegistrationInterface.this, "Some info missing!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inputData(){
        fullName = name.getText().toString().trim();
        emailAddress = email.getText().toString().trim();
        phoneNo = phone.getText().toString().trim();
        stdIC = ic.getText().toString().trim();
        stdID = id.getText().toString().trim();
        newPassword = password.getText().toString().trim();
        confirmPassword = confirmPass.getText().toString();
        image_uriIC = image_uri.toString();

        if(image_uriIC.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please insert your IC/ Passport Photo", Toast.LENGTH_SHORT).show();
        }

        if(fullName.isEmpty()){
            name.setError("Full Name is required");
            return;
        }
        if(stdID.isEmpty()){
            id.setError("Matric ID is required");
            return;
        }
        if(stdIC.isEmpty()){
            ic.setError("IC Number/ Passport Number is required");
            return;
        }
        if(phoneNo.isEmpty()){
            phone.setError("Contact Number is required");
            return;
        }
        if(emailAddress.isEmpty()){
            email.setError("Email Address is required");
            return;
        }
        if(newPassword.isEmpty()){
            password.setError("Password is required");
            return;
        }
        if (password.getText().length()> 5) {
            password.setError("Password minimum length is 6");
            return;
        }
        if(confirmPassword.isEmpty()){
            confirmPass.setError("Confirm Password is required");
            return;
        }

        if(!newPassword.equals(confirmPassword)) {
            confirmPass.setError("Password does not match!");
            return;
        }

        if (gambang.isChecked() == false && pekan.isChecked() == false){
            Toast.makeText(getApplicationContext(), "Please select your Campus", Toast.LENGTH_SHORT).show();
        }

        if(gambang.isChecked()){
            campusS = "Gambang";
        }
        else if(pekan.isChecked()){
            campusS = "Pekan";
        }

    }


    private void showImageDialogIC() {
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
                    if (checkCameraPermissionIC()){
                        //allowed, open camera
                        pickFromCameraIC();
                    }
                    else {
                        requestCameraPermissionIC();
                    }
                }
                else {
                    //gallery clicked
                    if (checkStoragePermissionIC()){
                        //allowed, open gallery
                        pickFromGalleryIC();
                    }
                    else
                        requestStoragePermissionIC();
                }
            }
        }).show();
    }



    private void pickFromGalleryIC() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK);
        intentToGallery.setType("image/*");
        startActivityForResult(intentToGallery, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCameraIC() {
        //intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intentToCamera, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissionIC() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissionIC() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissionIC() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermissionIC() {
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

                        pickFromCameraIC();
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
                        pickFromGalleryIC();
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
                icPhoto.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                icPhoto.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}