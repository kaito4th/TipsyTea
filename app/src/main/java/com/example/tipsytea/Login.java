package com.example.tipsytea;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Login extends AppCompatActivity {

    EditText user, pass;
    Button login;
    private ProgressDialog dialog;
    AlertDialog.Builder alertbuilder;
    AlertDialog adialog;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://tipsytea3-dev-ed.my.salesforce.com")
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();

    String grant_type = "password";
    String client_id = "3MVG9fe4g9fhX0E6waLmzNEpJuWI0Muer0.nzXd4G0qOaOyg7UXBhbucv9B6ZQpjNnX5LaVktdYUUTAouPnMH";
    String client_secret = "472ED5DBE6CA9C1D15CB11140B5B5456DBA33E58052C0C06C2A855689C9BC1EC";
    String username = "admin@tipsytea1.com";
    String password = "Tipsytea2021cgqi72muaI3Coeb0RySqgyN1C";

    SharedPreferences sharedPreferences;

    private static String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.login_user);
        pass = findViewById(R.id.login_pass);
        login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void login() throws IOException{
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Processing");
        dialog.show();

        final String USER = user.getText().toString();
        final String PASS = pass.getText().toString();

        if(USER.isEmpty()){
            user.setError("Empty Field");
        }else if(PASS.isEmpty()){
            pass.setError("Empty Field");
        }else if(USER.isEmpty() && PASS.isEmpty()){
            user.setError("Please fill me");
            pass.setError("Please fill me");
        }

        final OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "password")
                .addFormDataPart("client_id", "3MVG9fe4g9fhX0E6waLmzNEpJuWI0Muer0.nzXd4G0qOaOyg7UXBhbucv9B6ZQpjNnX5LaVktdYUUTAouPnMH")
                .addFormDataPart("client_secret", "472ED5DBE6CA9C1D15CB11140B5B5456DBA33E58052C0C06C2A855689C9BC1EC")
                .addFormDataPart("username", USER)
                .addFormDataPart("password", PASS)
                .build();

        final Request request = new Request.Builder()
                .url("https://tipsytea3-dev-ed.my.salesforce.com/services/oauth2/token")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    token = response.body().string();
                }
                Login.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        if(token != null) {
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            sharedPreferences = getSharedPreferences("GET_ACCESS_TOKEN", MODE_PRIVATE);

                            String checkSP = sharedPreferences.getString("ACCESS_TOKEN", null);
                            if (checkSP == null) {
                                //put data on shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("ACCESS_TOKEN", token);
                                editor.apply();
                            }
                            startActivity(intent);
                            finish();
                        }else{
                            alertbuilder = new AlertDialog.Builder(Login.this);
                            alertbuilder.setTitle("Error");
                            alertbuilder.setMessage("Invalid Username or Password");
                            adialog = alertbuilder.create();
                            adialog.show();
                        }

                    }
                });
            }
        });
    }
}
