package com.example.android.fyp;

/**
 * Created by AliAhmed on 16-Apr-18.
 */

public class UserDetailsData {

    String itemId, itmName, itemAmount;

    public UserDetailsData(String itemId, String itmName, String itemAmount) {
        this.itemId = itemId;
        this.itmName = itmName;
        this.itemAmount = itemAmount;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItmName() {
        return itmName;
    }

    public String getItemAmount() {
        return itemAmount;
    }
}
