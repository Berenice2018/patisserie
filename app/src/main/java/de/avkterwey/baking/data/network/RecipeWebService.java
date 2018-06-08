package de.avkterwey.baking.data.network;

/*
 * Created by Berenice on 19.05.18.
 */

import java.util.List;

import de.avkterwey.baking.data.database.RecipeEntry;
import de.avkterwey.baking.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeWebService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipesFromWeb();
}
