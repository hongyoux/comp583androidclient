package comp680team7.com.clienthighscore;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import comp680team7.com.clienthighscore.adapters.ScoreListAdapter;
import comp680team7.com.clienthighscore.models.Score;
import comp680team7.com.clienthighscore.viewmodels.ScoreViewModel;

public class ScoreListActivity extends AppCompatActivity implements OnListItemSelectedListener {

    private Integer gameId;
    private RecyclerView scoreListView;
    private ScoreViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);


        Intent sourceIntent = getIntent();
        gameId = sourceIntent.getIntExtra(String.valueOf(R.string.score_list_game_id_arg), -1);
         viewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        viewModel.init(gameId);

        scoreListView = (RecyclerView) findViewById(R.id.scoreListView);

        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        scoreListView.setAdapter(adapter);
        scoreListView.setLayoutManager(new LinearLayoutManager(this));

        viewModel.getScores().observe(this, new Observer<ArrayList<Score>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Score> scores) {
                getScoreAdapter().addScores(scores);
            }
        });

        configureToolBar((Toolbar) findViewById(R.id.scoreListToolbar));

        findViewById(R.id.newScoreFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScoreCreateActivity.class);
                startActivity(i);
            }
        });

    }

    private void configureToolBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.score_list_fragment_title);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private ScoreListAdapter getScoreAdapter() {
        return (ScoreListAdapter) scoreListView.getAdapter();
    }

    @Override
    public void onItemSelected(int position) {
        Snackbar.make(scoreListView, "Selected " + getScoreAdapter().getScoreAt(position).getUserId(), Snackbar.LENGTH_LONG).show();
    }
}
