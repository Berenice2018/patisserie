package de.avkterwey.baking.viewmodel;

/*
 * Created by Berenice on 22.05.18.
 */


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.ArrayMap;

import java.util.Map;
import java.util.concurrent.Callable;

import de.avkterwey.baking.data.MyRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private  MyRepository mRepo;

    public MainViewModelFactory(MyRepository repo){
        mRepo = repo;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new MainViewModel(mRepo);
    }


}