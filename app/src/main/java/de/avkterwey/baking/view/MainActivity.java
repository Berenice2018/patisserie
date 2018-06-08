package de.avkterwey.baking.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import de.avkterwey.baking.MyConstants;
import de.avkterwey.baking.R;
import de.avkterwey.baking.model.IMyItem;
import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.utils.ConnectivityUtil;
import de.avkterwey.baking.utils.ConverterUtil;
import de.avkterwey.baking.utils.InjectorUtil;
import de.avkterwey.baking.viewmodel.MainViewModel;
import de.avkterwey.baking.viewmodel.MainViewModelFactory;
import de.avkterwey.baking.widget.BakingAppWidget;

import static de.avkterwey.baking.MyConstants.EXTRA_RECIPE;
import static de.avkterwey.baking.MyConstants.EXTRA_WIDGET_CHOSEN_RECIPE;
import static de.avkterwey.baking.MyConstants.EXTRA_WIDGET_INGREDIENTS;
import static de.avkterwey.baking.MyConstants.KEY_INGREDIENT;


public class MainActivity extends AppCompatActivity implements IRecipeItemListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecipeListFragment mRecipeListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_WIDGET_CHOSEN_RECIPE)){
            Log.d(TAG, "onNewIntent(): recipe name extra = " + intent.getStringExtra(EXTRA_WIDGET_CHOSEN_RECIPE));

            MainViewModelFactory factory = InjectorUtil.provideMainViewModelFactory(this);
            MainViewModel viewModel = ViewModelProviders.of(this, factory)
                    .get(MainViewModel.class);
            viewModel.setRecipeFromWidget(intent.getStringExtra(EXTRA_WIDGET_CHOSEN_RECIPE));
        } else{
            Log.d(TAG, "onNewIntent(): intent is null or has no extra");
        }


        mRecipeListFragment = new RecipeListFragment();
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.recipeListContainer, mRecipeListFragment, "recipeFragment")
                .commit();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }


    @Override

    public void onItemClick(IMyItem item) {
        Recipe recipe = (Recipe) item;
        Log.d(TAG, "recipe in Callback = " + recipe.getName());

        // widget
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                EXTRA_WIDGET_INGREDIENTS,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(EXTRA_WIDGET_CHOSEN_RECIPE, recipe.getName());

        String jsonString = ConverterUtil.getJsonIngredients(recipe.getIngredients());
        editor.putString(KEY_INGREDIENT, jsonString);

        editor.commit();
        BakingAppWidget.sendRefreshBroadcast(this);


        // start Single Recipe Activity, pass the recipe
        Intent intent = new Intent(this, SingleRecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(TAG,"Action was DOWN");
                reloadData();
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    private void reloadData(){
        if(mRecipeListFragment != null && ConnectivityUtil.isConnected(this)){
            mRecipeListFragment.loadRecipes();
        }

    }
}
