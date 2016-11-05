package com.example.mom.mom.Model;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jesse on 3/31/2016.
 */
public class SearchMovieController {

    private static final String m_szURL = "http://www.omdbapi.com/?i=";

    public static Movie getMovieResult(Context oContext, final ArrayAdapter<Movie> adapter, final Movie movie) {
        RequestQueue oQueue = Volley.newRequestQueue(oContext);

        StringRequest request = new StringRequest(Request.Method.GET, m_szURL + movie.getID(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processResults(response, movie);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        oQueue.add(request);

        return movie;
    }

    private static void processResults(String response, Movie movie) {
        try {
            JSONObject jObject = new JSONObject(response);
            String title = jObject.getString("Title");
            String posterURL = jObject.getString("Poster");
            String year = jObject.getString("Year");

            movie.setTitle(title);
            movie.setPosterURL(posterURL);
            movie.setYear(year);
        } catch (JSONException e) {
        }
    }
}
