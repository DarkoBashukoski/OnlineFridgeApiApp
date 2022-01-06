package com.example.onlinefridgeapi.recipes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinefridgeapi.MainActivity;
import com.example.onlinefridgeapi.R;
import com.example.onlinefridgeapi.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecipeDetailsActivity extends AppCompatActivity {
    private Toolbar recipeDetailsToolbar;
    private TextView recipeDescriptionDetails, prepTimeDetails, foodCategoryDetails, cookTimeDetails;
    private LinearLayout detailsIngredientsLayout, detailsStepsLayout;

    private static final String url = "https://onlinefridge.azurewebsites.net/api/Recipe/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        referenceViews();
        fillData();
        setButtonListeners();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void referenceViews() {
        recipeDetailsToolbar = findViewById(R.id.recipeDetailsToolbar);
        recipeDescriptionDetails = findViewById(R.id.recipeDescriptionDetails);
        prepTimeDetails = findViewById(R.id.prepTimeDetails);
        foodCategoryDetails = findViewById(R.id.foodCategoryDetails);
        cookTimeDetails = findViewById(R.id.cookTimeDetails);
        detailsIngredientsLayout = findViewById(R.id.detailsIngredientsLayout);
        detailsStepsLayout = findViewById(R.id.detailsStepsLayout);
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    private void fillData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(url + getIntent().getExtras().getInt("recipeID"),
        response -> {
            try {
                recipeDetailsToolbar.setTitle(response.getString("recipeName"));
                recipeDescriptionDetails.setText(response.getString("recipeDesc"));
                prepTimeDetails.setText(response.getInt("prepTime") + " min");
                foodCategoryDetails.setText(Recipe.foodCategoryFromInt(response.getInt("foodCategory")));
                cookTimeDetails.setText(response.getInt("cookTime") + " min");

                JSONArray steps = response.getJSONArray("steps");
                for (int i=0; i<steps.length(); i++) {
                    JSONObject step = steps.getJSONObject(i);
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.details_text_view, null);
                    String text = String.format(Locale.getDefault(), "%d. %s", step.getInt("stepNumber"), step.getString("stepDescription"));
                    textView.setText(text);
                    detailsStepsLayout.addView(textView);
                }

                JSONArray ingredients = response.getJSONArray("quantities");
                for (int i=0; i<ingredients.length(); i++) {
                    JSONObject ing = ingredients.getJSONObject(i);
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.details_text_view, null);
                    String text = String.format(Locale.getDefault(), "• %s %s", trimDecimals(ing.getDouble("quantity")), ing.getJSONObject("ingredient").getString("ingredientName"));
                    textView.setText(text);
                    detailsIngredientsLayout.addView(textView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
        error -> Log.d("REST error", error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("ApiKey", "SecretKey");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    @SuppressLint("NonConstantResourceId")
    private void setButtonListeners() {
        recipeDetailsToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.deleteRecipe:
                    //TODO: add recipe delete
                case R.id.editRecipe:
                    Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                    return true;
                //TODO: add recipe edit
                default:
                    return false;
            }
        });
    }

    public static String trimDecimals(double d) {
        if (d == (long) d) {return String.format(Locale.getDefault(), "%d",(long) d);}
        else {return String.format("%s", d);}
    }
}