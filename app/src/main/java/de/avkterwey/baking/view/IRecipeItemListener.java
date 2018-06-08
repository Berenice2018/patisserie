package de.avkterwey.baking.view;

/*
 * Created by Berenice on 21.05.18.
 */

import de.avkterwey.baking.model.IMyItem;
import de.avkterwey.baking.model.Recipe;

public interface IRecipeItemListener {
    void onItemClick(IMyItem bakingItem);
}
