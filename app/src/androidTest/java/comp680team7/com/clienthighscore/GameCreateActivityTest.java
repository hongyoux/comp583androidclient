package comp680team7.com.clienthighscore;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameCreateActivityTest {

    @Rule
    public ActivityTestRule<GameCreateActivity> activityTestRule = new ActivityTestRule<>(GameCreateActivity.class);

    @Test
    public void hasActivityLoaded() {
        onView(withId(R.id.gameName)).check(matches(isDisplayed()));
    }

    @Test
    public void addGameDetail() {
        onView(withId(R.id.gameName))
                .perform(typeText("Great"))
                .check(matches(withText("Great")));
    }
}