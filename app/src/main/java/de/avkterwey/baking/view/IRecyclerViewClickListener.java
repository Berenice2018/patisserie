package de.avkterwey.baking.view;

import android.view.View;

import de.avkterwey.baking.model.IMyItem;
import de.avkterwey.baking.model.Recipe;

/*
 * Created by Berenice on 21.05.18.
 */


public interface IRecyclerViewClickListener {
        void onClick(View view, int position, IMyItem item);
}


