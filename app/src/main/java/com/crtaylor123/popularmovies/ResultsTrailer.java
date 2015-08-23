package com.crtaylor123.popularmovies;

import java.util.ArrayList;

public class ResultsTrailer {
    private int page;
    private ArrayList<Trailer> results;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Trailer> getResults() {
        return results;
    }

    public void setResults(ArrayList<Trailer> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ResultsTrailer{" +
                "page=" + page +
                ", results=" + results +
                '}';
    }
}