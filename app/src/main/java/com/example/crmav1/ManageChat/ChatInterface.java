package com.example.crmav1.ManageChat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.Adapter.ChatAdapter;
import com.example.crmav1.Model.CarOwner;
import com.example.crmav1.Model.Chat;
import com.example.crmav1.Model.Student;
import com.example.crmav1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatInterface extends AppCompatActivity {

    private TextView name;
    private EditText message;
    private ImageView back;
    private ImageButton send;
    private RecyclerView messaging;
    private ChatAdapter adapter;
    private List<Chat> list;

    private FirebaseUser user;
    private DatabaseReference nameRef, chatRef;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_interface);

        uid = getIntent().getStringExtra("uid");
        name = findViewById(R.id.name);
        message = findViewById(R.id.message);
        back = findViewById(R.id.back);
        send = findViewById(R.id.send);

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

                        System.out.println(carOwner.getCoName());
                    }
                    else if (student.getUserType().equalsIgnoreCase("Student")){
                        name.setText(student.getsName());
                        System.out.println(student.getsName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

            }
        });
    }

    private void sendMsg (String sender, String receiver, String msg){
        DatabaseReference chat = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg", msg);

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