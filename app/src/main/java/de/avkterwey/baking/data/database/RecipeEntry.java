package de.avkterwey.baking.data.database;

/*
 * Created by Berenice on 18.05.18.
 */


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import de.avkterwey.baking.model.Ingredient;





@Entity (tableName = "RECIPE")
public class RecipeEntry {

    @NonNull
    @PrimaryKey()
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("servings")
    private int servings;

    @SerializedName("imageUrl")
    private String imageUrl;

    /*/@TypeConverters(ConverterUtil.class)
    @SerializedName("ingredientsList")
    @Expose
    public  List<Ingredient> ingredientsList;
*/

// Getter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    // Setter
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
