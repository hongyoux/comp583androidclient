package comp680team7.com.clienthighscore.service;

import java.util.ArrayList;
import java.util.List;

import comp680team7.com.clienthighscore.models.Game;
import comp680team7.com.clienthighscore.models.Score;
import comp680team7.com.clienthighscore.models.User;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface BackendService {
    @POST("/auth")
    Call<User> authenticate(@Header("google_id_token") String idToken);

    @Multipart
    @POST("/uploadImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @GET("/games")
    Call<ArrayList<Game>> getGames();

    @GET("/user")
    Call<List<User>> getUsers();

    @GET("/score")
    Call<ArrayList<Score>> getScores(@Query("gameId") Integer gameId);

    @POST("/games")
    Call<ResponseBody> addGame(
            @Query("name") String name,
            @Query("publisher") String publisher,
            @Query("releaseDate") Long releaseDate
            );

    @POST("/score")
    Call<ResponseBody> addScore(
            @Query("gameId") Integer gameId,
            @Query("userId") Integer userId,
            @Query("score") Integer score,
            @Query("imageUrl") String imageUrl
    );
}
