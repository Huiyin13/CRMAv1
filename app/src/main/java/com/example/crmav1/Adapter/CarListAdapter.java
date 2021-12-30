package com.example.crmav1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.ManageCar.ACarDetailsInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.R;

import java.util.ArrayList;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.Holder> {

    private Context context;
    public ArrayList<Car> carList;
    private CarListAdapter.ItemClickListener carListener;

    public CarListAdapter(Context context, ArrayList<Car> carList, CarListAdapter.ItemClickListener carListener){
        this.context = context;
        this.carList = carList;
        this.carListener = carListener;
    }


    @NonNull
    @Override
    public CarListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View v = LayoutInflater.from(context).inflate(R.layout.car_list_adapter, parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarListAdapter.Holder holder, int position) {
        Car cID = carList.get(position);

        holder.carPlate.setText(cID.getcPlate());
        holder.carModel.setText(cID.getcModel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carListener.onItemClick(position);
                Intent intent2view = new Intent(context, ACarDetailsInterface.class);
                intent2view.putExtra("cid", cID.getCid());
                intent2view.putExtra("uid", cID.getUid());
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
        TextView carPlate, carModel;

        public Holder(@NonNull View itemView) {
            super(itemView);
            carPlate = itemView.findViewById(R.id.carPlate);
            carModel = itemView.findViewById(R.id.carModel);
        }
    }
}
