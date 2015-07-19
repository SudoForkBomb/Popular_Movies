package com.crtaylor123.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        /*
            Creates a new Intent and assigns it to the Intent for this class.
            Retrieves the extras Bundle that contained all the movie's information.
            If the intent exists and extras is not empty, it will create variables for the Views.
         */
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        if(intent != null && !extras.isEmpty()) {
            String titleString = extras.getString("EXTRA_TITLE");
            String posterString = extras.getString("EXTRA_POSTER");
            String synopsisString = extras.getString("EXTRA_SYNOPSIS");
            double ratingInt = extras.getDouble("EXTRA_RATING");
            String releaseDateString = extras.getString("EXTRA_RELEASEDATE");
            String backdropString = extras.getString("EXTRA_BACKDROP");



            /*
                Assigns the movie information to the corresponding view within fragment_details
                The line assigning the backdrop image has been commented out as I didn't quite like the layout with it in there. I'll leave it there for when I have time to properly design it.
             */
            // Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/" + backdropString).fit().centerCrop().into((ImageView) rootView.findViewById(R.id.backdropImageView));
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780/" + posterString).into((ImageView) rootView.findViewById(R.id.detailedPosterImageView));
            if (!titleString.equals("null"))
                ((TextView) rootView.findViewById(R.id.titleTextView)).setText(titleString);
            if (!synopsisString.equals("null"))
            ((TextView) rootView.findViewById(R.id.synopsisTextView)).setText(synopsisString);
            if (!releaseDateString.equals("null"))
                ((TextView) rootView.findViewById(R.id.releaseDateTextView)).setText(releaseDateString);

            RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

            ratingBar.setRating((float) (ratingInt/2.0));

        }


        return rootView;
    }
}
