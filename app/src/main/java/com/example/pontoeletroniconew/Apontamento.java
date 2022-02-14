package com.example.pontoeletroniconew;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;


public class Apontamento extends AppCompatActivity
{
    private TextView data;
    private TextView func;
    private TextView hora1;
    private TextView hora2;
    private TextView hora3;
    private TextView hora4;
    private TextView horaextra;
    private TextView horaextra2;
    private TextView local1;
    private TextView local2;
    private TextView local3;
    private TextView local4;
    private TextView localextra;
    private TextView localextra2;
    private String dataDt;
    private String funcDt;
    private String hora1Dt;
    private String hora2Dt;
    private String hora3Dt;
    private String hora4Dt;
    private String horaextraDt;
    private String horaextra2Dt;
    private String local1Dt;
    private String local2Dt;
    private String local3Dt;

    private String gps1;
    private String gps2;
    private String gps3;
    private String gps4;



    private String gpsextra;
    private String gpsextra2;
    private String local4Dt;
    private String localextraDt;
    private String localextra2Dt;
    private int funcionario;
    private ImageButton floatButton;
    private String dataReg;
    private long ROWID;

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.ponto_item);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();
        setTitle("Ponto");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dataReg  = savedInstanceState.getString("formatada");
        funcionario = savedInstanceState.getInt("funcionario");
        Log.i("dataReg",dataReg);
        Log.i("Funcionario",""+funcionario);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");

        data        = (TextView) findViewById(R.id.dataDt);
        func        = (TextView) findViewById(R.id.funcionarioDt);
        hora1       = (TextView) findViewById(R.id.horario1Dt);
        hora2       = (TextView) findViewById(R.id.horario2Dt);
        hora3       = (TextView) findViewById(R.id.horario3Dt);
        hora4       = (TextView) findViewById(R.id.horario4Dt);
        horaextra   = (TextView) findViewById(R.id.horarioExtraDt);
        horaextra2  = (TextView) findViewById(R.id.horarioExtra2Dt);
        local1      = (TextView) findViewById(R.id.local1Dt);
        local2      = (TextView) findViewById(R.id.local2Dt);
        local3      = (TextView) findViewById(R.id.local3Dt);
        local4      = (TextView) findViewById(R.id.local4Dt);
        localextra  = (TextView) findViewById(R.id.localExtraDt);
        localextra2 = (TextView) findViewById(R.id.localExtra2Dt);
        floatButton = (ImageButton) findViewById(R.id.floatingActionButton);

        local1.setClickable(true);
        local2.setClickable(true);
        local3.setClickable(true);
        local4.setClickable(true);
        localextra.setClickable(true);
        localextra2.setClickable(true);

        local1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Localização", LENGTH_LONG).show();
                Intent it = new Intent(Apontamento.this, LocalizacaoPonto.class);
                it.putExtra("data",data.getText());
                it.putExtra("funcionario",func.getText());
                it.putExtra("hora",hora1.getText());
                it.putExtra("local",local1.getText());
                it.putExtra("rowid",ROWID);
                it.putExtra("codfun",funcionario);
                it.putExtra("gps",gps1);
                startActivity(it);

            }
        });

        local2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Localização", LENGTH_LONG).show();
                Intent it = new Intent(Apontamento.this, LocalizacaoPonto.class);
                it.putExtra("data",data.getText());
                it.putExtra("funcionario",func.getText());
                it.putExtra("hora",hora2.getText());
                it.putExtra("local",local2.getText());
                it.putExtra("rowid",ROWID);
                it.putExtra("codfun",funcionario);
                it.putExtra("gps",gps2);
                startActivity(it);
            }
        });

        local3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Localização", LENGTH_LONG).show();
                Intent it = new Intent(Apontamento.this, LocalizacaoPonto.class);
                it.putExtra("data",data.getText());
                it.putExtra("funcionario",func.getText());
                it.putExtra("hora",hora3.getText());
                it.putExtra("local",local3.getText());
                it.putExtra("rowid",ROWID);
                it.putExtra("codfun",funcionario);
                it.putExtra("gps",gps3);
                startActivity(it);
            }
        });
        local4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Localização", LENGTH_LONG).show();
                Intent it = new Intent(Apontamento.this, LocalizacaoPonto.class);
                it.putExtra("data",data.getText());
                it.putExtra("funcionario",func.getText());
                it.putExtra("hora",hora4.getText());
                it.putExtra("local",local4.getText());
                it.putExtra("rowid",ROWID);
                it.putExtra("codfun",funcionario);
                it.putExtra("gps",gps4);
                startActivity(it);
            }
        });
        localextra.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Localização", LENGTH_LONG).show();
                Intent it = new Intent(Apontamento.this, LocalizacaoPonto.class);
                it.putExtra("data",data.getText());
                it.putExtra("funcionario",func.getText());
                it.putExtra("hora",horaextra.getText());
                it.putExtra("local",localextra.getText());
                it.putExtra("rowid",ROWID);
                it.putExtra("codfun",funcionario);
                it.putExtra("gps",gpsextra);
                startActivity(it);
            }
        });
        localextra2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Localização", LENGTH_LONG).show();
                Intent it = new Intent(Apontamento.this, LocalizacaoPonto.class);
                it.putExtra("data",data.getText());
                it.putExtra("funcionario",func.getText());
                it.putExtra("hora",horaextra2.getText());
                it.putExtra("local",localextra2.getText());
                it.putExtra("rowid",ROWID);
                it.putExtra("codfun",funcionario);
                it.putExtra("gps",gpsextra2);
                startActivity(it);
            }
        });

        floatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date d = new Date();

                SimpleDateFormat dia = new SimpleDateFormat("dd/MM/yyyy");
                String cmp = dia.format(d);
                Log.i("IfDia",cmp + " = " + data.getText());

                if(cmp.equals(data.getText())) {
                    Toast.makeText(getApplicationContext(), "Apontar", LENGTH_LONG).show();
                    Intent it = new Intent(Apontamento.this, Cadastro.class);
                    it.putExtra("tipo", 1);
                    it.putExtra("funcionarioApontamento", funcionario);
                    it.putExtra("dataApontamento", dataReg);
                    it.putExtra("ROWID", ROWID);
                    List<String> regUsed = new ArrayList<String >();
                    if (!hora1.getText().equals(""))
                    {
                        regUsed.add("1_Entrada");
                    }
                    if(!hora2.getText().equals(""))
                    {
                        regUsed.add("2_Saída para Almoço");
                    }
                    if(!hora3.getText().equals(""))
                    {
                        regUsed.add("3_Volta do Almoço");
                    }
                    if(!hora4.getText().equals(""))
                    {
                        regUsed.add("4_Saída");
                    }
                    if (!horaextra.getText().equals("")) {
                        regUsed.add("5_Entrada Extra");
                    }
                    if(!horaextra2.getText().equals(""))
                    {
                        regUsed.add("6_Saída Extra");
                    }
                    it.putStringArrayListExtra("regUsed",(ArrayList<String>)regUsed);
                    if(regUsed.size() == 6)
                    {
                        ErrorAlert al = new ErrorAlert(Apontamento.this);
                        al.showErrorDialog("Apontamento Fechado","Todos Apontamentos estão Marcados, Para Alteração Contate o Administrador");
                    }
                    else
                    {
                        it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(it);
                    }
                }
                else
                {
                    ErrorAlert al = new ErrorAlert(Apontamento.this);
                    al.showErrorDialog("Apontamento Dia Anterior","O Sistema não permite apontamento de uma data retroativa");


                }
            }
        });

       /* SQLiteDatabase banco = new DatabaseHelper(getApplicationContext()).getWritableDatabase();

        Cursor cursor = banco.rawQuery(queryItem(dataReg,funcionario), null);
        cursor.moveToFirst(); */

        preencheApontamento(dataReg,funcionario);


        /*
        data.setText(cursor.getString(0));
        func.setText(cursor.getString(1));
        hora1.setText(cursor.getString(2));
        hora2.setText(cursor.getString(3));
        hora3.setText(cursor.getString(4));
        hora4.setText(cursor.getString(5));
        horaextra.setText(cursor.getString(6));
        horaextra2.setText(cursor.getString(7));
        local1.setText(cursor.getString(8));
        local2.setText(cursor.getString(9));
        local3.setText(cursor.getString(10));
        local4.setText(cursor.getString(11));
        localextra.setText(cursor.getString(12));
        localextra2.setText(cursor.getString(13));
        ROWID = cursor.getLong(20);

        Toast.makeText(this,""+ROWID, Toast.LENGTH_LONG).show(); */


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

    public String retornaHora(String apont)
    {
        String hora = null;

        if(apont == null)
        {
            hora = "";
        }
        else
        {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date c = null;
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            try {
                c = (Date) entrada.parse(apont);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(c != null) {
                hora = format.format(c);
            }
            else
            {
                hora = "                ";
            }
            Log.i("HoraRetornada",""+hora);
        }

        return hora;
    }



    public void preencheApontamento(String dia, int codFuncionario)
    {
        final Ponto[] p = {new Ponto()};

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);

        Query q = myRef.child("apontamentos").orderByChild("datacodfuncionatio").equalTo(dia+"_"+codFuncionario);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren())
                {
                    Ponto pt = objSnapshot.getValue(Ponto.class);
                    p[0] = pt;
                }
                Log.i("PontoRec",p[0].toString());

                data.setText(p[0].getDATA());
                func.setText(p[0].getDESCRICAO());
                hora1.setText(retornaHora(p[0].getAPONT1()));
                hora2.setText(retornaHora(p[0].getAPONT2()));
                hora3.setText(retornaHora(p[0].getAPONT3()));
                hora4.setText(retornaHora(p[0].getAPONT4()));
                horaextra.setText(retornaHora(p[0].getAPONTEXTRA()));
                horaextra2.setText(retornaHora(p[0].getAPONTEXTRA2()));
                local1.setText(p[0].getLOCAL1());
                local2.setText(p[0].getLOCAL2());
                local3.setText(p[0].getLOCAL3());
                local4.setText(p[0].getLOCAL4());
                localextra.setText(p[0].getLOCALEXTRA());
                localextra2.setText(p[0].getLOCALEXTRA2());
                ROWID = p[0].getROWID();
                gps1 = p[0].getGPS1();
                gps2 = p[0].getGPS2();
                gps3 = p[0].getGPS3();
                gps4 = p[0].getGPS4();
                gpsextra = p[0].getGPSEXTRA();
                gpsextra2 = p[0].getGPSEXTRA2();

                Toast.makeText(getApplicationContext(),""+ROWID, LENGTH_LONG).show();
                Log.i("hora1",""+hora1.getText());
                Log.i("hora2",""+hora2.getText());
                Log.i("hora3",""+hora3.getText());
                Log.i("hora4",""+hora4.getText());
                Log.i("horaextra",""+horaextra.getText());
                Log.i("horaextra2",""+horaextra2.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public String getGps1() {
        return gps1;
    }

    public void setGps1(String gps1) {
        this.gps1 = gps1;
    }

    public String getGps2() {
        return gps2;
    }

    public void setGps2(String gps2) {
        this.gps2 = gps2;
    }

    public String getGps3() {
        return gps3;
    }

    public void setGps3(String gps3) {
        this.gps3 = gps3;
    }

    public String getGps4() {
        return gps4;
    }

    public void setGps4(String gps4) {
        this.gps4 = gps4;
    }

    public String getGpsextra() {
        return gpsextra;
    }

    public void setGpsextra(String gpsextra) {
        this.gpsextra = gpsextra;
    }

    public String getGpsextra2() {
        return gpsextra2;
    }

    public void setGpsextra2(String gpsextra2) {
        this.gpsextra2 = gpsextra2;
    }

    public String queryItem(String data, int funcionario)
    {
        SimpleDateFormat saida = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy");
        Date newDate = null;
        try
        {
            newDate = entrada.parse(data);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        String dataFormatada = saida.format(newDate);
        Log.i("DataIntent",""+dataFormatada);




        String selectQuery = "Select distinct strftime('%d/%m/%Y',a.data),b.nome,time(a.APONT1),time(a.APONT2),time(a.APONT3),time(a.APONT4),time(a.APONTEXTRA),time(a.APONTEXTRA2),a.LOCAL1,a.LOCAL2,a.LOCAL3,a.LOCAL4," +
                "a.LOCALextra,a.LOCALextra2,a.GPS1,a.GPS2,a.GPS3,a.GPS4,a.GPSextra,a.GPSextra2,a.ROWID from apontamentos a left outer join funcionarios b on (a.CODFUNCIONATIO = b.codfuncionatio) where a.CODFUNCIONATIO = "+ funcionario +" and CAST(strftime('%s', a.DATA)  AS  integer) = CAST(strftime('%s', '"+dataFormatada+"')  AS  integer)";
        Log.i("queryItem",selectQuery);
        return selectQuery;
    }
}
