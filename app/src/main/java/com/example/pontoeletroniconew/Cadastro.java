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


import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import kotlin.jvm.functions.FunctionN;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.text.SimpleDateFormat.*;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1;
    private TextView hoje;
    private Spinner funcionario;
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
    int tipo;
    private int funApontamentoActivity;
    private String dataReg;
    Date d;
    private long ROWIDapt;
    private List<String> fireFunc = new ArrayList<String>();

    @SuppressLint("MissingPermission")
    public void onCreate(Bundle savedInstanceState) {
        codFunc = 23;
        d = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_main);
        setTitle("Apontamento");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        tipo = savedInstanceState.getInt("tipo");
        if (tipo == 1) {
            ROWIDapt = savedInstanceState.getLong("ROWID");

        }
        funApontamentoActivity = savedInstanceState.getInt("funcionarioApontamento");
        dataReg = savedInstanceState.getString("dataApontamento");
        gps = new GPSTracker(this);
        Log.i("tipo", "" + tipo);
        Log.i("funApontamentoActivity", "" + funApontamentoActivity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps.showSettingsAlert();
        }

        if (!gps.canGetLocation()) {
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
        funcionario = (Spinner) findViewById(R.id.Funcionario);
        reg = (Spinner) findViewById(R.id.Registro);
        loadSpinnerData(tipo);
        btnSalvar = (Button) findViewById(R.id.Salvar);
        btnCancelar = (Button) findViewById(R.id.Cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                local.setText("");
                funcionario.setSelection(0);
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

    private void loadSpinnerData(final int tipo) {
        final ApontamentoDataSource db = new ApontamentoDataSource(getApplicationContext());

        List<JSONObject> labels = null;

        fireFunc.clear();

        ArrayAdapter<String> dataAdapter;

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        try {
            database.setPersistenceEnabled(true);

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
                    fireFunc.add(f.getCodNome());


                    }
               // query = myRef.child("funcionarios").equalTo(funApontamentoActivity);
               // query.addListenerForSingleValueEvent(this);
                //    Log.i("FuncUniq",""+ query.toString());
                if (tipo == 0) {
                    Log.i("fireFunc: ",fireFunc.toString());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Cadastro.this, android.R.layout.simple_spinner_item, fireFunc);
                    funcionario.setAdapter(dataAdapter);
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
                            funcionario.setAdapter(dataAdapter);
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

        Cursor cursor = db.rawQuery("select codfuncionatio || ': ' || nome from funcionarios where codfuncionatio = "+funcionario,null);
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
        String descricao = cursor.getString(0);

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
        final String descricao = cursor.getString(0);


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

    @Override
    public void onClick(View v)
    {

        try
        {
            obj = new String(funcionario.getSelectedItem().toString());
            codFunc = Integer.parseInt(obj.substring(0,3));
            Log.i("SpinnerFun",""+ codFunc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i("SpinnerFun","deu errado "+ funcionario.getSelectedItem().toString() +"\n Cod: "+codFunc);
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