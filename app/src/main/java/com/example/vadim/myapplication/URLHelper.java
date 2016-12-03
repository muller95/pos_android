package com.example.vadim.myapplication;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by vadim on 29.11.16.
 */

public class URLHelper {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static public String ParamsToString(HashMap<String, String> pairs) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(String key : pairs.keySet()){
            if (first)
                first = false;
            else
                result.append("&");

            try {
                //  result.append(URLEncoder.encode(key, StandardCharsets.UTF_8.toString()));
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                /*result.append(URLEncoder.encode((String)pairs.get(key),
                        StandardCharsets.UTF_8.toString()));*/

                result.append(URLEncoder.encode((String)pairs.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        return result.toString();
    }
}
