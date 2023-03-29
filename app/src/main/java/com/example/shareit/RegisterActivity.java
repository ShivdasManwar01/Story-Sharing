package com.example.shareit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText name, email, password, repassword,username;
    private RelativeLayout snackbarLayout;
    private Button register;
    private ImageView profileImage;
    Uri imageUri;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ProgressDialog pd;
    private TextView passwordError, emailError, nameError, repasswordError, mismatchError,usernameError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();

        init();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_username=username.getText().toString();
                String str_name=name.getText().toString();
                String str_email=email.getText().toString();
                String str_password=username.getText().toString();
                if(TextUtils.isEmpty(str_name)||TextUtils.isEmpty(str_username)||TextUtils.isEmpty(str_password)||TextUtils.isEmpty(str_email)){
                    Toast.makeText(RegisterActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
                if (validate()) {
                    register(str_username,str_name,str_email,str_password);
                    clearScreen();

                }
            }
        });
    }

    private void register( String username, String name,String email, String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    String userId=firebaseUser.getUid();
                    reference=FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username.toLowerCase());
                    hashMap.put("name",name);
                    hashMap.put("email",email);
                    hashMap.put("image","https://firebasestorage.googleapis.com/v0/b/shareit-cb6db.appspot.com/o/73-730477_first-name-profile-image-placeholder-png.png?alt=media&token=7576ee96-1dff-425e-9e98-720eb9857790");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                pd.dismiss();
                                Intent intent=new Intent(RegisterActivity.this,Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }
                    });


                }else {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "You can not register with this email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void clearScreen() {
        name.setText("");
        email.setText("");
        password.setText("");
        repassword.setText("");
        username.setText("");

    }

    private boolean validate() {
        if (name.getText().toString().equals("")) {
            nameError.setVisibility(View.VISIBLE);
            return false;
        } else {
            nameError.setVisibility(View.INVISIBLE);
        }
        if (email.getText().toString().equals("")) {
            emailError.setVisibility(View.VISIBLE);
            return false;
        } else
            emailError.setVisibility(View.INVISIBLE);
        if (username.getText().toString().isEmpty()) {
            usernameError.setVisibility(View.VISIBLE);
            return false;
        }
        else if(true) {
            Pattern pattern = Pattern.compile("[@_!#$%^&*()<>?/|}{~:]");
            Matcher matcher = pattern.matcher(username.getText().toString());

            if (!matcher.find()) {
                usernameError.setText("username cannot be empty");
                usernameError.setVisibility(View.INVISIBLE);
                return true;
            }
            else {
                usernameError.setText("Only alphanumeric characters allowed");
                usernameError.setVisibility(View.VISIBLE);
                return false;
            }


        }
        if (password.getText().toString().equals("")) {
            passwordError.setVisibility(View.VISIBLE);
            return false;
        } else
            passwordError.setVisibility(View.INVISIBLE);

        if (repassword.getText().toString().equals("")) {
            repasswordError.setVisibility(View.VISIBLE);
            return false;
        } else
            repasswordError.setVisibility(View.INVISIBLE);
        if (!password.getText().toString().equals(repassword.getText().toString())) {
            mismatchError.setVisibility(View.VISIBLE);
            return false;
        } else
            mismatchError.setVisibility(View.INVISIBLE);
        return true;


    }

//    private void insertData() {
//        DatabaseReference reference;
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", name.getText().toString());
//        map.put("email", email.getText().toString());
//        map.put("password", password.getText().toString());
//        map.put("username",username.getText().toString());
//
//
//        reference = FirebaseDatabase.getInstance().getReference("User");
//        reference.child(username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    if (task.getResult().exists()) {
//
//                        Snackbar snackbar = Snackbar.
//                                make(snackbarLayout, "User already exists with the username:"+username.getText().toString(), Snackbar.LENGTH_LONG).
//                                setAction("Log In", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(RegisterActivity.this, Login_Activity.class);
//                                startActivity(intent);
//                            }
//                        });
//                        snackbar.show();
//
//
//                    } else {
//                        FirebaseDatabase.getInstance().getReference("User").child(username.getText().toString())
//                                .setValue(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(RegisterActivity.this, Home.class);
//                                        clearScreen();
//                                        startActivity(intent);
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(Exception e) {
//                                        Toast.makeText(RegisterActivity.this, "Error while registration", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//
//
//
//
//                    }
//
//
//                }
//            }
//        });
//    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();
            profileImage.setImageURI(imageUri);


        }
    }


    private void init() {
        name = findViewById(R.id.name_txt);
        profileImage = findViewById(R.id.profile_image);
        email = findViewById(R.id.email_txt);
        password = findViewById(R.id.password_txt);
        repassword = findViewById(R.id.repassword_txt);
        register = findViewById(R.id.bt_register);
        passwordError = findViewById(R.id.pass_war);
        repasswordError = findViewById(R.id.repass_war);
        emailError = findViewById(R.id.email_war);
        nameError = findViewById(R.id.name_war);
        mismatchError = findViewById(R.id.password_error);
        snackbarLayout = findViewById(R.id.snackbar);
        username=findViewById(R.id.username_txt);
        usernameError=findViewById(R.id.username_war);

    }
}