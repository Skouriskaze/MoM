package com.example.mom.mom.Presenter;

import android.content.Context;

import com.example.mom.mom.Model.FirebaseMovieService;
import com.example.mom.mom.Model.LoginManager;
import com.example.mom.mom.Model.Movie;
import com.example.mom.mom.Model.Session;
import com.example.mom.mom.Model.User;
import com.example.mom.mom.MovieListActivity;
import com.example.mom.mom.View.BackListener;
import com.example.mom.mom.View.ClickListener;
import com.example.mom.mom.View.MovieView;
import com.example.mom.mom.View.Navigator;
import com.example.mom.mom.View.OnCreate;

/**
 * Created by Jesse on 3/8/2016.
 * Added database writing. Nat 3/11/2016
 */
public class MoviePresenter implements ClickListener, OnCreate, BackListener {

    private MovieView m_oView;
    private Navigator m_oNavigator;
    private Context m_oContext;

    public MoviePresenter(MovieView oView, Navigator oNavigator, Context oContext) {
        m_oView = oView;
        m_oNavigator = oNavigator;
        m_oContext = oContext;
    }

    @Override
    public void onClick() {
        FirebaseMovieService.getConstantRating(m_oView.getMovie(), m_oContext, m_oView.getRatingView());
        //m_oView.setTextRating(m_oView.getConstantRating());

        Movie oMovie = m_oView.getMovie();

        User oUser = Session.getUser();
        oMovie.rate(oUser, m_oView.getRating());

        FirebaseMovieService fs = new FirebaseMovieService(m_oContext);
        fs.writeRating(oMovie.getID(), oUser, m_oView.getRating());
    }

    @Override
    public void onCreate() {
        m_oView.setTitle(m_oView.getMovie().getTitle());
        m_oView.setYear(m_oView.getMovie().getYear());
        m_oView.setImage(m_oView.getMovie().getPosterURL());

        //Set rating bar listener
        FirebaseMovieService.setUserRatingBar(m_oView.getMovie(), m_oContext, m_oView.getRatingBar(), Session.getUser());

        //Set rating listener
        FirebaseMovieService.getConstantRating(m_oView.getMovie(), m_oContext, m_oView.getRatingView());
    }

    @Override
    public void onBackPressed() {
        m_oNavigator.addExtra(HomePresenter.EXTRA_TAG, m_oView.getSearch());
        m_oNavigator.startNewTopActivity(MovieListActivity.class);
    }
}
