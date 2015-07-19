package com.crtaylor123.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by crtaylor123 on 7/8/15.
 */
public class Movie implements Parcelable{
    public int id;
    public String title;
    public String posterPath;
    public String synopsis;
    public double rating;
    public String releaseDate;
    public String backdropPath;


    public Movie(int id, String title, String posterPath, String synopsis, double rating, String releaseDate, String backdropPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
    }

    private Movie(Parcel in){
        id = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        synopsis = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        backdropPath =  in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(posterPath);
        out.writeString(synopsis);
        out.writeDouble(rating);
        out.writeString(releaseDate);
        out.writeString(backdropPath);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
