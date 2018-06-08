package de.avkterwey.baking.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/*
 * Created by Berenice on 25.05.18.
 */

public class IngredientsRemoteViewService extends RemoteViewsService {

    /**
     * To be implemented by the derived service to generate appropriate factories for
     * the data.
     *
     * @param intent
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientRemoteViewFactory(getApplicationContext(),
                intent);
    }
}
