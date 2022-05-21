package com.fyp.javaidsgreenhouse.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.activities.MainActivity;
import com.fyp.javaidsgreenhouse.fragments.CartFragment;
import com.fyp.javaidsgreenhouse.models.ProductsModel;
import com.fyp.javaidsgreenhouse.utils.CartDB;

import java.util.ArrayList;

public class CartAdaptert extends RecyclerView.Adapter<CartAdaptert.ViewHolder> {
    Context context;
    ArrayList<ProductsModel> list;
    private Fragment fragment;
    MediaPlayer player;
    int total_price=0;

    public CartAdaptert(Context context, ArrayList<ProductsModel> itemListCart, boolean b, Fragment fragment) {
        this.context=context;
        this.list=itemListCart;
        this.fragment = fragment;
        player = MediaPlayer.create(context, R.raw.delete_sound);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductsModel model=list.get(position);
        holder.txt_quantity.setText(model.getProduct_count());
        holder.txt_name.setText(model.getProduct_name());
        holder.txt_price.setText(model.getProduct_price());
        holder.txt_counter.setText(model.getProduct_count());
       // Toast.makeText(context, ""+Integer.parseInt(model.getProduct_price())*Integer.parseInt(model.getProduct_count()), Toast.LENGTH_SHORT).show();
        holder.txt_total_price.setText(model.getSub_total());

 /*       total_price=total_price+Integer.parseInt(holder.txt_total_price.getText().toString());
        Toast.makeText(context, ""+total_price, Toast.LENGTH_SHORT).show();*/
        Glide.with(context).load(model.getProduct_image()).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(holder.img_item);

        holder.img_delete.setOnClickListener(view -> {
            player.start();
            Toast.makeText(context, "item removed", Toast.LENGTH_SHORT).show();
            CartDB.RemoveItemFromCart(model.getProduct_id(),context);
            ((CartFragment) fragment).GetCartData();
            ((MainActivity) context).getCartCount();
        });
        holder.img_plus.setOnClickListener(view -> {
            CartDB.UpdateItemCart(model.getProduct_id(),"quantity",Integer.parseInt(holder.txt_counter.getText().toString())+1,context);
            CartDB.UpdateItemCart(model.getProduct_id(),"sub_total",(Integer.parseInt(holder.txt_counter.getText().toString())+1)*Integer.parseInt(model.getProduct_price()),context);
            ((CartFragment) fragment).GetCartData();
        });
        holder.img_minus.setOnClickListener(view -> {
            if (Integer.parseInt(holder.txt_counter.getText().toString())>1){
                CartDB.UpdateItemCart(model.getProduct_id(),"quantity",Integer.parseInt(holder.txt_counter.getText().toString())-1,context);
                CartDB.UpdateItemCart(model.getProduct_id(),"sub_total",(Integer.parseInt(holder.txt_counter.getText().toString())-1)*Integer.parseInt(model.getProduct_price()),context);
                ((CartFragment) fragment).GetCartData();
            }else {
                Toast.makeText(context, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
            }

        });
        //((CartFragment) fragment).SetTotalPrice(total_price);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_quantity,txt_name,txt_price,txt_total_price,txt_counter;
        ImageView img_item, img_delete,img_plus,img_minus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_quantity=itemView.findViewById(R.id.txt_product_count);
            txt_name=itemView.findViewById(R.id.txt_item_name);
            txt_price=itemView.findViewById(R.id.txt_price);
            txt_total_price=itemView.findViewById(R.id.txt_total_price);
            txt_counter=itemView.findViewById(R.id.txt_counter);
            img_delete=itemView.findViewById(R.id.img_delete);
            img_item=itemView.findViewById(R.id.img_product);
            img_plus=itemView.findViewById(R.id.img_plus);
            img_minus=itemView.findViewById(R.id.img_minus);
        }
    }
}
