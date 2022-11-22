package com.example.badminton_app_admin.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.badminton_app_admin.R;

public class BookingViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_orderId, txt_fullName, txt_orderPrice, txt_orderCategory, txt_orderTime;

    public BookingViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_orderId = itemView.findViewById(R.id.order_id);
        txt_fullName = itemView.findViewById(R.id.full_name);
        txt_orderPrice = itemView.findViewById(R.id.order_price);
        txt_orderCategory = itemView.findViewById(R.id.order_category);
        txt_orderTime = itemView.findViewById(R.id.order_time);


    }


}

