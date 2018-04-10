package com.example.android.fyp;

/**
 * Created by AliAhmed on 07-Apr-18.
 */

public class FragDonorMessagesData {

    String MsgId,orgName, Post, LastMessage;

    public FragDonorMessagesData(String msgId, String orgName, String post, String lastMessage) {
        MsgId = msgId;
        this.orgName = orgName;
        Post = post;
        LastMessage = lastMessage;
    }

    public String getMsgId() {
        return MsgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getPost() {
        return Post;
    }

    public String getLastMessage() {
        return LastMessage;
    }
}
