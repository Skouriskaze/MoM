package com.example.mom.mom.Model;

import android.content.Context;

import java.util.List;

/**
 * Created by Jesse on 4/26/2016.
 */
public interface SearchControllerModel {
    void getSearchResults(Context oContext, String szSearch);

    List<Movie> getMovies();
}
