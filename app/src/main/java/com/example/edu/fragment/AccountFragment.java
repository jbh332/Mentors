package com.example.edu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.edu.MyInfoActivity;
import com.example.edu.R;
import com.example.edu.WrittenGroupActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {

    TextView tvName;
    Button btnInfo, btnReservation, btnPassword, btnWriting;
    Intent intent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account_fragment, container, false);

        String userName;
        tvName = view.findViewById(R.id.tvName);
        btnInfo = view.findViewById(R.id.btnInfo);
        btnReservation = view.findViewById(R.id.btnReservation);
        btnPassword = view.findViewById(R.id.btnPassword);
        btnWriting = view.findViewById(R.id.btnWriting);

        userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        tvName.setText(userName);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.frombottom,0);
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getActivity(), WrittenGroupActivity.class);
                startActivity(intent2);
            }
        });

        return view;
    }


    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

}
