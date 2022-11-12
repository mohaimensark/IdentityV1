package com.example.identity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.identity.databinding.ActivityLandingPageBinding;
import com.example.identity.databinding.ActivityOtherUserBinding;
import com.example.identity.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserActivity extends AppCompatActivity {

    ActivityOtherUserBinding binding;
    TextView name,profession,about,rating,country;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;
    String namedata,aboutdata,professiondata,agedata,ratingdata;
    TextView age;
    CircleImageView img;
    ActivityResultLauncher<String> launcher;
    FirebaseDatabase database;
    FirebaseStorage storage;
    CircleImageView imageView;
    String us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name=findViewById(R.id.nameOther);

        profession = findViewById(R.id.profOther);
        img = findViewById(R.id.imageOther);
        age = findViewById(R.id.ageOther);
        about = findViewById(R.id.aboutOther);
        imageView = findViewById(R.id.imageOther);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

       us = getIntent().getStringExtra("uid");
    Toast.makeText(OtherUserActivity.this, us, Toast.LENGTH_SHORT).show();


        //Loading Image
        StorageReference dc = storage.getReference().child("User").child(us);

        dc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                Glide
                        .with(OtherUserActivity.this)
                        .load(uri) // the uri you got from Firebase
                        .into(imageView); //Your imageView variable
                //   Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OtherUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




              DocumentReference documentReference = firebaseFirestore.collection("User").document(us);
                    documentReference.addSnapshotListener(OtherUserActivity.this,new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            assert value != null;
                            name.setText("Name  :"+value.getString("name"));
                            about.setText("About  :"+value.getString("about"));
                            age.setText("Age  :"+value.getString("age"));
                            profession.setText("Profession  :"+value.getString("profession"));
                           // country.setText(value.getString("country"));
                           // rating.setText(value.getString("rating"));
                        }
                    });



                    binding.messageRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


    }
}