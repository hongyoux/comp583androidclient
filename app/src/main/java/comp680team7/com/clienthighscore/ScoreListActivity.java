package comp680team7.com.clienthighscore;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.util.List;

import comp680team7.com.clienthighscore.adapters.ScoreListAdapter;
import comp680team7.com.clienthighscore.models.Score;
import comp680team7.com.clienthighscore.viewmodels.ScoreListViewModel;

public class ScoreListActivity extends AppCompatActivity implements OnListItemSelectedListener {

    private Integer gameId;
    private RecyclerView scoreListView;
    private ScoreListViewModel viewModel;
    private SearchView searchView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_scores_action, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                getScoreAdapter().getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                getScoreAdapter().getFilter().filter(query);
                return false;
            }
        });
        return true;

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
    public void onItemSelected(int position, View sourceView) {
        Intent intent = new Intent();
        Score theScore = getScoreAdapter().getScoreAt(position);

        if (sourceView.getId() == R.id.shareButton) {
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this high score of " + theScore.getScore() +" on ScoreChest! "
                            + " at " + theScore.getImageUrl());
            intent.setType("text/plain");
            startActivity(intent);
        }
        else {
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(theScore.getImageUrl()), "image/*");
            startActivity(intent);
        }


    }
}
