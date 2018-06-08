package de.avkterwey.baking.utils;

/*
 * Created by Berenice on 20.05.18.
 */

import android.content.Context;

import de.avkterwey.baking.AppExecutors;
import de.avkterwey.baking.data.MyRepository;
import de.avkterwey.baking.data.database.AppDatabase;
import de.avkterwey.baking.viewmodel.MainViewModelFactory;
import de.avkterwey.baking.viewmodel.StepsViewModelFactory;
import de.avkterwey.baking.viewmodel.SingleRecipeViewModelFactory;

public class InjectorUtil {

    public static MyRepository provideMyRepository(Context context){
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase db = AppDatabase.getInstance(context);
        return MyRepository.getInstance(db.recipeDao(), executors);
    }


    public static MainViewModelFactory provideMainViewModelFactory(Context context){
        MyRepository repository = provideMyRepository(context);
        return new MainViewModelFactory(repository);
    }

    public static StepsViewModelFactory provideDetailViewModelFactory(Context context){
        return new StepsViewModelFactory();
    }

    public static SingleRecipeViewModelFactory provideSinglerecipeViewModelFactory(Context context){
        return new SingleRecipeViewModelFactory(context);
    }


}
