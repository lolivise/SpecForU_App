package com.example.tomho.specforu.myshopactivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tomho.specforu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseFirestore db;
    private DatabaseReference myRef;

    private ImageView dishPhoto;
    private EditText dishName;
    private EditText price;
    private EditText ingredient;
    private EditText specify;
    private CalendarView duriation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        dishPhoto = (ImageView)findViewById(R.id.img_dish);
        dishName = (EditText)findViewById(R.id.et_dishName);
        price = (EditText)findViewById(R.id.et_price);
        ingredient = (EditText)findViewById(R.id.et_ingredient);
        specify = (EditText)findViewById(R.id.et_specify);
        duriation = (CalendarView)findViewById(R.id.calendarView);

        dishPhoto.setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_confirm:
                showSaveDialog();
                break;
            case R.id.img_dish:
                Toast.makeText(UploadActivity.this,"Not Yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showSaveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setMessage("Once the special is uploaded, it is no way to modify.")
                .setTitle("Confirmation");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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

    private void uploadInfo(){

    }

}
