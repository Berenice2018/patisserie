package de.avkterwey.baking.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import de.avkterwey.baking.R;
import de.avkterwey.baking.databinding.RecipeListFragmentBinding;
import de.avkterwey.baking.model.IMyItem;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.utils.ConnectivityUtil;
import de.avkterwey.baking.utils.InjectorUtil;
import de.avkterwey.baking.viewmodel.MainViewModel;
import de.avkterwey.baking.viewmodel.MainViewModelFactory;

/*
 * Created by Berenice on 21.05.18.
 */

public class RecipeListFragment extends Fragment  {

    private static final String TAG = RecipeListFragment.class.getSimpleName();
    private MainViewModel mViewModel;
    private RecipeListAdapter mAdapter;
    private RecipeListFragmentBinding mBinding;
    private IRecipeItemListener mCallback;
    private int mSpanCount;

    public RecipeListFragment(){}

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try
        {
            mCallback = (IRecipeItemListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement IRecipeItemListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, IMyItem item) {
                Recipe recipe = (Recipe) item;
                Log.d(TAG, "recipe clicked = " + recipe.getName());
                mCallback.onItemClick(recipe);
            }
        };

        mAdapter = new RecipeListAdapter(listener);


        // ViewModel
        MainViewModelFactory factory = InjectorUtil.provideMainViewModelFactory(
                this.getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(MainViewModel.class);
        mViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipeEntryList) {

                for(int i=0; i< recipeEntryList.size(); ++i){
                    long recipeId = recipeEntryList.get(i).getId();
                    Log.d(TAG, "recipe id = " + recipeId );
                }

                if(mAdapter == null)
                    return;

                mAdapter.setItemList(recipeEntryList);

                // TODO start SingleRecipeActivity, if widget was clicked
                String recipeNameFromWidget = mViewModel.getRecipeFromWidget();
                Log.d(TAG, "*** recipeNameFromWidget =  " + recipeNameFromWidget);
                if(!TextUtils.isEmpty(recipeNameFromWidget)){
                    for (Recipe recipe : recipeEntryList) {
                        if (recipe.getName().equals(recipeNameFromWidget)) {
                            mCallback.onItemClick(recipe);
                            break;
                        }
                    }
                }
            }
        });

        if(mAdapter.getItemCount() == 0 && !ConnectivityUtil.isConnected(getContext()))
            Toast.makeText(getContext(), "You are not connected.\nSwipe to load recipes.", Toast.LENGTH_LONG).show();
    }


    // inflate the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        mBinding = RecipeListFragmentBinding.inflate(inflater, container, false);

        int numberOfCols = getResources().getInteger(R.integer.num_of_cols);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                numberOfCols);
        mBinding.recipeList.setLayoutManager(gridLayoutManager);
        mBinding.recipeList.setAdapter(mAdapter);

        return mBinding.getRoot();
    }


    public void loadRecipes() {
        // no items in adapter, then load
        if(mAdapter.getItemCount() == 0)
            mViewModel.loadRecipes();
    }

}
