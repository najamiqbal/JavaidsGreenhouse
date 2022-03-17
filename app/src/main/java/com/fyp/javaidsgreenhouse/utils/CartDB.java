package com.fyp.javaidsgreenhouse.utils;

import android.content.Context;

import com.carteasy.v1.lib.Carteasy;

public class CartDB {

    public static  void AddToCart(String id, String name, String price, String img, int quantity,String sub_total, Context context){
        Carteasy cs = new Carteasy();
        cs.add(id,"p_id",id);
        cs.add(id,"name",name);
        cs.add(id,"price",price);
        cs.add(id,"image",img);
        cs.add(id,"quantity",quantity);
        cs.add(id,"sub_total",sub_total);
        cs.commit(context);
        cs.persistData(context,true);
    }

    public static void RemoveItemFromCart(String p_id,Context context){
        Carteasy cs = new Carteasy();
        cs.RemoveId(p_id, context);

    }
    public static void UpdateItemCart(String p_id,String key,Object newValue,Context context){
        Carteasy cs = new Carteasy();
        cs.update(p_id,key,newValue,context);
    }

}
