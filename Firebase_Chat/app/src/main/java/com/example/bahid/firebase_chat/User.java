package com.example.bahid.firebase_chat;

public class User {
    String PhoneNumber;
    String Name;

    public User()
    {
        PhoneNumber = "";
        Name = "";
    }

    public User(String phone, String name)
    {
        PhoneNumber = phone;
        Name = name;
    }
}
