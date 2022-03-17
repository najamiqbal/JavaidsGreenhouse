package com.fyp.javaidsgreenhouse.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fyp.javaidsgreenhouse.adapters.OrdersAdapter;
import com.fyp.javaidsgreenhouse.adapters.ProductsListAdapter;
import com.fyp.javaidsgreenhouse.models.OrderModel;
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

public class OrdersUserFragment extends Fragment {
    View view;
    RecyclerView rvItems;
    OrdersAdapter OrdersAdaptert;
    private ProgressDialog pDialog;
    ArrayList<OrderModel> ItemListOrders = new ArrayList<>();
    String user_id="",ordersurl="https://hos-hrm.tk/ecommerce-api/Api.php?action=myOrders";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.orders_user_fragment,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        rvItems = view.findViewById(R.id.rv_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            user_id=userModelClass.getUser_id();
        }else {
            Toast.makeText(getContext(), "some error in user id", Toast.LENGTH_SHORT).show();
        }
        GetMyOrders(user_id);

    }

    private void GetMyOrders(String user_id) {
        pDialog.setMessage("Loading...");
        pDialog.show();
        ItemListOrders.clear();
        Log.d("Response is", "CHECK RESPONSE"+ordersurl+" ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ordersurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE"+response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        OrderModel model=new OrderModel();
                        model.setOrder_id(jsonObject.getString("order_id"));
                        model.setOrder_total(jsonObject.getString("total"));
                        model.setOrder_status(jsonObject.getString("status"));

                        ItemListOrders.add(model);

                    }
                    pDialog.dismiss();
                    if (ItemListOrders != null) {
                        OrdersAdaptert = new OrdersAdapter(getContext(), ItemListOrders);
                        rvItems.setAdapter(OrdersAdaptert);
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
                params.put("user_id", user_id);
                return params;
            }
        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Orders");
        super.onViewCreated(view, savedInstanceState);
    }
}
