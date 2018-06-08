package de.avkterwey.baking.view;

/*
 * Created by Berenice on 24.05.18.
 */

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.avkterwey.baking.R;
import de.avkterwey.baking.databinding.IngredientBinding;
import de.avkterwey.baking.model.Ingredient;

import static android.view.LayoutInflater.from;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ItemViewHolder> {
    private final static String TAG = "IngredientsAdapter";
    private List<Ingredient> mItemList = new ArrayList<>();


    public IngredientsAdapter(List<Ingredient> items ) {
        mItemList.clear();
        mItemList.addAll(items);
    }


    public void setItemList(final List<Ingredient> itemList){
        mItemList.clear();
        mItemList.addAll(itemList);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public IngredientsAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        IngredientBinding binding = DataBindingUtil.inflate(
                from(parent.getContext()),
                R.layout.ingredient,
                parent,
                false
        );
        return new IngredientsAdapter.ItemViewHolder(binding);
    }



    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Ingredient item =  mItemList.get(position);
        holder.binding.quantity.setText(String.valueOf(item.getQuantity()));
        holder.binding.measure.setText(item.getMeasure());
        holder.binding.ingredientName.setText(item.getIngredient());
    }




    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final IngredientBinding binding;

        public ItemViewHolder(IngredientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
