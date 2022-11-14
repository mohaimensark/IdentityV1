package com.example.identity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.identity.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    TextView name,profession,email,about,profileLInk;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;
    TextView age;
    CircleImageView img;
    ActivityResultLauncher<String> launcher;
    FirebaseDatabase database;
    FirebaseStorage storage;
    public Button logout;



   //Location
    private static int MY_FINE_LOCATION_REQUEST = 99;
    private static int MY_BACKGROUND_LOCATION_REQUEST = 100;

    LocationService mLocationService = new LocationService();
    Intent mServiceIntent;
    TextView lat;
    Button startServiceBtn, stopServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        }
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name=findViewById(R.id.name);
        email= findViewById(R.id.email);
        profession = findViewById(R.id.prof);
        img = findViewById(R.id.image);
        age = findViewById(R.id.age);
        about = findViewById(R.id.about);
        logout = findViewById(R.id.logout);
      //  profileLInk = findViewById(R.id.profileLink);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();




        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {

                    @Override
                    public void onActivityResult(Uri result) {
                          binding.image.setImageURI(result);

                        StorageReference reference = storage.getReference().child("User").child(firebaseAuth.getCurrentUser().getUid());

                          reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                              @Override
                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //  Toast.makeText(ProfileActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri result) {
                                                        database.getReference().child("User").child("Image").child(firebaseAuth.getCurrentUser().getUid())
                                                                .setValue(result.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(ProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(ProfileActivity.this, "not uploaded", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(ProfileActivity.this, "failed uploaded", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                              }
                          })
                                  .addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {
                                          Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                      }
                                  });

                    }
                });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch("image/*");
            }
        });




       //Loading Image
       StorageReference dc = storage.getReference().child("User").child(firebaseAuth.getCurrentUser().getUid());

       dc.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
           public void onSuccess(Uri uri) {


               Glide
                       .with(ProfileActivity.this)
                       .load(uri) // the uri you got from Firebase
                       .into(img); //Your imageView variable
            //   Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
           }
       }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });

        DocumentReference documentReference = firebaseFirestore.collection("User").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                       name.setText(value.getString("name"));
                       email.setText(value.getString("email"));
                       profession.setText(value.getString("profession"));
                       age.setText(value.getString("age"));
                       about.setText(value.getString("about"));

            }
        });


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ProfileActivity.this, "Service stopped!!", Toast.LENGTH_SHORT).show();
                stopServiceFunc();

                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("User").child("latlan").child(firebaseAuth.getCurrentUser().getUid());
                dbref.removeValue();

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();
            }
        });

        binding.UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String profession = binding.prof.getText().toString();
                String about = binding.about.getText().toString();
                String age = binding.age.getText().toString();
                String email23 = binding.email.getText().toString();
            //    String link = binding.profileLink.getText().toString();
                Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
                                    intent.putExtra("age", age);
                                    intent.putExtra("name", name);
                                    intent.putExtra("profession", profession);
                                     intent.putExtra("about", about);
                                     intent.putExtra("email", email23);
                startActivity(intent);

            }
        });

        binding.appSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,SettingsActivity.class));
            }
        });

    }




    //Location

        private void stopServiceFunc(){
            mLocationService = new LocationService();
            mServiceIntent = new Intent(this, mLocationService.getClass());
            if (Util.isMyServiceRunning(mLocationService.getClass(), this)) {
                stopService(mServiceIntent);
                Toast.makeText(this, "Service stopped!!", Toast.LENGTH_SHORT).show();
                //saveLocation(); // explore it by your self
            } else {
                Toast.makeText(this, "Service is already stopped!!", Toast.LENGTH_SHORT).show();
            }
        }

}