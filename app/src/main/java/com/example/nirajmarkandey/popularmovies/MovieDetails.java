package com.example.nirajmarkandey.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.nirajmarkandey.popularmovies.app.AppController;
import com.example.nirajmarkandey.popularmovies.model.MovieModel;

/**
 * Created by niraj.markandey on 16/08/16.
 */
public class MovieDetails extends AppCompatActivity {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String titleText = extras.getString("title");
        String releaseDateText = extras.getString("release_date");
        String descriptionText = extras.getString("description");
        String ratingText = extras.getString("ratings");
        String imgeUrl = "http://image.tmdb.org/t/p/w185/"+extras.getString("image_url");

        setContentView(R.layout.movie_detail_page);
        TextView title,description,ratings,releaseDate;
        NetworkImageView thumbNail;

        title = (TextView) findViewById(R.id.tv_title);
        description = (TextView) findViewById(R.id.tv_description);
        ratings = (TextView) findViewById(R.id.tv_ratings);
        releaseDate = (TextView) findViewById(R.id.tv_release_date);
        thumbNail = (NetworkImageView) findViewById(R.id.iv_movie_poster);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(imgeUrl, ImageLoader.getImageListener(thumbNail, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        thumbNail.setImageUrl(imgeUrl, imageLoader);
        title.setText(titleText);
        description.setText(descriptionText);
        releaseDate.setText("Release Date : " +releaseDateText);
        ratings.setText("Ratings : " + ratingText);



    }
}
