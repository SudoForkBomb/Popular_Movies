package com.crtaylor123.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_details_xml, new DetailsActivityFragment())
                    .commit();
        }
    }
}
