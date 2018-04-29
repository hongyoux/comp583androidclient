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

import comp680team7.com.clienthighscore.MainActivity;
import comp680team7.com.clienthighscore.OnListItemSelectedListener;
import comp680team7.com.clienthighscore.R;
import comp680team7.com.clienthighscore.ScoreCreateActivity;
import comp680team7.com.clienthighscore.adapters.ScoreListAdapter;
import comp680team7.com.clienthighscore.models.Score;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreListFragment extends Fragment implements OnListItemSelectedListener {

    Integer gameId;
    RecyclerView scoreListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_score_list, container, false);

        configureToolBar((Toolbar) rootView.findViewById(R.id.scoreListToolbar));

        scoreListView = (RecyclerView) rootView.findViewById(R.id.scoreListView);

        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        scoreListView.setAdapter(adapter);
        scoreListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rootView.findViewById(R.id.newScoreFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ScoreCreateActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

    private void configureToolBar(Toolbar toolbar) {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.score_list_fragment_title);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private ScoreListAdapter getScoreAdapter() {
        return (ScoreListAdapter) scoreListView.getAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<ArrayList<Score>> scores = MainActivity.SERVICE.getScores(gameId);
        scores.enqueue(new Callback<ArrayList<Score>>() {
            @Override
            public void onResponse(Call<ArrayList<Score>> call, Response<ArrayList<Score>> response) {
                if (response.isSuccessful()) {
                    getScoreAdapter().addScores(response.body());
                } else {
                    Snackbar.make(scoreListView, "Issue Retrieving Game List", Snackbar.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Score>> call, Throwable t) {
                Snackbar.make(scoreListView, "Issue Score List for GameId:" + gameId, Snackbar.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onItemSelected(int position) {
        Snackbar.make(scoreListView, "Selected " + getScoreAdapter().getScoreAt(position).getUserId(), Snackbar.LENGTH_LONG).show();
    }
}
