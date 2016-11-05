package com.example.mom.mom.Model;

/**
 * Created by Nat on 3/9/2016.
 * bc things are being annoying
 */
public class MajorRate {

    private int iRaters;
    private float fRating;

    public MajorRate(int i, float f) {
        iRaters = i;
        fRating = f;
    }

    public void changeRating(float c) {
        fRating += c/iRaters;
    }

    public void newRating(float f) {
        iRaters++;
        fRating = (fRating + f)/iRaters;
        // We have rating 5, 5, 3
        // First add, we have 5/1 = 5
        // Second add, we have (5 + 5) / 2 = 5
        // Third add, we have (5 + 3) / 3 = 2.6?????
        // rating = (rating + f) * iRaters / ++iRaters;
        // ^ That's a one line average.
    }

    public int getRaters() {
        return iRaters;
    }

    public float getRating(){
        return fRating;
    }
}
