package com.fyp.javaidsgreenhouse.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegistrationFragment extends Fragment {
    View view;
    String IsUserExist = "https://hos-hrm.tk/ecommerce-api/Api.php?action=isUserExist";
    private ProgressDialog pDialog;
    Button registration_btn_buyer;
    EditText et_name_buyer, et_mobile_buyer, et_address_buyer, et_email_buyer, et_password_buyer, et_confirm_password_buyer;
    String   user_type = "3",buyer_name = "", buyer_email = "", buyer_mobile = "", buyer_address = "", buyer_password = "", buyer_confirm_password = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_registration,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        et_name_buyer = view.findViewById(R.id.user_name);
        et_email_buyer = view.findViewById(R.id.user_email);
        et_mobile_buyer = view.findViewById(R.id.user_mobile_number);
        et_address_buyer = view.findViewById(R.id.user_address);
        et_password_buyer = view.findViewById(R.id.user_password);
        et_confirm_password_buyer = view.findViewById(R.id.user_confirm_password);
        registration_btn_buyer = view.findViewById(R.id.register_btn_buyer);
        registration_btn_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    CheckingMobileNumber(buyer_email);
                }
            }
        });
    }

    //Validating data
    private boolean validate() {
        boolean valid = true;
        buyer_name = et_name_buyer.getText().toString();
        buyer_email = et_email_buyer.getText().toString();
        buyer_mobile = et_mobile_buyer.getText().toString();
        buyer_address = et_address_buyer.getText().toString();
        buyer_password = et_password_buyer.getText().toString();
        buyer_confirm_password = et_confirm_password_buyer.getText().toString();


        Log.d("IMG", "THIS is image" + buyer_email);

        if (buyer_name.isEmpty()) {
            et_name_buyer.setError("Pleaase enter name");
            valid = false;
        } else {
            et_name_buyer.setError(null);
        }
        if (buyer_name.matches("[0-9]+")) {
            et_name_buyer.setError("Numbers are not allow in name");
            valid = false;
        } else {
            et_name_buyer.setError(null);
        }

        if (buyer_email.isEmpty()) {
            et_email_buyer.setError("Please enter email");
            valid = false;
        } else {
            et_email_buyer.setError(null);
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(buyer_email).matches()) {
            et_email_buyer.setError("Email formate is wrong");
            valid = false;
        } else {
            et_email_buyer.setError(null);
        }
        if (!buyer_email.matches(".+@gmail.com")) {
            et_email_buyer.setError("only gmail.com allows");
            valid = false;
        } else {
            et_email_buyer.setError(null);
        }

        if (buyer_mobile.isEmpty()) {
            et_mobile_buyer.setError("Please enter mobile");
            valid = false;
        } else {
            et_mobile_buyer.setError(null);
        }
        if (buyer_mobile.length() <12) {
            et_mobile_buyer.setError("Please enter complete number");
            valid = false;
        } else {
            et_mobile_buyer.setError(null);
        }

        if (buyer_address.isEmpty()) {
            et_address_buyer.setError("Please enter address");
            valid = false;
        } else {
            et_address_buyer.setError(null);
        }
        if (buyer_password.isEmpty() || buyer_confirm_password.isEmpty() || !buyer_confirm_password.equals(buyer_password)) {
            et_password_buyer.setError("Password don't Match");
            et_confirm_password_buyer.setError("Password don't Match");
            valid = false;
        } else {
            et_password_buyer.setError(null);
            et_confirm_password_buyer.setError(null);
        }
        if (!isValidPassword(buyer_password)) {
            et_password_buyer.setError("Password must contain 8 character/numbers and special symbol");
            et_confirm_password_buyer.setError("Password must contain 8 character/numbers and special symbol");
            valid = false;
            Log.d("SignUp","validation");

        } else {
            et_password_buyer.setError(null);
            et_confirm_password_buyer.setError(null);
            Log.d("SignUp","Pass validation");
        }


        return valid;
    }


    //*****************************************************************
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d][A-Za-z\\d!@#$%^&*()_+]{7,19}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    private void CheckingMobileNumber(final String t_email) {
        Log.e("log", "mobile number" + t_email);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, IsUserExist, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            // also get verification code from response and pass next fragment

                            VerifyCodeFragment fragment=new VerifyCodeFragment();
                            Bundle args = new Bundle();
                            args.putString("Mobile", buyer_mobile);
                            args.putString("Name", buyer_name);
                            args.putString("Address", buyer_address);
                            args.putString("Email", buyer_email);
                            args.putString("Password", buyer_password);
                            args.putString("user_type", user_type);
                            args.putString("code", jsonObject.getString("code"));
                            fragment.setArguments(args);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.commit();

                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Number Already Registered", Toast.LENGTH_SHORT).show();

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
                params.put("email", t_email);
                return params;

            }

        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
        //VolleyRequestsent.getInstance().addRequestQueue(stringRequest);
    }

}
