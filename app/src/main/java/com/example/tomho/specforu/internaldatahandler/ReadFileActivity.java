package com.example.tomho.specforu.internaldatahandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadFileActivity extends Activity {

    private String inputString;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        filename = intent.getStringExtra("message");

        if(readFile()){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("message_return", inputString);
            setResult(RESULT_OK, resultIntent);
        }else{
            intent.putExtra("message_return", "Fail to read file");
        }
        finish();
    }

    private boolean readFile() {

        try {
            FileInputStream fileInputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            stringBuffer.append(bufferedReader.readLine());

            inputString = stringBuffer.toString();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
