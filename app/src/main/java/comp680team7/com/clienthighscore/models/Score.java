package comp680team7.com.clienthighscore.models;

public class Score {
    private Integer id;
    private Integer score;
    private Integer gameId;
    private Integer userId;
    private String imageUrl;

    public Score(Integer id, Integer score, Integer gameId, Integer userId, String imageUrl) {
        this.id = id;
        this.score = score;
        this.gameId = gameId;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
