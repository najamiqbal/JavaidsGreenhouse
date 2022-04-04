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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.fragments.ProductsListFragment;
import com.fyp.javaidsgreenhouse.models.CategoryModel;
import com.fyp.javaidsgreenhouse.models.ProductsModel;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    Context context;
    ArrayList<CategoryModel> list;

    public CategoriesAdapter(FragmentActivity context, ArrayList<CategoryModel> itemListcategory) {
        this.context = context;
        this.list = itemListcategory;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CategoryModel mModel = list.get(position);
        holder.tv_name.setText(mModel.getName());
        Glide.with(context).load(mModel.getImage_path()).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(holder.c_image);

        holder.c_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = ((AppCompatActivity)
                        context).getSupportFragmentManager();
                ProductsListFragment fragment=new ProductsListFragment();
                Bundle args = new Bundle();
                args.putString("categories_id", mModel.getC_id());
                args.putString("categories_name", mModel.getName());
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.frag_container, fragment);
                fragmentTransaction.addToBackStack("added");
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView c_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.category_name);
            c_image = itemView.findViewById(R.id.c_image);
        }
    }

}
