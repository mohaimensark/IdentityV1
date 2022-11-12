package com.example.identity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MessageActivity extends AppCompatActivity {
    EditText etToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MessageActivity.this,MainActivity.class));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        etToken = findViewById(R.id.etToken);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                           // Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                    //    String msg = getString(token);
                    //    Log.d(TAG, msg);
                        System.out.println(token);
                        Toast.makeText(MessageActivity.this, "Your device registration token is" + token
                                , Toast.LENGTH_SHORT).show();

                        etToken.setText(token);
                    }
                });
    }
}