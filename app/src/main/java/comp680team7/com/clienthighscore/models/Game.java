package comp680team7.com.clienthighscore.models;

import java.util.Date;

/**
 * Created by greatkiller on 2/21/2018.
 */

public class Game {
    private Integer id;
    private String name;
    private String publisher;
    private Date releaseDate;

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
