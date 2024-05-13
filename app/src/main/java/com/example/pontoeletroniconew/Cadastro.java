package com.example.pontoeletroniconew;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.*;
import android.net.ConnectivityManager;
import android.os.Bundle;


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_INTERNET = 1;
    private TextView hoje;
    private Spinner SpiFuncionario;
    private Button btnSalvar;
    private Button btnCancelar;
    private EditText local;
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private GPSTracker gps;
    private int codFunc;
    private String obj;
    private int registro;
    private Spinner reg;
    private String hr1;
    private String hr2;
    private String hr3;
    private String hr4;
    private String hre1;
    private String hre2;
    int tipo;
    private int funApontamentoActivity;
    private String dataReg;
    Date d;
    private long ROWIDapt;
    private List<String> fireFunc = new ArrayList<String>();
    private List<String> apontados = new ArrayList<String>();
    private List<String> registrosSpinner = new ArrayList<String>();
    private List<String> regUsed = new ArrayList<String>();
    @SuppressLint("MissingPermission")
    public void onCreate(Bundle savedInstanceState) {
        codFunc = 23;
        d = null;


        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.cadastro_main);
        setTitle("Apontamento");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_INTERNET);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        tipo = savedInstanceState.getInt("tipo");
        apontados = savedInstanceState.getStringArrayList("funcApontados");
        if (tipo == 1) {
            ROWIDapt = savedInstanceState.getLong("ROWID");
            hr1 = savedInstanceState.getString("hora1");
            hr2 = savedInstanceState.getString("hora2");
            hr3 = savedInstanceState.getString("hora3");
            hr4 = savedInstanceState.getString("hora4");
            hre1 = savedInstanceState.getString("horaextra");
            hre2 = savedInstanceState.getString("horaextra2");
            regUsed = savedInstanceState.getStringArrayList("regUsed");
        }
        funApontamentoActivity = savedInstanceState.getInt("funcionarioApontamento");
        dataReg = savedInstanceState.getString("dataApontamento");

        gps = new GPSTracker(this);
        Log.i("tipo", "" + tipo);
        Log.i("funApontamentoActivity", "" + funApontamentoActivity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("GPSs1","L1");
            gps.showSettingsAlert();
        }

        if (!gps.canGetLocation()) {
            Log.i("GPSs2","L2");
            gps.showSettingsAlert();
        }

        gps.refresh();
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();


        Log.i("API3", String.valueOf(latitude) + ", " + String.valueOf(longitude));

        local = (EditText) findViewById(R.id.Local);
        hoje = (TextView) findViewById(R.id.DataHoje);
        if (dataReg != null) {
            hoje.setText(dataReg);
        } else {
            hoje.setText("" + format.format(Calendar.getInstance().getTime()));
        }
        SpiFuncionario = (Spinner) findViewById(R.id.Funcionario);
        reg = (Spinner) findViewById(R.id.Registro);
        loadSpinnerData(tipo);

        registrosSpinner.add("1_Entrada");
        registrosSpinner.add("2_Saída para Almoço");
        registrosSpinner.add("3_Volta do Almoço");
        registrosSpinner.add("4_Saída");
        registrosSpinner.add("5_Entrada Extra");
        registrosSpinner.add("6_Saída Extra");

        Log.i("RegSpiner",registrosSpinner.size() +"  "+registrosSpinner.toString());
        if(tipo == 1)
        {
            List<String> aux = new ArrayList<String>();
            aux = regUsed;
            Log.i("regUsedArray",aux.toString());
            for(int i = 0; i < registrosSpinner.size(); i++)
            {
                for (int i2 = 0; i2 < aux.size(); i2++)
                {
                    if (aux.get(i2).equals(registrosSpinner.get(i)))
                    {
                        Log.i("regUsed","removeu: " +registrosSpinner.get(i));
                        registrosSpinner.remove(i);

                    }
                }
            }

        }

        ArrayAdapter<String> regAdapter = new ArrayAdapter<String>(Cadastro.this, android.R.layout.simple_spinner_item, registrosSpinner);
        reg.setAdapter(regAdapter);

        btnSalvar = (Button) findViewById(R.id.Salvar);
        btnCancelar = (Button) findViewById(R.id.Cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                local.setText("");
                SpiFuncionario.setSelection(0);
                reg.setSelection(0);
            }
        });
        btnSalvar.setOnClickListener(this);
        registro = Integer.parseInt(String.valueOf(reg.getSelectedItem().toString().charAt(0)));
        Log.i("SpinnerRegistro", "" + registro);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
          this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SuspiciousIndentation")
    private void loadSpinnerData(final int tipo) {
        final ApontamentoDataSource db = new ApontamentoDataSource(getApplicationContext());

        List<JSONObject> labels = null;

        fireFunc.clear();

        ArrayAdapter<String> dataAdapter;

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        try {
            database.setPersistenceEnabled(false);

        }
        catch (Exception e)
        {
             Toast.makeText(getApplicationContext(),"Erro Firebase",Toast.LENGTH_LONG).show();
        }
        final DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);

        Log.i("Referencia",""+database.getReference());

        Query q = null;

         q = myRef.child("funcionarios").orderByChild("ATIVO").equalTo("S");
                 q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Query query = null;

                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Funcionario f = objSnapshot.getValue(Funcionario.class);
                    // receber da activity principal lista de funcionarios para o dia atual
                    // comparar cada objeto recebido com eles caso não seja um deles adicione na lista se não pula
                    fireFunc.add(f.getCodNome());


                    }
               // query = myRef.child("funcionarios").equalTo(funApontamentoActivity);
               // query.addListenerForSingleValueEvent(this);
                //    Log.i("FuncUniq",""+ query.toString());
                if (tipo == 0) {
                    Log.i("fireFunc: ",fireFunc.toString());
                    Log.i("funcApontadosCad",apontados.toString());
                    for(int i = 0; i < fireFunc.size() ; i++ )
                    {
                        Log.i("funcApontadosFor1","For1 rodada:" + i);
                        for(int i2 = 0 ; i2 < apontados.size() ; i2++)
                        {
                            Log.i("funcApontadosFor2","For2 rodada:["+i+"][" + i2 +"]" );
                            Log.i("funcApontadosIf", fireFunc.get(i).toString() +" == "+ apontados.get(i2).toString());
                            if(fireFunc.get(i).equals(apontados.get(i2)))
                            {
                                fireFunc.remove(i);

                                Log.i("funcApontadosCad","removeu: "+apontados.get(i2));
                            }
                        }
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Cadastro.this, android.R.layout.simple_spinner_item, fireFunc);

                    SpiFuncionario.setAdapter(dataAdapter);
                }




                if (tipo == 1) {
                    query = myRef.child("funcionarios").orderByChild("CODFUNCIONATIO").equalTo(funApontamentoActivity);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            fireFunc.clear();
                            for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                                Funcionario f = objSnapshot.getValue(Funcionario.class);

                                fireFunc.add(f.getCodNome());

                            }
                            Log.i("fireFunc: ", fireFunc.toString());
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Cadastro.this, android.R.layout.simple_spinner_item, fireFunc);
                            SpiFuncionario.setAdapter(dataAdapter);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

