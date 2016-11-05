package com.example.mom.mom.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mom.mom.Model.FirebaseMovieService;
import com.example.mom.mom.Model.Major;
import com.example.mom.mom.Model.Movie;
import com.example.mom.mom.Model.Session;
import com.example.mom.mom.Model.VolleyImage;
import com.example.mom.mom.MovieListActivity;
import com.example.mom.mom.R;
import com.example.mom.mom.View.ClickListener;
import com.example.mom.mom.View.HomeView;
import com.example.mom.mom.View.Navigator;
import com.example.mom.mom.View.OnCreate;

//import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jesse on 2/29/16.
 */
public class HomePresenter implements ClickListener, OnCreate {

    public static final String EXTRA_TAG = "com.example.mom.mom.Presenter.Search";
    public static final int MAX_SUGGESTIONS = 5;

    private HomeView m_oView;
    private Navigator m_oNavigator;
    private Context m_oContext;

    public HomePresenter(HomeView oView, Navigator oNavigator, Context oContext) {
        m_oView = oView;
        m_oNavigator = oNavigator;
        m_oContext = oContext;
    }

    @Override
    public void onClick() {
        String szSearch = m_oView.getSearch();
        m_oNavigator.addExtra(EXTRA_TAG, szSearch);
        m_oNavigator.startNewActivity(MovieListActivity.class);
    }

    @Override
    public void onCreate() {

        RecommendAdapter adapterMajor = new RecommendAdapter(m_oContext, Session.getUser().getMajor());
        RecommendAdapter adapterOverall = new RecommendAdapter(m_oContext, null);

        List<Movie> aOverallSublist = FirebaseMovieService.getMovieList(m_oContext, adapterOverall);
        List<Movie> aMajorSublist = FirebaseMovieService.getMovieList(m_oContext, adapterMajor, Session.getUser().getMajor());

        Log.d("?", "");
        ListView lvOverall = m_oView.getListViewOverall();
        ListView lvMajor = m_oView.getListViewMajor();


        lvOverall.setAdapter(adapterOverall);
        lvMajor.setAdapter(adapterMajor);
    }

    public class RecommendAdapter extends ArrayAdapter<Movie> {
        Major eMajor;

        public RecommendAdapter(Context oContext, Major eMajor) {
            super(oContext, 0);
            this.eMajor = eMajor;
        }
        public RecommendAdapter(Context oContext, List<Movie> aMovies, Major eMajor) {
            super(oContext, 0, aMovies);
            this.eMajor = eMajor;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Movie oMovie = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_home, parent, false);
            }

            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.txtTitle);
            ImageView imgPoster = (ImageView) convertView.findViewById(R.id.imgPoster);

            // Populate the data into the template view using the data object
            tvName.setText(oMovie.getTitle());

            //Populate ImageView using Volley
            ImageLoader mImageLoader = VolleyImage.getInstance(this.getContext()).getImageLoader();
            NetworkImageView mNetworkImageView = (NetworkImageView) imgPoster;
            String szURL = oMovie.getPosterURL();
            if (szURL.equals("N/A")) {
                //Todo: Image not found
            } else {
                mNetworkImageView.setImageUrl(oMovie.getPosterURL(), mImageLoader);
            }

            //Set rating Text
            TextView oRate = (TextView) convertView.findViewById(R.id.txtRating);
            if (eMajor == null) {
                FirebaseMovieService.getRatingOnce(oMovie, m_oContext, oRate);
            } else {
                FirebaseMovieService.getRatingOnce(oMovie, m_oContext, oRate, eMajor);
            }

            //Set year text
            TextView oYear = (TextView) (convertView.findViewById(R.id.txtYear));
            oYear.setText("(" + oMovie.getYear() + ")");
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
