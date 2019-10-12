package com.example.ladiesfirst;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EmergencyContacts extends FragmentActivity {
    private static final String TAG = "Emergency_Contacts";

    private static final String KEY_TITLE = "name";
    private static final String KEY_DESCRIPTION = "number";
    private DocumentReference noteref;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;


    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = firebaseUser.getUid();

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("Users/"+uid);
    private CollectionReference notebookRef = noteRef.collection("contact1");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergencycontacts);

        editTextTitle = findViewById(R.id.name_c);
        editTextDescription = findViewById(R.id.number_c);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String name = note.getname();
                    String phone = note.getphone();

                    data += "ID: " + documentId
                            + "\nName: " + name + "\nNumber: " + phone + "\n\n";
                }

                textViewData.setText(data);
            }
        });
    }

    public void addNote(View v) {
        String name = editTextTitle.getText().toString();
        String phone = editTextDescription.getText().toString();

        Note note = new Note(name, phone);

        notebookRef.add(note);
    }

    public void loadNotes(View v) {
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

                            data += "ID: " + documentId
                                    + "\nName: " + name + "\nPhone: " + phone + "\n\n";
                        }

                        textViewData.setText(data);
                    }
                });
    }
}
