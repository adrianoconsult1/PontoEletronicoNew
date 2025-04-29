package com.example.pontoeletroniconew;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TimeAPIRequest {

    public static String getTimeResponse(String latitude, String longitude) {
        String response = "";
        //String urlString = "https://www.timeapi.io/api/Time/current/coordinate?latitude=" + latitude + "&longitude=" + longitude;
        String urlString = "http://api.timezonedb.com/v2.1/get-time-zone?key=YWQFH1OZ1DVX&format=json&by=position&lat="+latitude+"&lng="+longitude;
        Log.i("getTimeResponse","getTimeResponse1");
        try {
            Log.i("getTimeResponse","getTimeResponse2");
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            Log.i("getTimeResponse","getTimeResponse3");
            StrictMode.setThreadPolicy(gfgPolicy);
            Log.i("getTimeResponse","getTimeResponse4");
            URL url = new URL(urlString);
            Log.i("getTimeResponse","getTimeResponse5");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.i("getTimeResponse","getTimeResponse6");
            InputStream inputStream = urlConnection.getInputStream();
            Log.i("getTimeResponse","getTimeResponse7");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Log.i("getTimeResponse","getTimeResponse8");
            StringBuilder stringBuilder = new StringBuilder();
            Log.i("getTimeResponse","getTimeResponse9");
            String line;
            Log.i("getTimeResponse","getTimeResponse10");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            Log.i("getTimeResponse","getTimeResponse11");
            bufferedReader.close();
            Log.i("getTimeResponse","getTimeResponse12");
            inputStream.close();
            Log.i("getTimeResponse","getTimeResponse13");
            urlConnection.disconnect();
            Log.i("getTimeResponse","getTimeResponse14");
            response = stringBuilder.toString();
            Log.i("getTimeResponse","getTimeResponse15");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("STRLATLONG",response);
        return response;
    }
}


