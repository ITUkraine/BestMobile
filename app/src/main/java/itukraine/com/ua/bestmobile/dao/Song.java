package itukraine.com.ua.bestmobile.dao;

public class Song {

    public long id;
    public String title;
    public String artist;
    public String album;
    public String path;
    public int duration;
    public long albumId;

    public Song() {
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", albumId=" + albumId +
                '}';
    }
}
