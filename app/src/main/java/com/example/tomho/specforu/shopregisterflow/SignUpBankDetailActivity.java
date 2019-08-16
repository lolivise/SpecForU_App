package com.example.tomho.specforu.shopregisterflow;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tomho.specforu.R;
import com.example.tomho.specforu.internaldatahandler.JSONFileHandler;
import com.example.tomho.specforu.internaldatahandler.ReadFileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignUpBankDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_1 = 1;
    private final String BANKFILE = "bankTempDetail.txt";
    private String messageReturn;

    private EditText mCardNumber;
    private EditText mExpiredMonth;
    private EditText mExpiredYear;
    private EditText mCardHolder;
    private EditText mCSV;

    private JSONFileHandler jsonFileHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_bank_detail);

        jsonFileHandler = new JSONFileHandler();

        mCardNumber = (EditText)findViewById(R.id.et_card_num);
        mExpiredMonth = (EditText)findViewById(R.id.et_month);
        mExpiredYear = (EditText)findViewById(R.id.et_year);
        mCardHolder = (EditText)findViewById(R.id.et_card_holder);
        mCSV = (EditText)findViewById(R.id.et_csv);

        findViewById(R.id.previous_step).setOnClickListener(this);
        findViewById(R.id.next_step).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkTempFile();
    }

    private void checkTempFile(){
        File file = getBaseContext().getFileStreamPath(BANKFILE);

        if(file.exists()){
            // Read the file
            Intent intent = new Intent(SignUpBankDetailActivity.this, ReadFileActivity.class);
            intent.putExtra("message",BANKFILE);
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

        mCardNumber.setText(jsonObject.getString("cardNumber"));
        mExpiredMonth.setText(jsonObject.getString("expiredMonth"));
        mExpiredYear.setText(jsonObject.getString("expiredYear"));
        mCardHolder.setText(jsonObject.getString("cardHolder"));
        mCSV.setText(jsonObject.getString("csv"));
    }

    private boolean checkAllTheBlank(){

        if(mCardNumber.getText().toString().isEmpty()){
            mCardNumber.setError("");
            return false;
        }else if(mExpiredMonth.getText().toString().isEmpty()){
            mExpiredMonth.setError("");
            return false;
        }else if(mExpiredYear.getText().toString().isEmpty()){
            mExpiredYear.setError("");
            return false;
        }else if(mCardHolder.getText().toString().isEmpty()){
            mCardHolder.setError("");
            return false;
        }else if(mCSV.getText().toString().length() != 3){
            mCSV.setError("");
            return false;
        }

        return true;
    }

    private void storeCurrentValue(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("cardNumber",mCardNumber.getText().toString());
            jsonObject.put("expiredMonth",mExpiredMonth.getText().toString());
            jsonObject.put("expiredYear",mExpiredYear.getText().toString());
            jsonObject.put("cardHolder",mCardHolder.getText().toString());
            jsonObject.put("csv",mCSV.getText().toString());

            if(writeFile(jsonObject.toString())){
                Toast.makeText(SignUpBankDetailActivity.this,"File Saved.",Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Toast.makeText(SignUpBankDetailActivity.this,"Fail to Store file",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean writeFile(String outputString) {

        try {
            FileOutputStream fileOutputStream = openFileOutput(BANKFILE, MODE_PRIVATE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_step:
                SignUpBankDetailActivity.this.startActivity(new Intent(SignUpBankDetailActivity.this,SignUpShopDetailActivity.class));
                finish();
                break;
            case R.id.next_step:
                if(checkAllTheBlank()){
                    storeCurrentValue();
                    SignUpBankDetailActivity.this.startActivity(new Intent(SignUpBankDetailActivity.this,ShowFinalSignUpInfoActivity.class));
                    finish();
                }else {
                    Toast.makeText(SignUpBankDetailActivity.this,"Please complete the form.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        SignUpBankDetailActivity.this.startActivity(new Intent(SignUpBankDetailActivity.this,SignUpShopDetailActivity.class));
        finish();
    }
}
