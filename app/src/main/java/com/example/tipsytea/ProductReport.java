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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductReport extends AppCompatActivity {

    private ListView listView;
    ArrayList<HashMap<String,String>> arrayList;
    SharedPreferences sharedPreferences;
    TextView back;
    TextView item1,item2,item3,item4,item5,item6,item7,item8;
    TextView qty1,qty2,qty3,qty4,qty5,qty6,qty7,qty8;

    private ProgressDialog dialog;
    AlertDialog.Builder alertdialog;
    AlertDialog Adialog;

    String URL = "https://tipsytea3-dev-ed.my.salesforce.com/services/apexrest/TipsyTeaAPIConnection/v1/?tab=product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_report);

        arrayList = new ArrayList<>();
        back = findViewById(R.id.preport_back);

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
        dialog.dismiss();

//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if(response.isSuccessful()){
//                    final String myResponse = response.body().string();
//                    ProductReport.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            dialog.dismiss();
//                            try {
//                                final JSONArray jsonArray = new JSONArray(myResponse);
//                                for(int i = 0; i < jsonArray.length();i++ ){
//                                    JSONObject read = jsonArray.getJSONObject(i);
//                                    String PROD = read.getString("Product_Name__c");
//                                    String ID   = read.getString("Id");
//                                    String PRICE= read.getString("Product_Total_Price__c");
//                                    final String ITEM1= read.getString("Item_1__c");
//                                    final String ITEM2= read.getString("Item_2__c");
//                                    final String ITEM3= read.getString("Item_3__c");
//                                    final String ITEM4= read.getString("Item_4__c");
//                                    final String ITEM5= read.getString("Item_5__c");
//                                    final String ITEM6= read.getString("Item_6__c");
//                                    final String ITEM7= read.getString("Item_7__c");
//                                    final String ITEM8= read.getString("Item_8__c");
//                                    final String QTY1= read.getString("Quantity_1__c");
//                                    final String QTY2= read.getString("Quantity_2__c");
//                                    final String QTY3= read.getString("Quantity_3__c");
//                                    final String QTY4= read.getString("Quantity_4__c");
//                                    final String QTY5= read.getString("Quantity_5__c");
//                                    final String QTY6= read.getString("Quantity_6__c");
//                                    final String QTY7= read.getString("Quantity_7__c");
//                                    final String QTY8= read.getString("Quantity_8__c");
//
//                                    final HashMap<String,String> data = new HashMap<>();
//                                    data.put("Product_Name__c", PROD);
//                                    data.put("Id", ID);
//                                    data.put("Product_Total_Price__c",PRICE);
//                                    data.put("Item_1__c", ITEM1);
//                                    data.put("Item_2__c", ITEM2);
//                                    data.put("Item_3__c", ITEM3);
//                                    data.put("Item_4__c", ITEM4);
//                                    data.put("Item_5__c", ITEM5);
//                                    data.put("Item_6__c", ITEM6);
//                                    data.put("Item_7__c", ITEM7);
//                                    data.put("Item_8__c", ITEM8);
//                                    data.put("Quantity_1__c",QTY1);
//                                    data.put("Quantity_2__c",QTY2);
//                                    data.put("Quantity_3__c",QTY3);
//                                    data.put("Quantity_4__c",QTY4);
//                                    data.put("Quantity_5__c",QTY5);
//                                    data.put("Quantity_6__c",QTY6);
//                                    data.put("Quantity_7__c",QTY7);
//                                    data.put("Quantity_8__c",QTY8);
//
//
//
//                                    arrayList.add(data);
//                                    final ListAdapter listAdapter = new SimpleAdapter(ProductReport.this,arrayList,R.layout.single_productreport_data,
//                                            new String[]{ "Product_Name__c"}, new int[]{R.id.preport_name});
//                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                            alertdialog = new AlertDialog.Builder(ProductReport.this);
//                                            view = getLayoutInflater().inflate(R.layout.product_report_popup, null);
//                                            item1 = view.findViewById(R.id.preport_item1);
//                                            item2 = view.findViewById(R.id.preport_item2);
//                                            item3 = view.findViewById(R.id.preport_item3);
//                                            item4 = view.findViewById(R.id.preport_item4);
//                                            item5 = view.findViewById(R.id.preport_item5);
//                                            item6 = view.findViewById(R.id.preport_item6);
//                                            item7 = view.findViewById(R.id.preport_item7);
//                                            item8 = view.findViewById(R.id.preport_item8);
//                                            qty1 = view.findViewById(R.id.preport_qty1);
//                                            qty2 = view.findViewById(R.id.preport_qty2);
//                                            qty3 = view.findViewById(R.id.preport_qty3);
//                                            qty4 = view.findViewById(R.id.preport_qty4);
//                                            qty5 = view.findViewById(R.id.preport_qty5);
//                                            qty6 = view.findViewById(R.id.preport_qty6);
//                                            qty7 = view.findViewById(R.id.preport_qty7);
//                                            qty8 = view.findViewById(R.id.preport_qty8);
//
//                                            item1.setText(arrayList.get(i).put("Item_1__c",ITEM1));
//                                            item2.setText(arrayList.get(i).put("Item_2__c",ITEM2));
//                                            item3.setText(arrayList.get(i).put("Item_3__c",ITEM3));
//                                            item4.setText(arrayList.get(i).put("Item_4__c",ITEM4));
//                                            item5.setText(arrayList.get(i).put("Item_5__c",ITEM5));
//                                            item6.setText(arrayList.get(i).put("Item_6__c",ITEM6));
//                                            item7.setText(arrayList.get(i).put("Item_7__c",ITEM7));
//                                            item8.setText(arrayList.get(i).put("Item_8__c",ITEM8));
//                                            qty1.setText(arrayList.get(i).put("Quantity_1__c",QTY1));
//                                            qty2.setText(arrayList.get(i).put("Quantity_2__c",QTY2));
//                                            qty3.setText(arrayList.get(i).put("Quantity_3__c",QTY3));
//                                            qty4.setText(arrayList.get(i).put("Quantity_4__c",QTY4));
//                                            qty5.setText(arrayList.get(i).put("Quantity_5__c",QTY5));
//                                            qty6.setText(arrayList.get(i).put("Quantity_6__c",QTY6));
//                                            qty7.setText(arrayList.get(i).put("Quantity_7__c",QTY7));
//                                            qty8.setText(arrayList.get(i).put("Quantity_8__c",QTY8));
//
//                                            alertdialog.setView(view);
//                                            Adialog = alertdialog.create();
//                                            Adialog.show();
//                                        }
//                                    });
//                                    listView.setAdapter(listAdapter);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }
}