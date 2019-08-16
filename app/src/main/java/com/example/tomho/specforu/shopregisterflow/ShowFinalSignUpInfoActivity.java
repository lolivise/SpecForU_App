package com.example.tomho.specforu.shopregisterflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomho.specforu.MainPageActivity;
import com.example.tomho.specforu.R;
import com.example.tomho.specforu.internaldatahandler.JSONFileHandler;
import com.example.tomho.specforu.internaldatahandler.ReadFileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShowFinalSignUpInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_SHOP = 1;
    private static final int REQUEST_CODE_BANK = 2;
    private static final int REQUEST_CODE_SHOP_LIST = 3;
    private final String SHOPFILE = "shopTempDetail.txt";
    private final String BANKFILE = "bankTempDetail.txt";
    private String userShopListFile;

    private String shopID;

    private String messageReturn;

    private TextView mShopName;
    private TextView mPhone;
    private TextView mAddress;

    private String latitude;
    private String longitude;

    private TextView mWebsite;
    private TextView mFacebook;
    private TextView mInstagram;

    private TextView mCardHolder;
    private TextView mCardNumber;
    private TextView mExpiredMonth;
    private TextView mExpiredYear;
    private TextView mCSV;

    private JSONFileHandler jsonFileHandler;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private DatabaseReference myRef;

    private Map<String, Object> shopDetail;
    private Map<String, Object> bankDetail;

    private JSONObject jsonObjectShopList;
    private JSONObject jsonObjectShop;
    private JSONObject jsonObjectBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_final_sign_up_info);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        jsonFileHandler = new JSONFileHandler();

        mShopName = (TextView)findViewById(R.id.tv_shop_name);
        mPhone = (TextView)findViewById(R.id.tv_phone_number);
        mAddress = (TextView)findViewById(R.id.tv_address);
        mWebsite = (TextView)findViewById(R.id.tv_website);
        mFacebook = (TextView)findViewById(R.id.tv_facebook);
        mInstagram = (TextView)findViewById(R.id.tv_instagram);

        mCardHolder = (TextView)findViewById(R.id.tv_card_holder);
        mCardNumber = (TextView)findViewById(R.id.tv_card_num);
        mExpiredMonth = (TextView)findViewById(R.id.tv_month);
        mExpiredYear = (TextView)findViewById(R.id.tv_year);
        mCSV = (TextView)findViewById(R.id.tv_csv);

        findViewById(R.id.btn_adjust).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();

        userShopListFile = currentUser.getUid()+"_shopList.txt";

        readShopFile();
        readBankDetail();
        readShopListFile();
    }

    private void readShopListFile(){
        File file = getBaseContext().getFileStreamPath(userShopListFile);
        if(file.exists()){
            Intent intent = new Intent(ShowFinalSignUpInfoActivity.this, ReadFileActivity.class);
            intent.putExtra("message",userShopListFile);
            startActivityForResult(intent, REQUEST_CODE_SHOP_LIST);
        }else {
            jsonObjectShopList = new JSONObject();
        }
    }

    private void readShopFile(){
        Intent intent = new Intent(ShowFinalSignUpInfoActivity.this, ReadFileActivity.class);
        intent.putExtra("message",SHOPFILE);
        startActivityForResult(intent, REQUEST_CODE_SHOP);
    }

    private void readBankDetail(){
        Intent intent = new Intent(ShowFinalSignUpInfoActivity.this, ReadFileActivity.class);
        intent.putExtra("message",BANKFILE);
        startActivityForResult(intent, REQUEST_CODE_BANK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SHOP){
            if(resultCode == RESULT_OK){
                messageReturn = data.getStringExtra("message_return");
                try {
                    setShopDetail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CODE_BANK){
            if(resultCode == RESULT_OK){
                messageReturn = data.getStringExtra("message_return");
                try {
                    setBankDetail();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(requestCode == REQUEST_CODE_SHOP_LIST){
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

    private void setShopDetail() throws JSONException {
        jsonObjectShop = jsonFileHandler.parserJSON(messageReturn);

        mShopName.setText(jsonObjectShop.getString("shopName"));
        mPhone.setText(jsonObjectShop.getString("phone"));

        String address = jsonObjectShop.getString("streetNum")+" "
                +jsonObjectShop.getString("streetName")+", "
                +jsonObjectShop.getString("suburb")+", "
                +"WA, Australia";

        mAddress.setText(address);
        mWebsite.setText(jsonObjectShop.getString("website"));
        mFacebook.setText(jsonObjectShop.getString("facebook"));
        mInstagram.setText(jsonObjectShop.getString("instagram"));

        latitude = jsonObjectShop.getString("latitude");
        longitude = jsonObjectShop.getString("longitude");
    }

    private void setBankDetail() throws JSONException {
        jsonObjectBank = jsonFileHandler.parserJSON(messageReturn);

        mCardHolder.setText(jsonObjectBank.getString("cardHolder"));
        mCardNumber.setText(jsonObjectBank.getString("cardNumber"));
        mExpiredMonth.setText(jsonObjectBank.getString("expiredMonth"));
        mExpiredYear.setText(jsonObjectBank.getString("expiredYear"));
        mCSV.setText(jsonObjectBank.getString("csv"));
    }

    private void setShopListFile() throws JSONException {
        jsonObjectShopList = new JSONObject(messageReturn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_adjust:
                backAndAdjust();
                break;
            case R.id.btn_save:
                showSaveDialog();
                break;
            case R.id.btn_confirm:
                showConfirmDialog();
                break;
        }
    }

    private void showConfirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowFinalSignUpInfoActivity.this);
        builder.setMessage("Make sure all the information is correct.")
                .setTitle("Last Confirmation");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setShopID();
                firebaseStoreProcess();
                addShopToShopListFile();
                createShopDetail();
                deleteFile();
                ShowFinalSignUpInfoActivity.this.startActivity(new Intent(ShowFinalSignUpInfoActivity.this,MainPageActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void showSaveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowFinalSignUpInfoActivity.this);
        builder.setMessage("The current information will be saved, but the registration process is not complete.")
                .setTitle("Save Current Setting");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShowFinalSignUpInfoActivity.this.startActivity(new Intent(ShowFinalSignUpInfoActivity.this,MainPageActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAdjustDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowFinalSignUpInfoActivity.this);
        builder.setMessage("You want to go back and adjust the information?")
                .setTitle("Adjust");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ShowFinalSignUpInfoActivity.this.startActivity(new Intent(ShowFinalSignUpInfoActivity.this,SignUpShopDetailActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backAndAdjust() { showAdjustDialog(); }

    @Override
    public void onBackPressed() {
        backAndAdjust();
    }

    private void deleteFile(){
        File shopFile = getBaseContext().getFileStreamPath(SHOPFILE);
        File bankFile = getBaseContext().getFileStreamPath(BANKFILE);

        if(shopFile.exists()){
            File dir = getFilesDir();
            File file = new File(dir, SHOPFILE);
            boolean deleted = file.delete();
        }

        if(bankFile.exists()){
            File dir = getFilesDir();
            File file = new File(dir, BANKFILE);
            boolean deleted = file.delete();
        }

    }

    // Set shop ID
    private void setShopID(){
        shopID = mAddress.getText().toString().toLowerCase();
        shopID = shopID.replaceAll(" ","");
    }

    // Store final data on Firebase
    private void firebaseStoreProcess(){
        buildHashMap();
        storeShopToFirestore();
        storeBankToFirestore();
        setupSpecialToDatabase();
        addShopToDatabase();
        addShopIdToCurrentUser();
    }

    private void buildHashMap(){
        shopDetail = new HashMap<>();
        shopDetail.put("master",currentUser.getUid());
        shopDetail.put("shopName",mShopName.getText().toString());
        shopDetail.put("phone",mPhone.getText().toString());
        shopDetail.put("address",mAddress.getText().toString());
        shopDetail.put("website",mWebsite.getText().toString());
        shopDetail.put("facebook",mFacebook.getText().toString());
        shopDetail.put("instagram",mInstagram.getText().toString());

        bankDetail = new HashMap<>();
        bankDetail.put("master",currentUser.getUid());
        bankDetail.put("cardNumber",mCardNumber.getText().toString());
        bankDetail.put("expiredMonth",mExpiredMonth.getText().toString());
        bankDetail.put("expiredYear",mExpiredYear.getText().toString());
        bankDetail.put("cardHolder",mCardHolder.getText().toString());
        bankDetail.put("csv",mCSV.getText().toString());
    }

    private void storeShopToFirestore(){

        db.collection("shops").document(shopID)
                .set(shopDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShowFinalSignUpInfoActivity.this,"Shop Detail Saved",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void storeBankToFirestore(){
        db.collection("banks").document(shopID)
                .set(bankDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ShowFinalSignUpInfoActivity.this,"Bank Detail Saved",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupSpecialToDatabase(){
        HashMap<String, String> setupSpecial = new HashMap<>();
        setupSpecial.put("master", currentUser.getUid());
        setupSpecial.put("member", currentUser.getUid());
        myRef.child("specials").child(shopID).setValue(setupSpecial);
    }

    private void addShopIdToCurrentUser(){
        myRef.child("users")
                .child(currentUser.getUid())
                .child("shops")
                .child(mShopName.getText().toString())
                .setValue(shopID);
    }

    private void addShopToDatabase(){
        HashMap<String, String> setShopLocation = new HashMap<>();
        setShopLocation.put("latitude",latitude);
        setShopLocation.put("longitude",longitude);

        myRef.child("shopList")
                .child(shopID)
                .setValue(setShopLocation);

    }

    private void addShopToShopListFile() {
        try {
            jsonObjectShopList.put(shopID, mShopName.getText().toString());
            writeFile(userShopListFile, jsonObjectShopList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createShopDetail(){
        String filename = shopID+".txt";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shopName",mShopName.getText().toString());
            jsonObject.put("phone",mPhone.getText().toString());
            jsonObject.put("address",mAddress.getText().toString());
            jsonObject.put("website",mWebsite.getText().toString());
            jsonObject.put("facebook",mFacebook.getText().toString());
            jsonObject.put("instagram",mInstagram.getText().toString());

            writeFile(filename,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean writeFile(String filename, JSONObject jsonObject) {
        String outputString = jsonObject.toString();
        try {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            fileOutputStream.write(outputString.getBytes());
            fileOutputStream.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
