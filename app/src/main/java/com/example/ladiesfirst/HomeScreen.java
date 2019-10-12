package com.example.ladiesfirst;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
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
    private static final int SLEEP_TIME = 10000;
    private boolean IS_VIBRATE = true;
    Thread t=null;


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


        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendsms("+917767832966");
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
        });

//        smsManager=SmsManager.getDefault();
//
//        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.SEND_SMS},2);
//            Toast.makeText(this.getActivity(), "permission not granted", Toast.LENGTH_SHORT).show();
//        }
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                smsManager.sendTextMessage("+917767832966",null,"hello",null
//                        ,null );
//
//            }
//        });
        return v;
    }
    protected void sendsms(String phone){
        smsManager=SmsManager.getDefault();

        if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.SEND_SMS},2);
            Toast.makeText(this.getActivity(), "permission not granted", Toast.LENGTH_SHORT).show();
        }
        smsManager.sendTextMessage(phone,null,"Please save me!!!",null
                ,null );

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




