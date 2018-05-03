package comp680team7.com.clienthighscore;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.vansuita.pickimage.bean.PickResult;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import comp680team7.com.clienthighscore.models.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScoreListActivityTest {
    @Rule
    public ActivityTestRule<ScoreListActivity> activityTestRule =
            new ActivityTestRule<>(ScoreListActivity.class, true, false);

    @Test
    public void addScoreList(){
        MainActivity.CURRENT_ACTIVE_USER = new User(7, "test", "test@test.com");
        Intent intent = new Intent();
        intent.putExtra(String.valueOf(R.string.score_list_game_id_arg), 118);
        activityTestRule.launchActivity(intent);

        onView(withId(R.id.newScoreFAB))
                .perform(click());

        onView(withId(R.id.newScore)).perform(typeText("2000"));
        onView(withId(R.id.save)).perform(click());

        RecyclerView scoreListView = activityTestRule.getActivity().findViewById(R.id.scoreListView);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int itemCount = scoreListView.getAdapter().getItemCount();
        onView(withId(R.id.scoreListView)) //scroll to last added item; kind of bad, but it works
                .perform(RecyclerViewActions.scrollToPosition(itemCount-1));
    }
}