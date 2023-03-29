package com.example.shareit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.ArrayList;

import javax.net.ssl.KeyStoreBuilderParameters;

public class Login_Activity extends AppCompatActivity {


    private EditText userEmail,passWord;
    private Button logIn;
    private RelativeLayout snackbarLayout;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        logIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                readData(userEmail.getText().toString());
                ProgressDialog pd=new ProgressDialog(Login_Activity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_email=userEmail.getText().toString();
                String str_password=passWord.getText().toString();
                logIn(str_email,str_password);

                if(TextUtils.isEmpty(str_email)||TextUtils.isEmpty(str_password)){

                    Toast.makeText(Login_Activity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();

                }
                else{
                    auth=FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(str_email,str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        pd.dismiss();
                                        Intent intent=new Intent(Login_Activity.this,Home.class);
                                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK| intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        pd.dismiss();

                                    }
                                });

                            }
                            else{
                                pd.dismiss();
                                Snackbar snackbar = Snackbar.
                                make(snackbarLayout, "Authentication failed...", Snackbar.LENGTH_LONG).
                                setAction("Log In", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Login_Activity.this, RegisterActivity.class);
                                startActivity(intent);
                            }
                        });
                        snackbar.show();

                            }
                        }
                    });
                }
            }
        });
    }

    private void logIn(String email,String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


//    private void readData(String username) {
//
//        reference = FirebaseDatabase.getInstance().getReference("User");
//        reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//
//                if (task.isSuccessful()){
//                    Toast.makeText(Login_Activity.this, "Read successfully", Toast.LENGTH_SHORT).show();
//
//                    if (task.getResult().exists()){
//                        DataSnapshot dataSnapshot = task.getResult();
//                        String gotPassword = String.valueOf(dataSnapshot.child("password").getValue());
//                        if(gotPassword.equals(passWord.getText().toString())){
//                            Intent intent = new Intent(Login_Activity.this, Home.class);
//                            startActivity(intent);
//                        }
//                        else {
//                            Snackbar snackbar = Snackbar.make(snackbarLayout, "Invalid password", Snackbar.LENGTH_LONG).setAction("Sign In", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(Login_Activity.this, RegisterActivity.class);
//                                    startActivity(intent);
//                                }
//                            });
//                            snackbar.show();
//
//                        }
//
//
//
//                    }else {
//
//                        Snackbar snackbar = Snackbar.make(snackbarLayout, "Invalid credentials", Snackbar.LENGTH_LONG).setAction("Sign In", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(Login_Activity.this, RegisterActivity.class);
//                                startActivity(intent);
//                            }
//                        });
//                        snackbar.show();
//
//                    }
//
//
//                }else {
//
//                    Toast.makeText(Login_Activity.this,"Failed to read",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//    }
    private void init() {
        userEmail= findViewById(R.id.et_email);
        passWord = findViewById(R.id.et_password);
        logIn = findViewById(R.id.btn_login);
        snackbarLayout = findViewById(R.id.snackbarshow);
    }
}