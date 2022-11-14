package com.example.identity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.identity.databinding.ActivityRegisterBinding;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    View login_again;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        login_again = findViewById(R.id.haveAcc);
        binding.btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameReg.getText().toString();
                String email = binding.emailReg.getText().toString();
                String profession = binding.profess.getText().toString();
                String age = binding.age.getText().toString();
                String ProfileLink = binding.fbLink.getText().toString();
                String password = binding.passReg.getText().toString();
                String confpass = binding.passConf.getText().toString();
                progressDialog.show();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(profession) || TextUtils.isEmpty(confpass)||TextUtils.isEmpty(ProfileLink)) {
                    progressDialog.cancel();
                    Toast.makeText(RegisterActivity.this, "Empty credintials", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    progressDialog.cancel();
                    Toast.makeText(RegisterActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confpass)) {
                    progressDialog.cancel();
                    Toast.makeText(RegisterActivity.this, "Password Missmatched!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    double lat = 51.5074;
                                    double lng = 0.1278;
                                    GeoPoint hash3 = new GeoPoint(lat, lng);

                                    UserModel userModel = new UserModel() ;
                                    String rname = userModel.getName() ;
                                    progressDialog.cancel();
                                    RealTimeName hash = new RealTimeName(rname, profession);
                                    firebaseAuth = FirebaseAuth.getInstance();
                                    firebaseFirestore = FirebaseFirestore.getInstance();
                                    firebaseDatabase = FirebaseDatabase.getInstance();
                                    try {
                                        firebaseDatabase.getReference().child("User").child("Name").child(firebaseAuth.getCurrentUser().getUid())
                                                .setValue(hash);
                                    } catch (Exception e) {
                                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                    firebaseFirestore.collection("User")
                                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(new UserModel(name,email, profession, age,"null", hash3,ProfileLink));

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(RegisterActivity.this, e.getMessage() + "reg", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });

        login_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}