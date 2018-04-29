package comp680team7.com.clienthighscore.repos;

import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;

import comp680team7.com.clienthighscore.MainActivity;
import comp680team7.com.clienthighscore.models.Score;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//@Singleton
public class ScoreRepository {
//    private BackendService backendService;

    public static MutableLiveData<ArrayList<Score>> getScore(Integer gameId) {
        final MutableLiveData<ArrayList<Score>> data = new MutableLiveData<>();
//        data.setValue(getDummyScoreList()); //JUS FOR TEST. TODO: REMOVE
        Call<ArrayList<Score>> scores = MainActivity.SERVICE.getScores(gameId);
        scores.enqueue(new Callback<ArrayList<Score>>() {
            @Override
            public void onResponse(Call<ArrayList<Score>> call, Response<ArrayList<Score>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Score>> call, Throwable t) {

            }

        });

        return data;
    }

    public static void addScore(Integer gameId, Integer userId, Integer score) {

        Call<ResponseBody> call = MainActivity.SERVICE.addScore(gameId, userId, score);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    //TODO:something
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Snackbar.make(publishedDateFieldLayout, "Error saving game. Please try again", Snackbar.LENGTH_LONG).show();
            }
        });
    }
//    private static ArrayList<Score> getDummyScoreList() {
//        Score ob1 = new Score(0, 10.0f, "1", "user1", "fakeurl");
//        Score ob2 = new Score(1, 100.0f, "2", "user2", "fakeurl");
//        Score ob3 = new Score(2, 10000.0f, "2", "user333333", "fakeurl");
//
//        ArrayList<Score> a = new ArrayList<Score>();
//        a.add(ob1);
//        a.add(ob2);
//        a.add(ob3);
//
//        return a;
//    }
}
