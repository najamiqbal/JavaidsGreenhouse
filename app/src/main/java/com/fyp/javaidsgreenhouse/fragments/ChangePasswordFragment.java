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
import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.activities.LoginSignUpActivity;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.AppUtils;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;
import com.fyp.javaidsgreenhouse.utils.VolleySentRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordFragment extends Fragment {

    View view;
    EditText oldPass,newPass,confirmPass;
    Button updatebtn;
    String upDatePass_url="https://hos-hrm.tk/ecommerce-api/Api.php?action=forgetPassword";
    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.change_pass_fragment,container,false);
        initialization();
        return  view;
    }

    private void initialization() {
        pDialog=new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        oldPass=view.findViewById(R.id.CurrentPass);
        newPass=view.findViewById(R.id.NewPass);
        confirmPass=view.findViewById(R.id.ConfirmPass);
        updatebtn=view.findViewById(R.id.btnUpdate);
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideSoftKeyboard(getActivity());
                final UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
                if (validate() && userModelClass!=null){
                    UpDatePassword(userModelClass.getUser_email(),newPass.getText().toString());
                }

            }
        });
    }

    //Validating data
    private boolean validate() {
        boolean valid = true;
        final UserModelClass userModelClass=SharedPrefManager.getInstance(getContext()).getUser();

        if (oldPass.getText().toString().isEmpty()){
            oldPass.setError("Enter old password");
            valid=false;
        }else {
            oldPass.setError(null);
        }
        if (!oldPass.getText().toString().equals(userModelClass.getUser_password())){
            oldPass.setError("Wrong Password");
            valid=false;
        }else {
            oldPass.setError(null);
        }
        if (!isValidPassword(newPass.getText().toString())) {
            confirmPass.setError("Password must contain 8 character/numbers and special symbol");
            valid = false;
            Log.d("SignUp","validation");
        } else {
            confirmPass.setError(null);
            Log.d("SignUp","Pass validation");
        }
        if (newPass.getText().toString().isEmpty() || confirmPass.getText().toString().isEmpty() || !newPass.getText().toString().equals(confirmPass.getText().toString())) {
            newPass.setError("Password don't Match");
            valid = false;
        } else {
            newPass.setError(null);
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


    private void UpDatePassword(final String user_email, final String newpass) {
        pDialog.setMessage("Updating Wait.....");
        pDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,upDatePass_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.getString("status").equals("true")) {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                            SharedPrefManager.getInstance(getContext()).logOut();
                            startActivity(new Intent(getContext(), LoginSignUpActivity.class));
                            getActivity().finish();
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(getContext(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Error Please tyr again", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", user_email);
                params.put("password", newpass);
                return params;

            }
        };
        VolleySentRequest.getInstance().addRequestQueue(stringRequest);
    }
}
