package com.kuk.myreceipt.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuk.myreceipt.R;
import com.kuk.myreceipt.fragment.AllListFragment;
import com.kuk.myreceipt.model.ReceiptModel;
import com.kuk.myreceipt.viewholder.ReceiptViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KUK on 9/4/2560.
 */



public class ReceiptRecycleviewAdapter extends RecyclerView.Adapter<ReceiptViewHolder> {

    public List<ReceiptModel> receiptModelList;

    private AllListFragment fragment;

    public ReceiptRecycleviewAdapter(AllListFragment allListFragment) {
        receiptModelList = new ArrayList<>();
        fragment = allListFragment;

    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("photo","Start onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_all_list_view,parent,false);
        ReceiptViewHolder holder = new ReceiptViewHolder(view,fragment);
        Log.d("photo","End onCreateViewHolder");

        return holder;
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position) {
        holder.setData(receiptModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return receiptModelList.size();
    }

    public void updateData(List<ReceiptModel> data){
        receiptModelList = data;
        this.notifyDataSetChanged();
    }
}
