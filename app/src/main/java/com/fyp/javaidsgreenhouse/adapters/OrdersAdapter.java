package com.fyp.javaidsgreenhouse.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.fragments.OrderItemsFragment;
import com.fyp.javaidsgreenhouse.fragments.ProductDetailFragment;
import com.fyp.javaidsgreenhouse.models.OrderModel;
import com.fyp.javaidsgreenhouse.models.ProductsModel;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderModel> list;

    public OrdersAdapter(Context context, ArrayList<OrderModel> itemListOrders) {
        this.context = context;
        this.list = itemListOrders;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        final OrderModel model = list.get(position);
        holder.txt_order_num.setText(model.getOrder_id());
        holder.txt_order_total.setText(model.getOrder_total());
        if (model.getOrder_status().equals("0")){
            holder.txt_orderstatus.setText("pending");
        }else if (model.getOrder_status().equals("1")){
            holder.txt_orderstatus.setText("delivered");
        }
        holder.img_open_detail.setOnClickListener(view -> {

            FragmentManager manager = ((AppCompatActivity)
                    context).getSupportFragmentManager();
            OrderItemsFragment orderItemsFragment=new OrderItemsFragment();
            Bundle args = new Bundle();
            args.putString("order_id", model.getOrder_id());
            args.putString("total", model.getOrder_total());
            orderItemsFragment.setArguments(args);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R .id.frag_container,orderItemsFragment );
            fragmentTransaction.addToBackStack("added");
            fragmentTransaction.commit();

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_order_num, txt_order_total, txt_orderstatus;
        ImageView img_open_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_order_num = itemView.findViewById(R.id.txt_order_num);
            txt_order_total = itemView.findViewById(R.id.txt_order_total);
            txt_orderstatus = itemView.findViewById(R.id.txt_order_status);
            img_open_detail = itemView.findViewById(R.id.img_open_details);
        }
    }
}
