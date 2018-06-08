package de.avkterwey.baking.data;

/*
 * Created by Berenice on 19.05.18.
 *
 * Repository responsible for handling data operations.
 * knows where to get the data from and what API calls to make when data is updated.
 * Like a mediator between different data sources (persistent model, web service, etc.)
 *
 */

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import de.avkterwey.baking.AppExecutors;
import de.avkterwey.baking.MyConstants;
import de.avkterwey.baking.data.database.IRecipeDao;
import de.avkterwey.baking.data.database.RecipeEntry;
import de.avkterwey.baking.data.network.RecipeWebService;
import de.avkterwey.baking.model.Ingredient;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.utils.ConnectivityUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRepository {

    private static final String TAG = MyRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static  MyRepository sInstance;
    private final IRecipeDao mRecipeDao;
    private final RecipeWebService mRecipeWebservice;
    private final AppExecutors mExecutors;
    private final List<Recipe> mList = new ArrayList<>();
    private MutableLiveData<List<Recipe>> mAllRecipesLiveData = new MutableLiveData<List<Recipe>>();


    public MyRepository(IRecipeDao recipeDao, AppExecutors executors) {
        mRecipeDao = recipeDao;
        mExecutors = executors;
        mRecipeWebservice = fetchRecipesFromWeb();

        postAndInsertFetchedData();
    }


    public static synchronized MyRepository getInstance(IRecipeDao recipeDao, AppExecutors executors){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new MyRepository(recipeDao, executors);
                Log.d(TAG, "Made new repository");
            }
        }

        return sInstance;
    }

    public static MyRepository getInstance(){
            return sInstance;
    }


    public LiveData<List<Recipe>> getListOfAllRecipes(){
        //return mAllRecipesLiveData;
        return mRecipeDao.loadAllRecipes();
    }


    public LiveData<Recipe> getRecipeByName(String recipeName){
        return mRecipeDao.loadRecipeByName(recipeName);
    }


    private static RecipeWebService fetchRecipesFromWeb(){
        Gson gson = new GsonBuilder().create();
        Retrofit  retrofit = new Retrofit.Builder()
                .baseUrl(MyConstants.RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(RecipeWebService.class);
    }


    public void postAndInsertFetchedData(){

        if(mRecipeWebservice == null)
            fetchRecipesFromWeb();

        Call<List<Recipe>> loadRecipeCall = mRecipeWebservice.getRecipesFromWeb();
        loadRecipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mList.clear();

                for(Recipe recipe: response.body()) {
                    mList.add(recipe);
                }

                mAllRecipesLiveData.postValue(mList);

                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mRecipeDao.deleteOldRecipes();
                        Log.d(TAG, "Old data deleted");
                        // insert Dao
                        mRecipeDao.bulkInsert(mList);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

}
