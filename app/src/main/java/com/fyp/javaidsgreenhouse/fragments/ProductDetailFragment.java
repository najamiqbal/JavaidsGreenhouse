package com.fyp.javaidsgreenhouse.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.CartDB;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailFragment extends Fragment {
    View view;
    String p_name,p_id,p_price,p_image,p_des;
    int s_counter=1;
    ImageView p_img,img_plus,img_minus;
    TextView tv_p_name,tv_p_price,tv_p_des,txt_counter;
    Button addToCart, btn_rating;
    RatingBar ratingBar;
    private ProgressDialog pDialog;
    MediaPlayer player;
    String rating_value="",user_id="",p_rating="",rating_url="https://hos-hrm.tk/ecommerce-api/Api.php?action=addRating";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.product_detail_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        final UserModelClass Model = SharedPrefManager.getInstance(getActivity()).getUser();
        user_id=Model.getUser_id();
        player = MediaPlayer.create(getContext(), R.raw.delete_sound);
        if (getArguments() != null) {
            p_id = getArguments().getString("p_id");
            p_name = getArguments().getString("p_name");
            p_des = getArguments().getString("p_detail");
            p_price = getArguments().getString("p_price");
            p_image = getArguments().getString("p_image");
            p_rating = getArguments().getString("p_rating");
            Log.d("singin", "LOVE" + p_des);
        } else {
            Toast.makeText(getActivity(), "Basic info not save", Toast.LENGTH_SHORT).show();
        }
        p_img=view.findViewById(R.id.htab_header);
        btn_rating=view.findViewById(R.id.rating_btn);
        ratingBar=view.findViewById(R.id.rating_bar);
        img_minus=view.findViewById(R.id.img_minus);
        img_plus=view.findViewById(R.id.img_plus);
        tv_p_name=view.findViewById(R.id.txt_product_name);
        tv_p_price=view.findViewById(R.id.txt_product_price);
        tv_p_des=view.findViewById(R.id.txt_item_details);
        txt_counter=view.findViewById(R.id.txt_counter);
        addToCart=view.findViewById(R.id.btn_add_to_cart);
        Bindata();
        addToCart.setOnClickListener(view1 -> {
            player.start();
            Toast.makeText(getContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
            CartDB.AddToCart(p_id,p_name,p_price,p_image,s_counter, String.valueOf(Integer.parseInt(p_price)*s_counter),getContext());
        });
        img_plus.setOnClickListener(view1 -> {
            s_counter++;
            txt_counter.setText(s_counter + "");
        });
        img_minus.setOnClickListener(view1 ->{
            if (s_counter>1){
                s_counter--;
                txt_counter.setText(s_counter + "");
            }else {
                Toast.makeText(getContext(), "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
            }
        });
        btn_rating.setOnClickListener(view1 -> {
            rating_value= String.valueOf(ratingBar.getRating());
            Toast.makeText(getContext(), ""+rating_value, Toast.LENGTH_SHORT).show();
            GiveRatingToProduct(user_id,p_id,rating_value);
        });
        p_img.setOnClickListener(view1 -> {
            showImage(p_image);
        });
    }


    public void showImage(String imageUri) {
        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(getContext());
        //imageView.setImageURI(imageUri);
        if (imageUri != null) {
            Glide.with(getContext()).load(imageUri).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(imageView);
        }
        //Picasso.get().load(imageUri).placeholder(R.drawable.logo).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    private void GiveRatingToProduct(String user_id, String p_id, String rating_value) {
        pDialog.setMessage("Sending....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, rating_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "rating added successfully", Toast.LENGTH_SHORT).show();
                            // Goto Login Page
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "error catch"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.d("Response error","Volley response errror is"+error.getMessage());
                Toast.makeText(getActivity(), "Error Please tyr again "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("rating", rating_value);
                params.put("product_id", p_id);
                return params;
            }
        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }


    private void Bindata() {
        tv_p_name.setText(p_name);
        tv_p_price.setText(p_price);
        tv_p_des.setText(p_des);
        ratingBar.setRating(Float.parseFloat(p_rating));
        Glide.with(getContext()).load(p_image).dontAnimate().fitCenter().placeholder(R.drawable.applogo).into(p_img);
    }
}
