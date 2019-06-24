package com.example.pontoeletroniconew;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Cadastro extends Activity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1;
    private TextView hoje;
    private Spinner funcionario;
    private Button btnSalvar;
    private EditText local;
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private GPSTracker gps;
    private int codFunc;
    private JSONObject obj;
    private int registro;
    private Spinner reg;
    int tipo;
    private int funApontamentoActivity;
    private String dataReg;
    Date d;

    @SuppressLint("MissingPermission")
    public void onCreate(Bundle savedInstanceState) {
        codFunc = 23;
        d = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_main);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        tipo = savedInstanceState.getInt("tipo");
        funApontamentoActivity = savedInstanceState.getInt("funcionarioApontamento");
        dataReg = savedInstanceState.getString("dataApontamento");
        gps = new GPSTracker(this);
        Log.i("tipo",""+tipo);
        Log.i("funApontamentoActivity",""+funApontamentoActivity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            gps.showSettingsAlert();
        }

        if(!gps.canGetLocation())
        {
            gps.showSettingsAlert();
        }

        gps.refresh();
        latitude  = gps.getLatitude();
        longitude = gps.getLongitude();



        Log.i("API3",String.valueOf(latitude) + ", " + String.valueOf(longitude));

        local = (EditText) findViewById(R.id.Local);
        hoje  = (TextView) findViewById(R.id.DataHoje);
        if(dataReg != null)
        {
            hoje.setText(dataReg);
        }
        else
        {
            hoje.setText("" + format.format(Calendar.getInstance().getTime()));
        }
        funcionario = (Spinner) findViewById(R.id.Funcionario);
        reg = (Spinner) findViewById(R.id.Registro);
        loadSpinnerData(tipo);
        btnSalvar = (Button) findViewById(R.id.Salvar);
        btnSalvar.setOnClickListener(this);
        registro = Integer.parseInt(String.valueOf(reg.getSelectedItem().toString().charAt(0)));
        Log.i("SpinnerRegistro",""+registro);

    }

    private void loadSpinnerData(int tipo) {
        ApontamentoDataSource db = new ApontamentoDataSource(getApplicationContext());
        List<JSONObject> labels = null;
        if(tipo == 0) {
            labels = db.todosFuncionarios();
        }
        if(tipo == 1) {
            labels = db.todosFuncionarios(funApontamentoActivity);
        }
        // Creating adapter for spinner
        ArrayAdapter<JSONObject> dataAdapter = new ArrayAdapter<JSONObject>(this,android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        funcionario.setAdapter(dataAdapter);
    }


    public String insereDado(String data, int funcionario , String apont, String local, String gps, int registro){
        ContentValues valores;
        long resultado;

        SQLiteDatabase db = new DatabaseHelper(getApplicationContext()).getWritableDatabase();

        Cursor cursor = db.rawQuery("select codfuncionatio || ': ' || nome from funcionarios where codfuncionatio = "+funcionario,null);
        cursor.moveToFirst();
        String descricao = cursor.getString(0);

        valores = new ContentValues();
        valores.put("DATA", data);
        valores.put("CODFUNCIONATIO",funcionario);
        valores.put("DESCRICAO",descricao);
        if(registro == 1)
        {
            valores.put("APONT1", apont);
            valores.put("GPS1", gps);
            valores.put("LOCAL1", local);
        }
        else if(registro == 2)
        {
            valores.put("APONT2", apont);
            valores.put("GPS2", gps);
            valores.put("LOCAL2", local);
        }
        else if(registro == 3)
        {
            valores.put("APONT3", apont);
            valores.put("GPS3", gps);
            valores.put("LOCAL3", local);
        }
        else if(registro == 4)
        {
            valores.put("APONT4", apont);
            valores.put("GPS4", gps);
            valores.put("LOCAL4", local);
        }
        else if(registro == 5)
        {
            valores.put("APONTEXTRA", apont);
            valores.put("GPSEXTRA", gps);
            valores.put("LOCALEXTRA", local);
        }
        else if(registro == 6)
        {
            valores.put("APONTEXTRA2", apont);
            valores.put("GPSEXTRA2", gps);
            valores.put("LOCALEXTRA2", local);
        }


        Log.i("Query",valores.toString());
        resultado = db.insert("apontamentos", null, valores);

        db.close();

        if (resultado ==-1)
        {
            Toast.makeText(getApplicationContext(), "Erro ao inserir registro", Toast.LENGTH_LONG).show();
            return "Erro ao inserir registro";
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Registro Inserido com sucesso", Toast.LENGTH_LONG).show();
            return "Registro Inserido com sucesso";
        }
    }

    public void alteraDado(String data, int funcionario , String apont, String local, String gps, int registro){
        String cmpApont = null;
        String cmpGps = null;;
        String cmpLocal = null;;
        SQLiteDatabase db = new DatabaseHelper(getApplicationContext()).getWritableDatabase();

        if(registro == 1)
        {
            cmpApont = "APONT1";
            cmpGps = "GPS1";
            cmpLocal = "LOCAL1";
        }
        else if(registro == 2)
        {
            cmpApont = "APONT2";
            cmpGps = "GPS2";
            cmpLocal = "LOCAL2";
        }
        else if(registro == 3)
        {
            cmpApont = "APONT3";
            cmpGps = "GPS3";
            cmpLocal = "LOCAL3";
        }
        else if(registro == 4)
        {
            cmpApont = "APONT4";
            cmpGps = "GPS4";
            cmpLocal = "LOCAL4";
        }
        else if(registro == 5)
        {
            cmpApont = "APONTEXTRA";
            cmpGps = "GPSEXTRA";
            cmpLocal = "LOCALEXTRA";
        }
        else if(registro == 6)
        {
            cmpApont = "APONTEXTRA2";
            cmpGps   = "GPSEXTRA2";
            cmpLocal = "LOCALEXTRA2";
        }


        String update = "update apontamentos\n" +
                "set "+cmpApont+" = '"+data+"'||' '|| time(current_timestamp,'localtime'),\n" +
                "    "+cmpGps+" = '"+gps+"',\n" +
                "    "+cmpLocal+" = '"+local+"'\n" +
                "where codfuncionatio = "+funcionario+" and data = '"+data+"'||' '||'00:00:00'";
        Log.i("QueryUpdate",update);
        db.execSQL(update);

        db.close();
    }

    @Override
    public void onClick(View v)
    {

        try
        {
            obj = new JSONObject(funcionario.getSelectedItem().toString());
            codFunc = obj.getInt("Cod");
            Log.i("SpinnerFun",""+ codFunc);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.i("SpinnerFun","deu errado "+ funcionario.getSelectedItem().toString());
        }

        registro = Integer.parseInt(String.valueOf(reg.getSelectedItem().toString().charAt(0)));
        Log.i("SpinnerReg",""+registro);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.i("calendarHoraFormato",""+hora.format(Calendar.getInstance().getTime()));
        Log.i("calendarComFormato",""+format.format(Calendar.getInstance().getTime()));
        Log.i("Coordenadas",String.valueOf(latitude) + ", " + String.valueOf(longitude));
        if(dataReg != null)
        {


            try {
                d = new SimpleDateFormat("dd/MM/yyyy").parse(dataReg);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("dataRegFormat2",""+format.format(d));

        }
        if(tipo == 0)
        {
            insereDado(format.format(Calendar.getInstance().getTime())+" 00:00:00", codFunc, "" + hora.format(Calendar.getInstance().getTime()), local.getText().toString(), String.valueOf(latitude) + ", " + String.valueOf(longitude), registro);
        }
        if(tipo == 1)
        {

            alteraDado(""+format.format(d),funApontamentoActivity,"" + hora.format(Calendar.getInstance().getTime()),local.getText().toString(),String.valueOf(latitude) + ", " + String.valueOf(longitude),registro);

        }
        Toast.makeText(getApplicationContext(),"Apontar",Toast.LENGTH_LONG).show();
        Intent it = new Intent(Cadastro.this, SearchListActivity.class);
        startActivity(it);
    }

}






//