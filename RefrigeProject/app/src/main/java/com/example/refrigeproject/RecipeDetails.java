package com.example.refrigeproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity implements View.OnClickListener {
    ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();
    ArrayList<Recipe> description = new ArrayList<Recipe>();
    BasicRecipe recipe;

    private TextView tvTitle, tvExtraInfo, tvIngredient;
    private ImageButton ibtBack;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView tvDescription, tvTip, tvOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvExtraInfo = findViewById(R.id.tvExtraInfo);
        tvIngredient = findViewById(R.id.tvIngredient);
        imageView = findViewById(R.id.imageView);
        ibtBack = findViewById(R.id.ibtBack);
        recyclerView = findViewById(R.id.recyclerView);

        getRecipeInfo();
        setRecipeInfo();

        ibtBack.setOnClickListener(this);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DescriptionAdapter adapter = new DescriptionAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void getRecipeInfo() {
        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("recipe");
        if(recipe!=null) tvTitle.setText(recipe.getName());
    }

    private void setRecipeInfo() {
        tvExtraInfo.setText(recipe.getNationName() + " / " + recipe.getLevel() + " / " + recipe.getCookingTime());
        String imageUrl = recipe.getImageUrl();
        if(imageUrl!=null){
            Glide.with(this).load(imageUrl).into(imageView);
        }

        searchIngredient(SearchRecipeFragment.getJsonString("RecipeIngredient", this), ingredients, recipe.getRecipeID());
        for(RecipeIngredient ingredient : ingredients){
            Log.d("setRecipeInfo", ingredient.getRecipeID());
            tvIngredient.setText(tvIngredient.getText() + ingredient.getName() + " " + ingredient.getCapacity() + " " + ingredient.getTypeName() + "\n");
        }

        // 해당 레시피의 정렬된 단계별 레시피 가져오기
        searchRecipeDescription(SearchRecipeFragment.getJsonString("Recipe", this), description, recipe.getRecipeID());
    }

    // 특정 레시피 아이디를 가진 레시피 순서를 찾는 메소드
    private void searchRecipeDescription(String json, ArrayList<Recipe> description, String recipeID) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray recipeArray = jsonObject.getJSONArray("data");

            for(int i = 0 ; i < recipeArray.length() ; i++) {
                JSONObject recipe = recipeArray.getJSONObject(i);
                Recipe step = new Recipe();

                if(recipe.getString("RECIPE_ID").equals(recipeID)){
                    step.setImageUrl(recipe.getString("STRE_STEP_IMAGE_URL"));
                    step.setRecipeID(recipe.getString("RECIPE_ID"));
                    step.setTip(recipe.getString("STEP_TIP"));
                    step.setDescription(recipe.getString("COOKING_DC"));
                    step.setOrder(recipe.getString("COOKING_NO"));
                    description.add(step);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 특정 레시피 아이디를 가진 재료를 찾는 메소드
    private void searchIngredient(String json, ArrayList<RecipeIngredient> ingredients, String recipeID) {
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ingredientArray = jsonObject.getJSONArray("data");

            for(int i = 0 ; i < ingredientArray.length() ; i++) {
                JSONObject ingredientObject = ingredientArray.getJSONObject(i);
                RecipeIngredient ingredient = new RecipeIngredient();

                if(ingredientObject.getString("RECIPE_ID").equals(recipeID)){
                    ingredient.setRecipeID(ingredientObject.getString("RECIPE_ID"));
                    ingredient.setName(ingredientObject.getString("IRDNT_NM"));
                    ingredient.setSerialNumber(ingredientObject.getString("IRDNT_SN"));
                    ingredient.setTypeName(ingredientObject.getString("IRDNT_TY_NM"));
                    ingredient.setTypeCode(ingredientObject.getString("IRDNT_TY_CODE"));
                    ingredient.setCapacity(ingredientObject.getString("IRDNT_CPCTY"));
                    Log.d("Ingredient", "재료명 " + ingredient.getName());
                    Log.d("Ingredient", "활용레시피 번호 " + ingredient.getRecipeID());
                    ingredients.add(ingredient);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    private class DescriptionAdapter extends RecyclerView.Adapter<CustomViewHolder>{

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_description_item, null);
            CustomViewHolder customViewHolder = new CustomViewHolder(view);
            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            tvOrder.setText(description.get(position).getOrder());
            tvDescription.setText(description.get(position).getDescription());
            if(!description.get(position).getTip().equals("null")) {
                tvTip.setText("tip) " + description.get(position).getTip());
            } else {
                tvTip.setVisibility(View.GONE);
            }
            Log.d("나오ㅏ라", description.get(position).getOrder() + ") " + description.get(position).getDescription());
        }

        @Override
        public int getItemCount() {
            return description.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder{
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrder = itemView.findViewById(R.id.tvOrder);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTip = itemView.findViewById(R.id.tvTip);
        }
    }

//    private class DescriptionAdapter extends BaseAdapter{
//
//
//        @Override
//        public int getCount() {
//            return description.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return description.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_description_item, null);
//
//            tvDescription = convertView.findViewById(R.id.tvDescription);
//            tvTip = convertView.findViewById(R.id.tvTip);
//            tvOrder = convertView.findViewById(R.id.tvOrder);
//
//            Recipe step = description.get(position);
//            tvDescription.setText(step.getDescription());
//            tvOrder.setText(step.getOrder());
//            if(!step.getTip().equals("null")) tvTip.setText("tip) " + step.getTip());
//
//            return convertView;
//        }
//    }
}
