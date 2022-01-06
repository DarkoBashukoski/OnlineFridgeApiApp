package com.example.onlinefridgeapi.recipes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.onlinefridgeapi.MainActivity;
import com.example.onlinefridgeapi.R;
import com.example.onlinefridgeapi.exceptions.EmptyInputException;
import com.example.onlinefridgeapi.exceptions.InvalidIngredientException;
import com.example.onlinefridgeapi.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity {
    private static final String INGREDIENTS_URL = "https://onlinefridge.azurewebsites.net/api/Ingredient";
    private static final String RECIPE_URL = "https://onlinefridge.azurewebsites.net/api/Recipe";

    private Toolbar addRecipeToolbar;
    private Spinner inputFoodCategory;
    private EditText inputRecipeName, inputRecipeDescription, inputCookTime, inputPrepTime;
    private LinearLayout addIngredientLayout, addStepLayout;
    private Button addIngredientButton, addStepButton;

    private RequestQueue requestQueue;
    private HashMap<String, Integer> allIngredients;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        loadPage();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void loadRestOfPage() {
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, new ArrayList<>(allIngredients.keySet()));

        referenceViews();
        addButtonListeners();
    }

    private void referenceViews() {
        addRecipeToolbar = findViewById(R.id.addRecipeToolbar);
        inputFoodCategory = findViewById(R.id.inputFoodCategory);
        inputRecipeName = findViewById(R.id.inputRecipeName);
        inputRecipeDescription = findViewById(R.id.inputRecipeDescription);
        inputCookTime = findViewById(R.id.inputCookTime);
        inputPrepTime = findViewById(R.id.inputPrepTime);
        addIngredientLayout = findViewById(R.id.addIngredientLayout);
        addStepLayout = findViewById(R.id.addStepLayout);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        addStepButton = findViewById(R.id.addStepButton);
    }

    private void addButtonListeners() {
        addStepButton.setOnClickListener(v -> addStepLayout.addView(getStepRow()));
        addIngredientButton.setOnClickListener(v -> addIngredientLayout.addView(getIngredientRow()));
        addRecipeToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() != R.id.submitRecipe) {return false;}
            try {
                JSONObject recipe = getRecipeData();
                addRecipe(recipe);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } catch (EmptyInputException e) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return false;
            } catch (InvalidIngredientException e) {
                Toast.makeText(this, "Please select ingredients from the dropdown menu", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @SuppressLint("InflateParams")
    private View getStepRow() {
        View row = getLayoutInflater().inflate(R.layout.step_row, null);
        AppCompatImageButton deleteStepButton = row.findViewById(R.id.deleteStepButton);
        deleteStepButton.setOnClickListener(v -> addStepLayout.removeView((View) v.getParent()));
        return row;
    }

    @SuppressLint("InflateParams")
    private View getIngredientRow() {
        View row = getLayoutInflater().inflate(R.layout.ingredient_row, null);
        AppCompatImageButton deleteIngredientButton = row.findViewById(R.id.deleteIngredientButton);
        deleteIngredientButton.setOnClickListener(v -> addIngredientLayout.removeView((View) v.getParent()));
        AutoCompleteTextView ingredientNameInput = row.findViewById(R.id.ingredientNameInput);
        ingredientNameInput.setAdapter(adapter);
        ingredientNameInput.setThreshold(1);
        return row;
    }

    private JSONObject getRecipeData() throws EmptyInputException, InvalidIngredientException {
        try {
            JSONObject output = new JSONObject();
            output.put("applicationUserID", null);
            output.put("foodCategory", Recipe.intFromFoodCategory(inputFoodCategory.getSelectedItem().toString()));
            output.put("recipeName", checkForEmptyInput(inputRecipeName.getText().toString()));
            output.put("recipeDesc", checkForEmptyInput(inputRecipeDescription.getText().toString()));
            output.put("prepTime", checkForEmptyInput(inputPrepTime.getText().toString()));
            output.put("cookTime", checkForEmptyInput(inputCookTime.getText().toString()));

            JSONArray quantities = new JSONArray();
            for (int i=0; i<addIngredientLayout.getChildCount(); i++) {
                EditText ingredientNameInput = addIngredientLayout.getChildAt(i).findViewById(R.id.ingredientNameInput);
                String ingredientName = checkForEmptyInput(ingredientNameInput.getText().toString());
                if (!allIngredients.containsKey(ingredientName)) {
                    throw new InvalidIngredientException("Invalid Ingredient");
                }
                EditText ingredientQuantityInput = addIngredientLayout.getChildAt(i).findViewById(R.id.ingredientQuantityInput);
                JSONObject q = new JSONObject();
                q.put("quantity", Float.parseFloat(checkForEmptyInput(ingredientQuantityInput.getText().toString())));

                JSONObject ingredient = new JSONObject();
                ingredient.put("ingredientID", allIngredients.get(ingredientName));
                ingredient.put("ingredientName", ingredientName);
                q.put("ingredient", ingredient);

                quantities.put(q);
            }

            JSONArray steps = new JSONArray();
            for (int i=0; i<addStepLayout.getChildCount(); i++) {
                EditText stepInput = addStepLayout.getChildAt(i).findViewById(R.id.stepInput);
                JSONObject s = new JSONObject();
                s.put("stepNumber", i+1);
                s.put("stepDescription", checkForEmptyInput(stepInput.getText().toString()));
                steps.put(s);
            }

            output.put("quantities", quantities);
            output.put("steps", steps);
            return output;
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private void loadPage() {
        JsonArrayRequest request = new JsonArrayRequest(INGREDIENTS_URL,
        response -> {
            allIngredients = new HashMap<>();
            for (int i=0; i<response.length(); i++) {
                try {
                    JSONObject ing = response.getJSONObject(i);
                    allIngredients.put(ing.getString("ingredientName"), ing.getInt("ingredientID"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            loadRestOfPage();
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

    private static String checkForEmptyInput(String s) throws EmptyInputException {
        if (s == null || s.isEmpty()) {
            throw new EmptyInputException("Input is empty");
        }
        return s;
    }

    private void addRecipe(JSONObject recipe) {
        String jsonString = recipe.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECIPE_URL, response -> Log.i("LOG_VOLLEY", response), error -> Log.e("LOG_VOLLEY", error.toString())) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonString.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    System.out.println(responseString);
                }
                assert response != null;
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("ApiKey", "SecretKey");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }
}