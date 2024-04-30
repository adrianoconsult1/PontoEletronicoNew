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
        String urlString = "https://www.timeapi.io/api/Time/current/coordinate?latitude=" + latitude + "&longitude=" + longitude;

        try {
            StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(gfgPolicy);
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            bufferedReader.close();
            inputStream.close();
            urlConnection.disconnect();

            response = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("STRLATLONG",response);
        return response;
    }
}
