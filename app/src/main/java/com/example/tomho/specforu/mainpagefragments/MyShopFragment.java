package com.example.tomho.specforu.mainpagefragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tomho.specforu.R;
import com.example.tomho.specforu.internaldatahandler.ReadFileActivity;
import com.example.tomho.specforu.myshopactivity.AnalyseActivity;
import com.example.tomho.specforu.myshopactivity.BillingActivity;
import com.example.tomho.specforu.myshopactivity.ManageActivity;
import com.example.tomho.specforu.myshopactivity.UploadActivity;
import com.example.tomho.specforu.shopregisterflow.SignUpShopDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyShopFragment extends Fragment implements View.OnClickListener{

    private static final int REQUEST_CODE_SHOP_LIST = 1;
    private View v;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private String messageReturn;
    private Map<String, String> ShopMap;

    private Spinner shopSpinner;
    private JSONObject jsonObjectShopList;

    public MyShopFragment() {
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
        v = inflater.inflate(R.layout.fragment_my_shop, container, false);

        shopSpinner = (Spinner)v.findViewById(R.id.shop_spinner);



        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);

        v.findViewById(R.id.btn_manage).setOnClickListener(this);
        v.findViewById(R.id.btn_upload).setOnClickListener(this);
        v.findViewById(R.id.btn_analysing).setOnClickListener(this);
        v.findViewById(R.id.btn_billing).setOnClickListener(this);
        v.findViewById(R.id.btn_new_shop).setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        getShopList();
    }

    private void getShopList(){
        String filename = currentUser.getUid()+"_shopList.txt";
        Intent intent = new Intent(getContext(), ReadFileActivity.class);
        intent.putExtra("message",filename);
        startActivityForResult(intent, REQUEST_CODE_SHOP_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SHOP_LIST){
            if(resultCode == RESULT_OK){
                messageReturn = data.getStringExtra("message_return");
                Log.d("MyShopFragment",messageReturn+"!!!!!!!!!!");
                try {
                    setShopListFile();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setShopListFile() throws JSONException {
        jsonObjectShopList = new JSONObject(messageReturn);
        ShopMap = new HashMap<>();
        for (Iterator<?> iterator = jsonObjectShopList.keys(); iterator.hasNext();) {
            String key = (String) iterator.next();
            Object value = jsonObjectShopList.get(key);
            if (value instanceof String) {
                ShopMap.put(key, (String) value);
            }
        }
        setupSpinner();
    }

    private void setupSpinner() {

        ArrayList<String> list = new ArrayList<>();
        for(String key: ShopMap.keySet()){
            list.add(key);
        }
        //list.add("Add a New Shop");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopSpinner.setAdapter(dataAdapter);
        shopSpinner.setSelection(0);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_manage:
                intent = new Intent(getActivity(), ManageActivity.class);
                intent.putExtra("SHOP_ID", shopSpinner.getSelectedItem().toString());
                getActivity().startActivity(intent);
                break;
            case R.id.btn_upload:
                intent = new Intent(getActivity(), UploadActivity.class);
                intent.putExtra("SHOP_ID", shopSpinner.getSelectedItem().toString());
                getActivity().startActivity(intent);
                break;
            case R.id.btn_analysing:
                intent = new Intent(getActivity(),AnalyseActivity.class);
                intent.putExtra("SHOP_ID", shopSpinner.getSelectedItem().toString());
                getActivity().startActivity(intent);
                break;
            case R.id.btn_billing:
                intent = new Intent(getActivity(),BillingActivity.class);
                intent.putExtra("SHOP_ID", shopSpinner.getSelectedItem().toString());
                getActivity().startActivity(intent);
                break;
            case R.id.btn_new_shop:
                registerNewShop();
                break;
        }


    }

    private void registerNewShop(){
        getActivity().startActivity(new Intent(getActivity(),SignUpShopDetailActivity.class));
        getActivity().finish();
    }
}
