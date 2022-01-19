package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageCar.ACarListInterface;
import com.example.crmav1.ManageCar.CarListInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddFaqInterface extends AppCompatActivity {

    private TextView title, answer;
    private Button add, cancel;
    private RadioButton std, co, both;
    private RadioGroup type;
    ProgressDialog progressDialog;

    private String question, ans, userType;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faq_interface);

        title = findViewById(R.id.title);
        answer = findViewById(R.id.answer);
        add = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        std = findViewById(R.id.std);
        co = findViewById(R.id.co);
        both = findViewById(R.id.both);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding FAQ....");
        progressDialog.dismiss();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
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
                else if (both.isChecked()){
                    userType = "Both";
                }

                if(question.isEmpty()){
                    title.setError("Question is required");
                    return;
                }

                if(ans.isEmpty()){
                    answer.setError("Answer is required");
                    return;
                }

                if (co.isChecked() == false && std.isChecked() == false && both.isChecked() == false){
                    Toast.makeText(getApplicationContext(), "Please select the user type/category.", Toast.LENGTH_SHORT).show();
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("Faq");
                String fid = databaseReference.push().getKey();
                DatabaseReference faqRef = databaseReference.child(fid);

                if (title.getText().length()!=0 && answer.getText().length()!=0 && std.isChecked() == true || co.isChecked() == true || both.isChecked() == true){
                    progressDialog.show();
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("fid", "" + fid);
                    hashMap.put("userType", "" + userType);
                    hashMap.put("title", "" + question);
                    hashMap.put("answer", "" + ans);

                    faqRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddFaqInterface.this,"FAQ Added", Toast.LENGTH_SHORT).show();
                            Intent intent2add = new Intent(AddFaqInterface.this, AdminMainInterface.class);
                            startActivity(intent2add);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddFaqInterface.this, "Add FAQ Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.car:
                        startActivity(new Intent(getApplicationContext(), ACarListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), CarOwnerListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), AdminMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }


}