package com.crtaylor123.popularmovies;

import java.util.ArrayList;


public class ResultsMovie {
    private int page;
    private ArrayList<Movie> results;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ResultsMovie{" +
                "page=" + page +
                ", results=" + results +
                '}';
    }
}