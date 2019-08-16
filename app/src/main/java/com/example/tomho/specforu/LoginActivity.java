package com.example.tomho.specforu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomho.specforu.datastrucuture.Profile;
import com.example.tomho.specforu.internaldatahandler.ReadFileActivity;
import com.example.tomho.specforu.shopregisterflow.SignUpShopDetailActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;
    private static final int REQUEST_CODE_1 = 2;

    private EditText mEmail;
    private EditText mPassword;

    private TextView notification;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private DatabaseReference myRef;

    private Profile profileObj;

    private GoogleSignInClient mGoogleSignInClient;

    private Map<String, Object> userProfile;


    private String email;
    private String password;

    private String profileFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase function
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("users");

        // Declare item in the layout
        mEmail = (EditText)findViewById(R.id.et_email);
        mPassword = (EditText)findViewById(R.id.et_password);

        notification = (TextView)findViewById(R.id.notification);

        findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);
        findViewById(R.id.tv_forget_password).setOnClickListener(this);
        findViewById(R.id.btn_google_login).setOnClickListener(this);

        // Config Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            checkFirstTimeSigned();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    private void checkFirstTimeSigned(){
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    // check Firestore has the document
                    if(document.exists()){
                        // check current profile has been save in the internal storage
                        if(!checkTempFile()){
                            storeCurrentProfileToInternal();
                        }
                    }else {
                        //if current user have not been save in firestore
                        try {
                            // set up a new account file
                            setNewUser();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // swap back to main page
                    swapToMainPage();
                }
            }
        });
    }


    // Create A New Email Account
    private void createNewEmailAccount(){

        if(isValid()){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                user = mAuth.getCurrentUser();
                                try {
                                    setNewUser();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(LoginActivity.this,"Verification email is sent.",Toast.LENGTH_LONG).show();
                                    }

                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }


    // Sign in With Email Account
    private void signInEmailAccount(){

        if(isValid()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                user = mAuth.getCurrentUser();
                                if(user.isEmailVerified()){
                                    swapToMainPage();
                                }else {
                                    notification.setText("The email is not verify yet.");
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

    private boolean isValid(){
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();

        if(email.isEmpty()){
            mEmail.setError("wrong form");
            return false;
        }else if(password.isEmpty()){
            mPassword.setError("wrong form");
            return false;
        }
        return true;
    }


    private void setNewUser() throws JSONException {
        if(user!=null){

            // upload to firebase database and storage
            userProfile = new HashMap<>();
            userProfile.put("photo",0);
            userProfile.put("id",user.getUid());
            userProfile.put("email",user.getEmail());
            userProfile.put("name",user.getDisplayName());
            userProfile.put("birth","");
            storeProfileToFireStore();

            // store profile in internal storage
            JSONObject profile = new JSONObject();
            profile.put("photo",0);
            profile.put("id",user.getUid());
            profile.put("email",user.getEmail());
            profile.put("name",user.getDisplayName());
            profile.put("birth","");
            setProfileName();
            writeFile(profileFile,profile.toString());

        }
    }

    private void storeCurrentProfileToInternal(){
        db.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        profileObj = documentSnapshot.toObject(Profile.class);
                        // store profile in internal storage
                        JSONObject profile = new JSONObject();
                        try {
                            profile.put("photo",profileObj.getPhoto());
                            profile.put("id",profileObj.getId());
                            profile.put("email",profileObj.getEmail());
                            profile.put("name",profileObj.getName());
                            profile.put("birth",profileObj.getBirth());
                            setProfileName();
                            writeFile(profileFile,profile.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void setProfileName(){
        profileFile = user.getUid()+"_profile.txt";
    }

    // store profile in Firestore
    private void storeProfileToFireStore(){

        db.collection("users").document(user.getUid())
                .set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signin:
                signInEmailAccount();
                break;
            case R.id.btn_signup:
                createNewEmailAccount();
                break;
            case R.id.tv_forget_password:

                break;
            case R.id.btn_google_login:
                signIn();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        swapToMainPage();
    }

    private void swapToMainPage(){
        LoginActivity.this.startActivity(new Intent(LoginActivity.this,MainPageActivity.class));
        finish();
    }

    private boolean writeFile(String filename, String outputString) {

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

    private boolean checkTempFile(){
        setProfileName();
        File file = getBaseContext().getFileStreamPath(profileFile);
        return file.exists();

    }
}
