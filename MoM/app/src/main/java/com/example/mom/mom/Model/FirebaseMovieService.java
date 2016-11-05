package com.example.mom.mom.Model;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by jesse on 3/30/16.
 */
public class FirebaseMovieService {

    Firebase firebase;
    public FirebaseMovieService(final Context context) {
        Firebase.setAndroidContext(context);
        firebase = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");

        //Calculate overall rating
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot movie : dataSnapshot.getChildren()) {
                    float fAverageRating = 0;
                    int nCount = 0;
                    //Major map
                    Map<String, Float> aAverageMajorRatings = new HashMap<>();
                    Map<String, Integer> aMajorCounts = new HashMap<>();

                    //Calculating rating
                    for (DataSnapshot user : movie.child("Ratings").getChildren()) {
                        String szUsername = user.getKey();
                        float fRating = user.child("rating").getValue(float.class);

                        fAverageRating = (fAverageRating * nCount + fRating) / ++nCount;

//                        User oUser = LoginManager.getUser(user.getKey() == "admindummy" ? "" : user.getKey());
//                        Float fAverageMajorRating = aAverageMajorRatings.get(oUser.getMajor().toString());
//                        fAverageMajorRating = fAverageMajorRating == null ? 0 : fAverageRating;
//                        Integer nMajorCount = aMajorCounts.get(oUser.getMajor().toString());
//                        nMajorCount = nMajorCount == null ? 0 : nMajorCount;
//                        fAverageMajorRating = (fAverageRating * nMajorCount + fRating) / (nMajorCount + 1);
//                        aMajorCounts.put(oUser.getMajor().toString(), nMajorCount + 1);
//                        aAverageMajorRatings.put(oUser.getMajor().toString(), fAverageMajorRating);

                        String szMajor = user.child("major").getValue(String.class);

                        Float fAverageMajorRating = aAverageMajorRatings.get(szMajor);
                        fAverageMajorRating = fAverageMajorRating == null ? 0 : fAverageRating;
                        Integer nMajorCount = aMajorCounts.get(szMajor);
                        nMajorCount = nMajorCount == null ? 0 : nMajorCount;
                        fAverageMajorRating = (fAverageRating * nMajorCount + fRating) / (nMajorCount + 1);
                        aMajorCounts.put(szMajor, nMajorCount + 1);
                        aAverageMajorRatings.put(szMajor, fAverageMajorRating);
                    }
                    //Set rating
                    String movieID = movie.getKey();
                    firebase.child(movieID).child("Average Rating").setValue(fAverageRating);
                    firebase.child(movieID).child("Major Ratings").setValue(aAverageMajorRatings);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(context, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static List<Movie> getMovieList(final Context oContext, final ArrayAdapter<Movie> adapter) {
        final List<Movie> topMovies = new ArrayList<>();
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");
        //Todo: Limit to last CONST
        Query refQuery = ref.orderByChild("Average Rating").limitToLast(5);

        refQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Stack<Movie> reverser = new Stack<>();
                for (DataSnapshot dataMovie : dataSnapshot.getChildren()) {
                    if (reverser.size() <= 5) {
                        Movie movie = new Movie("", "", "", dataMovie.getKey());
                        SearchMovieController.getMovieResult(oContext, adapter, movie);
                        reverser.add(movie);
                        //topMovies.add(movie);
                    }
                }
                while (!reverser.empty()) {
                    adapter.add(reverser.pop());
                }
                Collections.reverse(topMovies);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return topMovies;
    }

    public static List<Movie> getMovieList(final Context oContext, final ArrayAdapter<Movie> adapter, Major eMajor) {
        final List<Movie> topMovies = new ArrayList<>();
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");
        //Todo: Limit to last CONST
        Query refQuery = ref.orderByChild("Major Ratings/" + eMajor.toString()).limitToLast(5);

        refQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Stack<Movie> reverser = new Stack<>();
                for (DataSnapshot dataMovie : dataSnapshot.getChildren()) {
                    if (reverser.size() <= 5) {
                        Movie movie = new Movie("", "", "", dataMovie.getKey());
                        SearchMovieController.getMovieResult(oContext, adapter, movie);
                        reverser.add(movie);
                        //topMovies.add(movie);
                    }
                }
                while (!reverser.empty()) {
                    adapter.add(reverser.pop());
                }
                Collections.reverse(topMovies);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return topMovies;
    }

    public static void getConstantRating(final Movie oMovie, Context oContext, final TextView view) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float fRating = dataSnapshot.child(oMovie.getID()).child("Average Rating").getValue(float.class);
                view.setText(fRating + "");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void getRatingOnce(final Movie oMovie, Context oContext, final TextView view) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float fRating = dataSnapshot.child(oMovie.getID()).child("Average Rating").getValue(float.class);
                view.setText(String.format("%.2f/5", fRating));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void getConstantRating(final Movie oMovie, Context oContext, final TextView view, final Major eMajor) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float fRating = dataSnapshot.child(oMovie.getID()).child("Major Ratings").child(eMajor.toString()).getValue(float.class);
                view.setText(fRating + "");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void getRatingOnce(final Movie oMovie, Context oContext, final TextView view, final Major eMajor) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float fRating = dataSnapshot.child(oMovie.getID()).child("Major Ratings").child(eMajor.toString()).getValue(float.class);
                view.setText(String.format("%.2f/5", fRating == null ? 0 : fRating));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void writeRating(String szKey, User oUser, float fRating) {
        Rating oRating = new Rating(oUser.getMajor(), fRating);
        if (oUser.getUsername() != "") {
            //Todo: Add rating class with major and float rating
            firebase.child(szKey).child("Ratings").child(oUser.getUsername()).setValue(oRating);
        }
        else {
            firebase.child(szKey).child("Ratings").child("admindummy").setValue(oRating);
        }
    }

    public static void setUserRatingBar(final Movie oMovie, Context oContext, final RatingBar rb, final User oUser) {
        Firebase.setAndroidContext(oContext);
        Firebase ref = new Firebase("https://boiling-heat-5021.firebaseio.com/Movies");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Float fRating = dataSnapshot.child(oMovie.getID()).child("Ratings").child(oUser.getUsername() == "" ? "admindummy" : oUser.getUsername()).child("rating").getValue(float.class);
                rb.setRating(fRating == null ? 0 : fRating);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
