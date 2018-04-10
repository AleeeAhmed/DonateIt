package com.example.android.fyp;

/**
 * Created by AliAhmed on 07-Apr-18.
 */

public class FragRequestsData {
    String ID,Title, Description;
    int Progress, Target;

    public FragRequestsData(String ID, String title, String description, int progress, int target) {
        this.ID = ID;
        Title = title;
        Description = description;
        Progress = progress;
        Target = target;
    }

    public String getID() {
        return ID;
    }


    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public int getProgress() {
        return Progress;
    }

    public int getTarget() {
        return Target;
    }

}
