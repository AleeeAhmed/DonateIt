package com.example.android.fyp;

/**
 * Created by AliAhmed on 14-Apr-18.
 */

public class FragOrgInvitationsSentData {

    String Id , Title,  OrgId,  OrgName;
    int Progress, Target;

    public FragOrgInvitationsSentData(String id, String title, String orgId, String orgName, int progress, int target) {
        Id = id;
        Title = title;
        OrgName = orgName;
        OrgId = orgId;
        Progress = progress;
        Target = target;
    }

    public String getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getOrgName() {
        return OrgName;
    }

    public String getOrgId() {
        return OrgId;
    }

    public int getProgress() {
        return Progress;
    }

    public int getTarget() {
        return Target;
    }
}
