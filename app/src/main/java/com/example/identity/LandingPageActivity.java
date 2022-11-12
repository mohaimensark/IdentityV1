package com.example.identity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identity.databinding.ActivityLandingPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LandingPageActivity extends AppCompatActivity {


    ActivityLandingPageBinding binding;
    TextView lat, lan;
    FirebaseAuth firebaseAuth;
    Task<DataSnapshot> firebaseDatabase;
    DatabaseReference dbRef;
    DocumentReference dc;
    FirebaseFirestore firebaseFirestore, firebaseFirestore2;
    TableLayout tableLayout;
    TableRow tableRow;
    ListView listView;
    MenuView.ItemView itemVIew;
    TextView textView;
    String name = "null", profession = "null";
    public String s2 = "lati", s1 = "langi", userlat = "null", userlon = "null";
    public String nameOther = "null";
    public String professionOther = "null";
    public String searchValue = "";

    EditText searchProfession;
    Button search;
    ArrayList<String> info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(LandingPageActivity.this, MainActivity.class));
        }
        super.onCreate(savedInstanceState);
        binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.goProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingPageActivity.this, ProfileActivity.class));

            }
        });
        binding.Mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LandingPageActivity.this, MessageActivity.class));

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("latlan").child(firebaseAuth.getCurrentUser().getUid()).get();
        String bid = firebaseAuth.getUid();
        assert bid != null;
        FirebaseDatabase.getInstance().getReference().child("User").child("latlan").child(bid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    int cnt = 0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (cnt == 0) {
                            s1 = snapshot1.getValue().toString();
                        }
                        if (cnt == 1) {
                            s2 = snapshot1.getValue().toString();
                        }
                        cnt++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LandingPageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchValue = binding.searchProfession.getText().toString();
            }
        });


        HashMap<String, String> namemap = new HashMap<>();
        HashMap<String, String> professmap = new HashMap<>();

        try {
            listView = findViewById(R.id.listview);
            Map<String, Integer> mpp = new HashMap<>();
            ArrayList<String> list = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);
            firebaseFirestore = FirebaseFirestore.getInstance();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child("latlan");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<String> useridList = new ArrayList<>();
                    list.clear();
                    int cnt = 0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        //Previous geodata
                        GeoData location1 = snapshot1.getValue(GeoData.class);
                        assert location1 != null;
                        String Lattitude = location1.getLatitude();
                        String Longitude = location1.getLongitude();
                        double userlatitude = Double.parseDouble(Lattitude);
                        double userlongitude = Double.parseDouble(Longitude);
                        double currentlat = Double.parseDouble(s1);
                        double currentlan = Double.parseDouble(s2);
                        String uid = snapshot1.getKey();
                        useridList.add(uid);
                        double distanceFin = GetDistance.CalculateDistanceInMeters(userlatitude, userlongitude, currentlat, currentlan);

                        //     Getting each username and profession
                        DocumentReference documentReference = firebaseFirestore.collection("User").document(uid);
                        documentReference.addSnapshotListener(LandingPageActivity.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                String nm = value.getString("name");
                                String prof = value.getString("profession");
                                String age = value.getString("age");
                                namemap.put(uid, nm);
                                professmap.put(uid, prof);
                            }
                        });

                        if (!searchValue.equals("")) {
                            String lowervalue  = searchValue.toLowerCase() ;
                            String str =  professmap.get(uid) ;
                            String currprofession = str.toLowerCase() ;
                            Toast.makeText(LandingPageActivity.this, lowervalue, Toast.LENGTH_SHORT).show();
                            if (lowervalue.equals(currprofession)) {
                                String t = "User Name : " + namemap.get(uid) + "\n" + "User Profession : " + professmap.get(uid) + "\n" + "Latitude : " + Lattitude + " \n" + "Longitude : " + Longitude + "\n" + "Distance From You : " + distanceFin + " m ";
                                list.add(t);
                            }
                            //   searchValue = "null";
                        } else {
                            String t = "User Name : " + namemap.get(uid) + "\n" + "User Profession : " +  professmap.get(uid) + "\n" + "Latitude : " + Lattitude + " \n" + "Longitude : " + Longitude + "\n" + "Distance From You : " + distanceFin + " m ";
                            list.add(t);
                            //   searchValue = "null";
                        }

                    }
                    adapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(LandingPageActivity.this, OtherUserActivity.class);
                            intent.putExtra("uid", useridList.get(position));
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

