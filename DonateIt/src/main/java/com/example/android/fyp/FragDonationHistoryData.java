package com.example.android.fyp;

/**
 * Created by AliAhmed on 15-Apr-18.
 */

public class FragDonationHistoryData {

    String postName, Amount;

    public FragDonationHistoryData(String postName, String amount) {
        this.postName = postName;
        Amount = amount;
    }

    public String getPostName() {
        return postName;
    }

    public String getAmount() {
        return Amount;
    }
}
