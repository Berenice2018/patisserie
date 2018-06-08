package de.avkterwey.baking.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import de.avkterwey.baking.data.MyRepository;
import de.avkterwey.baking.data.database.RecipeEntry;
import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Recipe;

/*
 * Created by Berenice on 18.05.18.
 */

public  class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    //private final AppDatabase appDb;
    private final MyRepository mRepository;
    private final LiveData<List<Recipe>> recipes;
    private MutableLiveData<String> recipeFromWidget = new MutableLiveData<>();


    public MainViewModel(MyRepository repo) {
        mRepository = repo;
        recipes = repo.getListOfAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes(){
        return recipes;
    }

   /* public List<Ingredient> getIngredientsListOfSingleRecipe(String recipeId){
        return mRepository.getIngredients(recipeId);
    }*/

   public void loadRecipes(){
       mRepository.postAndInsertFetchedData();
   }


    public String getRecipeFromWidget() {
        return recipeFromWidget.getValue();
    }

    public void setRecipeFromWidget(String recipeNameFromWidget) {
        Log.d(TAG, "set recipe name from widget =" + recipeNameFromWidget);
        recipeFromWidget.setValue(recipeNameFromWidget);
    }
}
