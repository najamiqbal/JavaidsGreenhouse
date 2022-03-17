package com.fyp.javaidsgreenhouse.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.fyp.javaidsgreenhouse.activities.LoginSignUpActivity;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {
    View view;
    EditText et_email, et_user_name,et_user_mobile,et_user_address;
    Button btn_update;
    String userName = "", userEmail = "", userId = "",userAddress="",userMobile="";
    String update_profile_url = "https://hos-hrm.tk/ecommerce-api/Api.php?action=updateProfile";
    private ProgressDialog pDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.edit_profile_fragment,container,false);
        initialization();
        return  view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        et_email = view.findViewById(R.id.user_email_edit);
        et_user_name = view.findViewById(R.id.user_name_edit);
        et_user_address = view.findViewById(R.id.user_address_edit);
        et_user_mobile = view.findViewById(R.id.user_mobile_number_edit);
        btn_update = view.findViewById(R.id.update_btn_profile);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    updateProfile(userId,userEmail,userName,userAddress,userMobile);
                }else {
                    Toast.makeText(getContext(), "Some Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        BindData();
    }

    private void BindData() {
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            et_user_name.setText(userModelClass.getUser_name());
            et_email.setText(userModelClass.getUser_email());
            et_user_mobile.setText(userModelClass.getUser_mobile());
            et_user_address.setText(userModelClass.getUser_address());
        }
    }


    private void updateProfile(final String userId, final String userEmail, final String userName,final String userAddress,final String userMobile) {
        Log.e("check1122", "mobile number" + userId);
        pDialog.setMessage("updating...");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, update_profile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(getContext()).logOut();
                            startActivity(new Intent(getContext(), LoginSignUpActivity.class));
                            getActivity().finish();
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
                params.put("user_id", userId);
                params.put("username", userName);
                params.put("email", userEmail);
                params.put("mobile", userMobile);
                params.put("address", userAddress);
                return params;

            }

        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }


    //Validating data
    private boolean validate() {
        boolean valid = true;
        UserModelClass userModelClass = SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass != null) {
            userId = userModelClass.getUser_id();
        }
        userName = et_user_name.getText().toString().trim();
        userEmail = et_email.getText().toString().trim();
        userMobile = et_user_mobile.getText().toString().trim();
        userAddress = et_user_address.getText().toString().trim();
        if (userId.isEmpty()) {
            Toast.makeText(getContext(), "Please refresh the activity", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (userName.isEmpty()) {
            et_user_name.setError("Please Enter Phone");
            valid = false;
        } else {
            et_user_name.setError(null);
        }

        if (userEmail.isEmpty()) {
            et_email.setError("Please Enter Email");
            valid = false;
        } else {
            et_email.setError(null);
        }
        if (userMobile.isEmpty()) {
            et_user_mobile.setError("Please Enter Mobile");
            valid = false;
        } else {
            et_user_mobile.setError(null);
        }
        if (userAddress.isEmpty()) {
            et_user_address.setError("Please Enter Mobile");
            valid = false;
        } else {
            et_user_address.setError(null);
        }
        return valid;
    }
}
