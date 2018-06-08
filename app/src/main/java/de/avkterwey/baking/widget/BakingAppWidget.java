package de.avkterwey.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import de.avkterwey.baking.MyConstants;
import de.avkterwey.baking.R;
import de.avkterwey.baking.data.MyRepository;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.view.MainActivity;
import de.avkterwey.baking.view.SingleRecipeActivity;

import static de.avkterwey.baking.MyConstants.EXTRA_WIDGET_CHOSEN_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    private static final String TAG = BakingAppWidget.class.getSimpleName();
    private static int[] mAppWidgetIds;


    public static void sendRefreshBroadcast(Context context){
        Log.d(TAG, "sendRefreshBroadcast() ###");

        if(mAppWidgetIds != null){
            for (int appWidgetId : mAppWidgetIds){
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.baking_app_widget);
                SharedPreferences prefs = context.getSharedPreferences(
                        MyConstants.EXTRA_WIDGET_INGREDIENTS,
                        Context.MODE_PRIVATE);
                String chosenRecipeName = prefs.getString(EXTRA_WIDGET_CHOSEN_RECIPE,
                        "Ingredients:");
                AppWidgetManager manager = AppWidgetManager.getInstance(context);

                remoteViews.setTextViewText(R.id.widgetRecipeName, chosenRecipeName);


                // TODO setOnClickPendingIntent: click widget, send recipe name to MainActivity
                // to open the respective SingleRecipeActivity
                Intent recipeIntent = new Intent(context, MainActivity.class);
                recipeIntent.putExtra(EXTRA_WIDGET_CHOSEN_RECIPE, chosenRecipeName);
                PendingIntent titlePendingIntent = PendingIntent
                        .getActivity(context, 0, recipeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.widgetRecipeName, titlePendingIntent);

                // Instruct the widget manager to update the widget
                manager.updateAppWidget(appWidgetId, remoteViews);
                
            }
        }


        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context,
                BakingAppWidget.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onReceive(final Context context, Intent intent){
        Log.d(TAG, "onReceive() ###");
        final String action = intent.getAction();
        if(action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            // refresh all widgets
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName compName = new ComponentName(context,
                    BakingAppWidget.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(compName),
                    R.id.baking_app_widget_listview);

        }

        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate() ###");
        mAppWidgetIds = appWidgetIds;

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.baking_app_widget);


            Intent intent = new Intent(context, IngredientsRemoteViewService.class);
            remoteViews.setRemoteAdapter(R.id.baking_app_widget_listview, intent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

