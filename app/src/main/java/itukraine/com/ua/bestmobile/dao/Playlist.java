package itukraine.com.ua.bestmobile.dao;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;

public class Playlist {

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String totalTime;

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
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalTime='" + totalTime + '\'' +
                ", songsId=" + songsId +
                '}';
    }
}
