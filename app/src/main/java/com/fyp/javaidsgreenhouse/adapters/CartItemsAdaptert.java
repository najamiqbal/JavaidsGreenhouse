package com.fyp.javaidsgreenhouse.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.models.ProductsModel;

import java.util.ArrayList;

public class CartItemsAdaptert extends RecyclerView.Adapter<CartItemsAdaptert.ViewHolder> {
    Context context;
    ArrayList<ProductsModel> list;
    public CartItemsAdaptert(FragmentActivity context, ArrayList<ProductsModel> itemListCart) {
        this.context=context;
        this.list=itemListCart;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_view_holder, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductsModel model=list.get(position);
        holder.txt_quantity.setText(model.getProduct_count());
        holder.txt_name.setText(model.getProduct_name());
        holder.txt_price.setText(model.getProduct_price());
        Glide.with(context).load(model.getProduct_image()).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(holder.img_item);
        holder.txt_total_price.setText(String.valueOf(Integer.parseInt(model.getProduct_count())*Integer.parseInt(model.getProduct_price())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_quantity,txt_name,txt_price,txt_total_price;
        ImageView img_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_quantity=itemView.findViewById(R.id.txt_product_count);
            txt_name=itemView.findViewById(R.id.txt_item_name);
            txt_price=itemView.findViewById(R.id.txt_price);
            txt_total_price=itemView.findViewById(R.id.txt_total_price);
            img_item=itemView.findViewById(R.id.img_product);
        }
    }
}
