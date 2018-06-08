package de.avkterwey.baking.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;

import de.avkterwey.baking.data.MyRepository;

/*
 * Created by Berenice on 20.05.18.
 */

public class SingleRecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context mContext;

    public SingleRecipeViewModelFactory(Context context){
        mContext = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new SingleRecipeViewModel(mContext);
    }

}
