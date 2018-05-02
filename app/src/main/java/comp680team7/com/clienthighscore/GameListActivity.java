package comp680team7.com.clienthighscore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import comp680team7.com.clienthighscore.adapters.GameListAdapter;
import comp680team7.com.clienthighscore.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameListActivity extends AppCompatActivity implements OnListItemSelectedListener{

    RecyclerView gameListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        configureToolBar((Toolbar) findViewById(R.id.gameListToolbar));

        gameListView = (RecyclerView) findViewById(R.id.gameListView);

        final GameListAdapter adapter = new GameListAdapter(this);
        gameListView.setAdapter(adapter);
        gameListView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.newGameFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameCreateActivity.class);
                startActivity(i);
            }
        });

    }

    private void configureToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.game_list_fragment_title);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private GameListAdapter getGameAdapter() {
        return (GameListAdapter) gameListView.getAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<ArrayList<Game>> games = MainActivity.SERVICE.getGames();
        games.enqueue(new Callback<ArrayList<Game>>() {
            @Override
            public void onResponse(Call<ArrayList<Game>> call, Response<ArrayList<Game>> response) {
                if (response.isSuccessful()) {
                    getGameAdapter().addGames(response.body());
                } else {
                    Snackbar.make(gameListView, "Issue Retrieving Game List", Snackbar.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Game>> call, Throwable t) {
                Snackbar.make(gameListView, "Issue Retrieving Game List", Snackbar.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onItemSelected(int position, View sourceView) {
//         Snackbar.make(gameListView, "Selected " +
//                 getGameAdapter().getGameAt(position).getName(), Snackbar.LENGTH_LONG).show();
        Intent scoreListActivityIntent = new Intent(this , ScoreListActivity.class);
        scoreListActivityIntent.putExtra(String.valueOf(R.string.score_list_game_id_arg),
                getGameAdapter().getGameAt(position).getId());
        startActivity(scoreListActivityIntent);

    }
}

