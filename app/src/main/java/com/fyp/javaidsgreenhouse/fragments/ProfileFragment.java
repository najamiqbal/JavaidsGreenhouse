package com.fyp.javaidsgreenhouse.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fyp.javaidsgreenhouse.R;
import com.fyp.javaidsgreenhouse.activities.LoginSignUpActivity;
import com.fyp.javaidsgreenhouse.models.UserModelClass;
import com.fyp.javaidsgreenhouse.utils.SharedPrefManager;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    public ProfileFragment(){
        // require a empty public constructor
    }
    View view;
    TextView txt_user_name,txt_em,txt_mobile,txt_address,edit_profile,change_pass,about_us, logout,invite_frnd,all_orders,txt_pending,txt_shipped,txt_canceled,txt_completed,txt_feedback;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_profile,container,false);
        initialization();
        return view;
    }

    private void initialization() {
        edit_profile=view.findViewById(R.id.txt_edit_profile);
        change_pass=view.findViewById(R.id.txt_change_password);
        about_us=view.findViewById(R.id.txt_about_us);
        txt_feedback=view.findViewById(R.id.txt_feedback);
        logout=view.findViewById(R.id.txt_logout);
        invite_frnd=view.findViewById(R.id.txt_invite_friends);
        all_orders=view.findViewById(R.id.txt_all_order);
        txt_pending=view.findViewById(R.id.txt_pending);
        txt_shipped=view.findViewById(R.id.txt_prepared);
        txt_canceled=view.findViewById(R.id.txt_canceled);
        txt_completed=view.findViewById(R.id.txt_shipped);
        txt_em=view.findViewById(R.id.txt_email);
        txt_mobile=view.findViewById(R.id.txt_mobile);
        txt_address=view.findViewById(R.id.txt_address);
        txt_user_name=view.findViewById(R.id.tv_user_name);

        edit_profile.setOnClickListener(this);
        txt_feedback.setOnClickListener(this);
        change_pass.setOnClickListener(this);
        about_us.setOnClickListener(this);
        logout.setOnClickListener(this);
        invite_frnd.setOnClickListener(this);
        all_orders.setOnClickListener(this);
        txt_shipped.setOnClickListener(this);
        txt_pending.setOnClickListener(this);
        txt_completed.setOnClickListener(this);
        txt_canceled.setOnClickListener(this);

        BindData();
    }

    private void BindData() {
        UserModelClass userModelClass= SharedPrefManager.getInstance(getContext()).getUser();
        if (userModelClass!=null){
            txt_em.setText(userModelClass.getUser_email());
            txt_address.setText(userModelClass.getUser_address());
            txt_mobile.setText(userModelClass.getUser_mobile());
            txt_user_name.setText(userModelClass.getUser_name());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
             case R.id.txt_change_password:
                 ChangePasswordFragment fragment1 = new ChangePasswordFragment();
                 FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                 fragmentTransaction.replace(R.id.frag_container, fragment1);
                 fragmentTransaction.addToBackStack("forgetpass_fragment");
                 fragmentTransaction.commit();
            break;
             case  R.id.txt_about_us:
                 AboutUsFragment fragment2 = new AboutUsFragment();
                 FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
                 fragmentTransaction2.replace(R.id.frag_container, fragment2);
                 fragmentTransaction2.addToBackStack("forgetpass_fragment");
                 fragmentTransaction2.commit();
            break;
             case  R.id.txt_edit_profile:
                 EditProfileFragment fragment3 = new EditProfileFragment();
                 FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
                 fragmentTransaction3.replace(R.id.frag_container, fragment3);
                 fragmentTransaction3.addToBackStack("forgetpass_fragment");
                 fragmentTransaction3.commit();
            break;
            case R.id.txt_invite_friends:

                final String appPackageName = getContext().getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                getContext().startActivity(sendIntent);

                break;
            case R.id.txt_logout:

                SharedPrefManager.getInstance(getContext()).logOut();
                startActivity(new Intent(getContext(), LoginSignUpActivity.class));
                getActivity().finish();

                break;
            case R.id.txt_all_order:
                AllOrdersMethod();
                break;
            case R.id.txt_pending:
                AllOrdersMethod();
                break;
            case R.id.txt_shipped:
                AllOrdersMethod();
                break;
            case R.id.txt_canceled:
                AllOrdersMethod();
                break;
            case R.id.txt_prepared:
                AllOrdersMethod();
            break;
            case R.id.txt_feedback:
                FeedBackFragment fragment4 = new FeedBackFragment();
                FragmentTransaction fragmentTransaction4 = getFragmentManager().beginTransaction();
                fragmentTransaction4.replace(R.id.frag_container, fragment4);
                fragmentTransaction4.addToBackStack("forgetpass_fragment");
                fragmentTransaction4.commit();
                break;
        }
    }

    private void AllOrdersMethod() {
        OrdersUserFragment ordersUserFragment = new OrdersUserFragment();
        FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
        fragmentTransaction3.replace(R.id.frag_container, ordersUserFragment);
        fragmentTransaction3.addToBackStack("forgetpass_fragment");
        fragmentTransaction3.commit();
    }
}