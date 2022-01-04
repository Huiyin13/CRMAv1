package com.example.crmav1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.ManageAccount.UpdateFaqInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.Model.FAQ;
import com.example.crmav1.R;

import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.Holder> {
    private Context context;
    public ArrayList<FAQ> faq;
    private FaqAdapter.ItemClickListener faqListener;

    public FaqAdapter(Context context, ArrayList<FAQ> faq, ItemClickListener faqListener) {
        this.context = context;
        this.faq = faq;
        this.faqListener = faqListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.faq_list_adapter, parent,false);
        return new FaqAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        FAQ FaQ = faq.get(position);
        holder.title.setText(FaQ.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faqListener.onItemClick(position);
                Intent inten2edit = new Intent(context, UpdateFaqInterface.class);
                inten2edit.putExtra("fid", FaQ.getFid());
                context.startActivity(inten2edit);
            }
        });

    }

    @Override
    public int getItemCount() {
        return faq.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView title;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
