package com.example.tomho.specforu.mainpagefragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomho.specforu.LoginActivity;
import com.example.tomho.specforu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends Fragment implements View.OnClickListener{

    private View v;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    public OtherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_other, container, false);

        v.findViewById(R.id.btn_profile).setOnClickListener(this);
        v.findViewById(R.id.btn_sign_out).setOnClickListener(this);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_profile:
                break;
            case R.id.btn_sign_out:
                FirebaseAuth.getInstance().signOut();
                getActivity().startActivity(new Intent(getContext(),LoginActivity.class));
                getActivity().finish();
                break;
        }

    }


}
