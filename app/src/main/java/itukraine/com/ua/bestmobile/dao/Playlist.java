package itukraine.com.ua.bestmobile.dao;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;

public class Playlist {

    @DatabaseField(id = true)
    public String name;
    @DatabaseField
    public int totalTime;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public ArrayList<Long> songsId = new ArrayList<>();

    public Playlist() {
    }

    public Playlist(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", songsId=" + songsId +
                '}';
    }
}
