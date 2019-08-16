package com.example.tomho.specforu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomho.specforu.mainpagefragments.FavoriteFragment;
import com.example.tomho.specforu.mainpagefragments.FriendFragment;
import com.example.tomho.specforu.mainpagefragments.MyShopFragment;
import com.example.tomho.specforu.mainpagefragments.OtherFragment;
import com.example.tomho.specforu.mainpagefragments.SearchFragment;
import com.example.tomho.specforu.shopregisterflow.SignUpShopDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPageActivity extends AppCompatActivity{

    private FragmentManager manager;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;

    private boolean isLogin;
    private boolean hasShop;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    swapSearchFragment();
                    return true;
                case R.id.navigation_favorite:
                    if(isLogin){
                        swapFavoriteFragment();
                    }else {
                        swapToLoginPage();
                    }
                    return true;
                case R.id.navigation_friend:
                    if(isLogin){
                        swapFriendFragment();
                    }else {
                        swapToLoginPage();
                    }

                    return true;
                case R.id.navigation_myshop:
                    if(isLogin){
                        if(hasShop){
                            swapMyShopFragment();
                        }else {
                            showRegisterShopDialog();
                        }
                    }else {
                        swapToLoginPage();
                    }
                    return true;
                case R.id.navigation_more:
                    if(isLogin){
                        swapOtherFragment();
                    }else {
                        swapToLoginPage();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        manager = getSupportFragmentManager();
        swapSearchFragment();
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        hasShop = false;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            isLogin = currentUser.isEmailVerified();
            checkUserHasShops();
            Toast.makeText(MainPageActivity.this,currentUser.getEmail(),Toast.LENGTH_LONG).show();

        }else {
            isLogin = false;
            Toast.makeText(MainPageActivity.this,"Welcome guest",Toast.LENGTH_LONG).show();
        }

    }

    private void checkUserHasShops(){
        myRef.child("users")
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("shops")){
                            hasShop = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void swapSearchFragment(){
        SearchFragment searchFragment = new SearchFragment();
        manager.beginTransaction().replace(R.id.main_layout, searchFragment).commit();
    }

    private void swapFavoriteFragment(){
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        manager.beginTransaction().replace(R.id.main_layout, favoriteFragment).commit();
    }

    private void swapFriendFragment(){
        FriendFragment friendFragment = new FriendFragment();
        manager.beginTransaction().replace(R.id.main_layout, friendFragment).commit();
    }

    private void swapMyShopFragment(){
        MyShopFragment myShopFragment = new MyShopFragment();
        manager.beginTransaction().replace(R.id.main_layout, myShopFragment).commit();
    }

    private void swapOtherFragment(){
        OtherFragment otherFragment = new OtherFragment();
        manager.beginTransaction().replace(R.id.main_layout, otherFragment).commit();
    }

    private void swapToLoginPage(){
        MainPageActivity.this.startActivity(new Intent(MainPageActivity.this,LoginActivity.class));
        finish();
    }

    private void showRegisterShopDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainPageActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_register_shop, null);
        Button mRegister = (Button) mView.findViewById(R.id.btn_register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPageActivity.this.startActivity(new Intent(MainPageActivity.this,SignUpShopDetailActivity.class));
                finish();
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


}
