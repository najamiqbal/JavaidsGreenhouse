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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.adapters.CategoriesAdapter;
import com.fyp.javaidsgreenhouse.models.CategoryModel;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeFragment(){
        // require a empty public constructor
    }

    View view;
    private ProgressDialog pDialog;
    ArrayList<CategoryModel> ItemListcategory = new ArrayList<>();
    RecyclerView recyclerView;
    String categorylist_url="https://hos-hrm.tk/ecommerce-api/Api.php?action=getCategories";
    CategoriesAdapter categoriesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        recyclerView = view.findViewById(R.id.re_category);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        GetCategories();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Javaid's GreenHouse");
        super.onViewCreated(view, savedInstanceState);
    }

    private void GetCategories() {
        pDialog.setMessage("Loading...");
        pDialog.show();
        ItemListcategory.clear();
        Log.d("Response is", "CHECK RESPONSE"+categorylist_url+" ");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, categorylist_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE"+response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        CategoryModel model=new CategoryModel();
                        model.setName(jsonObject.getString("category_name"));
                        model.setC_id(jsonObject.getString("category_id"));
                        model.setImage_path(jsonObject.getString("category_image"));
                        ItemListcategory.add(model);

                    }
                    pDialog.dismiss();
                    if (ItemListcategory != null) {
                        categoriesAdapter = new CategoriesAdapter((FragmentActivity) getContext(),ItemListcategory);
                        recyclerView.setAdapter(categoriesAdapter);
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
        });
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }
}