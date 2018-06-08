package de.avkterwey.baking.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/*
 * Created by Berenice on 18.05.18.
 */

public class StepsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    //private final AppDatabase mDb;
    //private final long mRecipeId;


    public StepsViewModelFactory(){}

    /*public StepsViewModelFactory(AppDatabase db, long recipeId) {
        mDb = db;
        mRecipeId = recipeId;
    }*/


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //return super.create(modelClass);
        return (T) new StepsViewModel();
    }
}
