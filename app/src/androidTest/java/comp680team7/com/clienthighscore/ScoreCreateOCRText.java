package comp680team7.com.clienthighscore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import comp680team7.com.clienthighscore.models.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScoreCreateOCRText {

    @Rule
    public ActivityTestRule<ScoreCreateActivity> activityTestRule =
            new ActivityTestRule<>(ScoreCreateActivity.class, true, false);

    @Test
    public void addScoreTest() {

        MainActivity.CURRENT_ACTIVE_USER = new User(1, "testing", "supertesting");
        Intent intent = new Intent();
        intent.putExtra(ScoreCreateActivity.GAME_ID, 1);
        intent.putExtra(ScoreCreateActivity.USER_ID, 1);

        activityTestRule.launchActivity(intent);

        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    Method startOCR = activityTestRule.getActivity().getClass().getDeclaredMethod("startOCR", Bitmap.class);
                    startOCR.setAccessible(true);
                    startOCR.invoke(activityTestRule.getActivity(), BitmapFactory.decodeResource(activityTestRule.getActivity().getResources(), R.mipmap.score_image));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.newScore)).check(matches(withText("122430")));
    }
}