package com.example.android.fyp;

/**
 * Created by AliAhmed on 11-Apr-18.
 */

public class ChatData {

    String Message , Date, Sender;

    public ChatData(String message, String date, String sender) {
        Message = message;
        Date = date;
        Sender = sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getMessage() {
        return Message;
    }

    public String getDate() {
        return Date;
    }

    public String getSender() {
        return Sender;
    }
}
