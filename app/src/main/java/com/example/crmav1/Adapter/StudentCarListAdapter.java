package com.example.crmav1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.Model.Car;
import com.example.crmav1.R;

import java.util.ArrayList;

public class StudentCarListAdapter extends RecyclerView.Adapter<StudentCarListAdapter.Holder> {

    private Context context;
    public ArrayList<Car> carArrayList;
    private  StudentCarListAdapter.ItemClickListener carListener;

    public StudentCarListAdapter(Context context, ArrayList<Car> carArrayList, ItemClickListener carListener) {
        this.context = context;
        this.carArrayList = carArrayList;
        this.carListener = carListener;
    }

    @NonNull
    @Override
    public StudentCarListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View v = LayoutInflater.from(context).inflate(R.layout.photo_adapter_std, parent,false);
        return new StudentCarListAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentCarListAdapter.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface ItemClickListener {
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
