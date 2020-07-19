package com.fightalarm.fightalarm.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fightalarm.fightalarm.R;

import com.fightalarm.fightalarm.activity.HomeActivity;
import com.fightalarm.fightalarm.fragment.EventFragment;
import com.fightalarm.fightalarm.helpers.DownloadImageHelper;
import com.fightalarm.fightalarm.models.Category;

import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.providers.Extras;
import com.fightalarm.fightalarm.providers.ScheduleLocalStore;
import com.fightalarm.fightalarm.providers.Setup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private ArrayList<Category> categories = new ArrayList<Category>();
    Context context;
    Extras extras = new Extras();

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.categories = categories;
        this.context = context;
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
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorieslayout, parent, false);

        return new CategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        final Category category = this.categories.get(position);

        holder.title.setText(category.getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void navigate(Category category) {
        Setup setup = new Setup();
        setup.setCategory(category);
        Intent i = new Intent(this.context, HomeActivity.class);
        i.putExtra("tabnumber", 1);
        i.putExtra("viewtype", "category");
        i.putExtra("category_id", category.getId());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.getApplicationContext().startActivity(i);
    }


}
