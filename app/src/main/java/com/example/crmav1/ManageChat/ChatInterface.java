package com.example.crmav1.ManageChat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.Adapter.ChatAdapter;
import com.example.crmav1.ManageBooking.CBookingDetailsInterface;
import com.example.crmav1.ManageBooking.SBookingDetailsInterface;
import com.example.crmav1.Model.CarOwner;
import com.example.crmav1.Model.Chat;
import com.example.crmav1.Model.Student;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatInterface extends AppCompatActivity {

    private TextView name;
    private EditText message;
    private ImageButton send;
    private ImageView back, image;
    private RecyclerView messaging;
    private ChatAdapter adapter;
    private List<Chat> list;

    private FirebaseUser user;
    private DatabaseReference nameRef, chatRef;

    private String uid, userType, bid, cid, coId, sId;

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
        setContentView(R.layout.activity_chat_interface);

        uid = getIntent().getStringExtra("uid");
        name = findViewById(R.id.name);
        message = findViewById(R.id.message);
        back = findViewById(R.id.back);
        send = findViewById(R.id.send);
        image = findViewById(R.id.image);

        //init permission array
        cameraPermission =  new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission =  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        bid = getIntent().getStringExtra("bid");
        cid = getIntent().getStringExtra("cid");

        messaging = findViewById(R.id.chatMsg);
        messaging.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messaging.setLayoutManager(linearLayoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();
        readMsg(user.getUid(), uid);
        nameRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CarOwner carOwner = snapshot.getValue(CarOwner.class);
                    Student student = snapshot.getValue(Student.class);
                    if (carOwner.getUserType().equalsIgnoreCase("Car Owner")){
                        name.setText(carOwner.getCoName());
                        userType = carOwner.getUserType();
                        coId = carOwner.getCoId();
                        System.out.println(carOwner.getCoName());
                    }
                    else if (student.getUserType().equalsIgnoreCase("Student")){
                        name.setText(student.getsName());
                        userType = student.getUserType();
                        sId = student.getsId();
                        System.out.println(student.getsName());
                    }
                }
                else{
                    name.setText("User not existed!");
                    message.setVisibility(View.GONE);
                    send.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msging = message.getText().toString().trim();
                if (!msging.equalsIgnoreCase("")){
                    sendMsg(user.getUid(), uid, msging);
                    Toast.makeText(ChatInterface.this, "Message sent.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ChatInterface.this, " You cannot send empty message.", Toast.LENGTH_SHORT).show();
                }
                message.setText("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equalsIgnoreCase("Student"))
                {
                    Intent intent2back = new Intent(ChatInterface.this, CBookingDetailsInterface.class);
                    intent2back.putExtra("bid", bid);
                    intent2back.putExtra("sId", sId);
                    intent2back.putExtra("cid", cid);
                    startActivity(intent2back);
                    finish();
                }
                else {
                    Intent intent2back = new Intent(ChatInterface.this, SBookingDetailsInterface.class);
                    intent2back.putExtra("bid", bid);
                    intent2back.putExtra("coId", coId);
                    intent2back.putExtra("cid", cid);
                    startActivity(intent2back);
                    finish();
                }

            }
        });
    }

    private void showImageDialog() {
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
                    if (checkCameraPermission()){
                        //allowed, open camera
                        pickFromCamera();
                    }
                    else {
                        requestCameraPermission();
                    }
                }
                else {
                    //gallery clicked
                    if (checkStoragePermission()){
                        //allowed, open gallery
                        pickFromGallery();
                    }
                    else
                        requestStoragePermission();
                }
            }
        }).show();
    }



    private void pickFromGallery() {
        Intent intentToGallery = new Intent(Intent.ACTION_PICK);
        intentToGallery.setType("image/*");
        startActivityForResult(intentToGallery, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentToCamera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intentToCamera, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
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

                        pickFromCamera();
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
                        pickFromGallery();
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
                try {
                    sendImage(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to image view
                try {
                    sendImage(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendImage(Uri image_uri) throws IOException {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Image....");
        progressDialog.show();
        String filePathAndName1 = "ChatImage" + "" + user.getUid();

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] data = baos.toByteArray();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName1);
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                String downloadImageUri = uriTask.getResult().toString();

                if (uriTask.isSuccessful()){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender", user.getUid());
                    hashMap.put("receiver", uid);
                    hashMap.put("msg", downloadImageUri);
                    hashMap.put("type", "image");

                    databaseReference.child("Chat").push().setValue(hashMap);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    private void sendMsg (String sender, String receiver, String msg){
        DatabaseReference chat = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg", msg);
        hashMap.put("type", "text");

        chat.child("Chat").push().setValue(hashMap);
    }

    public void  readMsg (String myID, String receiverID){

        list = new ArrayList<>();
        chatRef = FirebaseDatabase.getInstance().getReference("Chat");

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myID) && chat.getSender().equals(receiverID) ||
                            chat.getReceiver().equals(receiverID) && chat.getSender().equals(myID)){
                        list.add(chat);
                    }
                    adapter = new ChatAdapter(ChatInterface.this, list);
                    messaging.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}