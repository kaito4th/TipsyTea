package com.example.tipsytea;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Sales extends AppCompatActivity {

    private ListView listView;
    ArrayList<HashMap<String,String>> arrayList;
    SharedPreferences sharedPreferences;
    TextView back;

    private ProgressDialog dialog;

    String URL = "https://tipsytea3-dev-ed.my.salesforce.com/services/apexrest/TipsyTeaAPIConnection/v1/?tab=sales";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        listView = findViewById(R.id.sales_list);
        arrayList = new ArrayList<>();
        back = findViewById(R.id.sales_back);

        sharedPreferences = getSharedPreferences("PARSED_ACCESS_TOKEN", MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPreferences.getString("PARSED_TOKEN", null);
        Log.e("token",ACCESS_TOKEN);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                startActivity(intent);
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");
        dialog.show();

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
                final String myResponse = response.body().string();
                Sales.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        try {
                            final JSONArray jsonArray = new JSONArray(myResponse);
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject read = jsonArray.getJSONObject(i);
                                String NAME = read.getString("Order_Item__c");
                                String PRICE = read.getString("Order_Item_Price__c");
                                String SUB = read.getString("Order_Item_Sub_Price__c");
                                String QTY = read.getString("Order_Quantity__c");
                                String PROF = read.getString("Profit__c");
                                String DATE = read.getString("Order_Created_Date__c");

                                HashMap<String,String> data = new HashMap<>();
                                data.put("Order_Item__c",NAME);
                                data.put("Order_Item_Price__c",PRICE);
                                data.put("Order_Item_Sub_Price__c",SUB);
                                data.put("Order_Quantity__c",QTY);
                                data.put("Profit__c",PROF);
                                data.put("Order_Created_Date__c",DATE);
                                arrayList.add(data);

                                final ListAdapter listAdapter = new SimpleAdapter(Sales.this,arrayList,R.layout.single_sales_data,
                                        new String[]{"Order_Item__c","Order_Item_Price__c","Order_Item_Sub_Price__c","Order_Quantity__c","Profit__c","Order_Created_Date__c"},
                                        new int[]{R.id.sales_name, R.id.sales_price, R.id.sales_sub,R.id.sales_qty,R.id.sales_profit,R.id.sales_date});
                                listView.setAdapter(listAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}