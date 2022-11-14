package com.example.identity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding binding;
    String name,age,profession,email,about,link;
    EditText aboutUpdate,nameUpdate,ageUpdate,professUpdate,updateLink;
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
        updateLink = findViewById(R.id.updateLink);



          name = getIntent().getStringExtra("name");
          email = getIntent().getStringExtra("email");
          profession = getIntent().getStringExtra("profession");
          age = getIntent().getStringExtra("age");
          about = getIntent().getStringExtra("about");


        DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                updateLink.setText(value.getString("profilelink"));

            }
        });



          aboutUpdate.setText(about);
          ageUpdate.setText(age);
          nameUpdate.setText(name);
          professUpdate.setText(profession);
          updateLink.setText(link);



          binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                         String upname = binding.nameUpdate.getText().toString();
                          String upage = binding.ageUpdate.getText().toString();
                          String upabout = binding.aboutUpdate.getText().toString();
                          String upprofess = binding.professUpdate.getText().toString();
                          String updateL = binding.updateLink.getText().toString();

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
                          .document(firebaseAuth.getCurrentUser().getUid()).set(new UserModel(upname,email,upprofess,upage,upabout,hash,updateL))
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