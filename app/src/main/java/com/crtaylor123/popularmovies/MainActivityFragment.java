package com.crtaylor123.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements OnTaskCompleted{

    /*
        API Key for TheMovieDatabase (www.themoviedb.org)
        Fill in with your own API Key.
     */
    private String apiKey = "";


    MovieAdapter movieAdapter;
    List<Movie> movieResults;
    Toast mAppToast;
    String TMDB_sort_popularity = "sort_by=popularity.desc";
    String TMDB_sort_rating = "sort_by=vote_average.desc";
    static String TMDB_default = "sort_by=popularity.desc";
    String TMDB_choice;



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
        savedInstanceState.putString("choice", TMDB_choice);
        savedInstanceState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) movieResults);
        super.onSaveInstanceState(savedInstanceState);
    }

    /*
        Handles the menu button selections.
        Assigns the TMDB_choice to a static String, TMDB_default, for when the Activity is recreated during a rotation.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_popularity) {
            TMDB_choice = "sort_by=popularity.desc";
            updateMovies();
            return true;
        }

        if (id == R.id.menu_rating) {
            TMDB_choice = "sort_by=vote_average.desc";
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    Method used for running the GetMovies class and updating the GridView layout.
 */
    private void updateMovies(){
        GetMoviesTask moviesTask = new GetMoviesTask(this);
        moviesTask.execute(TMDB_choice);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    /*
        Where everything is first created.
        Where the movieResults and custom ArrayAdapter, movieAdapter, are defined.
        Assigns movieAdapter to the GridView.
        Defines what happens when the user clicks on the movie posters.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MovieAdapter(getActivity(), movieResults);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_movie);
         gridView.setAdapter(movieAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //String movieTitle = movieResults.get(position).getTitle();

                Intent detailIntent = new Intent(getActivity(), DetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_TITLE", movieResults.get(position).getTitle());
                extras.putString("EXTRA_POSTER", movieResults.get(position).getPosterPath());
                extras.putString("EXTRA_SYNOPSIS", movieResults.get(position).getSynopsis());
                extras.putDouble("EXTRA_RATING", movieResults.get(position).getRating());
                extras.putString("EXTRA_RELEASEDATE", movieResults.get(position).getReleaseDate());
                extras.putString("EXTRA_BACKDROP", movieResults.get(position).getBackdropPath());
                detailIntent.putExtras(extras);
                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    /*
        Listener that is called at on PostExecute, at the end of the AsyncTask.
    */
    @Override
    public void onTaskCompeleted(List<Movie> result) {
        if (result != null) {
            movieAdapter.clear();
            for (Movie movie : result) {
                movieAdapter.add(movie);
            }
        }
    }

    /*
        Starts an AsyncTask task that fetches the TMDB JSON file in the Background.
        Adds each movie to the movieAdapter in the PostExecute.
     */

    public class GetMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = GetMoviesTask.class.getSimpleName();
        private OnTaskCompleted listener;

        public GetMoviesTask(OnTaskCompleted listener){
            this.listener = listener;
        }

        //URL Strings
        final String TMDB_base_url = "http://api.themoviedb.org/3/discover/movie?";

        /*
            Method that parses the JSON Text.
            Creates a JSONObject based on the 'results', then creates a JSONArray of each individual 'movie.'
            Then creates a Movie object of each JSONObject in the JSONArray, and adds it to an ArrayList finalMovieList.
            Finally returns the finalMovieList.
         */
        private List<Movie> getMovieDataFromJson(String movieJsonString) throws JSONException {

            //JSON Node Names
            final String TMDB_RESULTS = "results";
            final String TMDB_ID = "id";
            final String TMDB_TITLE = "original_title";
            final String TMDB_POSTER = "poster_path";
            final String TMDB_SYNOPSIS = "overview";
            final String TMDB_RATING = "vote_average";
            final String TMDB_RELEASEDATE = "release_date";
            final String TMDB_BACKDROP = "backdrop_path";

            //Creates a JSONObject based on the 'results', then creates a JSONArray of each individual 'movie.'
            JSONObject moviesJson = new JSONObject(movieJsonString);
            JSONArray movies = moviesJson.getJSONArray(TMDB_RESULTS);

            //Then creates a Movie object of each JSONObject in the JSONArray, and adds it to an ArrayList finalMovieList.
            List<Movie> finalMovieList = new ArrayList<Movie>();

            for (int i = 0; i < movies.length(); i++) {
                JSONObject movieObject = movies.getJSONObject(i);

                int id = movieObject.getInt(TMDB_ID);
                String title = movieObject.getString(TMDB_TITLE);
                String posterPath = movieObject.getString(TMDB_POSTER);
                String synopsis = movieObject.getString(TMDB_SYNOPSIS);
                double rating = movieObject.getDouble(TMDB_RATING);
                String releaseDate = movieObject.getString(TMDB_RELEASEDATE);
                String backdropPath = movieObject.getString(TMDB_BACKDROP);

                finalMovieList.add(new Movie(id, title, posterPath, synopsis, rating, releaseDate, backdropPath));
            }

            for (Movie i : finalMovieList) {
                Log.v(LOG_TAG, i.toString());
            }

            return finalMovieList;
        }//End of getMovieDataFromJson


        /*
            Background task of GetMovieTask.
            Opens a HttpURLConnection to TMDB JSON.
            Brings in JSON text and passes it to a getMovieDataFromJson method.
         */
        @Override
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonString = null;

            try {
                //Creates URL to get JSON
                String fullUrl = TMDB_base_url + TMDB_choice + "&api_key=" + apiKey;
                Log.v(LOG_TAG, "Built URL " + fullUrl);

                URL url = new URL(fullUrl);

                //Establishes Connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Brings in JSON text
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonString = buffer.toString();

                Log.v(LOG_TAG, "Movie JSON string: " + movieJsonString);
            }
            catch(IOException e){
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

            //Closes the urlConnection and BufferedReader
            finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            //Calls getMovieDataFromJson method with the JSON text as a parameter.
            try {
                return getMovieDataFromJson(movieJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }//End of doInBackground

        /*
            Calls onTaskComleteted Listener to complete task outside of the Background Thread.
            Takes resulting ArrayList of movies and adds them to the movieAdapter.
         */
        @Override
        protected void onPostExecute (List<Movie> result) {
            listener.onTaskCompeleted(result);
        }//End of onPostExecute
    }//End of getMovieDataFromJson
}//End of MainActivityFragment


