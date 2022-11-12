package com.example.identity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identity.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    View reg;
    View forget;

    FirebaseAuth firebaseAuth;
    ActivityMainBinding binding;
    ProgressDialog progressDialog;

    private static int MY_FINE_LOCATION_REQUEST = 99;
    private static int MY_BACKGROUND_LOCATION_REQUEST = 100;

    LocationService mLocationService = new LocationService();
    Intent mServiceIntent;
    TextView lat;
    Button startServiceBtn, stopServiceBtn,btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        reg= findViewById(R.id.txtReg);

        forget = findViewById(R.id.txtFor);

        btnSignin = findViewById(R.id.btnSignin);


        btnSignin.setOnClickListener(new View.OnClickListener() {

            //Location
            public void onClick(View v) {



                //Location end

                String email = binding.emailSin.getText().toString();
                String password = binding.passSin.getText().toString();
                progressDialog.show();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    progressDialog.cancel();
                    Toast.makeText(MainActivity.this, "Empty credintials", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6) {
                    progressDialog.cancel();
                    Toast.makeText(MainActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    progressDialog.cancel();
                                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                            == PackageManager.PERMISSION_GRANTED) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                                    != PackageManager.PERMISSION_GRANTED) {





                                                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                                alertDialog.setTitle("Background permission");
                                                alertDialog.setMessage(getString(R.string.background_location_permission_message));

                                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start service anyway",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                starServiceFunc();
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Grant background Permission",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                requestBackgroundLocationPermission();
                                                                dialog.dismiss();
                                                            }
                                                        });

                                                alertDialog.show();


                                            }else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                                    == PackageManager.PERMISSION_GRANTED){
                                                starServiceFunc();
                                            }
                                        }else{
                                            starServiceFunc();
                                        }

                                    }else if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                            != PackageManager.PERMISSION_GRANTED){
                                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {


                                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                            alertDialog.setTitle("ACCESS_FINE_LOCATION");
                                            alertDialog.setMessage("Location permission required");

                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            requestFineLocationPermission();
                                                            dialog.dismiss();
                                                        }
                                                    });


                                            alertDialog.show();

                                        } else {
                                            requestFineLocationPermission();
                                        }
                                    }
                                    Toast.makeText(MainActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, LandingPageActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });



        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgetActivity();
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });


    }

    public void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openForgetActivity(){
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }






    //Location services
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Toast.makeText(this, Integer.toString(requestCode), Toast.LENGTH_LONG).show();

        if ( requestCode == MY_FINE_LOCATION_REQUEST){

            if (grantResults.length !=0 /*grantResults.isNotEmpty()*/ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    requestBackgroundLocationPermission();
                }

            } else {
                Toast.makeText(this, "ACCESS_FINE_LOCATION permission denied", Toast.LENGTH_LONG).show();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                 /*   startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", this.getPackageName(), null),),);*/

                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:com.trickyworld.locationupdates")
                    ));

                }
            }
            return;

        }else if (requestCode == MY_BACKGROUND_LOCATION_REQUEST){

            if (grantResults.length!=0 /*grantResults.isNotEmpty()*/ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Background location Permission Granted", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Background location permission denied", Toast.LENGTH_LONG).show();
            }
            return;
        }

    }

    private void starServiceFunc(){
        mLocationService = new LocationService();
        mServiceIntent = new Intent(this, mLocationService.getClass());
        if (!Util.isMyServiceRunning(mLocationService.getClass(), this)) {
            startService(mServiceIntent);
            Toast.makeText(this, getString(R.string.service_start_successfully), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.service_already_running), Toast.LENGTH_SHORT).show();
        }
    }

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

    private void requestBackgroundLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                MY_BACKGROUND_LOCATION_REQUEST);
    }

    private void requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, MY_FINE_LOCATION_REQUEST);
    }

    public void saveLocation(){
        File dir = new File(this.getFilesDir(), "trickyworld");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File userLocation = new File(dir, "userlocation.txt");
            FileWriter writer = new FileWriter(userLocation);
            writer.append(LocationService.locationArrayList.toString());
            writer.flush();
            writer.close();
            LocationService.locationArrayList.clear();
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
