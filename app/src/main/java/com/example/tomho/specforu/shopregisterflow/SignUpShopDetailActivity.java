package com.example.tomho.specforu.shopregisterflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tomho.specforu.MainPageActivity;
import com.example.tomho.specforu.R;
import com.example.tomho.specforu.extratools.GeocodingLocation;
import com.example.tomho.specforu.internaldatahandler.JSONFileHandler;
import com.example.tomho.specforu.internaldatahandler.ReadFileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignUpShopDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_1 = 1;
    private static final String TAG = "SignUpShopDetailActivity";
    private final String SHOPFILE = "shopTempDetail.txt";
    private final String BANKFILE = "bankTempDetail.txt";

    private String messageReturn;

    private EditText mShopName;
    private EditText mPhone;
    private EditText mStreetNum;
    private EditText mStreetName;
    private EditText mSuburb;
    private EditText mWebsite;
    private EditText mFacebook;
    private EditText mInstagram;

    private String address;
    private String shopID;

    private String latitude;
    private String longitude;

    private JSONFileHandler jsonFileHandler;

    private FirebaseFirestore db;

    private String locationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_shop_detail);

        db = FirebaseFirestore.getInstance();

        jsonFileHandler = new JSONFileHandler();

        mShopName = (EditText)findViewById(R.id.et_shop_name);
        mPhone = (EditText)findViewById(R.id.et_phone_number);
        mStreetNum = (EditText)findViewById(R.id.et_street_number);
        mStreetName = (EditText)findViewById(R.id.et_street_name);
        mSuburb = (EditText)findViewById(R.id.et_suburb);
        mWebsite = (EditText)findViewById(R.id.et_website);
        mFacebook = (EditText)findViewById(R.id.et_facebook);
        mInstagram = (EditText)findViewById(R.id.et_instagram);


        findViewById(R.id.previous_step).setOnClickListener(this);
        findViewById(R.id.next_step).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkTempFile();
    }

    private void checkTempFile(){
        File file = getBaseContext().getFileStreamPath(SHOPFILE);

        if(file.exists()){
            // Read the file
            Intent intent = new Intent(SignUpShopDetailActivity.this, ReadFileActivity.class);
            intent.putExtra("message",SHOPFILE);
            startActivityForResult(intent, REQUEST_CODE_1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_1){
            if(resultCode == RESULT_OK){
                messageReturn = data.getStringExtra("message_return");
                try {
                    setPreviousValue();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setPreviousValue() throws JSONException {
        JSONObject jsonObject = jsonFileHandler.parserJSON(messageReturn);

        mShopName.setText(jsonObject.getString("shopName"));
        mPhone.setText(jsonObject.getString("phone"));
        mStreetNum.setText(jsonObject.getString("streetNum"));
        mStreetName.setText(jsonObject.getString("streetName"));
        mSuburb.setText(jsonObject.getString("suburb"));
        mWebsite.setText(jsonObject.getString("website"));
        mFacebook.setText(jsonObject.getString("facebook"));
        mInstagram.setText(jsonObject.getString("instagram"));
    }

    private boolean checkAllTheBlank(){

        if(mShopName.getText().toString().isEmpty()){
            mShopName.setError("");
            return false;
        }else if(mPhone.getText().toString().isEmpty()){
            mPhone.setError("");
            return false;
        }else if(mStreetNum.getText().toString().isEmpty()){
            mStreetNum.setError("");
            return false;
        }else if(mStreetName.getText().toString().isEmpty()){
            mStreetName.setError("");
            return false;
        }else if(mSuburb.getText().toString().isEmpty()){
            mSuburb.setError("");
            return false;
        }

        return true;
    }

    private void storeCurrentValue(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("shopName",mShopName.getText().toString());
            jsonObject.put("phone",mPhone.getText().toString());
            jsonObject.put("streetNum",mStreetNum.getText().toString());
            jsonObject.put("streetName",mStreetName.getText().toString());
            jsonObject.put("suburb",mSuburb.getText().toString());
            jsonObject.put("latitude",latitude);
            jsonObject.put("longitude",longitude);
            jsonObject.put("website",mWebsite.getText().toString());
            jsonObject.put("facebook",mFacebook.getText().toString());
            jsonObject.put("instagram",mInstagram.getText().toString());

            if(writeFile(jsonObject.toString())){
                Toast.makeText(SignUpShopDetailActivity.this,"Finish Writing Text",Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            Toast.makeText(SignUpShopDetailActivity.this,"Fail to Store file",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean writeFile(String outputString) {

        try {
            FileOutputStream fileOutputStream = openFileOutput(SHOPFILE, MODE_PRIVATE);
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

    private void showRegisterShopDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpShopDetailActivity.this);
        builder.setMessage("Are you sure to cancel the registration?")
                .setTitle("Cancel process");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteFile();
                SignUpShopDetailActivity.this.startActivity(new Intent(SignUpShopDetailActivity.this,MainPageActivity.class));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_step:
                // Show a dialog to alert user to cancel process
                showRegisterShopDialog();
                break;
            case R.id.next_step:
                if(checkAllTheBlank()){
                    setShopID();
                    getGeoInfo();
                    if(locationResult!=null){
                        checkShopExist();
                    }else {
                        Toast.makeText(SignUpShopDetailActivity.this,"The Address is incorrect.",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(SignUpShopDetailActivity.this,"Please complete the form.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void setShopID(){
        address = mStreetNum.getText().toString()+
                mStreetName.getText().toString()+","+
                mSuburb.getText().toString()+",WA,Australia";

        shopID = address.replaceAll(" ","");
        shopID = shopID.toLowerCase();
    }

    private void checkShopExist(){

        DocumentReference docRef = db.collection("shops").document(shopID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Toast.makeText(SignUpShopDetailActivity.this,"Current Shop has been registered!",Toast.LENGTH_SHORT).show();
                    }else {
                        storeCurrentValue();
                        SignUpShopDetailActivity.this.startActivity(new Intent(SignUpShopDetailActivity.this,SignUpBankDetailActivity.class));
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showRegisterShopDialog();
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

    private void getGeoInfo(){
        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(address,
                getApplicationContext(), new GeocoderHandler());
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationResult = bundle.getString("address");
                    String[] location = locationResult.split("\n");
                    Log.d(TAG,locationResult);
                    latitude = location[0];
                    longitude = location[1];
                    break;
                default:
                    locationResult = null;
            }

        }
    }

}
