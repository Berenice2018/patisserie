package de.avkterwey.baking.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.util.List;

import de.avkterwey.baking.MyConstants;
import de.avkterwey.baking.R;
import de.avkterwey.baking.model.IMyItem;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.model.Step;
import de.avkterwey.baking.utils.InjectorUtil;
import de.avkterwey.baking.viewmodel.MainViewModelFactory;
import de.avkterwey.baking.viewmodel.SingleRecipeViewModelFactory;
import de.avkterwey.baking.viewmodel.SingleRecipeViewModel;
import de.avkterwey.baking.viewmodel.StepsViewModel;

import static de.avkterwey.baking.MyConstants.EXTRA_RECIPE;

/*
 * Created by Berenice on 21.05.18.
 */


public class SingleRecipeActivity extends AppCompatActivity implements IRecipeItemListener {

    private static final String TAG = "SingleRecipeActivity";
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    private Recipe mRecipe;
    private SingleRecipeViewModel mRecipeViewModel;
    private StepsViewModel mStepsViewModel;
    private StepListFragment mStepListFragment;
    private InstructionsFragment mInstructionsFragment;
    private int mFragmentId;
    private boolean isTablet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (!getIntent().hasExtra(EXTRA_RECIPE))
            finish();

        isTablet = getResources().getBoolean(R.bool.isTablet);

        // Bundle data
        Bundle bundleData = getIntent().getExtras();
        if (bundleData != null) {
            mRecipe = bundleData.getParcelable(MyConstants.EXTRA_RECIPE);
        }

        setTitle((CharSequence) mRecipe.getName());

        SingleRecipeViewModelFactory factory = InjectorUtil.provideSinglerecipeViewModelFactory(
                this);
        mRecipeViewModel = ViewModelProviders.of(this, factory).get(SingleRecipeViewModel.class);
        mRecipeViewModel.setRecipe(mRecipe);

        mStepsViewModel = ViewModelProviders.of(this).get(StepsViewModel.class);
        List<Step> currentStepList = mRecipeViewModel.getSteps();
        mStepsViewModel.setStepsLiveData(currentStepList);


        // TODO add step list fragment
        if(mStepListFragment == null){
            mStepListFragment = StepListFragment.getInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ingredientAndStepContainer,
                            mStepListFragment,
                            "step_fragment_tag")
                    .commit();
        } else {
            mStepListFragment = (StepListFragment) getSupportFragmentManager()
                    .findFragmentByTag("step_fragment_tag");
        }

    }


    // overlay the instruction fragment with the exoplayer.
    // see also https://medium.com/@bherbst/managing-the-fragment-back-stack-373e87e4ff62

    @Override
    public void onItemClick(IMyItem bakingItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, 0);
        Log.d(TAG, "######  Step clicked");


        // Add the new tab fragment
        if(mInstructionsFragment == null){
            mInstructionsFragment = InstructionsFragment.getInstance();
            mFragmentId = fragmentManager.beginTransaction()
                    .replace(R.id.instructionsContainer,
                            mInstructionsFragment,
                            "instr_fragment_tag")
                    .addToBackStack(BACK_STACK_ROOT_TAG)
                    .commit();
        } else {
            Log.d(TAG, "now in Fragment; ELSE");
            mInstructionsFragment = (InstructionsFragment) fragmentManager
                    .findFragmentByTag("instr_fragment_tag");
        }
    }



    @Override
    public void onBackPressed() {
        if(isTablet){
            finish();
        }
        else{
            int fragCount = getFragmentManager().getBackStackEntryCount();
            Log.d(TAG, "number of fragments = " + fragCount);
            if (fragCount == 0) {
                super.onBackPressed();
            } else {
                //getFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, 0);
                getFragmentManager().popBackStack(mFragmentId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

    }

}
