package com.example.crmav1.Adapter;

import android.content.ClipData;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.R;

public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cModel, cPlate;
    public ItemClickListener itemClickListener;

    public Holder(@NonNull View itemView) {
        super(itemView);
        cModel = itemView.findViewById(R.id.carModel);
        cPlate = itemView.findViewById(R.id.carPlate);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
