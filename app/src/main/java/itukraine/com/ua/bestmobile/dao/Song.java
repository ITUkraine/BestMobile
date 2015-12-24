package itukraine.com.ua.bestmobile.dao;

import com.j256.ormlite.field.DatabaseField;

public class Song {

    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    String artist;
    @DatabaseField
    String name;
    @DatabaseField
    String time;

    public Song() {
    }

    public Song(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Artist: " + artist + " Name: " + name + " Time " + time;
    }
}
