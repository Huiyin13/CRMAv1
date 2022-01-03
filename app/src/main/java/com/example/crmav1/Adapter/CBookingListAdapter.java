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
import com.example.crmav1.Model.Booking;
import com.example.crmav1.Model.Car;
import com.example.crmav1.Model.Student;
import com.example.crmav1.R;

import java.util.ArrayList;

public class CBookingListAdapter extends RecyclerView.Adapter<CBookingListAdapter.Holder> {

    private Context context;
    public ArrayList<Booking> carBook;
    private CBookingListAdapter.ItemClickListener bookListener;

    public CBookingListAdapter(Context context, ArrayList<Booking> carBook, ItemClickListener bookListener) {
        this.context = context;
        this.carBook = carBook;
        this.bookListener = bookListener;
    }

    @NonNull
    @Override
    public CBookingListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View v = LayoutInflater.from(context).inflate(R.layout.cbooking_list_adapter, parent,false);
        return new CBookingListAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CBookingListAdapter.Holder holder, int position) {

        Booking bookingC = carBook.get(position);
        holder.date.setText(bookingC.getFinalFrom() + " To "+ bookingC.getFinalTo());
        holder.status.setText(bookingC.getbStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookListener.onItemClick(position);
                Intent intent2view = new Intent(context, CBookingDetailsInterface.class);
                intent2view.putExtra("bid", bookingC.getBid());
                intent2view.putExtra("sId", bookingC.getsId());
                intent2view.putExtra("cid", bookingC.getCid());
                context.startActivity(intent2view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carBook.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView date, status;
        public Holder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
        }
    }
}
