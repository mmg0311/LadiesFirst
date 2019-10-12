package com.example.ladiesfirst;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeScreen  extends Fragment {
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = firebaseUser.getUid();
    String phone = null;
    /*callIntent.setData(Uri.parse("tel:"));*/
    String telephone = "02024372041";
    String longitude = "74.793521",latitude = "13.351639";

    final int REQUEST_CODE = 123; // Request Code for permission request callback

    final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    final long MIN_TIME = 5000;

    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 2000;


    LocationManager mLocationManager;
    LocationListener mLocationListener;


    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("Users/"+uid);
    private CollectionReference notebookRef = noteRef.collection("contact1");
    Button alarm;     //button which produces sound on click
    SmsManager smsManager;
    private static final String TAG = "Home";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page, container, false);
        alarm = v.findViewById(R.id.sound);
         final MediaPlayer mp = MediaPlayer.create(this.getActivity(), R.raw.action);

        Switch toggle = (Switch) v.findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    long start = System.currentTimeMillis();
                    long runtime =100000;

                    if(System.currentTimeMillis()-start-runtime<0) {
                        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);


// Vibrate for 400 milliseconds
                        v.vibrate(500);

                    }


                    Toast.makeText(getActivity(),"isChecked", Toast.LENGTH_SHORT).show();


                } else {

                    Toast.makeText(getActivity(), "UNCHECKED", Toast.LENGTH_SHORT).show();
                }
            }
        });
//myAsyncTask m = new myAsyncTask().execute();

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //  sendsms(telephone);
                notebookRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Note note = documentSnapshot.toObject(Note.class);
                                    note.setDocumentId(documentSnapshot.getId());

                              /*      String documentId = note.getDocumentId();
                                    String name = note.getname();*/
                                     phone = note.getphone();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendsms("+919834060006");
                                        }
                                    }, 10000);

                                    Log.d("send sms","sending...");

                                   /* data += "ID: " + documentId
                                            + "\nName: " + name + "\nPhone: " + phone + "\n\n";*/
                                }

                            }
                        });


            }
        });

        return v;
    }


  /*  @Override
    public void onResume() {
        super.onResume();
        Log.d("Clima", "onResume() called.");
        Log.d("Clima", "Getting weather for current location.");
        getWeatherForCurrentLocation();
    }*/




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("Clima","onRequestPermissionResult(): Permission Granted!");

            }else{
                Log.d("Clima","Permission denied =( ");
            }
        }
    }


    protected void sendsms(String phone){
        smsManager=SmsManager.getDefault();
        Log.d("hhhhhhhhhhhhhhh","here");
        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.SEND_SMS},2);
            Toast.makeText(this.getActivity(), "permission not granted", Toast.LENGTH_SHORT).show();
        }
        smsManager.sendTextMessage(phone,null,"Please save me!!!"+"http://maps.google.com/?q="+"<"+latitude+","+longitude+">",null
                ,null );
        Log.d("msg","msg send");
    }
    public void alarm(View v) {
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();
                            String name = note.getname();
                            String phone = note.getphone();
                            sendsms(phone);

                            data += "ID: " + documentId
                                    + "\nName: " + name + "\nPhone: " + phone + "\n\n";
                        }

                    }
                });
    }
}




