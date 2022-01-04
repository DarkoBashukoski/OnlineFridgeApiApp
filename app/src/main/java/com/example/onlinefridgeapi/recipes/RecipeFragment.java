package com.example.onlinefridgeapi.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinefridgeapi.R;
import com.example.onlinefridgeapi.models.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {
    private static final String URL = "https://onlinefridge.azurewebsites.net/api/Recipe";


    private RequestQueue requestQueue;
    private RecyclerView recipeRecyclerView;

    public RecipeFragment() {
        // Required empty public constructor
    }

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipe, container, false);
        recipeRecyclerView = root.findViewById(R.id.recipeRecyclerView);
        FloatingActionButton addRecipeButton = root.findViewById(R.id.addRecipeButton);

        addRecipeButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddRecipeActivity.class));
            requireActivity().finish();
        });

        requestQueue = Volley.newRequestQueue(requireActivity().getApplicationContext());
        getAllRecipes();
        return root;
    }

    private void getAllRecipes() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
        response -> {
            List<Recipe> recipes = new ArrayList<>();
            for (int i=0; i<response.length(); i++) {
                try {
                    JSONObject recipe = response.getJSONObject(i);
                    recipes.add(new Recipe(
                            recipe.getInt("recipeID"),
                            recipe.getString("applicationUserID"),
                            recipe.getString("recipeName"),
                            recipe.getString("recipeDesc"),
                            recipe.getInt("cookTime"),
                            recipe.getInt("prepTime"),
                            Recipe.foodCategoryFromInt(recipe.getInt("foodCategory"))
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            RecipeAdapter recipeAdapter = new RecipeAdapter(getActivity(), recipes);
            recipeRecyclerView.setHasFixedSize(true);
            recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recipeRecyclerView.setAdapter(recipeAdapter);
        },
        error -> Log.d("REST error", error.getMessage()));

        requestQueue.add(request);
    }
}