package com.example.vadim.myapplication;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.view.ViewGroup.LayoutParams;

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.lang.String;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {
    private String worker_id, company_id, order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int start = 0, delimPos;
        AlertDialog alert;
        AlertDialog.Builder builder;
        String requestUrl = "http://192.168.1.36/belyaev/apk/get_menu.php";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        worker_id = getIntent().getStringExtra("worker_id");
        company_id = getIntent().getStringExtra("company_id");
        order = getIntent().getStringExtra("order");

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
        alert.setMessage(order);
        alert.show();

        while ((delimPos = order.indexOf(":", start)) != -1) {
            int end;
            String strDishId, strDishCount;

            end = order.indexOf(" ", start);
            end = (end < 0) ? order.length() - 1 : end;

            //strDishId = order.substring()


        }
    }
}
