package com.example.bahid.firebase_chat;

public class Message {
    public String From;
    public String To;
    public String Content;


    public Message(String from, String to, String content) {
        To = to;
        From = from;
        Content = content;
    }

    public Message() {
    }
}
