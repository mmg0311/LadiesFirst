package com.example.ladiesfirst;


import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentId;
    private String name;
    private String phone;

    public Note() {
        //public no-arg constructor needed
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Note(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getname() {
        return  name;
    }

    public String getphone() {
        return phone;
    }
}