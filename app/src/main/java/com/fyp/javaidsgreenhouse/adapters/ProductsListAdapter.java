package com.fyp.javaidsgreenhouse.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.activities.MainActivity;
import com.fyp.javaidsgreenhouse.fragments.CartFragment;
import com.fyp.javaidsgreenhouse.fragments.ProductDetailFragment;
import com.fyp.javaidsgreenhouse.fragments.ProductsListFragment;
import com.fyp.javaidsgreenhouse.models.CategoryModel;
import com.fyp.javaidsgreenhouse.models.ProductsModel;
import com.fyp.javaidsgreenhouse.utils.CartDB;

import java.util.ArrayList;

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductsModel> list;
    private int quantity_plus = 1;
    MediaPlayer player;
    public ProductsListAdapter(FragmentActivity homeFragment, ArrayList<ProductsModel> arrayList) {
        this.context=homeFragment;
        this.list=arrayList;
        player = MediaPlayer.create(context, R.raw.delete_sound);
    }

    @NonNull
    @Override
    public ProductsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsListAdapter.ViewHolder holder, int position) {
        final ProductsModel pModel=list.get(position);
        holder.tv_name.setText(pModel.getProduct_name());
        holder.product_price.setText("Rs "+pModel.getProduct_price());
        Glide.with(context).load(pModel.getProduct_image()).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(holder.c_image);

        holder.c_image.setOnClickListener(v -> {

            FragmentManager manager = ((AppCompatActivity)
                    context).getSupportFragmentManager();
            ProductDetailFragment productDetailFragment=new ProductDetailFragment();
            Bundle args = new Bundle();
            args.putString("p_id", pModel.getProduct_id());
            args.putString("p_name", pModel.getProduct_name());
            args.putString("p_price", pModel.getProduct_price());
            args.putString("p_image", pModel.getProduct_image());
            args.putString("p_detail", pModel.getProduct_des());
            args.putString("p_rating", pModel.getProduct_rating());
            productDetailFragment.setArguments(args);
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R .id.frag_container,productDetailFragment );
            fragmentTransaction.addToBackStack("added");
            fragmentTransaction.commit();
        });

        holder.img_plus.setOnClickListener(view -> {
            quantity_plus++;
            holder.item_count.setText(quantity_plus + "");
        });
        holder.img_minus.setOnClickListener(view -> {
            if (quantity_plus > 1)
            quantity_plus--;
            holder.item_count.setText(quantity_plus + "");
        });
        holder.btn_addToCart.setOnClickListener(view -> {
            player.start();
            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
            CartDB.AddToCart(pModel.getProduct_id(),pModel.getProduct_name(),pModel.getProduct_price(),pModel.getProduct_image(),quantity_plus, String.valueOf(Integer.parseInt(pModel.getProduct_price())*quantity_plus),context);
            ((MainActivity) context).getCartCount();

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,product_price,item_count;
        ImageView c_image,img_plus,img_minus;
        TextView btn_addToCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            c_image=itemView.findViewById(R.id.img_product);
            tv_name=itemView.findViewById(R.id.txt_product_name);
            product_price=itemView.findViewById(R.id.txt_price);
            img_minus=itemView.findViewById(R.id.img_minus);
            img_plus=itemView.findViewById(R.id.img_plus);
            item_count=itemView.findViewById(R.id.txt_counter);
            btn_addToCart=itemView.findViewById(R.id.btn_request);
        }
    }

}
