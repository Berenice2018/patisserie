package de.avkterwey.baking.utils;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Step;

/*
 * Created by Berenice on 26.05.18.
 */

public class ConverterUtil {

    @TypeConverter
    public static String getJsonIngredients(List<Ingredient> ingredients) {
        Type listOfRecipeIngredient = new TypeToken<List<Ingredient>>() {
        }.getType();
        String gsonString = new Gson().toJson(ingredients, listOfRecipeIngredient);
        //Log.d("Converter", "gson String = " + gsonString);
        return gsonString;
    }

    @TypeConverter
    public static List<Ingredient> getIngredientsFromJson(String jsonIngredients) {
        Type listOfRecipeIngredient = new TypeToken<List<Ingredient>>() {
        }.getType();
        return new Gson().fromJson(jsonIngredients, listOfRecipeIngredient);
    }


    @TypeConverter
    public static String getJsonSteps(List<Step> steps){
        Type listOfSteps = new TypeToken<List<Step>>(){}.getType();
        return new Gson().toJson(steps, listOfSteps);
    }

    @TypeConverter
    public static List<Step> getStepFromJson(String jsonSteps){
        Type listOfSteps = new TypeToken<List<Step>>(){}.getType();
        return new Gson().fromJson(jsonSteps, listOfSteps);
    }
}
