package com.example.vadim.myapplication;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import java.lang.String;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;



public class UserPageActivity extends AppCompatActivity {
    private String worker_id, company_id;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AlertDialog alert;
        AlertDialog.Builder builder;
        String requestUrl = "http://192.168.1.36/belyaev/apk/get_orders.php";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        worker_id = getIntent().getStringExtra("worker_id");
        company_id = getIntent().getStringExtra("company_id");

        builder = new AlertDialog.Builder(this);
        builder.setTitle("INFO")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        alert = builder.create();

        try {
            HashMap <String, String> pairs;
            String response;

            URL url = new URL(requestUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            pairs = new HashMap<String, String>();
            pairs.put("worker_id", worker_id);
            pairs.put("company_id", company_id);
            String queryParams = URLHelper.ParamsToString(pairs);

            writer.write(queryParams);
            writer.flush();
            writer.close();
            os.close();

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
            BufferedReader buff = new BufferedReader(in);

            response = buff.readLine();
            if (response.indexOf("ERR") >= 0) {
                alert.setMessage(buff.readLine());
                alert.show();
            } else {

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            alert.setMessage(e.getMessage());
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert.setMessage(e.getMessage());
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void CreateOrder(View view) {
        Intent intent;

        intent = new Intent(this, CreateOrderActivity.class);
        intent.putExtra("worker_id", worker_id);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
    }
}
