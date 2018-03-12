package comp680team7.com.clienthighscore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import comp680team7.com.clienthighscore.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by greatkiller on 3/11/2018.
 *
 */

public class GameListActivity extends AppCompatActivity {
    RecyclerView gameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        gameList = (RecyclerView) findViewById(R.id.gameListView);

        final GameListAdapter adapter = new GameListAdapter();
        gameList.setAdapter(adapter);
        gameList.setLayoutManager(new LinearLayoutManager(this));

        Call<List<Game>> games = MainActivity.SERVICE.getGames();
        games.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if(response.isSuccessful()) {
                    adapter.addGames(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {

            }
        });
    }
}
