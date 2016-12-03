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


public class CreateOrderActivity extends AppCompatActivity {
    private String worker_id, company_id, current_menu_id;
    private HashMap<Integer, String> mapDishCount;
    private HashMap<Integer, Integer>  mapDishAdd, mapDishSub;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AlertDialog alert;
        AlertDialog.Builder builder;
        String requestUrl = "http://192.168.1.36/belyaev/apk/get_menu.php";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

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

        mapDishCount = new HashMap<Integer, String>();
        mapDishAdd = new HashMap<Integer, Integer>();
        mapDishSub = new HashMap<Integer, Integer>();

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
                current_menu_id = buff.readLine();
                if (current_menu_id.indexOf("-1")  == 0) {
                    Intent intent;

                    alert.setMessage("В вашем предприятии не установлено меню.");
                    alert.show();
                } else {
                    int i = 0;
                    String str;

                    TableLayout tableMenuLayout = (TableLayout) findViewById(R.id.tableMenu);

                    while ((str = buff.readLine()) != null) {
                        Integer textCountId, btnAddId, btnSubId;
                        Button btnDishAdd, btnDishSub;
                        TableRow tableRow;
                        TextView textDishName, textDishPrice,textDishCount;

                        if (str.indexOf("CATEGORY") == 0) {
                            TextView textCategoryName, textName, textPrice;

                            tableRow = new TableRow(this);
                            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT));

                            textCategoryName = new TextView(this);
                            textCategoryName.setText(buff.readLine());
                            textCategoryName.setTypeface(
                                    Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                            textCategoryName.setTextSize(16.0f);

                            tableRow.addView(textCategoryName);
                            tableMenuLayout.addView(tableRow, i);
                            i++;

                            tableRow = new TableRow(this);
                            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT));

                            textName = new TextView(this);
                            textName.setText("Название");
                            textName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

                            textPrice = new TextView(this);
                            textPrice.setText("Цена");
                            textPrice.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

                            tableRow.addView(textName);
                            tableRow.addView(textPrice);
                            tableMenuLayout.addView(tableRow, i);
                            i++;
                            continue;
                        } else if (str.indexOf("END CATEGORY") == 0) {
                            TextView textDummy1, textDummy2;

                            tableRow = new TableRow(this);
                            tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                    LayoutParams.WRAP_CONTENT));

                            textDummy1 = new TextView(this);
                            textDummy1.setText(" ");
                            textDummy1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

                                textDummy2 = new TextView(this);
                            textDummy2.setText(" ");
                            textDummy2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));

                            tableRow.addView(textDummy1);
                            tableRow.addView(textDummy2);
                            tableMenuLayout.addView(tableRow, i);
                            i++;
                            continue;
                        }

                        /*dishes.add(str);
                        prices.add(buff.readLine());*/

                        tableRow = new TableRow(this);
                        tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));

                        textDishName = new TextView(this);
                        textDishPrice = new TextView(this);

                        textDishName.setText(str);
                        textDishPrice.setText(buff.readLine());

                        tableRow.addView(textDishName);
                        tableRow.addView(textDishPrice);

                        tableMenuLayout.addView(tableRow, i);
                        i++;

                        tableRow = new TableRow(this);
                        tableRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));

                        textDishCount = new TextView(this);


                        textDishCount.setText("0");
                        textCountId = textDishCount.generateViewId();
                        textDishCount.setId(textCountId);
                        mapDishCount.put(textCountId, buff.readLine());

                        btnDishAdd = new Button(this);
                        btnAddId = btnDishAdd.generateViewId();
                        btnDishAdd.setId(btnAddId);
                        btnDishAdd.setText("+");
                        btnDishAdd.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View viewCaller) {
                                Integer count;
                                TextView textDishCount;

                                textDishCount = (TextView)findViewById(
                                        mapDishAdd.get(viewCaller.getId()));
                                count = Integer.parseInt(textDishCount.getText().toString());
                                count++;
                                textDishCount.setText(count.toString());

                            }
                        });
                        mapDishAdd.put(btnAddId, textCountId);

                        btnDishSub = new Button(this);
                        btnSubId = btnDishSub.generateViewId();
                        btnDishSub.setId(btnSubId);
                        btnDishSub.setText("—");
                        btnDishSub.setOnClickListener(new Button.OnClickListener() {
                            public void onClick(View viewCaller) {
                                Integer count;
                                TextView textDishCount;

                                textDishCount = (TextView)findViewById(
                                        mapDishSub.get(viewCaller.getId()));
                                count = Integer.parseInt(textDishCount.getText().toString());
                                count--;
                                count = Math.max(0, count);
                                textDishCount.setText(count.toString());

                            }
                        });
                        mapDishSub.put(btnSubId, textCountId);

                        tableRow.addView(btnDishSub);
                        tableRow.addView(textDishCount);
                        tableRow.addView(btnDishAdd);

                        tableMenuLayout.addView(tableRow, i);
                        i++;
                    }

                  /*  ListView listView = (ListView)findViewById(R.id.listMenu);

// используем адаптер данных
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            R.layout.menu_category_layout, R.id.label, dishes);*/


               //     listView.setAdapter(adapter);
                }
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

    protected void PrepareOrder(View v) {
        Intent intent;
        String order = "";
        for(Integer textDishCountId : mapDishCount.keySet()) {
            TextView textDishCount;
            String dishCount;

            textDishCount = (TextView)findViewById(textDishCountId);
            dishCount = textDishCount.getText().toString();

            if (dishCount.indexOf("0") != 0) {
                String dishId = mapDishCount.get(textDishCountId);
                order += ((order.length() == 0) ? "" : " " )+ dishId + ":" + dishCount;
            }
        }

        intent = new Intent(this, ConfirmOrderActivity.class);
        intent.putExtra("worker_id", worker_id);
        intent.putExtra("company_id", company_id);
        intent.putExtra("order", order);
        startActivity(intent);


        System.out.println(order);
    }



    protected void ToUserPage(View view) {
        Intent intent;

        intent = new Intent(this, UserPageActivity.class);
        intent.putExtra("worker_id", worker_id);
        intent.putExtra("company_id", company_id);
        startActivity(intent);
    }
}
