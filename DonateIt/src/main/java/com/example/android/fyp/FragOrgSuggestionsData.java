package com.example.android.fyp;

/**
 * Created by AliAhmed on 14-Apr-18.
 */

public class FragOrgSuggestionsData {

    String RequestID,donorID, donorName, itemName, itemAmount;

    public FragOrgSuggestionsData(String requestID, String donorID, String donorName, String itemName, String itemAmount) {
        RequestID = requestID;
        this.donorID = donorID;
        this.donorName = donorName;
        this.itemName = itemName;
        this.itemAmount = itemAmount;
    }

    public String getRequestID() {
        return RequestID;
    }

    public String getDonorID() {
        return donorID;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemAmount() {
        return itemAmount;
    }
}
