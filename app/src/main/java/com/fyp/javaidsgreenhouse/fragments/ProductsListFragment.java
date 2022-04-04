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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.adapters.CategoriesAdapter;
import com.fyp.javaidsgreenhouse.adapters.ProductsListAdapter;
import com.fyp.javaidsgreenhouse.models.CategoryModel;
import com.fyp.javaidsgreenhouse.models.ProductsModel;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsListFragment extends Fragment {

    View view;
    private ProgressDialog pDialog;
    ArrayList<ProductsModel> ItemListProducts = new ArrayList<>();
    RecyclerView recyclerView;
    String c_id="",c_name="";
    TextView text_products;
    String productslist_url="https://hos-hrm.tk/ecommerce-api/Api.php?action=getProducts";
    ProductsListAdapter productsListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_products, container, false);
        initialization();
        return view;

    }

    private void initialization() {
        if (getArguments() != null) {
            c_id = getArguments().getString("categories_id");
            c_name = getArguments().getString("categories_name");
            Log.d("singin", "LOVE" + c_id);
        } else {
            Toast.makeText(getActivity(), "Basic info not save", Toast.LENGTH_SHORT).show();
        }

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        recyclerView = view.findViewById(R.id.re_products);
        text_products = view.findViewById(R.id.text_products);
        text_products.setText(c_name);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        GetProductsData(c_id);
    }

    private void GetProductsData(String c_id) {
        pDialog.setMessage("Loading...");
        pDialog.show();
        ItemListProducts.clear();
        Log.d("Response is", "CHECK RESPONSE"+productslist_url+" ");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, productslist_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE"+response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        ProductsModel model=new ProductsModel();
                        model.setProduct_id(jsonObject.getString("product_id"));
                        model.setProduct_name(jsonObject.getString("name"));
                        model.setProduct_rating(jsonObject.getString("rating"));
                        model.setProduct_des(jsonObject.getString("description"));
                        model.setProduct_price(jsonObject.getString("price"));
                        model.setProduct_image(jsonObject.getString("image"));
                        ItemListProducts.add(model);

                    }
                    pDialog.dismiss();
                    if (ItemListProducts != null) {
                        productsListAdapter = new ProductsListAdapter((FragmentActivity) getContext(),ItemListProducts);
                        recyclerView.setAdapter(productsListAdapter);
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
                params.put("category_id", c_id);
                return params;
            }
        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Product's");
        super.onViewCreated(view, savedInstanceState);
    }
}
