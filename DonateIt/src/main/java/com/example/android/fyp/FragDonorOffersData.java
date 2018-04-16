package com.example.android.fyp;

/**
 * Created by AliAhmed on 16-Apr-18.
 */

public class FragDonorOffersData {

    String offerID , Title, Quantity, Status;

    public FragDonorOffersData(String offerID, String title, String quantity, String status) {
        this.offerID = offerID;
        Title = title;
        Quantity = quantity;
        Status = status;
    }

    public String getOfferID() {
        return offerID;
    }

    public String getTitle() {
        return Title;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getStatus() {
        return Status;
    }
}
