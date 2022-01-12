package com.example.crmav1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.ManageBooking.CBookingDetailsInterface;
import com.example.crmav1.ManageChat.ChatInterface;
import com.example.crmav1.Model.Chat;
import com.example.crmav1.Model.Student;
import com.example.crmav1.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {

    private Context context;
    private List<Student> studentList;
    private ChatListAdapter.ItemClickListener nameListener;

    public ChatListAdapter(Context context, List<Student> studentList, ItemClickListener nameListener) {
        this.context = context;
        this.studentList = studentList;
        this.nameListener = nameListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View v = LayoutInflater.from(context).inflate(R.layout.chat_list_adapter, parent,false);
        return new ChatListAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Student student = studentList.get(position);

        holder.name.setText(student.getsName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameListener.onItemClick(position);
                Intent intent2chat = new Intent(context, ChatInterface.class);
                intent2chat.putExtra("uid", student.getsId());
                context.startActivity(intent2chat);
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView name;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
