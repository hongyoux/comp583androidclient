package comp680team7.com.clienthighscore.service;

import java.util.List;

import comp680team7.com.clienthighscore.models.Game;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by greatkiller on 2/17/2018.
 *
 */

public interface BackendService {
    @Multipart
    @POST("/uploadImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @GET("/games")
    Call<List<Game>> getGames();
}
