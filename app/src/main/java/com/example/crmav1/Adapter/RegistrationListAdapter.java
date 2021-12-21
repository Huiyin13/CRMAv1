package com.example.crmav1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.Model.CarOwner;
import com.example.crmav1.R;

import java.util.ArrayList;

public class RegistrationListAdapter extends RecyclerView.Adapter<RegistrationListAdapter.Holder>{

    private Context context;
    //array list <data class>
    public ArrayList<CarOwner> coList;
    private ItemClickListenser coListener;

    //constructor
    public  RegistrationListAdapter(Context context, ArrayList<CarOwner> list, ItemClickListenser coListener){
        this.context = context;
        this.coList = list;
        this.coListener = coListener;
    }

    @NonNull
    @Override
    public RegistrationListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View v = LayoutInflater.from(context).inflate(R.layout.car_owner_list_adapter, parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistrationListAdapter.Holder holder, int position) {
        CarOwner coID = coList.get(position);

        holder.registerName.setText(coID.getCoName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView registerName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            registerName = itemView.findViewById(R.id.registerName);
        }
    }

    public interface ItemClickListenser {
        void onItemClick(int position);
    }
}
