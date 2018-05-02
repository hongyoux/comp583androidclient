package comp680team7.com.clienthighscore.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comp680team7.com.clienthighscore.GameCreateActivity;
import comp680team7.com.clienthighscore.ScoreListActivity;
import comp680team7.com.clienthighscore.adapters.GameListAdapter;
import comp680team7.com.clienthighscore.MainActivity;
import comp680team7.com.clienthighscore.OnListItemSelectedListener;
import comp680team7.com.clienthighscore.R;
import comp680team7.com.clienthighscore.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameListFragment extends Fragment implements OnListItemSelectedListener {

    RecyclerView gameListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_game_list, container, false);

        configureToolBar((Toolbar) rootView.findViewById(R.id.gameListToolbar));

        gameListView = (RecyclerView) rootView.findViewById(R.id.gameListView);

        final GameListAdapter adapter = new GameListAdapter(this);
        gameListView.setAdapter(adapter);
        gameListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rootView.findViewById(R.id.newGameFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), GameCreateActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

    private void configureToolBar(Toolbar toolbar) {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.game_list_fragment_title);
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
//        Snackbar.make(gameListView, "Selected " + getGameAdapter().getGameAt(position).getName(), Snackbar.LENGTH_LONG).show();
        Intent scoreListActivityIntent = new Intent(getActivity(), ScoreListActivity.class);
        scoreListActivityIntent.putExtra(String.valueOf(R.string.score_list_game_id_arg),
                getGameAdapter().getGameAt(position).getId());
        startActivity(scoreListActivityIntent);
    }
}
