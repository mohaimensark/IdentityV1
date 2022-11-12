//package com.example.identity;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.identity.databinding.ActivityUploadImageBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import org.checkerframework.checker.nullness.qual.NonNull;
//import org.checkerframework.checker.nullness.qual.Nullable;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Objects;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import kotlinx.coroutines.channels.ChannelResult;
//
//public class UploadImageActivity extends AppCompatActivity {
//
//
//
//    String name, email, address, password, profession, age,about_text;
//    FirebaseAuth auth;
//    FirebaseFirestore firebaseFirestore;
//    FirebaseDatabase database;
//    FirebaseStorage storage;
//    Uri selectedImage;
//    ProgressDialog dialog;
//    CircleImageView imageView;
//    Button continueBtn;
//    EditText about;
//
//     ActivityUploadImageBinding binding;
//
//    TextView textView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityUploadImageBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        imageView = findViewById(R.id.image2);
//
//        continueBtn = findViewById(R.id.uploadbtn);
//
//
//        about = findViewById(R.id.about);
//        about_text = about.getText().toString();
//
//
//        database = FirebaseDatabase.getInstance();
//        storage = FirebaseStorage.getInstance();
//        auth = FirebaseAuth.getInstance();
//
//
//        name = getIntent().getStringExtra("name");
//        email = getIntent().getStringExtra("email");
//        profession = getIntent().getStringExtra("profession");
//        age = getIntent().getStringExtra("age");
//
//        about.setText(name);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, 45);
//            }
//        });
//
//
//        continueBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.show();
//                if (selectedImage != null) {
//                    StorageReference reference = storage.getReference().child("User").child(auth.getCurrentUser().getUid());
//                    Toast.makeText(UploadImageActivity.this, "Hi", Toast.LENGTH_SHORT).show();
//                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//
//                                        String imageUrl = uri.toString();
//                                        String email2 = auth.getCurrentUser().getEmail();
//
//                                        UserModel addNewUser = new UserModel(email2, name, profession, age, about_text, imageUrl);
//                                        firebaseFirestore.collection("User")
//                                                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(addNewUser);
//
//                                                        Intent intent = new Intent(UploadImageActivity.this, MainActivity.class);
//                                                        Toast.makeText(UploadImageActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
//                                                        startActivity(intent);
//
//
//                                    }
//                                });
//                            }
//                            else
//                            {
//                                Toast.makeText(UploadImageActivity.this,"Failed!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } else {
//
//                    String email1 = auth.getCurrentUser().getEmail();
//
//                    UserModel addNewUser = new UserModel(email, name, profession, age, about_text, "NoImage");
//
//                    firebaseFirestore.collection("User")
//                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).set(addNewUser);
//
//                                    Intent intent = new Intent(UploadImageActivity.this, MainActivity.class);
//                                    Toast.makeText(UploadImageActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
//                                    startActivity(intent);
//
//                }
//
//            }
//        });
//
//
//
//
//
//
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (data != null) {
//            if (data.getData() != null) {
//                Uri uri = data.getData(); // filepath
//                FirebaseStorage storage = FirebaseStorage.getInstance();
//                long time = new Date().getTime();
//                StorageReference reference = storage.getReference().child("User").child(time + "");
//                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String filePath = uri.toString();
//                                    HashMap<String, Object> obj = new HashMap<>();
//                                    obj.put("image", filePath);
//                                    database.getReference().child("User")
//                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(UploadImageActivity.this, "Hello", Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                }
//                            });
//                        }
//                    }
//                });
//                imageView.setImageURI(data.getData());
//                selectedImage = data.getData();
//            }
//        }
//    }
//
//
//}