package com.example.crmav1.ManageCar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.Adapter.CarPhotoHolder;
import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageAccount.StudentProfileInterface;
import com.example.crmav1.ManageBooking.CBookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.ManageLoginandRegistration.LoginInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.Model.cPhotoUri;
import com.example.crmav1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CarDetailsInterface extends AppCompatActivity {

    private TextView model, plate, person, type, reason, notice,tvReason;
    private EditText description, fee;
    private ImageView sticker;
    private RecyclerView photo;
    private Button update, delete;
    private RadioButton list, unlist;
    private RadioGroup status;

    private FirebaseDatabase db;
    private DatabaseReference carDBRef, carDBRef2, updateRef;
    private FirebaseUser user;
    private Uri stickerC, photoC;

    private String cid, newFee, newDescription, newStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details_interface);

        model = findViewById(R.id.cModel);
        plate = findViewById(R.id.cPlate);
        person = findViewById(R.id.cPax);
        type = findViewById(R.id.cType);
        description = findViewById(R.id.description);
        fee = findViewById(R.id.cFee);
        sticker = findViewById(R.id.cSticker);
        photo = findViewById(R.id.photoListView);
        reason = findViewById(R.id.cHideReason);
        update = findViewById(R.id.cUpdate);
        delete = findViewById(R.id.cDelete);
        list = findViewById(R.id.listed);
        unlist = findViewById(R.id.unlisted);
        status = findViewById(R.id.carStatus);
        notice = findViewById(R.id.notice);
        tvReason = findViewById(R.id.tReason);

        photo = findViewById(R.id.photoListView);
        photo.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photo.setLayoutManager(layoutManager);

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
                        startActivity(new Intent(CarDetailsInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CarDetailsInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });



        cid = getIntent().getStringExtra("cid");
        user = FirebaseAuth.getInstance().getCurrentUser();
        carDBRef = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid);
        System.out.println(carDBRef);
        carDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    Car car = snapshot.getValue(Car.class);
                    System.out.println(car.getcModel());
                    model.setText(car.getcModel());
                    plate.setText(car.getcPlate());
                    person.setText(car.getcPerson());
                    type.setText(car.getcType());
                    description.setText(car.getDescription());
                    fee.setText(car.getRentFee());
                    reason.setText(car.getcHideReason());
                    Picasso.get().load(car.getcSticker()).into(sticker);
                    if (car.getcStatus().equalsIgnoreCase("Free")) {
                        list.setChecked(true);
                        tvReason.setVisibility(View.GONE);
                        reason.setVisibility(View.GONE);
                    }
                    else if (car.getcStatus().equalsIgnoreCase("Unlisted")){
                        unlist.setChecked(true);
                        tvReason.setVisibility(View.GONE);
                        reason.setVisibility(View.GONE);
                    }
                    else if (car.getcStatus().equalsIgnoreCase("Booked") || car.getcStatus().equalsIgnoreCase("Applying")){
                        status.setVisibility(View.GONE);
                        tvReason.setVisibility(View.GONE);
                        reason.setVisibility(View.GONE);
                        notice.setText("This car is in the process of applying or book. Status of car cannot be changed!");
                    }
                    else {
                        status.setVisibility(View.GONE);
                        notice.setText("You car is unlisted by the admin. Please view on the reason!");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updateRef = FirebaseDatabase.getInstance().getReference("Car");

        updateRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               update.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if (fee.getText().length()!=0 && description.getText().length()!=0 ){
                           String uid = user.getUid();
                           newFee = fee.getText().toString();
                           newDescription = description.getText().toString();
                           if (list.isChecked()){
                               newStatus = "Free";
                           }
                           else{
                               newStatus = "Unlisted";
                           }

                           updateRef.child(uid).child(cid).child("description").setValue(newDescription);
                           updateRef.child(uid).child(cid).child("rentFee").setValue(newFee);
                           updateRef.child(uid).child(cid).child("cStatus").setValue(newStatus);
                           Toast.makeText(CarDetailsInterface.this, "Update Successful!", Toast.LENGTH_SHORT).show() ;
                           Intent intent2done = new Intent(CarDetailsInterface.this, StudentAccountInterface.class);
                           startActivity(intent2done);
                       }
                       else {
                           Toast.makeText(CarDetailsInterface.this, "Fill all!", Toast.LENGTH_SHORT).show();
                       }
                   }
               });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog deleteAlert = confirmation();
                deleteAlert.show();
            }
        });

    }

    private AlertDialog confirmation() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Sure?")
                .setMessage("Do you want to Delete this car?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        DatabaseReference deleteCUri = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid).child("cPhotoUri");
                        DatabaseReference deleteBooking = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid).child("Booking");
                        DatabaseReference deleteC = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid);
                        deleteC.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                deleteCUri.removeValue();
                                deleteBooking.removeValue();
                                Toast.makeText(CarDetailsInterface.this, "This car details is deleted.", Toast.LENGTH_SHORT).show();
                                Intent intent2delete = new Intent(CarDetailsInterface.this, CarListInterface.class);
                                startActivity(intent2delete);
                            }
                        });

                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }

    @Override
    protected void onStart() {
        super.onStart();
        carDBRef2 = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid).child("cPhotoUri");
        FirebaseRecyclerOptions<cPhotoUri> carPhoto =
                new  FirebaseRecyclerOptions.Builder<cPhotoUri>()
                .setQuery(carDBRef2, cPhotoUri.class)
                .build();
        FirebaseRecyclerAdapter<cPhotoUri, CarPhotoHolder> photoAdapter = new FirebaseRecyclerAdapter<cPhotoUri, CarPhotoHolder>(carPhoto) {
            @Override
            protected void onBindViewHolder(@NonNull CarPhotoHolder holder, int position, @NonNull cPhotoUri model) {

                holder.progressBar.setVisibility(View.VISIBLE);
                Picasso.get().load(model.getImageLink()).into(holder.cPhoto, new ImageLoadedCallback(holder.progressBar){
                    @Override
                    public void onSuccess() {
                        this.progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @NonNull
            @Override
            public CarPhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_adapter, parent, false);
                CarPhotoHolder holder = new CarPhotoHolder(view);
                return holder;
            }
        };
        photo.setAdapter(photoAdapter);
        photoAdapter.startListening();
    }
    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(Exception e) {

        }
    }

}