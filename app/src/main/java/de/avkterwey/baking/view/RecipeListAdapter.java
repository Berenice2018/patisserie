package de.avkterwey.baking.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.avkterwey.baking.MyConstants;
import de.avkterwey.baking.R;
import de.avkterwey.baking.databinding.RecipeBinding;
import de.avkterwey.baking.model.Recipe;

import static android.view.LayoutInflater.*;

/*
 * Created by Berenice on 21.05.18.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ItemViewHolder>  {

    private final static String TAG = "RecipeAdapter";
    private List<Recipe> mItemList;
    private final IRecyclerViewClickListener mRecyclerViewClickListener;


    public RecipeListAdapter(IRecyclerViewClickListener recyclerViewClickListener) {
        this.mRecyclerViewClickListener = recyclerViewClickListener;
    }



    public void setItemList(final List<Recipe> itemList){
        mItemList = itemList;

        notifyDataSetChanged();

        if(mItemList != null){
            for (int i=0; i < mItemList.size(); ++i){
                Recipe item = mItemList.get(i);
                Log.d(TAG, "item = " + item.getName());
            }
        }

    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeBinding binding = DataBindingUtil.inflate(
                from(parent.getContext()),
                R.layout.recipe,
                parent,
                false
        );
        return new ItemViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Recipe item =  mItemList.get(position);
        holder.binding.setObj(item);

        String thumbUrl;
        if(!TextUtils.isEmpty(item.getImage())){
            thumbUrl = item.getImage();
        Glide.with(holder.binding.thumb.getContext())
                .load(thumbUrl)
                .thumbnail(0.1f)
                .into(holder.binding.thumb
                );
        }
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

        private final RecipeBinding binding;

        public ItemViewHolder(RecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.thumb.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Recipe item =  mItemList.get(pos);
            mRecyclerViewClickListener.onClick(v, pos, item);
            Log.d("Adapter", " pos = " + pos);
        }
    }
}
