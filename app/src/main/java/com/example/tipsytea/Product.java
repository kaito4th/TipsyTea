package com.example.tipsytea;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Product extends AppCompatActivity {

    private ListView listView;
    ArrayList<HashMap<String,String>> arrayList;
    SharedPreferences sharedPreferences;

    String URL = "https://tipsytea3-dev-ed.my.salesforce.com/services/apexrest/TipsyTeaAPIConnection/v1/?tab=product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        listView = findViewById(R.id.product_list);
        arrayList = new ArrayList<>();

        sharedPreferences = getSharedPreferences("PARSED_ACCESS_TOKEN", MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPreferences.getString("PARSED_TOKEN", null);
        Log.e("token",ACCESS_TOKEN);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization", "OAuth "+ACCESS_TOKEN)
                .addHeader("Content-type", "application/json")
                .build();
        Log.e("token", String.valueOf(request));

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    Product.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final JSONArray jsonArray = new JSONArray(myResponse);
                                for(int i = 0; i < jsonArray.length();i++ ){
                                    JSONObject read = jsonArray.getJSONObject(i);
                                    String PROD = read.getString("Product_Name__c");
                                    String ID   = read.getString("Id");
                                    String PRICE= read.getString("Product_Total_Price__c");

                                    final HashMap<String,String> data = new HashMap<>();
                                    data.put("Product_Name__c", PROD);
                                    data.put("Id", ID);
                                    data.put("Product_Total_Price__c",PRICE);

                                    arrayList.add(data);
                                    final ListAdapter listAdapter = new SimpleAdapter(Product.this,arrayList,R.layout.single_product_data,
                                            new String[]{"Product_Name__c", "Id", "Product_Total_Price__c"}, new int[]{R.id.product_name, R.id.product_id, R.id.product_price});
                                    listView.setAdapter(listAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}