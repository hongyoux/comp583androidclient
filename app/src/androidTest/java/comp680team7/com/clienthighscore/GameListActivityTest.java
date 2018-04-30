package comp680team7.com.clienthighscore;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameListActivityTest {

    @Rule
    public ActivityTestRule<GameListActivity> activityTestRule = new ActivityTestRule<>(GameListActivity.class);


    @Test
    public void addGameTest() {
        onView(withId(R.id.newGameFAB))
                .perform(click());

        Calendar instance = Calendar.getInstance();
        String gameName = "GameName_" + instance.getTimeInMillis();
        String gamePub = "GamePub_" + instance.getTimeInMillis();

        onView(withId(R.id.gameName))
                .perform(typeText(gameName));
        onView(withId(R.id.gamePublisher))
                .perform(typeText(gamePub));
        onView(withId(R.id.datePublishedEditText))
                .perform(click());
//        onView(withClassName(Matchers.equalTo(DatePickerDialog.class.getName()))).perform(.setDate(year, month + 1, day));
    }
}