package com.example.crmav1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.ManageBooking.SBookingDetailsInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.R;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.Holder> {

    private Context context;
    public ArrayList<Booking> bookings;
    private BookingListAdapter.ItemClickListener bookListener;

    public BookingListAdapter(Context context, ArrayList<Booking> bookings, ItemClickListener bookListener) {
        this.context = context;
        this.bookings = bookings;
        this.bookListener = bookListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.booking_list_adapter, parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Booking booking = bookings.get(position);

        holder.bookid.setText(booking.getBid());
        holder.date.setText(booking.getFinalFrom()+ " To " + booking.getFinalTo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookListener.onItemClick(position);
                Intent intent2view = new Intent(context, SBookingDetailsInterface.class);
                intent2view.putExtra("bid", booking.getBid());
                intent2view.putExtra("coId", booking.getCoId());
                intent2view.putExtra("cid", booking.getCid());
                context.startActivity(intent2view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView bookid, date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            bookid = itemView.findViewById(R.id.bid);
            date = itemView.findViewById(R.id.dateTime);
        }
    }
}
