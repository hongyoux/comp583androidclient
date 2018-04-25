package comp680team7.com.clienthighscore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import comp680team7.com.clienthighscore.fragments.GameListFragment;

/**
 * Created by greatkiller on 3/11/2018.
 *
 */

public class GameListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

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
//            firstFragment.setArguments(getIntent().getExtras());

//            Bundle bun = new Bundle();
//            bun.putParcelableArrayList(getString(R.string.game_list_fragment_arg),
//                    response.body());
//            firstFragment.setArguments(bun);
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();

        }
    }
}
