package com.example.refrigeproject.search_recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.refrigeproject.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchRecipeFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SearchRecipeFragment";
    private View view;
    private Context context;
    private  InputMethodManager imm;

    private RecyclerView recyclerView;
    private AutoCompleteTextView autoCompleteTextView;
    private Button btnSearch;
    private CheckBox chkRecipe, chkIngredient;
    private ConstraintLayout empty_text;

    // 리사이클러뷰 관련
    private TextView tvTitle, tvSummary;
    private ImageView ivRecipe;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;

    private ArrayList<RecipeIngredient> ingredients = new ArrayList<RecipeIngredient>();
    private HashMap<String, BasicRecipe> recipes = new HashMap<String, BasicRecipe>();
    private ArrayList<BasicRecipe> recipeList = new ArrayList<BasicRecipe>();
    private static String keyword = null;
    private boolean recipeChecked = false;
    private boolean ingredientChecked = false;
    private Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_recipe, null, false);
        this.context = container.getContext();

        imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        recyclerView = view.findViewById(R.id.recyclerView);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        chkRecipe = view.findViewById(R.id.chkRecipe);
        chkIngredient = view.findViewById(R.id.chkIngredient);
        btnSearch = view.findViewById(R.id.btnSearch);
        empty_text = view.findViewById(R.id.empty_text);

        chkRecipe.setChecked(true);
        chkIngredient.setChecked(true);

        btnSearch.setOnClickListener(this);
        chkRecipe.setOnCheckedChangeListener(this);
        chkIngredient.setOnCheckedChangeListener(this);

        setAutoCompleteFunction();

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        bundle = getArguments();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setKeyword();
    }

    private void setKeyword() {
        try{
            if(bundle.getString("name") != null){
                // 번들에 값이 있으면 = 냉장고 음식리스트에서 검색했으면
                keyword = bundle.getString("name");
            }

            autoCompleteTextView.setText(keyword);
            if (recipeChecked) chkRecipe.setChecked(true);
            if (ingredientChecked) chkIngredient.setChecked(true);
            Log.d(TAG, "bundle not null - " + autoCompleteTextView.getText().toString());
            bundle.remove("name");
            btnSearch.callOnClick();

        }catch (NullPointerException e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 다른 프래그먼트가 켜질 때
        // 현재 검색어 저장
        keyword = autoCompleteTextView.getText().toString();

        // 요리명/재료명 체크박스 저장
        if (chkRecipe.isChecked()) recipeChecked = true;
        if (chkIngredient.isChecked()) ingredientChecked = true;
    }


    @Override
    public void onClick(View v) {
        if (autoCompleteTextView.getText().toString().equals("")) {
            // 빈칸일 경우
            return;
        }
        recipes.clear();
        ingredients.clear();
        recipeList.clear();
        recyclerView.removeAllViews();

        String keyword = autoCompleteTextView.getText().toString().trim();

        // 해당 키워드의 레시피 검색
        searchRecipe(keyword);

        imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(),0); // 키보드 내리기
        adapter.notifyDataSetChanged();
    }

    private void searchRecipe(String keyword) {
        // 검색하여 Hashmap에 저장
        if (chkRecipe.isChecked()) {
            searchRecipeName(getJsonString("BasicRecipe", context), recipes, keyword);
        }
        if (chkIngredient.isChecked()) {
            searchRecipeByIngredient(getJsonString("RecipeIngredient", context), ingredients, keyword);
            searchRecipeByID(getJsonString("BasicRecipe", context), recipes, ingredients);
        }

        // HashMap을 ArrayList로 변환
        recipeList.addAll(recipes.values());
        Log.d(TAG, recipeList.size() + "");

    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        btnSearch.callOnClick();
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View customView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, null);
            CustomViewHolder customViewHolder = new CustomViewHolder(customView);

            return customViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            BasicRecipe recipe = recipeList.get(position);
            do {
                Log.d(TAG, "onBindViewHolder" + recipe.getName());
                tvTitle.setText(recipe.getName());
                tvSummary.setText(recipe.getSummary());
                String imageUrl = recipe.getImageUrl();
                if (imageUrl != null) {
                    Glide.with(context).load(imageUrl).into(ivRecipe);
                }
            } while (!tvTitle.getText().toString().equals(recipe.getName()));
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, recipeList.size() + "");
            int count = recipeList.size();

            if (count == 0) {
                empty_text.setVisibility(View.VISIBLE);
            } else {
                empty_text.setVisibility(View.GONE);
            }

            return count;
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSummary = itemView.findViewById(R.id.tvSummary);
            ivRecipe = itemView.findViewById(R.id.ivRecipe);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, RecipeDetails.class);
            intent.putExtra("recipe", recipeList.get(getAdapterPosition()));

            startActivity(intent);
        }
    }

    public static String getJsonString(String fileName, Context context) {
        String json = "";

        try {
            InputStream is = context.getAssets().open(fileName);
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            // JSON파일을 여는 데 실패했을 경우
            ex.printStackTrace();
        }

        return json;
    }

    // 이름을 통해 레시피를 찾는 메소드
    public static void searchRecipeName(String json, HashMap<String, BasicRecipe> recipes, String keyword) {
        Log.d(TAG, "initial size " + recipes.size());
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray recipeArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipeObject = recipeArray.getJSONObject(i);
                BasicRecipe recipe = new BasicRecipe();

                if (recipeObject.getString("RECIPE_NM_KO").contains(keyword)) {
                    recipe.setRecipeID(recipeObject.getString("RECIPE_ID"));
                    recipe.setName(recipeObject.getString("RECIPE_NM_KO"));
                    recipe.setIngredient(recipeObject.getString("IRDNT_CODE"));
                    recipe.setCalorie(recipeObject.getString("CALORIE"));
                    recipe.setCookingTime(recipeObject.getString("COOKING_TIME"));
                    recipe.setNationName(recipeObject.getString("NATION_NM"));
                    recipe.setNationCode(recipeObject.getString("NATION_CODE"));
                    recipe.setLevel(recipeObject.getString("LEVEL_NM"));
                    recipe.setPrice(recipeObject.getString("PC_NM"));
                    recipe.setTypeCode(recipeObject.getString("TY_CODE"));
                    recipe.setTypeName(recipeObject.getString("TY_NM"));
                    recipe.setSummary(recipeObject.getString("SUMRY"));
                    recipe.setQuantity(recipeObject.getString("QNT"));
                    recipe.setDetUrl(recipeObject.getString("DET_URL"));
                    recipe.setImageUrl(recipeObject.getString("IMG_URL"));
                    recipes.put(recipe.getRecipeID(), recipe);
                    Log.d("searchRecipeName ADD중 ", recipe.getName());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 아이디를 통해 레시피를 찾는 메소드
    public void searchRecipeByID(String json, HashMap<String, BasicRecipe> recipes, ArrayList<RecipeIngredient> ingredients) {
        Log.d(TAG, "ByID initial size " + recipes.size());
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray recipeArray = jsonObject.getJSONArray("data");

            for (RecipeIngredient ingredient : ingredients) {
                for (int i = 0; i < recipeArray.length(); i++) {
                    JSONObject recipeObject = recipeArray.getJSONObject(i);
                    BasicRecipe recipe = null;
                    String idToFind = ingredient.getRecipeID();

                    if (recipeObject.getString("RECIPE_ID").equals(idToFind)) {
                        recipe = new BasicRecipe();
                        recipe.setRecipeID(recipeObject.getString("RECIPE_ID"));
                        recipe.setName(recipeObject.getString("RECIPE_NM_KO"));
                        recipe.setIngredient(recipeObject.getString("IRDNT_CODE"));
                        recipe.setCalorie(recipeObject.getString("CALORIE"));
                        recipe.setCookingTime(recipeObject.getString("COOKING_TIME"));
                        recipe.setNationName(recipeObject.getString("NATION_NM"));
                        recipe.setNationCode(recipeObject.getString("NATION_CODE"));
                        recipe.setLevel(recipeObject.getString("LEVEL_NM"));
                        recipe.setPrice(recipeObject.getString("PC_NM"));
                        recipe.setTypeCode(recipeObject.getString("TY_CODE"));
                        recipe.setTypeName(recipeObject.getString("TY_NM"));
                        recipe.setSummary(recipeObject.getString("SUMRY"));
                        recipe.setQuantity(recipeObject.getString("QNT"));
                        recipe.setDetUrl(recipeObject.getString("DET_URL"));
                        recipe.setImageUrl(recipeObject.getString("IMG_URL"));
                    }

                    if (recipe != null && ingredients != null) {
                        // 중복 제거 되어 저장
                        recipes.put(recipe.getRecipeID(), recipe);
                    } else if (recipe != null) {
                        recipes.put(idToFind, recipe);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 검색어가 재료명에 포함되었을 경우를 찾는 메소드
    private void searchRecipeByIngredient(String json, ArrayList<RecipeIngredient> arrayList, String keyword) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray ingredientArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < ingredientArray.length(); i++) {
                JSONObject ingredientObject = ingredientArray.getJSONObject(i);
                RecipeIngredient ingredient = new RecipeIngredient();

                if (ingredientObject.getString("IRDNT_NM").equals(keyword)) {
                    ingredient.setRecipeID(ingredientObject.getString("RECIPE_ID"));
                    ingredient.setName(ingredientObject.getString("IRDNT_NM"));
                    ingredient.setSerialNumber(ingredientObject.getString("IRDNT_SN"));
                    ingredient.setTypeName(ingredientObject.getString("IRDNT_TY_NM"));
                    ingredient.setTypeCode(ingredientObject.getString("IRDNT_TY_CODE"));
                    ingredient.setCapacity(ingredientObject.getString("IRDNT_CPCTY"));
                    Log.d("Ingredient", "재료명 " + ingredient.getName());
                    Log.d("Ingredient", "활용레시피 번호 " + ingredient.getRecipeID());
                    arrayList.add(ingredient);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 자동 완성 기능
    private void setAutoCompleteFunction() {
        ArrayList<String> data = new ArrayList<String>();
        String recipeJson = getJsonString("BasicRecipe", context);
        String ingredientJson = getJsonString("RecipeIngredient", context);
        getRecipeAndIngredientName(recipeJson, ingredientJson, data);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, data);
        autoCompleteTextView.setAdapter(adapter);
    }

    // 등록된 모든 레시피와 재료의 이름만을 찾는 메소드
    public void getRecipeAndIngredientName(String json1, String json2, ArrayList<String> data) {
        try {
            JSONObject jsonObject = new JSONObject(json1);
            JSONArray recipeArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipeObject = recipeArray.getJSONObject(i);
                data.add(recipeObject.getString("RECIPE_NM_KO"));
            }

            jsonObject = new JSONObject(json2);
            recipeArray = jsonObject.getJSONArray("data");

            // 중복 제거 해야
            for (int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipeObject = recipeArray.getJSONObject(i);
                String name = recipeObject.getString("IRDNT_NM");
                if (!data.contains(name)) {
                    data.add(name);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}