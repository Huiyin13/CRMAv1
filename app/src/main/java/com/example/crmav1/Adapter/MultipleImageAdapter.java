package com.example.crmav1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.Model.Car;
import com.example.crmav1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MultipleImageAdapter extends RecyclerView.Adapter<MultipleImageAdapter.Holder> {

    private Context context;
    public ArrayList<Car> photo;

    public MultipleImageAdapter(Context context, ArrayList<Car> photo){
        this.context = context;
        this.photo = photo;
    }

    @NonNull
    @Override
    public MultipleImageAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.multiple_photo_adapter, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        //Picasso.get().load(photo.get(position).getImageC()).placeholder(R.drawable.add_image).into(holder.photoC);
    }

    @Override
    public int getItemCount() {
        return photo.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView photoC;

        public Holder(@NonNull View itemView) {
            super(itemView);

            photoC = itemView.findViewById(R.id.cPhoto);

        }
    }
}
