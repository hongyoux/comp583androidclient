package comp680team7.com.clienthighscore.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by greatkiller on 2/21/2018.
 */

public class Game implements Parcelable{
    private Integer id;
    private String name;
    private String publisher;
    private Date releaseDate;

    private Game(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    private void readFromParcel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        publisher = in.readString();
        releaseDate = (Date) in.readSerializable();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeSerializable(releaseDate);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
