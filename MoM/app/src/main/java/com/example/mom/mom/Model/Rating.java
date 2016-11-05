package com.example.mom.mom.Model;

/**
 * Created by Jesse on 4/1/2016.
 */
public class Rating {

    Major eMajor;
    float fRating;

    public Rating(Major eMajor, float fRating) {
        this.eMajor = eMajor;
        this.fRating = fRating;
    }

    public Major getMajor() {
        return eMajor;
    }
    public void setMajor(Major eMajor) {
        this.eMajor = eMajor;
    }

    public float getRating() {
        return fRating;
    }
    public void setRating(float f) {
        this.fRating = f;
    }
}
