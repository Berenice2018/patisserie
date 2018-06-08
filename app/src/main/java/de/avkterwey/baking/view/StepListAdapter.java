package de.avkterwey.baking.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.avkterwey.baking.R;
import de.avkterwey.baking.databinding.RecipeBinding;
import de.avkterwey.baking.databinding.StepBinding;
import de.avkterwey.baking.model.IMyItem;
import de.avkterwey.baking.model.Recipe;
import de.avkterwey.baking.model.Step;

import static android.view.LayoutInflater.from;

/*
 * Created by Berenice on 21.05.18.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.ItemViewHolder> {
    private final static String TAG = "StepListAdapter";
    private List<Step> mItemList = new ArrayList<>();
    private final IRecyclerViewClickListener mRecyclerViewClickListener;


    public StepListAdapter(List<Step> items, IRecyclerViewClickListener recyclerViewClickListener) {
        //mItemList = items;
        mItemList.clear();
        mItemList.addAll(items);
        mRecyclerViewClickListener = recyclerViewClickListener;
    }

    public StepListAdapter(IRecyclerViewClickListener recyclerViewClickListener) {
        mRecyclerViewClickListener = recyclerViewClickListener;
    }



    public void setItemList(final List<Step> itemList){
        //mItemList = itemList;
        mItemList.clear();
        mItemList.addAll(itemList);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public StepListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        StepBinding binding = DataBindingUtil.inflate(
                from(parent.getContext()),
                R.layout.step,
                parent,
                false
        );
        return new StepListAdapter.ItemViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull StepListAdapter.ItemViewHolder holder, int position) {
        Step item =  mItemList.get(position);
        holder.binding.singleStepShortDescription.setText(item.getShortDescription());
        holder.binding.stepNumber.setText(String.valueOf(position));
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


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final StepBinding binding;

        public ItemViewHolder(StepBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.singleStepShortDescription.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Step item =  mItemList.get(pos);
            mRecyclerViewClickListener.onClick(v, pos, item);
        }
    }
}
