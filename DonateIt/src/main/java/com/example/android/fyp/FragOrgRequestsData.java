package com.example.android.fyp;

/**
 * Created by AliAhmed on 11-Apr-18.
 */

public class FragOrgRequestsData {

    String Id, Title, Invitations, Offers, Description, Progress, Target;

    public FragOrgRequestsData(String id, String title, String invitations, String offers, String description, String progress, String target) {
        Id = id;
        Title = title;
        Invitations = invitations;
        Offers = offers;
        Description = description;
        Progress = progress;
        Target = target;
    }

    public String getProgress() {
        return Progress;
    }

    public String getTarget() {
        return Target;
    }

    public String getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getInvitations() {
        return Invitations;
    }

    public String getOffers() {
        return Offers;
    }

    public String getDescription() {
        return Description;
    }
}
