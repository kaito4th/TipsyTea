package com.example.tipsytea;

import androidx.appcompat.app.AlertDialog;
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

public class Inventory extends AppCompatActivity {

    private ListView listView;
    ArrayList<HashMap<String,String>> arrayList;
    SharedPreferences sharedPreferences;
    TextView back;

    private ProgressDialog dialog;
    AlertDialog.Builder alertbuilder;
    AlertDialog adialog;

    String URL = "https://tipsytea3-dev-ed.my.salesforce.com/services/apexrest/TipsyTeaAPIConnection/v1/?tab=inventory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        listView = findViewById(R.id.inventory_list);
        arrayList = new ArrayList<>();
        back = findViewById(R.id.inventory_back);

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
                if(response.isSuccessful()){
                   final String myResponse = response.body().string();
                   Inventory.this.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           dialog.dismiss();
                           try {
                               final JSONArray jsonArray = new JSONArray(myResponse);
                               for(int i = 0; i < jsonArray.length(); i++){
                                   JSONObject read = jsonArray.getJSONObject(i);
                                   String NAME = read.getString("Item_Name__c");
                                   String QTY = read.getString("Quantity__c");
                                   String UNIT = read.getString("Unit__c");
                                   String SELL = read.getString("Estimated_Sell_Per_Unit__c");
                                   String CAP = read.getString("Capital__c");

                                   HashMap<String, String> data = new HashMap<>();
                                   data.put("Item_Name__c",NAME);
                                   data.put("Quantity__c",QTY);
                                   data.put("Unit__c",UNIT);
                                   data.put("Estimated_Sell_Per_Unit__c",SELL);
                                   data.put("Capital__c",CAP);
                                   arrayList.add(data);

                                   final ListAdapter listAdapter = new SimpleAdapter(Inventory.this,arrayList,R.layout.single_inventory_data,
                                           new String[]{"Item_Name__c","Quantity__c","Unit__c","Estimated_Sell_Per_Unit__c","Capital__c"},new int[]{R.id.inventory_name,
                                   R.id.inventory_qty,R.id.inventory_unit,R.id.inventory_estimate,R.id.inventory_cap});
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