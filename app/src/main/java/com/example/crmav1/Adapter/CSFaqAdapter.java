package com.example.crmav1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crmav1.ManageAccount.CViewFaqInterface;
import com.example.crmav1.Model.FAQ;
import com.example.crmav1.R;

import java.util.ArrayList;

public class CSFaqAdapter extends RecyclerView.Adapter<CSFaqAdapter.Holder>{

    private Context context;
    public ArrayList<FAQ> faq;

    public CSFaqAdapter(Context context, ArrayList<FAQ> faq) {
        this.context = context;
        this.faq = faq;
    }

    @NonNull
    @Override
    public CSFaqAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cs_faq_list_adapter, parent,false);
        return new CSFaqAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CSFaqAdapter.Holder holder, int position) {
        FAQ FaQ = faq.get(position);
        holder.title.setText(FaQ.getTitle());
        holder.ans.setText(FaQ.getAnswer());
    }

    @Override
    public int getItemCount() {
        return faq.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView title, ans;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            ans = itemView.findViewById(R.id.ans);
        }
    }
}