/*
                    String fun = null;
                    for(int i = 0; i <= fireFunc.size(); i++)
                    {
                        String aux = fireFunc.get(i).substring(0,3);

                        int aux2 = Integer.parseInt(aux);

                        Log.i("ResultFunc",""+aux2);
                        if(aux2 == funApontamentoActivity)
                        {
                            fun = fireFunc.get(i);
                            i = (fireFunc.size() +1);
                        }
                    }

                    fireFunc.add(fun);*/
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });
        Log.i("fireFuncDPS: ",fireFunc.toString());
        /*

        if (tipo == 0) {
            labels = db.todosFuncionarios();
        }
        if (tipo == 1) {
            labels = db.todosFuncionarios(funApontamentoActivity);
        }
        // Creating adapter for spinner
        ArrayAdapter<JSONObject> dataAdapter = new ArrayAdapter<JSONObject>(this, android.R.layout.simple_spinner_item, labels);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        // attaching data adapter to spinner


    }

    public void insereDadoP(Ponto p)
    {
        ContentValues valores;
        long resultado;

        SQLiteDatabase db = new DatabaseHelper(getApplicationContext()).getWritableDatabase();

        Cursor cursor = db.rawQuery("select codfuncionatio || ': ' || nome from funcionarios where codfuncionatio = "+ SpiFuncionario,null);
        cursor.moveToFirst();
        String descricao = cursor.getString(0);

        valores = new ContentValues();
        valores.put("DATA", p.getDATA());
        valores.put("CODFUNCIONATIO",p.getCODFUNCIONATIO());
        valores.put("DESCRICAO",descricao);
        valores.put("APONT1",p.getAPONT1());
        valores.put("GPS1", p.getGPS1());
        valores.put("LOCAL1", p.getLOCAL1());
        valores.put("APONT2", p.getAPONT2());
        valores.put("GPS2", p.getGPS2());
        valores.put("LOCAL2", p.getLOCAL2());
        valores.put("APONT3", p.getAPONT3());
        valores.put("GPS3", p.getGPS3());
        valores.put("LOCAL3", p.getLOCAL3());
        valores.put("APONT4", p.getAPONT4());
        valores.put("GPS4", p.getGPS4());
        valores.put("LOCAL4", p.getLOCAL4());
        valores.put("APONTEXTRA", p.getAPONTEXTRA());
        valores.put("GPSEXTRA", p.getGPSEXTRA());
        valores.put("LOCALEXTRA", p.getLOCALEXTRA());
        valores.put("APONTEXTRA2", p.getAPONTEXTRA2());
        valores.put("GPSEXTRA2", p.getGPSEXTRA2());
        valores.put("LOCALEXTRA2", p.getGPSEXTRA2());
        valores.put("ROWID",p.getROWID());
        Log.i("Query",valores.toString());
        resultado = db.insert("apontamentos", null, valores);
        db.close();


        Date c = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            c = (Date) format.parse(p.getDATA());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(c != null) {
            p.setDATA(format.format(c));
        }
        Log.i("PInsert",p.toString());

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);








    }


    public String insereDado(String data, int funcionario , String apont, String local, String gps, int registro){
        ContentValues valores;
        long resultado;

        SQLiteDatabase db = new DatabaseHelper(getApplicationContext()).getWritableDatabase();

        Cursor cursor = db.rawQuery("select codfuncionatio || ': ' || nome from funcionarios where codfuncionatio = "+funcionario,null);
        cursor.moveToFirst();
        String descricao = SpiFuncionario.getSelectedItem().toString();

        Ponto p = new Ponto();

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);

        SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date c = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            c = (Date) entrada.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(c != null) {
            p.setDATA(format.format(c));
        }

        Log.i("Data Alt",p.getDATA());


        p.setROWID((long)(Math.random() * 2 * 9223372036854775.0 - 9223372036854776.0));
        p.setCODFUNCIONATIO(codFunc);
        p.setDESCRICAO(descricao);
        p.setDATACODFUNCIONATIO(p.getDATA()+"_"+codFunc);

        valores = new ContentValues();
        valores.put("DATA", data);
        valores.put("CODFUNCIONATIO",funcionario);
        valores.put("DESCRICAO",descricao);
        if(registro == 1)
        {
            valores.put("APONT1", apont);
            valores.put("GPS1", gps);
            valores.put("LOCAL1", local);
            p.setAPONT1(apont);
            p.setGPS1(gps);
            p.setLOCAL1(local);
        }
        else if(registro == 2)
        {
            valores.put("APONT2", apont);
            valores.put("GPS2", gps);
            valores.put("LOCAL2", local);
            p.setAPONT2(apont);
            p.setGPS2(gps);
            p.setLOCAL2(local);
        }
        else if(registro == 3)
        {
            valores.put("APONT3", apont);
            valores.put("GPS3", gps);
            valores.put("LOCAL3", local);
            p.setAPONT3(apont);
            p.setGPS3(gps);
            p.setLOCAL3(local);
        }
        else if(registro == 4)
        {
            valores.put("APONT4", apont);
            valores.put("GPS4", gps);
            valores.put("LOCAL4", local);
            p.setAPONT4(apont);
            p.setGPS4(gps);
            p.setLOCAL4(local);
        }
        else if(registro == 5)
        {
            valores.put("APONTEXTRA", apont);
            valores.put("GPSEXTRA", gps);
            valores.put("LOCALEXTRA", local);
            p.setAPONTEXTRA(apont);
            p.setGPSEXTRA(gps);
            p.setLOCALEXTRA(local);
        }
        else if(registro == 6)
        {
            valores.put("APONTEXTRA2", apont);
            valores.put("GPSEXTRA2", gps);
            valores.put("LOCALEXTRA2", local);
            p.setAPONTEXTRA2(apont);
            p.setGPSEXTRA2(gps);
            p.setLOCALEXTRA2(local);
        }
        valores.put("ROWID",p.getROWID());

        Log.i("Query",valores.toString());
        Log.i("PInsert",p.toString());
        resultado = db.insert("apontamentos", null, valores);

        try{
            myRef.child("apontamentos").child(""+p.getROWID()).setValue(p);
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Erro Inserir no Firebase",Toast.LENGTH_LONG).show();
        }

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

    public void alteraDado(final String data, final int funcionario , final String apont, final String local, final String gps, final int registro){
        final String[] cmpApont = {null};
        final String[] cmpGps = {null};
        final String[] cmpLocal = {null};


        final SQLiteDatabase db = new DatabaseHelper(getApplicationContext()).getWritableDatabase();
        Cursor cursor = db.rawQuery("select codfuncionatio || ': ' || nome from funcionarios where codfuncionatio = " + funcionario, null);
        cursor.moveToFirst();
        final String descricao = SpiFuncionario.getSelectedItem().toString();


        final Ponto[] p = {new Ponto()};

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);



        Log.i("PontoROWID",""+ROWIDapt+"\t "+"Reg "+registro);
        myRef.child("apontamentos").child(""+ROWIDapt).addValueEventListener(new ValueEventListener() {
           @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                p[0] = dataSnapshot.getValue(Ponto.class);
                try {
                    Log.i("Ponto", "" + p[0].toString());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

               SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd");
               Date c = null;
               SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
               try {
                   c = (Date) entrada.parse(data);
               } catch (ParseException e) {
                   e.printStackTrace();
               }
               if(c != null) {
                   p[0].setDATA(format.format(c));
               }

               Log.i("Data Alt",p[0].getDATA());


                p[0].setROWID(ROWIDapt);
                p[0].setCODFUNCIONATIO(codFunc);
                p[0].setDESCRICAO(descricao);
                p[0].setDATACODFUNCIONATIO(p[0].getDATA()+"_"+codFunc);

                if(registro == 1)
                {
                    cmpApont[0] = "APONT1";
                    cmpGps[0] = "GPS1";
                    cmpLocal[0] = "LOCAL1";
                    p[0].setAPONT1(apont);
                    p[0].setGPS1(gps);
                    p[0].setLOCAL1(local);
                }
                else if(registro == 2)
                {
                    cmpApont[0] = "APONT2";
                    cmpGps[0] = "GPS2";
                    cmpLocal[0] = "LOCAL2";
                    p[0].setAPONT2(apont);
                    p[0].setGPS2(gps);
                    p[0].setLOCAL2(local);
                }
                else if(registro == 3)
                {
                    cmpApont[0] = "APONT3";
                    cmpGps[0] = "GPS3";
                    cmpLocal[0] = "LOCAL3";
                    p[0].setAPONT3(apont);
                    p[0].setGPS3(gps);
                    p[0].setLOCAL3(local);
                }
                else if(registro == 4)
                {
                    cmpApont[0] = "APONT4";
                    cmpGps[0] = "GPS4";
                    cmpLocal[0] = "LOCAL4";
                    p[0].setAPONT4(apont);
                    p[0].setGPS4(gps);
                    p[0].setLOCAL4(local);
                }
                else if(registro == 5)
                {
                    cmpApont[0] = "APONTEXTRA";
                    cmpGps[0] = "GPSEXTRA";
                    cmpLocal[0] = "LOCALEXTRA";
                    p[0].setAPONTEXTRA(apont);
                    p[0].setGPSEXTRA(gps);
                    p[0].setLOCALEXTRA(local);
                }
                else if(registro == 6)
                {
                    cmpApont[0] = "APONTEXTRA2";
                    cmpGps[0] = "GPSEXTRA2";
                    cmpLocal[0] = "LOCALEXTRA2";
                    p[0].setAPONTEXTRA2(apont);
                    p[0].setGPSEXTRA2(gps);
                    p[0].setLOCALEXTRA2(local);
                }

                try{
                    myRef.child("apontamentos").child(""+p[0].getROWID()).setValue(p[0]);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Erro Inserir no Firebase",Toast.LENGTH_LONG).show();
                }


                String update = "update apontamentos\n" +
                        "set "+ cmpApont[0] +" = '"+data+"'||' '|| time(current_timestamp,'localtime'),\n" +
                        "    "+ cmpGps[0] +" = '"+gps+"',\n" +
                        "    "+ cmpLocal[0] +" = '"+local+"'\n" +
                        "where codfuncionatio = "+funcionario+" and data = '"+data+"'||' '||'00:00:00'";
                Log.i("QueryUpdate",update);

                db.execSQL(update);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("The read failed: ",""+ databaseError.getCode());
            }
        });
        Log.i("PontoFora",p[0].toString());


    }
    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    public void onClick(View v)
    {
        Log.i("isOnline1",Boolean.toString(isOnline()));
        Log.i("isOnline2",Boolean.toString((gps.canGetLocation() || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))));
        Log.i("isOnline3",Boolean.toString(!((String.valueOf(latitude).trim() + "," + String.valueOf(longitude).trim()).equals("0.0,0.0"))));
        if (isOnline() && (gps.canGetLocation() || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) && (!((String.valueOf(latitude).trim() + "," + String.valueOf(longitude).trim()).equals("0.0,0.0"))) ) {
                String lat;
                String longi;
                String compare;
                try {
                    obj = new String(SpiFuncionario.getSelectedItem().toString());
                    codFunc = Integer.parseInt(obj.substring(0, 3));
                    Log.i("SpinnerFun", "" + codFunc);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("SpinnerFun", "deu errado " + SpiFuncionario.getSelectedItem().toString() + "\n Cod: " + codFunc);
                }

                registro = Integer.parseInt(String.valueOf(reg.getSelectedItem().toString().charAt(0)));
                Log.i("SpinnerReg", "" + registro);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat hora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.i("calendarHoraFormato", "" + hora.format(Calendar.getInstance().getTime()));
                Log.i("calendarComFormato", "" + format.format(Calendar.getInstance().getTime()));
                Log.i("Coordenadas", String.valueOf(latitude) + ", " + String.valueOf(longitude));
                if (dataReg != null) {


                    try {
                        d = new SimpleDateFormat("dd/MM/yyyy").parse(dataReg);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.i("dataRegFormat2", "" + format.format(d));

                }


                if (tipo == 0) {
                    lat = String.valueOf(latitude).trim();
                    longi = String.valueOf(longitude).trim();
                    Log.i("LatLongi", lat + "," + longi);
                    compare = lat + "," + longi;
                    Log.i("LatLongi2", lat + "," + longi);
                    String LatLong = TimeAPIRequest.getTimeResponse(String.valueOf(latitude).trim(), String.valueOf(longitude).trim());
                    String dateTime  = LatLong.split("dateTime")[1].split("\":\"")[1].split(",\"")[0].split("\\.")[0].replace("T"," ");
                    String date = LatLong.split("\"date\":\"")[1].split("\",\"")[0];
                    Log.i("SUBSTRDATE",dateTime.substring(0,10));
                    insereDado(dateTime.substring(0,10) + " 00:00:00", codFunc, "" + dateTime, local.getText().toString(), lat + ", " + longi, registro);
                    //Log.i("DADOINSERINDO","" + hora.format(Calendar.getInstance().getTime()));

                }
                if (tipo == 1) {
                    lat = String.valueOf(latitude).trim();
                    longi = String.valueOf(longitude).trim();
                    Log.i("LatLongi", lat + "," + longi);
                    compare = lat + "," + longi;
                    Log.i("LatLongi2", lat + "," + longi);
                    String LatLong = TimeAPIRequest.getTimeResponse(String.valueOf(latitude).trim(), String.valueOf(longitude).trim());
                    String dateTime  = LatLong.split("dateTime")[1].split("\":\"")[1].split(",\"")[0].split("\\.")[0].replace("T"," ");
                    String date = LatLong.split("\"date\":\"")[1].split("\",\"")[0];
                    Log.i("SUBSTRDATE",dateTime.substring(0,10));
                    alteraDado("" + dateTime.substring(0,10) + " 00:00:00", funApontamentoActivity, "" + dateTime, local.getText().toString(), lat + ", " + longi, registro);

                }


                Toast.makeText(getApplicationContext(), "Apontar", Toast.LENGTH_LONG).show();
                Intent it = new Intent(Cadastro.this, SearchListActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(it);
            }
        else
        {
            if(!isOnline())
            {
                gps.showSettingsInternetAlert();
            }
            if((!gps.canGetLocation() || !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)))
            {
                gps.showSettingsAlert();
            }
            if(((String.valueOf(latitude).trim() + "," + String.valueOf(longitude).trim()).equals("0.0,0.0")))
            {
                gps.showSettingsAlert();
            }
        }
    }
}






//