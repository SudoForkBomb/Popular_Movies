package com.crtaylor123.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by crtaylor123 on 7/8/15.
 */
public class ReviewAdapter extends ArrayAdapter<Review> {
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    /*
        Custom ArrayAdapter.
        Adds Movie Posters from a List of Movies into the ImageViews within the Movie GridView within the fragment_main layout.
        Uses Picasso to fetch and handle the images for the Movie posters. We get the posterPath from the movie at position.
     */
    public ReviewAdapter(Activity context, List<Review> reviewResults) {
        super(context, 0, reviewResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        Review reviewResults = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_review, parent, false);
        }
        //Creates an imageView and fills it with movieResult's poster.
        TextView authorTextView = (TextView) convertView.findViewById(R.id.authorTextView);
        authorTextView.setText(reviewResults.getAuthor());
        TextView contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);
        contentTextView.setText(reviewResults.getContent());

        return convertView;
    }
}
