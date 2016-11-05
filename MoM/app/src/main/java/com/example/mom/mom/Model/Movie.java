package com.example.mom.mom.Model;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jesse on 2/18/2016.
 * Nat: changed rating method 3/9/2016
 */
public class Movie {
    private String m_szTitle;
    private String m_szPosterURL;
    private String m_szYear;
    private String m_szID;
    private float m_fAverageRating;
    private Set<User> m_aRatingsUsers;
    private List<Float> m_aRatingsNumbers;
    //Map<String, Float> m_aRatings;

    public Movie(String szTitle, String szPosterURL, String szYear, String szID) {
        m_szTitle = szTitle;
        m_szPosterURL = szPosterURL;
        m_szYear = szYear;
        m_szID = szID;
        //m_aRatings = new HashMap<>();
        m_aRatingsUsers = new HashSet();
        m_aRatingsNumbers = new ArrayList<>();
        m_fAverageRating = 0;
    }

    /**
     * Rates a movie
     * @param oUser user that rated the movie
     * @param fRating rating the user rated
     */
    public void rate(User oUser, float fRating) {

        //Todo: Fix rating system
        //m_aRatings.put(oUser.getUsername(), rating);
        if (m_aRatingsUsers.add(oUser)) {
            m_aRatingsNumbers.add(fRating);
        }
        m_fAverageRating = calculateRating();
    }

    public void rate(User oUser, float fRating, Context oContext) {

        //Todo: Fix rating system
        //m_aRatings.put(oUser.getUsername(), rating);
        if (m_aRatingsUsers.add(oUser)) {
            m_aRatingsNumbers.add(fRating);
        }
        m_fAverageRating = calculateRating();
    }

    /**
     * Calculate overall rating
     * @return overall rating
     */
    private float calculateRating() {
        //Todo: Load m_aRatings from firebase
        float fAverage = 0;
        //Todo: average smarter
        for (Float f : m_aRatingsNumbers) {
            fAverage += f;
        }
        fAverage /= m_aRatingsNumbers.size();

        return Float.compare(Float.NaN, fAverage) == 0 ? 0 : fAverage;
    }

    /**
     * Calculate rating based on major
     * @param eMajor major to be based on
     * @return rating
     */
    private float calculateRating(Major eMajor) {
        float fAverage = 0;
        int nCounter = 0;
        //Todo: average smarter
//        for (String s : m_aRatings.keySet()) {
//            User o = LoginManager.getLoginUser(s);
//            if (o.getMajor().equals(eMajor)) {
//                fAverage += m_aRatings.get(o);
//                nCounter++;
//            }
//        }

        fAverage /= nCounter;

        return Float.compare(Float.NaN, fAverage) == 0 ? 0 : fAverage;
    }


    public String getTitle() {
        return m_szTitle;
    }

    public void setTitle(String szTitle) {
        m_szTitle = szTitle;
    }

    public String getPosterURL() {
        return m_szPosterURL;
    }

    public void setPosterURL(String szURL) {
        m_szPosterURL = szURL;
    }

    public String getYear() {
        return m_szYear;
    }

    public void setYear(String szYear) {
        m_szYear = szYear;
    }

    public String getID() {
        return m_szID;
    }

    public float getRating() {
        return m_fAverageRating;
    }

    public float getRating(Major eMajor) {
        return calculateRating(eMajor);
    }

//    public static Movie getMovieByID(Context oContext, final ArrayAdapter<Movie> adapter, String id) {
//        return SearchMovieController.getMovieResult(oContext, adapter, this);
//    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Movie)) {
            return false;
        }
        Movie oOther = (Movie) o;
        return (oOther.getTitle().equals(getTitle()) && oOther.getYear().equals(getYear()));
    }

    @Override
    public String toString() {
        return m_szTitle;
    }
}
