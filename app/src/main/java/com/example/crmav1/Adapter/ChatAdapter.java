package com.example.crmav1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.Model.Chat;
import com.example.crmav1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> chat;

    FirebaseUser user;

    public ChatAdapter(Context context, List<Chat> chat) {
        this.context = context;
        this.chat = chat;
    }

    @NonNull
    @Override
    public ChatAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.Holder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.Holder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.Holder holder, int position) {
        Chat chat1 = chat.get(position);
        if (chat1.getType().equals("text")){
            holder.show_message.setVisibility(View.VISIBLE);
            holder.show_image.setVisibility(View.GONE);
            holder.show_message.setText(chat1.getMsg());
        }
        else {
            holder.show_message.setVisibility(View.GONE);
            holder.show_image.setVisibility(View.VISIBLE);
            Picasso.get().load(chat1.getMsg()).placeholder(R.drawable.ic_image).into(holder.show_image);

        }

    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView show_message;
        ImageView show_image;
        public Holder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            show_image = itemView.findViewById(R.id.show_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (chat.get(position).getSender().equals(user.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
