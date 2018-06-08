package de.avkterwey.baking;

/*
 * Created by Berenice on 27.05.18.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.model.Step;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.is;


@RunWith(AndroidJUnit4.class)
public class RecipeParcelTest {

    @Test
    public void testRecipeIsParcelable(){
        Recipe recipe = new Recipe(
                10, // id
                "TestName",
                new ArrayList<Ingredient>(1),
                new ArrayList<Step>(1),
                4, // servings
                "imageUrl"
        );

        // access the Parcel pool
        Parcel parcel = Parcel.obtain();
        // write object to the parcel
        recipe.writeToParcel(parcel, recipe.describeContents());

        // move the parcel back to 0 with the setDataPosition method
        // (it accepts a value between 0 and the data size stored in the parcel)
        parcel.setDataPosition(0);

        // extract the object from the parcel with the specific creator of the model class
        Recipe createdFromParcel =
                Recipe.CREATOR.createFromParcel(parcel);

        // Are the properties retained through the parcel mechanism?
        assertThat(createdFromParcel.getName(), is("TestName"));
        assertThat(createdFromParcel.getServings(), is(4));
    }

}
