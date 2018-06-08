package de.avkterwey.baking;

/*
 * Created by Berenice on 27.05.18.
 */

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.view.MainActivity;
import de.avkterwey.baking.view.SingleRecipeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
public class SingleRecipeLaunchedTest {

    @Rule
    public final IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class, true, true);


    @Test
    public void ensureIntentStarted() throws InterruptedException {
        onView(withId(R.id.recipeList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(SingleRecipeActivity.class.getName()));
    }



}