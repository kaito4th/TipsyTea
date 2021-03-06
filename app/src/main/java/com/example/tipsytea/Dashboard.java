package com.example.tipsytea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView logout, product, inventory,sales,Preport;

    String AC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        product = findViewById(R.id.product_button);
        inventory = findViewById(R.id.inventory_button);
        sales = findViewById(R.id.sales_button);
        Preport = findViewById(R.id.Preport_button);
        logout = findViewById(R.id.logout);

        sharedPreferences = getSharedPreferences("GET_ACCESS_TOKEN", MODE_PRIVATE);
        String ACCESS_TOKEN = sharedPreferences.getString("ACCESS_TOKEN", null);

        try {
            JSONObject jsonObject = new JSONObject(ACCESS_TOKEN);
            AC = jsonObject.getString("access_token");
//            access.setText(AC);
            Log.e("Token",AC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Product.class);
                sharedPreferences = getSharedPreferences("PARSED_ACCESS_TOKEN", MODE_PRIVATE);

                String checkSP = sharedPreferences.getString("PARSED_TOKEN", null);
                if (checkSP == null) {
                    //put data on shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PARSED_TOKEN", AC);
                    editor.apply();
                }
                startActivity(intent);
            }
        });

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Inventory.class);
                sharedPreferences = getSharedPreferences("PARSED_ACCESS_TOKEN", MODE_PRIVATE);

                String checkSP = sharedPreferences.getString("PARSED_TOKEN", null);
                if (checkSP == null) {
                    //put data on shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PARSED_TOKEN", AC);
                    editor.apply();
                }
                startActivity(intent);
            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Sales.class);
                sharedPreferences = getSharedPreferences("PARSED_ACCESS_TOKEN", MODE_PRIVATE);

                String checkSP = sharedPreferences.getString("PARSED_TOKEN", null);
                if (checkSP == null) {
                    //put data on shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PARSED_TOKEN", AC);
                    editor.apply();
                }
                startActivity(intent);
            }
        });

        Preport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProductReport.class);
                sharedPreferences = getSharedPreferences("PARSED_ACCESS_TOKEN", MODE_PRIVATE);

                String checkSP = sharedPreferences.getString("PARSED_TOKEN", null);
                if (checkSP == null) {
                    //put data on shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("PARSED_TOKEN", AC);
                    editor.apply();
                }
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                popupMenu.inflate(R.menu.menu_activity);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id == R.id.menu_logout){
                            Intent intent = new Intent(getBaseContext(), Login.class);
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                            Toast.makeText(getBaseContext(), "Log out Successfully", Toast.LENGTH_SHORT).show();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        return false;
                    }
                });
            }
        });
    }
}
