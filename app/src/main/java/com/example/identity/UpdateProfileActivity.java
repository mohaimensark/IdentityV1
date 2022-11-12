package com.example.identity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.identity.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;
    String name,age,profession,email,about;
    EditText aboutUpdate,nameUpdate,ageUpdate,professUpdate;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(UpdateProfileActivity.this,MainActivity.class));
        }
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        nameUpdate = findViewById(R.id.nameUpdate);
        ageUpdate = findViewById(R.id.ageUpdate);
        aboutUpdate = findViewById(R.id.aboutUpdate);
        professUpdate = findViewById(R.id.professUpdate);


          name = getIntent().getStringExtra("name");
          email = getIntent().getStringExtra("email");
          profession = getIntent().getStringExtra("profession");
          age = getIntent().getStringExtra("age");
          about = getIntent().getStringExtra("about");



          aboutUpdate.setText(about);
          ageUpdate.setText(age);
          nameUpdate.setText(name);
          professUpdate.setText(profession);



          binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                         String upname = binding.nameUpdate.getText().toString();
                          String upage = binding.ageUpdate.getText().toString();
                          String upabout = binding.aboutUpdate.getText().toString();
                          String upprofess = binding.professUpdate.getText().toString();

                  Toast.makeText(UpdateProfileActivity.this, email, Toast.LENGTH_SHORT).show();
                  double lat = 51.5074;
                  double lng = 0.1278;
                  GeoPoint hash =new GeoPoint(lat, lng);

                  RealTimeName hash3 =new RealTimeName(upname, upprofess);



                  firebaseAuth = FirebaseAuth.getInstance();
                  firebaseFirestore = FirebaseFirestore.getInstance();
                  firebaseDatabase = FirebaseDatabase.getInstance();
                  try {
                      firebaseDatabase.getReference().child("User").child("Name").child(firebaseAuth.getCurrentUser().getUid())
                              .setValue(hash3);
                  }
                  catch (Exception e)
                  {
                      Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                  }




                  firebaseFirestore.collection("User")
                          .document(firebaseAuth.getCurrentUser().getUid()).set(new UserModel(email,upname,upprofess,upage,hash,upabout))
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void unused) {
                                  Toast.makeText(UpdateProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                  startActivity(new Intent(UpdateProfileActivity.this,ProfileActivity.class));
                              }
                          })
                          .addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                              }
                          });
              }
          });


    }
}