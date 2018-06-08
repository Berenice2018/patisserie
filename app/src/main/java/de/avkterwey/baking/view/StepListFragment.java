package de.avkterwey.baking.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.avkterwey.baking.databinding.StepsListFragmentBinding;
import de.avkterwey.baking.model.IMyItem;
import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Step;
import de.avkterwey.baking.viewmodel.SingleRecipeViewModel;
import de.avkterwey.baking.viewmodel.StepsViewModel;

/*
 * Created by Berenice on 21.05.18.
 */

public class StepListFragment extends Fragment {

    private static final String TAG = StepListFragment.class.getSimpleName();
    private StepsListFragmentBinding mBinding;
    private StepListAdapter mAdapter;
    private IRecipeItemListener mCallback;
    private StepsViewModel mStepsViewModel;
    private SingleRecipeViewModel mRecipeViewModel;

    private IngredientsAdapter mIngredientsAdapter;



    public StepListFragment(){}

    private static StepListFragment sInstance;

    public static StepListFragment getInstance() {
        if (sInstance == null) {
            sInstance = new StepListFragment();
        }
        return sInstance;
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mCallback = (IRecipeItemListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ " must implement IRecipeItemListener");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        IRecyclerViewClickListener listener = new IRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, IMyItem item) {
                Step step = (Step) item;
                setCurrentStep(step);
                mCallback.onItemClick(step);
            }
        };


        mRecipeViewModel = ViewModelProviders.of(getActivity()).get(SingleRecipeViewModel.class);
        //Log.d(TAG, "## number of ingredients " + mRecipeViewModel.getIngredients().size());
         mAdapter = new StepListAdapter(mRecipeViewModel.getSteps(), listener);
        mIngredientsAdapter = new IngredientsAdapter(mRecipeViewModel.getIngredients());

        mStepsViewModel = ViewModelProviders.of(getActivity()).get(StepsViewModel.class);

        /*mStepsViewModel.getCurrentStepLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer stepNumber) {
                if (mRecipeViewModel.getStepsLiveData() == null)
                    return;

                //resetCurrentStep(stepNumber);

                mAdapter.setItemList(mRecipeViewModel.getStepsLiveData());
            }
        });*/



        mRecipeViewModel.getStepsLiveData().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                if (mAdapter == null)
                    return;
                mAdapter.setItemList(steps);
            }
        });

        mRecipeViewModel.getIngredientsLiveData().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                mIngredientsAdapter.setItemList(ingredients);
            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState){
        mBinding = StepsListFragmentBinding.inflate(inflator, container, false);

        int servings = mRecipeViewModel.getRecipe().getServings();
        mBinding.servings.setText(String.valueOf(servings) + " servings");
        mBinding.stepList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.stepList.setAdapter(mAdapter);

        mBinding.ingredientsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.ingredientsList.setAdapter(mIngredientsAdapter);
       /* mBinding.ingredientsList.addItemDecoration(new DividerItemDecoration(mBinding.ingredientsList.getContext(),
                LinearLayoutManager.VERTICAL));*/

        return mBinding.getRoot();
    }



    private void setCurrentStep(Step step) {
        int position = 0;
        if (step != null) {
            position = mRecipeViewModel.getSteps().indexOf(step);
        }

        mStepsViewModel.setCurrentStep(position);
    }



    private void resetCurrentStep(int position) {
        for (Step s : mRecipeViewModel.getSteps()) {
            s.setSelected(false);
        }
        mRecipeViewModel.getSteps().get(position).setSelected(true);
        mAdapter.notifyDataSetChanged();
    }
}
