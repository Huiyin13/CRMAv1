package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageCar.CarDetailsInterface;
import com.example.crmav1.ManageCar.CarListInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.Model.FAQ;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileReader;
import java.util.HashMap;

public class UpdateFaqInterface extends AppCompatActivity {
    private TextView title, answer;
    private Button update, delete;
    private RadioButton std, co;

    private String question, ans, userType, fid;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faq_interface);

        title = findViewById(R.id.title);
        answer = findViewById(R.id.answer);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        std = findViewById(R.id.std);
        co = findViewById(R.id.co);

        fid = getIntent().getStringExtra("fid");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog deleteAlert = confirmation();
                deleteAlert.show();
            }
        });

        DatabaseReference display = FirebaseDatabase.getInstance().getReference("Faq").child(fid);
        display.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    FAQ qna = snapshot.getValue(FAQ.class);
                    title.setText(qna.getTitle());
                    answer.setText(qna.getAnswer());
                    if (qna.getUserType().equalsIgnoreCase("Car owner")){
                        co.setChecked(true);
                    }
                    else {
                        std.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question = title.getText().toString().trim();
                ans = answer.getText().toString().trim();
                if (co.isChecked()){
                    userType = "Car Owner";
                }
                else if (std.isChecked()){
                    userType = "Student";
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("Faq").child(fid);
                if (title.getText().length()!=0 && answer.getText().length()!=0 && std.isChecked() || co.isChecked()){
                    databaseReference.child("title").setValue(question);
                    databaseReference.child("answer").setValue(ans);
                    databaseReference.child("userType").setValue(userType);
                    Intent intent2update = new Intent(UpdateFaqInterface.this, AdminMainInterface.class);
                    startActivity(intent2update);
                    finish();
                }
            }
        });

    }
    private AlertDialog confirmation() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Sure?")
                .setMessage("Do you want to Delete this FAQ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        DatabaseReference deleteC = FirebaseDatabase.getInstance().getReference("Faq").child(fid);
                        deleteC.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(UpdateFaqInterface.this, "This FAQ is deleted.", Toast.LENGTH_SHORT).show();
                                Intent intent2delete = new Intent(UpdateFaqInterface.this, AdminMainInterface.class);
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