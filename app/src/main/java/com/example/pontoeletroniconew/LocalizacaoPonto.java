package com.example.pontoeletroniconew;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class LocalizacaoPonto extends Activity  {

    private TextView data;
    private TextView func;
    private TextView hora1;
    private TextView local1;
    private int funcionario;
    private long ROWID;
    private double latitude;
    private double longitude;
    private String gps1;
    private LocationManager locationManager;
    private GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.localizacao_ponto);
        data        = (TextView) findViewById(R.id.dataDt);
        func        = (TextView) findViewById(R.id.funcionarioDt);
        hora1       = (TextView) findViewById(R.id.horario1Dt);
        local1      = (TextView) findViewById(R.id.local1Dt);

        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();

        data.setText(savedInstanceState.getString("data"));
        func.setText(savedInstanceState.getString("funcionario"));
        hora1.setText(savedInstanceState.getString("hora"));
        local1.setText(savedInstanceState.getString("local"));
        ROWID = savedInstanceState.getLong("rowid");
        gps1 = savedInstanceState.getString("gps");
        Log.i("gpsLoc",gps1);
        String latlong[] = gps1.split(", ");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        Log.i("gpsLocLtd",""+latitude);
        Log.i("gpsLocLgt",""+longitude);


    }
}
