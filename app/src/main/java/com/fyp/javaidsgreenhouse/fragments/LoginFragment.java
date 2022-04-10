package com.fyp.javaidsgreenhouse.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.activities.MainActivity;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.AppUtils;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment implements View.OnClickListener{
    View view;
    TextView forgetpass, registration;
    EditText et_email, et_pass;
    Button btn_login;
    String t_email="", t_password="";
    String Login_url = "https://hos-hrm.tk/ecommerce-api/Api.php?action=login";
    private ProgressDialog pDialog;
    UserModelClass userModelClass = new UserModelClass();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_login,container,false);
        initializeVariable();
        return view;
    }

    private void initializeVariable() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        registration = view.findViewById(R.id.newuser);
        forgetpass = view.findViewById(R.id.forgetpass);
        et_email=view.findViewById(R.id.et_email);
        et_pass=view.findViewById(R.id.password);
        btn_login=view.findViewById(R.id.loginbtn);

        // click
        registration.setOnClickListener(this);
        forgetpass.setOnClickListener(this);
        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginbtn:

                AppUtils.hideSoftKeyboard(getActivity());
                if (validate()) {
                    LoginMethod(t_email, t_password);
                }

                break;
            case R.id.newuser:
                RegistrationFragment homeFragment = new RegistrationFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack("added").commit();
                break;
            case R.id.forgetpass:
                ForgetPassFragment forgetPassFragment = new ForgetPassFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, forgetPassFragment);
                fragmentTransaction.addToBackStack("forgetpass_fragment");
                fragmentTransaction.commit();
                break;
        }
    }

    //Validating data
    private boolean validate() {
        boolean valid = true;
        t_email = et_email.getText().toString().trim();
        t_password = et_pass.getText().toString().trim();

        if (t_email.isEmpty()) {
            et_email.setError("Please Enter email");
            valid = false;
            return valid;
        } else {
            et_email.setError(null);
        }
        if (!t_email.matches(".+@gmail.com")) {
            et_email.setError("only gmail.com allows");
            valid = false;
        } else {
            et_email.setError(null);
        }

        if (t_password.isEmpty()) {
            et_pass.setError("Please Enter Correct Password");
            valid = false;
        } else {
            et_pass.setError(null);
        }

        return valid;
    }

    private void LoginMethod(final String t_email, final String t_pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        pDialog.setMessage("Login please Wait....");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response is", response.toString());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        if (jsonObject.getString("status").equals("true")){

                            if (jsonObject.getString("user_type").equals("3")) {

                                userModelClass.setUser_name(jsonObject.getString("username"));
                                userModelClass.setUser_mobile(jsonObject.getString("mobile"));
                                userModelClass.setUser_email(jsonObject.getString("email"));
                                userModelClass.setUser_address(jsonObject.getString("address"));
                                userModelClass.setUser_password(jsonObject.getString("password"));
                                userModelClass.setUser_id(jsonObject.getString("id"));

                                if (SharedPrefManager.getInstance(getContext()).addUserToPref(userModelClass)) {
                                    pDialog.dismiss();
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    pDialog.dismiss();
                                    Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                pDialog.dismiss();
                                Toast.makeText(getContext(), "Not able to login", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "Some Error Here", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", t_email);
                params.put("password", t_pass);
                return params;

            }
        };

        requestQueue.add(stringRequest);
        stringRequest.setTag("TAG");
    }

}
