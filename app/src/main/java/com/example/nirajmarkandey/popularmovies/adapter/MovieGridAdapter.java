package com.example.nirajmarkandey.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.nirajmarkandey.popularmovies.R;
import com.example.nirajmarkandey.popularmovies.app.AppController;
import com.example.nirajmarkandey.popularmovies.model.MovieModel;
//import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by niraj.markandey on 11/08/16.
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MyViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<MovieModel> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String imgUrl = "http://image.tmdb.org/t/p/w185/";


    public MovieGridAdapter(Activity activity, List<MovieModel> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView thumbnail;


        public MyViewHolder(View view) {
            super(view);
            thumbnail = (NetworkImageView) view.findViewById(R.id.iv_movies);

        }
    }


    public MovieGridAdapter(List<MovieModel> moviesList) {
        this.movieItems = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        MovieModel movie = movieItems.get(position);
        imageLoader.get(imgUrl+movie.getPosterPath(), ImageLoader.getImageListener(holder.thumbnail, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.thumbnail.setImageUrl(imgUrl+movie.getPosterPath(), imageLoader);
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }


}


