package de.avkterwey.baking.model;

/*
 * Created by Berenice on 18.05.18.
 */

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;



@Entity
public class Ingredient implements Parcelable {
    double quantity;
    String measure;

    @PrimaryKey
    @NonNull
    @SerializedName("ingredient")
    String ingredient;

    public Ingredient(double quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String name) {
        this.ingredient = name;
    }
}



/*
@Entity(foreignKeys = @ForeignKey(
        entity = RecipeEntry.class,
        parentColumns = "id",
        childColumns = "recipeId"
))
@TypeConverters(DataConverter.class)
public class Ingredient {


    @ColumnInfo(name = "recipeId")
    private long recipeId;

    private float quantity;

    //private Measure measure;

    @NonNull
    @PrimaryKey
    @SerializedName("ingredient")
    private String ingredient;




// Getter & Setter
    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }


    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
*/