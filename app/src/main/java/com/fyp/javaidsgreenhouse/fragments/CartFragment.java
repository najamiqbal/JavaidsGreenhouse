package com.fyp.javaidsgreenhouse.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.carteasy.v1.lib.Carteasy;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.activities.MainActivity;
import com.fyp.javaidsgreenhouse.adapters.CartAdaptert;
import com.fyp.javaidsgreenhouse.models.ProductsModel;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {
    public CartFragment() {
        // require a empty public constructor
    }

    View view;
    RecyclerView rvItems;
    CartAdaptert cartAdaptert;
    ArrayList<ProductsModel> ItemListCart = new ArrayList<>();
    TextView txt_no_data, txt_total_price, check_out;
    String user_id="",grand_total="",prodcuts_array="", order_url="https://hos-hrm.tk/ecommerce-api/Api.php?action=placeOrder";
    private ProgressDialog pDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            user_id=userModelClass.getUser_id();
        }else {
            Toast.makeText(getContext(), "User info missing", Toast.LENGTH_SHORT).show();
        }
        txt_no_data = view.findViewById(R.id.txt_no_data);
        check_out = view.findViewById(R.id.txt_check_out);
        txt_total_price = view.findViewById(R.id.txt_total_price);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItems = view.findViewById(R.id.rv_items);
        rvItems.setLayoutManager(layoutManager);
        GetCartData();

        check_out.setOnClickListener(view1 -> {
            grand_total=txt_total_price.getText().toString();
            PrePareData();
        });
    }

    private void PrePareData() {
        Map<Integer, Map> data;
        Carteasy cs = new Carteasy();
        data = cs.ViewAll(getActivity());
        if (data != null && data.size() != 0) {
             JSONArray jsonArray = new JSONArray();
            JSONObject jsonObjOne = null;
            //Log.d("CART DATA HERE", data.toString());
            for (Map.Entry<Integer, Map> entry : data.entrySet()) {

                //Retrieve the values of the Map by starting from index 0 - zero

                jsonObjOne = new JSONObject();
                //Get the sub values of the Map
                Map<String, String> innerdata = entry.getValue();

//                Log.d("CART DATA HERE",innerdata.toString());
                for (Map.Entry<String, String> innerEntry : innerdata.entrySet()) {
                    System.out.println(innerEntry.getKey() + "=" + innerEntry.getValue());
                    try {

                        String product = innerEntry.getKey();
                        switch (product) {
                            case "p_id":
                                jsonObjOne.put("p_id",innerEntry.getValue() );
                                break;
                            case "name":
                                jsonObjOne.put("name", innerEntry.getValue());
                                break;
                            case "price":
                                jsonObjOne.put("price", innerEntry.getValue());
                                break;
                            case "image":
                                jsonObjOne.put("image", innerEntry.getValue());
                                break;
                            case "quantity":
                                jsonObjOne.put("quantity", innerEntry.getValue());
                                break;
                            case "sub_total":
                                jsonObjOne.put("sub_total", innerEntry.getValue());
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                jsonArray.put(jsonObjOne);
                Log.d("CART FINAL DATA ->", jsonArray.toString());
            }

            PlaceOrder(user_id,grand_total,jsonArray.toString());
        }
    }

    private void PlaceOrder(String user_id, String grand_total, String prodcuts_array) {
        pDialog.setMessage("updating...");
        pDialog.show();
        Log.d("CART FINAL CHECKOUT==>",user_id+grand_total+prodcuts_array);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, order_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API RESPONSE=>",response);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Carteasy cs = new Carteasy();
                            cs.clearCart(getContext());
                            GetCartData();
                            Toast.makeText(getContext(), "order placed", Toast.LENGTH_SHORT).show();

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "HELLO", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "Error Please tyr again"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("total", grand_total);
                params.put("products", prodcuts_array);
                return params;

            }

        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }

    public void GetCartData() {
        Map<Integer, Map> data;
        Carteasy cs = new Carteasy();
        data = cs.ViewAll(getActivity());
        if (data != null && data.size() != 0) {
            ItemListCart.clear();
            int totalprice = 0;
            Log.d("CART DATA HERE", data.toString());
            for (Map.Entry<Integer, Map> entry : data.entrySet()) {

                //Retrieve the values of the Map by starting from index 0 - zero
                ProductsModel cartitem = new ProductsModel();
                //Get the sub values of the Map
                Map<String, String> innerdata = entry.getValue();
//                Log.d("CART DATA HERE",innerdata.toString());
                for (Map.Entry<String, String> innerEntry : innerdata.entrySet()) {
                    System.out.println(innerEntry.getKey() + "=" + innerEntry.getValue());

                    String product = innerEntry.getKey();
                    switch (product) {
                        case "p_id":
                            cartitem.setProduct_id(innerEntry.getValue());
                            break;
                        case "name":
                            cartitem.setProduct_name(innerEntry.getValue());
                            break;
                        case "price":
                            cartitem.setProduct_price(innerEntry.getValue());

                            break;
                        case "image":
                            cartitem.setProduct_image(innerEntry.getValue());
                            break;
                        case "quantity":
                            cartitem.setProduct_count(innerEntry.getValue());
                            break;
                        case "sub_total":
                            cartitem.setSub_total(innerEntry.getValue());
                            totalprice = totalprice + Integer.parseInt(innerEntry.getValue());
                            break;
                    }
                }
                //Toast.makeText(getContext(), ""+totalprice, Toast.LENGTH_SHORT).show();
                ItemListCart.add(cartitem);
                cartAdaptert = new CartAdaptert(getContext(), ItemListCart, true, CartFragment.this);
                rvItems.setAdapter(cartAdaptert);
                Log.d("CART LIST HERE", "");


            }
            totalprice=totalprice+200;
            txt_total_price.setText("" + totalprice);
            ((MainActivity)getActivity()).getCartCount();
        } else {
            txt_no_data.setVisibility(View.VISIBLE);
            ItemListCart.clear();
            txt_total_price.setText("");
            cartAdaptert = new CartAdaptert(getContext(), ItemListCart, true, CartFragment.this);
            rvItems.setAdapter(cartAdaptert);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Cart");
        super.onViewCreated(view, savedInstanceState);
    }

}