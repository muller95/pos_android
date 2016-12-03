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



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void TryAuthorize(View view) {
        String requestUrl = "http://192.168.1.36/belyaev/apk/worker_auth.php";

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                    Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("INFO")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();


        try {
            EditText editWorkerId, editWorkerPasswd;
            HashMap <String, String> pairs;
            String response;

            editWorkerId = (EditText)findViewById(R.id.editWorkerId);
            editWorkerPasswd = (EditText)findViewById(R.id.editWorkerPasswd);

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
            pairs.put("worker_id", editWorkerId.getText().toString());
            pairs.put("worker_passwd", editWorkerPasswd.getText().toString());
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
                Intent intent;

                intent = new Intent(this, UserPageActivity.class);
                intent.putExtra("worker_id", editWorkerId.getText().toString());
                intent.putExtra("company_id", buff.readLine());
                startActivity(intent);
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
}
