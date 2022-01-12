package com.example.crmav1.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.R;

public class CarPhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView cPhoto;
    public ProgressBar progressBar;
    public CarPhotoHolder(@NonNull View itemView) {
        super(itemView);
        cPhoto = itemView.findViewById(R.id.cPhoto);
        progressBar = itemView.findViewById(R.id.progressBar_productlistlayout);
    }

    @Override
    public void onClick(View view) {

    }

}
