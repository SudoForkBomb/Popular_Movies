package com.crtaylor123.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by crtaylor123 on 7/8/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    /*
        Custom ArrayAdapter.
        Adds Movie Posters from a List of Movies into the ImageViews within the Movie GridView within the fragment_main layout.
        Uses Picasso to fetch and handle the images for the Movie posters. We get the posterPath from the movie at position.
     */
    public MovieAdapter(Activity context, List<Movie> movieResults) {
        super(context, 0, movieResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Movie movieResult = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_view_posters, parent, false);
        }
        //Creates an imageView and fills it with movieResult's poster.
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_poster_image);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342/" + movieResult.getPosterPath()).into(imageView);

        return convertView;
    }
}
