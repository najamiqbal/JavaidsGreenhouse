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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.adapters.FeedbackListAdapter;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.models.feedbackModel;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFeedBackFragment extends Fragment {

    View view;
    RecyclerView dr_recyclerView;
    List<feedbackModel> ItemList;
    private ProgressDialog pDialog;
    FeedbackListAdapter mAdapter;
    String getfeedbackUrl = "https://hos-hrm.tk/ecommerce-api/Api.php?action=getFeedbacks",user_id="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.my_feedbacks_fragment,container,false);
        initilization();
        return view;
    }
    private void initilization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        user_id=userModelClass.getUser_id();
        dr_recyclerView = view.findViewById(R.id.recycler_view_queries);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,2);
        ItemList = new ArrayList<>();
        dr_recyclerView.setLayoutManager(linearLayoutManager);
        GetMyFeedbacks(user_id);

    }

    private void GetMyFeedbacks(String user_id) {
        pDialog.setMessage("please Wait....");
        pDialog.show();
        Log.d("status", "CHECK=====>id" + user_id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,  getfeedbackUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        //Log.d("status", "CHECK" + jsonObject.getString("mobile"));

                        feedbackModel model = new feedbackModel();

                        model.setId(jsonObject.getString("id"));
                        model.setFeedback(jsonObject.getString("feedback"));
                        ItemList.add(model);

                    }
                    pDialog.dismiss();
                    if (ItemList != null) {
                        mAdapter = new FeedbackListAdapter(getContext(), ItemList);
                        dr_recyclerView.setAdapter(mAdapter);
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
        getActivity().setTitle("MY Feedbacks");
        super.onViewCreated(view, savedInstanceState);
    }
}
