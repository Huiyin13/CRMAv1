package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageBooking.BookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.LoginInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.Model.Chat;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

public class StudentProfileInterface extends AppCompatActivity {

    private EditText name,  phone;
    private TextView ic, email, id;
    private RadioButton gambang, pekan;
    private Button update, delete;
    private ImageView icPhoto, idPhoto, licencePhoto;

    private FirebaseDatabase db;
    private DatabaseReference stdDBRef;
    private FirebaseAuth auth;
    private Uri icFile, idFile, licenceFile;

    ProgressDialog progressDialog;

    private String uid, nameStd, emailStd, phoneStd, campusStd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_interface);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");

        name = findViewById(R.id.stdName);
        ic = findViewById(R.id.stdIC);
        id = findViewById(R.id.stdID);
        email = findViewById(R.id.stdEmail);
        phone = findViewById(R.id.stdPhone);
        icPhoto = findViewById(R.id.stdICPhoto);
        idPhoto = findViewById(R.id.stdIDPhoto);
        licencePhoto = findViewById(R.id.stdLicencePhoto);
        gambang = findViewById(R.id.rbGambang);
        pekan = findViewById(R.id.rbPekan);
        update = findViewById(R.id.stdUpdate);
        delete = findViewById(R.id.stdDelete);

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.book:
                        startActivity(new Intent(getApplicationContext(), BookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(StudentProfileInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        name.setText(getIntent().getStringExtra("nameS").toString());
        ic.setText(getIntent().getStringExtra("icS").toString());
        id.setText(getIntent().getStringExtra("matricIDS").toString());
        email.setText(getIntent().getStringExtra("emailS").toString());
        phone.setText(getIntent().getStringExtra("phoneS").toString());
        if (getIntent().getStringExtra("campusS").equals("Gambang")){
            gambang.setChecked(true);
        }
        else{
            pekan.setChecked(true);
        }

        String ic = getIntent().getStringExtra("ICUriS");
        icFile = Uri.parse(ic);
        String id = getIntent().getStringExtra("IDUriS");
        idFile = Uri.parse(id);
        String licence = getIntent().getStringExtra("LicenceUriS");
        licenceFile = Uri.parse(licence);
        try{
            Picasso.get().load(icFile).placeholder(R.drawable.add_image).into(icPhoto);
        }
        catch (Exception e) {

        }
        try{
            Picasso.get().load(idFile).placeholder(R.drawable.add_image).into(idPhoto);
        }
        catch (Exception e) {

        }
        try{
            Picasso.get().load(licenceFile).placeholder(R.drawable.add_image).into(licencePhoto);
        }
        catch (Exception e) {

        }


        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        stdDBRef = db.getReference("Users");

        stdDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (name.getText().length()!=0 && phone.getText().length()!=0){
                            uid = auth.getUid();
                            nameStd = name.getText().toString().toUpperCase();
                            phoneStd = phone.getText().toString();

                            if (gambang.isChecked()){
                                campusStd = "Gambang";
                            }
                            else if (pekan.isChecked()){
                                campusStd = "Pekan";
                            }

                            stdDBRef.child(uid).child("sName").setValue(nameStd);
                            stdDBRef.child(uid).child("sPhone").setValue(phoneStd);
                            stdDBRef.child(uid).child("sCampus").setValue(campusStd);
                            Toast.makeText(StudentProfileInterface.this, "Profile Update Successful!", Toast.LENGTH_SHORT).show() ;
                            Intent intent2done = new Intent(StudentProfileInterface.this, StudentAccountInterface.class);
                            startActivity(intent2done);
                        }else {
                            Toast.makeText(StudentProfileInterface.this, "All field must be filled!", Toast.LENGTH_SHORT).show();
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

    private AlertDialog confirmation(){
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("THINK TWICE!!")
                .setMessage("Do you want to Delete Your account?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        FirebaseUser sID = FirebaseAuth.getInstance().getCurrentUser();
                        sID.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                String userID = sID.getUid();
                                DatabaseReference chat = FirebaseDatabase.getInstance().getReference("Chat");
                                chat.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for (DataSnapshot  dataSnapshot : snapshot.getChildren()){
                                                Chat chat1 = dataSnapshot.getValue(Chat.class);
                                                if (chat1.getReceiver().equalsIgnoreCase(userID)){
                                                    chat.child(dataSnapshot.getKey()).removeValue();
                                                }
                                                if (chat1.getSender().equalsIgnoreCase(userID)){
                                                    chat.child(dataSnapshot.getKey()).removeValue();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                DatabaseReference ID = FirebaseDatabase.getInstance().getReference("Users");
                                DatabaseReference booking = FirebaseDatabase.getInstance().getReference("Booking").child(userID);
                                ID.child(userID).removeValue();
                                booking.removeValue();
                                Toast.makeText(StudentProfileInterface.this, "Your account is deleted. Please REGISTER again for login!!", Toast.LENGTH_SHORT).show();
                                Intent intent2delete = new Intent(StudentProfileInterface.this, LoginInterface.class);
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
}