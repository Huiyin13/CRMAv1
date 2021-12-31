package com.example.crmav1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.ManageBooking.SCarDetailsInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.R;

import java.util.ArrayList;

public class StdCarPhotoHolder extends RecyclerView.Adapter<StdCarPhotoHolder.Holder> {

    private Context context;
    public ArrayList<Car> carList;
    private StdCarPhotoHolder.ItemClickListener carListener;

    public StdCarPhotoHolder(Context context, ArrayList<Car> carList, ItemClickListener carListener) {
        this.context = context;
        this.carList = carList;
        this.carListener = carListener;
    }

    @NonNull
    @Override
    public StdCarPhotoHolder.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.photo_adapter_std, parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StdCarPhotoHolder.Holder holder, int position) {
        Car car = carList.get(position);

        holder.model.setText(car.getcModel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carListener.onItemClick(position);
                Intent intent2view = new Intent(context, SCarDetailsInterface.class);
                intent2view.putExtra("cid", car.getCid());
                intent2view.putExtra("coID", car.getCoId());
                context.startActivity(intent2view);
            }
        });


    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView model;

        public Holder(@NonNull View itemView) {
            super(itemView);
            model = itemView.findViewById(R.id.carModel);
        }

    }
}
