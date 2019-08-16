package com.example.tomho.specforu.myshopactivity;

import android.content.Intent;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tomho.specforu.R;
import com.example.tomho.specforu.internaldatahandler.ReadFileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManageActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SHOP_DETAIL = 1;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView shopName;
    private TextView address;
    private TextView phone;
    private TextView website;
    private TextView facebook;
    private TextView instagram;

    private String messageReturn;
    private Map<String, String> ShopMap;

    private JSONObject jsonObjectShopList;

    private String shopFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        String shopID = getIntent().getStringExtra("SHOP_ID");
        shopFile = shopID+".txt";

        mAuth = FirebaseAuth.getInstance();

        shopName = (TextView)findViewById(R.id.shop_name);
        address = (TextView)findViewById(R.id.tv_address);
        phone = (TextView)findViewById(R.id.tv_phone);
        website = (TextView)findViewById(R.id.tv_website);
        facebook = (TextView)findViewById(R.id.tv_facebook);
        instagram = (TextView)findViewById(R.id.tv_instagram);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        getShopList();
    }

    private void getShopList(){
        Intent intent = new Intent(ManageActivity.this, ReadFileActivity.class);
        intent.putExtra("message",shopFile);
        startActivityForResult(intent, REQUEST_CODE_SHOP_DETAIL);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SHOP_DETAIL){
            if(resultCode == RESULT_OK){
                messageReturn = data.getStringExtra("message_return");

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
                Log.d("ManageActivity",key+"!!!!!!!!!!"+(String) value);
            }
        }
        showShopDetail();
    }

    private void showShopDetail(){
        shopName.setText(ShopMap.get("shopName"));
        address.setText(ShopMap.get("address"));
        phone.setText(ShopMap.get("phone"));
        website.setText(ShopMap.get("website"));
        facebook.setText(ShopMap.get("facebook"));
        instagram.setText(ShopMap.get("instagram"));
    }
}
