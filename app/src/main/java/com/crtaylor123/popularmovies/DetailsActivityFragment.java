package com.crtaylor123.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;


    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        Movie movie = (Movie) intent.getParcelableExtra("movie");

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);


        final List<Trailer> trailerResults = movie.getTrailers();
        List<Review> reviewResults = movie.getReviews();

        ListView trailerListView = (ListView) rootView.findViewById(R.id.trailersListView);
        ListView reviewListView = (ListView) rootView.findViewById(R.id.reviewsListView);
        trailerAdapter = new TrailerAdapter(getActivity(), trailerResults);
        reviewAdapter = new ReviewAdapter(getActivity(), reviewResults);
        trailerListView.setAdapter(trailerAdapter);
        reviewListView.setAdapter(reviewAdapter);

        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = trailerResults.get(position);

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));

            }
        });
        //reviewAdapter = new ReviewAdapter(getActivity(), reviewResults);

        /*
            Creates a new Intent and assigns it to the Intent for this class.
            Retrieves the extras Bundle that contained all the movie's information.
            If the intent exists and extras is not empty, it will create variables for the Views.
         */


        if(intent != null) {
            /*
                Assigns the movie information to the corresponding view within fragment_details
                The line assigning the backdrop image has been commented out as I didn't quite like the layout with it in there. I'll leave it there for when I have time to properly design it.
             */


            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780/" + movie.getPoster_path()).into((ImageView) rootView.findViewById(R.id.detailedPosterImageView));
            if (!movie.getTitle().equals("null"))
                ((TextView) rootView.findViewById(R.id.titleTextView)).setText(movie.getTitle());
            if (!movie.getOverview().equals("null"))
            ((TextView) rootView.findViewById(R.id.synopsisTextView)).setText(movie.getOverview());
            if (!movie.getRelease_date().equals("null"))
                ((TextView) rootView.findViewById(R.id.releaseDateTextView)).setText(movie.getRelease_date());

            RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
            ratingBar.setRating((float) (movie.getVote_average()/2.0));

        }


        return rootView;
    }
}
