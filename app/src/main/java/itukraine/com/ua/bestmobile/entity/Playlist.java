package itukraine.com.ua.bestmobile.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable
public class Playlist {

    @DatabaseField(id = true)
    public String name;

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
                ", songsId=" + songsId +
                '}';
    }
}
