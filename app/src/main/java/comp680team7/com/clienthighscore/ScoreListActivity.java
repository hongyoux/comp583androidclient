package comp680team7.com.clienthighscore;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import comp680team7.com.clienthighscore.adapters.ScoreListAdapter;
import comp680team7.com.clienthighscore.models.Score;
import comp680team7.com.clienthighscore.viewmodels.ScoreListViewModel;

public class ScoreListActivity extends AppCompatActivity implements OnListItemSelectedListener {

    private Integer gameId;
    private RecyclerView scoreListView;
    private ScoreListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);


        Intent sourceIntent = getIntent();
        gameId = sourceIntent.getIntExtra(String.valueOf(R.string.score_list_game_id_arg), -1);
        viewModel = ViewModelProviders.of(this).get(ScoreListViewModel.class);
        viewModel.init(this, gameId);

        scoreListView = (RecyclerView) findViewById(R.id.scoreListView);

        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        scoreListView.setAdapter(adapter);
        scoreListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        scoreListView.setLayoutManager(new LinearLayoutManager(this));

        viewModel.scoresListLiveData().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> scores) {
                getScoreAdapter().addScores(scores);
            }
        });

        configureToolBar((Toolbar) findViewById(R.id.scoreListToolbar));

        findViewById(R.id.newScoreFAB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScoreListActivity.this, ScoreCreateActivity.class);
                i.putExtra(ScoreCreateActivity.GAME_ID, gameId);
                i.putExtra(ScoreCreateActivity.USER_ID, MainActivity.CURRENT_ACTIVE_USER.getId());
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshScoreList();
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
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(getScoreAdapter().getScoreAt(position).getImageUrl()), "image/*");
        startActivity(intent);
//        Snackbar.make(scoreListView, "Selected " + getScoreAdapter().getScoreAt(position).getUserId(), Snackbar.LENGTH_LONG).show();
    }
}
