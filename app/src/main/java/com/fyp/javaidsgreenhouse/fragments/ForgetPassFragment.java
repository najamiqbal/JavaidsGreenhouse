package com.fyp.javaidsgreenhouse.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassFragment extends Fragment
{
    View view;
    Button btn_submit;
    EditText et_email;
    TextView tv_gologin;
    String t_email="";
    private ProgressDialog pDialog;
    String Isexist_url = "https://hos-hrm.tk/ecommerce-api/Api.php?action=resetPassword";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_forgetpassword,container,false);
        initialiazation();
        return view;
    }

    private void initialiazation() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        btn_submit=view.findViewById(R.id.submit_btn);
        et_email=view.findViewById(R.id.et_phone_no);
        tv_gologin=view.findViewById(R.id.backtologin);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t_email=et_email.getText().toString();
                if (!t_email.isEmpty()){
                    IsUserExist(t_email);

                }else {
                    Toast.makeText(getContext(), "Please enter email", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment homeFragment = new LoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            }
        });
    }


    private void IsUserExist(final String t_email) {
        Log.e("check1122", "mobile number" + t_email);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,Isexist_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", "CHECK RESPONSE"+response.toString());
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            //Toast.makeText(getContext(), "HELLO", Toast.LENGTH_SHORT).show();
                            ForgetPassVerifyCode fragment=new ForgetPassVerifyCode();
                            Bundle args = new Bundle();
                            args.putString("email", t_email);
                            args.putString("code", jsonObject.getString("code"));
                            fragment.setArguments(args);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.commit();

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Number Not Registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "erro catch "+e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("email", t_email);
                return params;
            }

        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }

}
