package com.crtaylor123.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILSACTIVITYFRAGMENT_TAG = "DFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.activity_details_xml) != null){
            mTwoPane =  true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_details_xml, new DetailsActivityFragment(), DETAILSACTIVITYFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();


        if (id == R.id.menu_popularity) {

            //menuIntent.putExtra(Intent.EXTRA_TEXT, TMDB_sort_popularity);
            return false;
        }

        if (id == R.id.menu_rating) {
            //Toast mAppToast = Toast.makeText(this, TMDB_sort_rating, Toast.LENGTH_SHORT);
            //menuIntent.putExtra(Intent.EXTRA_TEXT, TMDB_sort_rating);
            return false;
        }

        if (id == R.id.menu_popularity) {

            //menuIntent.putExtra(Intent.EXTRA_TEXT, TMDB_sort_popularity);
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

}
