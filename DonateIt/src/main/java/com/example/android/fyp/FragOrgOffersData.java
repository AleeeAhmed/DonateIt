package com.example.android.fyp;

/**
 * Created by AliAhmed on 13-Apr-18.
 */

public class FragOrgOffersData {

    private String  offerID , ReqID, SenderID, OrgID,SenderRating, OfferedQuantity, PostName;

    public FragOrgOffersData(String offerID, String reqID, String senderID, String orgID,String senderRating, String offeredQuantity, String postName) {
        this.offerID = offerID;
        ReqID = reqID;
        SenderID = senderID;
        OrgID = orgID;
        SenderRating = senderRating;
        OfferedQuantity = offeredQuantity;
        PostName = postName;
    }

    public String getSenderRating() {
        return SenderRating;
    }

    public String getOfferID() {
        return offerID;
    }

    public String getReqID() {
        return ReqID;
    }

    public String getSenderID() {
        return SenderID;
    }

    public String getOrgID() {
        return OrgID;
    }

    public String getOfferedQuantity() {
        return OfferedQuantity;
    }

    public String getPostName() {
        return PostName;
    }
}
