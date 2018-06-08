package de.avkterwey.baking.data.database;

/*
 * Created by Berenice on 18.05.18.
 */

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Recipe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
public interface IRecipeDao {

    @Insert(onConflict = REPLACE)
    void saveRecipe(Recipe recipe);

    // insert a list of entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Recipe> recipe);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(Recipe recipeEntry);

    @Query("SELECT * FROM RECIPE WHERE name  LIKE :s")
    LiveData<Recipe> loadRecipeByName(String s);

    @Query("SELECT * FROM RECIPE")
    LiveData<List<Recipe>> loadAllRecipes();

    @Query("DELETE FROM RECIPE")
    void deleteOldRecipes();

  /*  @Query("SELECT * FROM ingredient WHERE recipeId IS :id")
    List<Ingredient> getIngredientsForRecipe(String recipeId);
*/

}
