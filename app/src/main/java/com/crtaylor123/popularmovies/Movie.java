package com.crtaylor123.popularmovies;

/**
 * Created by crtaylor123 on 7/8/15.
 */
public class Movie {
    public int id;
    public String title;
    public String posterPath;
    public String synopsis;
    public double rating;
    public String releaseDate;
    public String backdropPath;

    public Movie() {
        this.id = -1;
        this.title = null;
        this.posterPath = null;
        this.synopsis = null;
        this.rating = -1;
        this.releaseDate = null;
        this.backdropPath =  null;
    }

    public Movie(int id, String title, String posterPath, String synopsis, double rating, String releaseDate, String backdropPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", rating=" + rating +
                ", releaseDate='" + releaseDate + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                '}';
    }
}
