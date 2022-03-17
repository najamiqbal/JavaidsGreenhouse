package com.fyp.javaidsgreenhouse.fragments;

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

import com.fyp.javaidsgreenhouse.R;

public class ForgetPassVerifyCode extends Fragment {
    View view;
    Button verify;
    TextView timer;
    EditText Code;
    Handler handler;
    int count = 120;
    private ProgressDialog pDialog;
    String  t_email="",verify_code="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.forgetpass_verifycode_fragment,container,false);
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

            t_email = getArguments().getString("email");
            verify_code = getArguments().getString("code");

        }
        Log.d("Mobile_registration", "data mobile number" + t_email);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Code.getText().toString().isEmpty()) {

                    if (Code.getText().toString().equals(verify_code)) {

                        ResetPassFragment fragment=new ResetPassFragment();
                        Bundle args = new Bundle();
                        args.putString("email", t_email);
                        fragment.setArguments(args);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.commit();

                    }else {
                        Toast.makeText(getContext(), "Empty string", Toast.LENGTH_SHORT).show();
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
}
