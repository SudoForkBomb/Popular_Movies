package com.crtaylor123.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{




    /*
        API Key for TheMovieDatabase (www.themoviedb.org)
        Fill in with your own API Key.
     */
    static final public String apiKey = "";


    MovieAdapter movieAdapter;
    List<Movie> movieResults;
    String TMDB_sort_popularity = "popularity.desc";
    String TMDB_sort_rating = "vote_average.desc";
    String TMDB_sort_favorite = "favorite";

    static String TMDB_default = "popularity.desc";
    String TMDB_choice;
    List<Movie> favoritesList= new ArrayList<Movie>();

    public MainActivityFragment() {
    }

    /*
        Checks to see if there if the savedInstanceState is null or if it doesn't contain the ArrayList of Movies.
        If it is either null or doesn't have contain the movies, it creates a new ArrayList and sets the TMDB_choice to the default search.
        Else, it restores the ArrayList of Movies and restores the TMDB_choice.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState == null || !savedInstanceState.containsKey("key")){
            movieResults = new ArrayList<Movie>();
            TMDB_choice = TMDB_default;
        }
        else {
            movieResults = savedInstanceState.getParcelableArrayList("key");
            TMDB_choice = savedInstanceState.getString("choice");
        }
    }

    /*
        Stores the TMDB_choice with the key "choice" and the movieResults with key "key".
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("choice", TMDB_choice);
        savedInstanceState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) movieResults);

    }

    /*
        Handles the menu button selections.
        Assigns the TMDB_choice to a static String, TMDB_default, for when the Activity is recreated during a rotation.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_popularity) {
            TMDB_choice = TMDB_sort_popularity;
            updateMovies();
            return true;
        }

        if (id == R.id.menu_rating) {
            TMDB_choice = TMDB_sort_rating;
            updateMovies();
            return true;
        }

        if (id == R.id.menu_favorites) {
            TMDB_choice = TMDB_sort_favorite;
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /*
    Method used for running the GetMovies class and updating the GridView layout.
    */
    private void updateMovies(){
        if(TMDB_choice.equals(TMDB_sort_favorite)){
            movieAdapter.clear();
            movieAdapter.addAll(favoritesList);
        }
        else getMoviesRetrofit(TMDB_choice);
    }

    @Override
    public void onStart() {
        super.onStart();

        Context context = getActivity();
        SharedPreferences favorites = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = favorites.getString("movieList", "");
        Type type = new TypeToken<List<Movie>>(){}.getType();
        favoritesList = gson.fromJson(json, type);
        if(favoritesList == null)
            favoritesList = new ArrayList<Movie>();
        updateMovies();
    }

    @Override
    public void onPause() {
        super.onPause();
        Context context = getActivity();
        SharedPreferences favorites = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor  editor = favorites.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favoritesList);
        editor.putString("movieList", json);
        editor.commit();

    }


    /*
            Where everything is first created.
            Where the movieResults and custom ArrayAdapter, movieAdapter, are defined.
            Assigns movieAdapter to the GridView.
            Defines what happens when the user clicks on the movie posters.
         */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MovieAdapter(getActivity(), movieResults);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_movie);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                final Movie selectedMovie = movieResults.get(position);
                Intent detailIntent = new Intent(getActivity(), DetailsActivityFragment.class);
                detailIntent.putExtra("movie", selectedMovie);
                //startActivity(detailIntent);
            }
        });

        return rootView;
    }

    /*
        Uses the Retrofit library to fetch the TMDB JSON file in the Background.
        Adds each movie to the movieAdapter on success.
    */
    public void getMoviesRetrofit(String sortChoice){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .build();

        Map movieMap = new HashMap<>();
        movieMap.put("sort_by", sortChoice);
        movieMap.put("api_key", apiKey);

        final MovieAPI movieAPI = restAdapter.create(MovieAPI.class);
        movieAPI.getMovie(movieMap, new Callback<ResultsMovie>() {

            @Override
            public void success(ResultsMovie resultsMovie, Response response) {
                if (resultsMovie != null) {
                    movieAdapter.clear();
                    for (final Movie movie : resultsMovie.getResults()) {
                        movieAPI.getTrailer(movie.getId(), apiKey, new Callback<ResultsTrailer>() {
                            @Override
                            public void success(ResultsTrailer resultsTrailer, Response response) {
                                movie.setTrailers(resultsTrailer.getResults());
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });

                        movieAPI.getReview(movie.getId(), apiKey, new Callback<ResultsReview>() {

                            @Override
                            public void success(ResultsReview resultsReview, Response response) {
                                movie.setReviews(resultsReview.getResults());
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                        movieAdapter.add(movie);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}//End of MainActivityFragment


