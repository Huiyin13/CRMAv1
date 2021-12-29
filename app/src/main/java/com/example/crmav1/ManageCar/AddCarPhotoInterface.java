package com.example.crmav1.ManageCar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.Adapter.MultipleImageAdapter;
import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageAccount.CarOwnerProfileInterface;
import com.example.crmav1.ManageBooking.CBookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;

public class AddCarPhotoInterface extends AppCompatActivity {

    private static final int IMAGE_CODE = 1;
    private Button addP, addC;
    private TextView alert, alert2;
    private RecyclerView photoList;
    private ArrayList<Uri> carPhoto;
    private ArrayList<Car> carPhotos;
    private MultipleImageAdapter adapater;
    private String modelC;
    private String plateC;
    private String feeC;
    private String paxC;
    private String typeC;
    private String descriptionC;
    private Uri uriSticker;
    private int uploadCount = 0;
    DatabaseReference topath, frompath;
    private StorageReference storage, storageRef;
    private FirebaseAuth auth;
    private FirebaseDatabase db;

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference delref = FirebaseDatabase.getInstance().getReference("Car").child("carPhotoUri");
        delref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    delref.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_photo_interface);

        addP = findViewById(R.id.addPhoto);
        addC = findViewById(R.id.addCar);
        //photoList = findViewById(R.id.photos);
        alert = findViewById(R.id.alert);
        alert2 = findViewById(R.id.alert2);
//        photoList.setHasFixedSize(true);
//        photoList.setLayoutManager(new LinearLayoutManager(this));
//        photoList.setAdapter(adapater);

        storage = FirebaseStorage.getInstance().getReference();
        carPhoto = new ArrayList<Uri>();
        carPhotos = new ArrayList<>();

        addP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_CODE);
            }
        });

        addC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.book:
                        startActivity(new Intent(getApplicationContext(), CBookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(AddCarPhotoInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(AddCarPhotoInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void inputData() {
        modelC = getIntent().getStringExtra("modelC");
        plateC = getIntent().getStringExtra("plateC");
        feeC = getIntent().getStringExtra("feeC");
        paxC = getIntent().getStringExtra("paxC");
        descriptionC = getIntent().getStringExtra("descriptionC");
        typeC = getIntent().getStringExtra("typeC");

        String image_path = getIntent().getStringExtra("stickerUri");
        uriSticker = Uri.parse(image_path);

        savaCar();

    }

    private void savaCar() {
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        DatabaseReference id = FirebaseDatabase.getInstance().getReference("Car").child(auth.getUid());
        String cid = id.push().getKey();
        DatabaseReference car = id.child(cid);
        String filePathAndName1 = "CarSticker/" + "" + cid;
        storageRef = FirebaseStorage.getInstance().getReference(filePathAndName1);

        storageRef.putFile(uriSticker).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //get uri of uploaded image
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadImageUriSticker = uriTask.getResult();


                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("uid", "" + auth.getUid());
                hashMap.put("cid", "" + cid);
                hashMap.put("cType", "" + typeC);
                hashMap.put("cModel", "" + modelC);
                hashMap.put("description", "" + descriptionC);
                hashMap.put("rentFee", "" + feeC);
                hashMap.put("cSticker", "" + downloadImageUriSticker);
                hashMap.put("cStatus", "" + "Free");
                hashMap.put("cPlate", "" + plateC);
                hashMap.put("cPerson", "" + paxC);
                hashMap.put("cHideReason", "" + " ");


                car.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        for (uploadCount = 0; uploadCount < carPhoto.size(); uploadCount++ ){
                            Uri IndividualFile = carPhoto.get(uploadCount);
                            final  StorageReference imagename = storage.child("CarPhoto/"+IndividualFile.getLastPathSegment());

                            imagename.putFile(IndividualFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url = String.valueOf(uri);
                                            SendLink(url);
                                            topath = car.child("cPhotoUri");
                                            frompath = FirebaseDatabase.getInstance().getReference("Car").child("carPhotoUri");
                                            frompath.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    topath.setValue(snapshot.getValue());

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }

                        Toast.makeText(AddCarPhotoInterface.this,"Car Added", Toast.LENGTH_SHORT).show();
                        Intent intent2add = new Intent(AddCarPhotoInterface.this, CarListInterface.class);
                        startActivity(intent2add);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCarPhotoInterface.this, "Add Car Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }

    private void SendLink(String url) {

        final String timestamp = "" + System.currentTimeMillis();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Car").child("carPhotoUri");
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("ImageLink",url);
        databaseReference.push().setValue(hashMap);
        carPhoto.clear();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_CODE){
            if(resultCode == RESULT_OK){
                if(data.getClipData() != null){
                    int countClipData = data.getClipData().getItemCount();
                    int currentImageSelect = 0;
                    while (currentImageSelect < countClipData){
                        Uri imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        carPhoto.add(imageUri);
                        currentImageSelect = currentImageSelect +1;
                    }

                    alert.setVisibility(View.VISIBLE);
                    alert2.setVisibility(View.GONE);
                    alert.setText("You Have Selected "+ carPhoto.size() +" Images");

                }else{
                    Toast.makeText(this, "Please Select Multiple File", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}