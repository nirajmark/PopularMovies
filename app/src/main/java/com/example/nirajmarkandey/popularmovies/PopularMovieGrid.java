package com.example.nirajmarkandey.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nirajmarkandey.popularmovies.adapter.MovieGridAdapter;
import com.example.nirajmarkandey.popularmovies.app.AppController;
import com.example.nirajmarkandey.popularmovies.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PopularMovieGrid extends AppCompatActivity {
    private static final int RESULT_SETTINGS = 1;
    String userMoviePrefrence;

    private List<MovieModel> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MovieGridAdapter mAdapter;
    private ProgressDialog pDialog;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movie_grid);

        // preference
        PreferenceManager.setDefaultValues(this, R.xml.preference_user, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userMoviePrefrence = sharedPref.getString(SettingsActivity.KEY_MOVIE_PREFERENCE, "");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        mAdapter = new MovieGridAdapter(this,movieList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieModel movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getOriginalTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent openMovieDetails = new Intent(PopularMovieGrid.this,MovieDetails.class);
                openMovieDetails.putExtra("title",movie.getOriginalTitle());
                openMovieDetails.putExtra("description",movie.getOverView());
                openMovieDetails.putExtra("release_date",movie.getReleaseDate());
                openMovieDetails.putExtra("ratings",movie.getVoteAverage());
                openMovieDetails.putExtra("image_url",movie.getPosterPath());
                startActivity(openMovieDetails);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preference:
                Toast.makeText(getApplicationContext(), "User Preference is selected ", Toast.LENGTH_SHORT).show();
                Intent startSettingsActivity = new Intent(PopularMovieGrid.this,SettingsActivity.class);
                startActivityForResult(startSettingsActivity,RESULT_SETTINGS);
                return true;

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showUserSettings();
                break;

        }

    }

    private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        userMoviePrefrence= sharedPrefs.getString(SettingsActivity.KEY_MOVIE_PREFERENCE, "");
        Toast.makeText(getApplicationContext(),"Preference chnaged to "+ userMoviePrefrence , Toast.LENGTH_SHORT).show();
        prepareMovieData();

    }

    private void prepareMovieData() {
        // add your api key in following URL
        url = "http://api.themoviedb.org/3/movie/"+userMoviePrefrence+"?api_key=<ADD_YOUR_API_KEY_HERE>";
        movieList.clear();
        JsonObjectRequest movieReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response-->", response.toString());
                        hidePDialog();
                        JSONArray results = new JSONArray();
                        try {
                            results = response.getJSONArray("results");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Parsing json
                        for (int i = 0; i < results.length(); i++) {
                            try {

                                JSONObject obj = results.getJSONObject(i);
                                MovieModel movie = new MovieModel();
                                movie.setOriginalTitle(obj.getString("original_title"));
                                movie.setOverView(obj.getString("overview"));
                                movie.setPosterPath(obj.getString("backdrop_path"));
                                movie.setReleaseDate(obj.getString("release_date"));
                                movie.setVoteAverage(obj.getString("vote_average"));

                                // adding movie to movies array
                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley Err", "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private PopularMovieGrid.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final PopularMovieGrid.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
}
