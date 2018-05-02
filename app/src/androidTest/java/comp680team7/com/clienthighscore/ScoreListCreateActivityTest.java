package comp680team7.com.clienthighscore;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import comp680team7.com.clienthighscore.models.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScoreListCreateActivityTest {

    @Rule
    public ActivityTestRule<ScoreListActivity> activityTestRule =
            new ActivityTestRule<>(ScoreListActivity.class, true, false);
    //{
//        @Override
//        public Activity launchActivity(@Nullable Intent startIntent) {
//            Intent i = new Intent();
//            i.putExtra(String.valueOf(R.string.score_list_game_id_arg), 1);
//            MainActivity.CURRENT_ACTIVE_USER = new User(1, "testing", "supertesting");
//            return super.launchActivity(i);
//        }
//    };

//    @Rule
//    public ActivityTestRule<ScoreCreateActivity> createScoreTestRule = new ActivityTestRule<>(ScoreCreateActivity.class);

//    @Before
//    public void testLaunch() {
////        Context targetContext = InstrumentationRegistry.getInstrumentation()
////                .getTargetContext();
////        Intent intent = new Intent(targetContext, ScoreListActivity.class);
//        Intent intent = new Intent();
//        intent.putExtra(String.valueOf(R.string.score_list_game_id_arg), 1);
//
//        MainActivity.CURRENT_ACTIVE_USER = new User(1, "testing", "supertesting");
//
//        activityTestRule.launchActivity(intent);
//    }

    @Test
    public void addScoreTest() {

        Intent intent = new Intent();
        intent.putExtra(String.valueOf(R.string.score_list_game_id_arg), 1);

        MainActivity.CURRENT_ACTIVE_USER = new User(1, "testing", "supertesting");

        activityTestRule.launchActivity(intent);
        onView(withId(R.id.newScoreFAB))
                .perform(click());


        onView(withId(R.id.selectImageButton)).perform(click());
        try {
            Method startOCR = activityTestRule.getActivity().getClass().getMethod("startOCR");
            startOCR.setAccessible(true);
            URL url = new URL("https://i.imgur.com/PEbBKYt.jpg");
            startOCR.invoke(activityTestRule.getActivity(), BitmapFactory.decodeStream(url.openConnection().getInputStream()));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.newScore)).check(matches(not(withText(""))));
        onView(withId(R.id.save)).perform(click());


//        onView(withText("Gallery")).perform(click());
//        onView(withId)
//        activityTestRule.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                activityTestRule.
//            }
//        });
//        onView(withId(R.id.gameName))
//                .perform(typeText(gameName));
//        onView(withId(R.id.gamePublisher))
//                .perform(typeText(gamePub));
//        onView(withId(R.id.datePublishedEditText))
//                .perform(click());
//        onView(withClassName(equalTo(DatePicker.class
//                .getName()))).perform(PickerActions.setDate(2017, 1, 2));
//        onView(withText("OK")).perform(click());
//        onView(withId(R.id.save)).perform(click());
//
//
//        RecyclerView gameListView = activityTestRule.getActivity().findViewById(R.id.gameListView);
//        int itemCount = gameListView.getAdapter().getItemCount();
//        onView(withId(R.id.gameListView)) //scroll to last added item; kind of bad, but it works
//                .perform(RecyclerViewActions.scrollToPosition(itemCount-1));
    }
}