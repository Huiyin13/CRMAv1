package com.example.crmav1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.ManageBooking.SViewBookingDateInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.Model.Car;
import com.example.crmav1.R;

import java.util.ArrayList;

public class ViewBookedDateAdapter extends RecyclerView.Adapter<ViewBookedDateAdapter.Holder>{

    private Context context;
    public ArrayList<Booking> bookdate;

    public ViewBookedDateAdapter(Context context, ArrayList<Booking> bookdate) {
        this.context = context;
        this.bookdate = bookdate;
    }

    @NonNull
    @Override
    public ViewBookedDateAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_booked_date_adapter, parent, false);
        return  new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBookedDateAdapter.Holder holder, int position) {

        Booking booking = bookdate.get(position);
        holder.dateF.setText(booking.getDateF());
        holder.dateT.setText(booking.getDateT());
    }

    @Override
    public int getItemCount() {
        return bookdate.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView dateF, dateT;

        public Holder(@NonNull View itemView) {
            super(itemView);

            dateF = itemView.findViewById(R.id.dateFo);
            dateT = itemView.findViewById(R.id.dateTo);
        }
    }
}
