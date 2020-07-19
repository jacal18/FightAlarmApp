package com.fightalarm.fightalarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fightalarm.fightalarm.R;
import com.fightalarm.fightalarm.activity.HomeActivity;
import com.fightalarm.fightalarm.interfaces.CategoryClick;
import com.fightalarm.fightalarm.models.Category;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.Setup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryFilterAdapter extends  RecyclerView.Adapter<CategoryFilterAdapter.ViewHolder>{

    private ArrayList<Category> categories = new ArrayList<Category>();
    Context context;
    Extras extras = new Extras();

    CategoryClick callback;

    Integer selected;

    public CategoryFilterAdapter(Context context, ArrayList<Category> categories, CategoryClick listener) {
        this.categories = categories;
        this.context = context;
        this.callback = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView title;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card);
            title = (TextView) view.findViewById(R.id.title);
        }
    }


    @Override
    public CategoryFilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorieslayout, parent, false);

        return new CategoryFilterAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryFilterAdapter.ViewHolder holder, int position) {
        final Category category = this.categories.get(position);
        final Integer newposition = position;

        holder.title.setText(category.getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected != null && selected.equals(newposition)){
                    selected = null;
                } else {
                    selected = newposition;
                }
                setFilter(category);
            }
        });

        if(selected != null && position == selected){
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.darkerIcon));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.icon));
        }


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setFilter(Category category) {
        this.callback.onCategoryClick(category.getId());
        notifyDataSetChanged();
    }


}
