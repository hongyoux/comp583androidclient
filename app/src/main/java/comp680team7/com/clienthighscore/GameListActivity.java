package comp680team7.com.clienthighscore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import comp680team7.com.clienthighscore.fragments.GameListFragment;
import comp680team7.com.clienthighscore.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by greatkiller on 3/11/2018.
 *
 */

public class GameListActivity extends AppCompatActivity
        implements GameListFragment.OnListItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

            Call<ArrayList<Game>> games = MainActivity.SERVICE.getGames();
            games.enqueue(new Callback<ArrayList<Game>>() {
                @Override
                public void onResponse(Call<ArrayList<Game>> call, Response<ArrayList<Game>> response) {
                    if (response.isSuccessful()) {
                        // Check that the activity is using the layout version with
                        // the fragment_container FrameLayout
                        if (findViewById(R.id.fragment_container) != null) {

                            // However, if we're being restored from a previous state,
                            // then we don't need to do anything and should return or else
                            // we could end up with overlapping fragments.
//                            if (savedInstanceState != null) {
//                                return;
//                            }

                            // Create a new Fragment to be placed in the activity layout
                            final GameListFragment firstFragment = new GameListFragment();

                            // In case this activity was started with special instructions from an
                            // Intent, pass the Intent's extras to the fragment as arguments
                            firstFragment.setArguments(getIntent().getExtras());

                            Bundle bun = new Bundle();
                            bun.putParcelableArrayList(getString(R.string.game_list_fragment_arg),
                                    response.body());
                            firstFragment.setArguments(bun);
                            // Add the fragment to the 'fragment_container' FrameLayout
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.fragment_container, firstFragment).commit();

                        }
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Game>> call, Throwable t) {

                }
            });

    }

    @Override
    public void onGameSelected(int position) {
        Toast.makeText(this, position,  Toast.LENGTH_LONG).show();
    }
}
