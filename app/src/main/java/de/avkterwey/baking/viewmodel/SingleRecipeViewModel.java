package de.avkterwey.baking.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.model.Step;

/*
 * Created by Berenice on 22.05.18.
 */

public class SingleRecipeViewModel extends ViewModel{
    private MutableLiveData<List<Ingredient>> ingredients;
    private MutableLiveData<List<Step>> steps;
    private MutableLiveData<Recipe> recipe;

    public SingleRecipeViewModel(Context mContext) {
        this.ingredients = new MutableLiveData<>();
        this.ingredients.setValue(new ArrayList<Ingredient>());
        this.steps = new MutableLiveData<>();
        this.steps.setValue(new ArrayList<Step>());
        this.recipe = new MutableLiveData<>();
    }

    public MutableLiveData<List<Ingredient>> getIngredientsLiveData() {
        return ingredients;
    }

    public MutableLiveData<List<Step>> getStepsLiveData() {
        return steps;
    }

    public MutableLiveData<Recipe> getRecipeLiveData() {
        return recipe;
    }

    public List<Ingredient> getIngredients() {
        return ingredients.getValue();
    }

    public List<Step> getSteps() {
        return steps.getValue();
    }

    public Recipe getRecipe() {
        return recipe.getValue();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe.setValue(recipe);
        this.steps.setValue(recipe.getSteps());
        this.ingredients.setValue(recipe.getIngredients());
    }
}
