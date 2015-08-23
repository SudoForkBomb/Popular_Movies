package com.crtaylor123.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by crtaylor123 on 7/8/15.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {
    private static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    /*
        Custom ArrayAdapter.
        Adds Movie Posters from a List of Movies into the ImageViews within the Movie GridView within the fragment_main layout.
        Uses Picasso to fetch and handle the images for the Movie posters. We get the posterPath from the movie at position.
     */
    public TrailerAdapter(Activity context, List<Trailer> trailerResults) {
        super(context, 0, trailerResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Trailer trailerResults = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_trailers, parent, false);
        }
        //Creates an imageView and fills it with movieResult's poster.
        ImageView imageView = (ImageView) convertView.findViewById(R.id.trailerImageView);
        TextView textView = (TextView) convertView.findViewById(R.id.trailerTitleTextView);
        Picasso.with(getContext()).load("http://img.youtube.com/vi/" + trailerResults.getKey() + "/0.jpg").into(imageView);
        textView.setText(trailerResults.getName());

        return convertView;
    }
}
