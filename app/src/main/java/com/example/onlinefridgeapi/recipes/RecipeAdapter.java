package com.example.onlinefridgeapi.recipes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinefridgeapi.R;
import com.example.onlinefridgeapi.models.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final Activity context;
    private final List<Recipe> recipes;

    public RecipeAdapter(Activity context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemList = inflater.inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(itemList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe r = recipes.get(position);
        holder.recipeName.setText(r.getName());
        holder.recipeDescription.setText(String.valueOf(r.getDescription()));
        holder.showPrepTime.setText(r.getPrepTime() + " min");
        holder.showFoodCategory.setText(String.valueOf(r.getFoodCategory()));
        holder.showCookTime.setText(r.getCookTime() + " min");

        holder.recipeCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("recipeID", r.getId());
            context.startActivity(intent);
            context.finish();
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    protected static class RecipeViewHolder extends RecyclerView.ViewHolder {
        protected CardView recipeCard;
        protected TextView recipeName, recipeDescription, showPrepTime, showFoodCategory, showCookTime;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeCard = itemView.findViewById(R.id.recipeCard);
            recipeName = itemView.findViewById(R.id.recipeName);
            recipeDescription = itemView.findViewById(R.id.recipeDescription);
            showPrepTime = itemView.findViewById(R.id.showPrepTime);
            showFoodCategory = itemView.findViewById(R.id.showFoodCategory);
            showCookTime = itemView.findViewById(R.id.showCookTime);
        }
    }
}