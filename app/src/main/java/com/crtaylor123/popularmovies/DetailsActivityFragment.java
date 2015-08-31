package com.crtaylor123.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    View rootView;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    Boolean isFavorite;
    List<Movie> favoritesList= new ArrayList<Movie>();;

    public DetailsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity();
        SharedPreferences favorites = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = favorites.getString("movieList", "");
        Type type = new TypeToken<List<Movie>>(){}.getType();
        favoritesList = gson.fromJson(json, type);
        if(favoritesList == null)
            favoritesList = new ArrayList<Movie>();
    }

    public void getFavoritesSharedPreferences(){
        Context context = getActivity();
        SharedPreferences favorites = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = favorites.getString("movieList", "");

        Type type = new TypeToken<List<Movie>>() {
        }.getType();

        favoritesList = gson.fromJson(json, type);
        if (favoritesList == null)
            favoritesList = new ArrayList<Movie>();

    }

    public void saveFavoritesSharedPreferences(){
        Context context = getActivity();
        SharedPreferences favorites = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = favorites.edit();

        Gson gson = new Gson();
        String json = gson.toJson(favoritesList);

        editor.putString("movieList", json);
        editor.apply();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        Intent intent = getActivity().getIntent();
        Movie movie = (Movie) intent.getParcelableExtra("movie");
        updateDetails(movie);

        return rootView;
    }



    public void updateDetails(Movie movieParameter){

        getFavoritesSharedPreferences();
        isFavorite = false;

        final Movie movie = movieParameter;


        if (movie != null) {

            final List<Trailer> trailerResults = movie.getTrailers();
            final List<Review> reviewResults = movie.getReviews();


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
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                    String chooserTitle = getResources().getString(R.string.chooser_title);
                    Intent chooser = Intent.createChooser(videoIntent, chooserTitle);
                    startActivity(chooser);
                }
            });

            final Button addToFavoritesButton = (Button) rootView.findViewById(R.id.addToFavoritesButton);

            for(int i = 0; i < favoritesList.size(); i++){
                Movie x = favoritesList.get(i);
                if(x.getId() == movie.getId()){
                    isFavorite = true;
                }
            }

            if(isFavorite){
                addToFavoritesButton.setText(R.string.remove_favorites_button_text);
            }else addToFavoritesButton.setText(R.string.add_favorites_button_text);


            addToFavoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFavorite) {
                        isFavorite = true;
                        favoritesList.add(movie);
                        addToFavoritesButton.setText(R.string.remove_favorites_button_text);
                    } else {
                        for(int i = 0; i < favoritesList.size(); i++){
                            Movie x = favoritesList.get(i);
                            if(x.getId() == movie.getId()){
                                favoritesList.remove(i);
                            }
                        }
                        isFavorite = false;
                        addToFavoritesButton.setText(R.string.add_favorites_button_text);

                    }
                }

            });



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
            ratingBar.setRating((float) (movie.getVote_average() / 2.0));

            saveFavoritesSharedPreferences();

        }
    }
}