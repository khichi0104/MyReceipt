package com.kuk.myreceipt.viewholder;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kuk.myreceipt.R;
import com.kuk.myreceipt.ViewReceiptActivity;
import com.kuk.myreceipt.fragment.AllListFragment;
import com.kuk.myreceipt.model.ReceiptModel;
import com.squareup.picasso.Picasso;
import android.content.Intent;

import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;

/**
 * Created by KUK on 9/4/2560.
 */

public class ReceiptViewHolder extends RecyclerView.ViewHolder implements Serializable {

    TextView txttitle;
    TextView txtDate;
    TextView txtPrice;
    TextView txtdesc;
    ImageView imageView;

    ReceiptModel data;
    AllListFragment fragment;
    CardView cardview;
Context context;

    public ReceiptViewHolder(View itemView, AllListFragment fragment) {
        super(itemView);
        context = itemView.getContext();

        txttitle = (TextView)itemView.findViewById(R.id.txttitle);
        txtDate = (TextView)itemView.findViewById(R.id.txtdate);
        txtPrice = (TextView)itemView.findViewById(R.id.txtprice);
        txtdesc =(TextView)itemView.findViewById(R.id.txtdesc);
        imageView = (ImageView)itemView.findViewById(R.id.imageView);
        cardview = (CardView)itemView.findViewById(R.id.cardview);

        this.fragment = fragment;

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewReceiptActivity.class);
                i.putExtra("selectedData", data);
                context.startActivity( i );
            }
        });
    }

    public void setData(ReceiptModel data) {
        this.data = data;
        txttitle.setText(data.getTitle());
        txtDate.setText(data.getDateTime());
        txtPrice.setText(String.valueOf(data.getPrice()));
        txtdesc.setText(data.getDescription());

        File filePath = new File(data.getFilePath());
        filePath = new File(data.getThumbnailPath());
        Picasso.with(itemView.getContext()).load(filePath).into(imageView);
    }
}
