package com.fyp.javaidsgreenhouse.fragments;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class VerifyCodeFragment extends Fragment {
    View view;
    private ProgressDialog pDialog;
    Button verify;
    TextView timer;
    EditText Code;
    Handler handler;
    int count = 120;
    String buyer_mobile="",buyer_Name="",buyer_Password="",buyer_Email="",buyer_Address="",verification_code="",user_type="";
    String registration_url = "https://hos-hrm.tk/ecommerce-api/Api.php?action=register";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_verifycode,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        verify = view.findViewById(R.id.btn_verify);
        timer = view.findViewById(R.id.resettimer);
        handler = new Handler(getActivity().getMainLooper());
        Code = view.findViewById(R.id.verificationCode);
        if (getArguments()!=null){

                buyer_mobile = getArguments().getString("Mobile");
                buyer_Name = getArguments().getString("Name");
                buyer_Email = getArguments().getString("Email");
                buyer_Address = getArguments().getString("Address");
                buyer_Password = getArguments().getString("Password");
                verification_code = getArguments().getString("code");
                user_type = getArguments().getString("user_type");

               // Log.d("VerifyCode","Buyer Data  "+buyer_Name);


        }

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Code.getText().toString().isEmpty()) {

                    if(Code.getText().toString().equals(verification_code)){

                         //call registration method
                        UserRegistration(buyer_Name,buyer_Email,buyer_mobile,buyer_Address,buyer_Password,user_type);



                    }else {
                        Toast.makeText(getContext(), "Please enter Valid code", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
                }

            }
        });
        thread();

    }
    private void thread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (count >= 0) {
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        Log.i("TAG", e.getMessage());
                    }
                    Log.i("TAG", "Thread id in while loop: " + Thread.currentThread().getId() + ", Count : " + count);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            timer.setText("Seconds Left " + count);
                        }
                    });
                    if (count == 0) {
                        // getActivity().onBackPressed();
                        // save the changes
                    }
                    count--;
                }
            }
        }).start();
    }


    private void UserRegistration(final String buyer_name, final String buyer_email, final String buyer_mobile, final String buyer_address, final String buyer_password,final String user_type) {
        pDialog.setMessage("Loading....");
        pDialog.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, registration_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                Log.d("VerifyActivity","buyer method call"+response.toString());
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                            // Goto Login Page
                            LoginFragment loginFragment = new LoginFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                        } else {

                            pDialog.dismiss();
                            Toast.makeText(getContext(), " Sorry try Again", Toast.LENGTH_SHORT).show();
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
                Log.d("Response error","Volley response errror is"+error.getMessage());
                Toast.makeText(getActivity(), "Please ty again", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", buyer_name);
                params.put("mobile", buyer_mobile);
                params.put("password", buyer_password);
                params.put("address", buyer_address);
                params.put("email", buyer_email);
                params.put("user_type", user_type);
                return params;

            }
        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest2);
    }

}
