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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.adapters.CartAdaptert;
import com.fyp.javaidsgreenhouse.adapters.CartItemsAdaptert;
import com.fyp.javaidsgreenhouse.adapters.ProductsListAdapter;
import com.fyp.javaidsgreenhouse.models.ProductsModel;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderItemsFragment  extends Fragment {
    View view;
    RecyclerView rvItems;
    CartItemsAdaptert cartItemsAdapter;
    ArrayList<ProductsModel> ItemListCart = new ArrayList<>();
    TextView txt_no_data, txt_total_price;
    String user_id="",grand_total="",order_id="", orderItems_url="https://hos-hrm.tk/ecommerce-api/Api.php?action=singleOrders";
    private ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.order_items_fragment,container,false);
        initialization();
        return  view;
    }

    private void initialization() {
        if (getArguments() != null) {
            order_id = getArguments().getString("order_id");
            grand_total = getArguments().getString("total");
            Log.d("singin", "LOVE" + order_id);
        } else {
            Toast.makeText(getActivity(), "Basic info not save", Toast.LENGTH_SHORT).show();
        }
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            user_id=userModelClass.getUser_id();
        }else {
            Toast.makeText(getContext(), "User info missing", Toast.LENGTH_SHORT).show();
        }
        txt_no_data = view.findViewById(R.id.txt_no_data);
        txt_total_price = view.findViewById(R.id.txt_total_price);
        txt_total_price.setText(grand_total);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItems = view.findViewById(R.id.rv__order_items);
        rvItems.setLayoutManager(layoutManager);
        GetOrderData(order_id);
    }

    private void GetOrderData(String order_id) {
        pDialog.setMessage("Loading...");
        pDialog.show();
        ItemListCart.clear();
        Log.d("Response is", "CHECK RESPONSE"+orderItems_url+" ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderItems_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE"+response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        ProductsModel model=new ProductsModel();
                        model.setProduct_id(jsonObject.getString("product_id"));
                        model.setProduct_name(jsonObject.getString("product_name"));
                        model.setProduct_count(jsonObject.getString("qty"));
                        model.setProduct_price(jsonObject.getString("product_price"));
                        model.setProduct_image(jsonObject.getString("image"));
                        ItemListCart.add(model);

                    }
                    pDialog.dismiss();
                    if (ItemListCart != null) {
                        cartItemsAdapter = new CartItemsAdaptert((FragmentActivity) getContext(),ItemListCart);
                        rvItems.setAdapter(cartItemsAdapter);
                    } else {
                        Toast.makeText(getContext(), "NO DATA", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "Some Error", Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_id", order_id);
                return params;
            }
        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }
}
