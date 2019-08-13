package com.example.pontoeletroniconew;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.widget.Toast.LENGTH_LONG;


public class Apontamento extends Activity
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
        setContentView(R.layout.ponto_item);
        Intent intent = getIntent();
        savedInstanceState = intent.getExtras();

        dataReg  = savedInstanceState.getString("formatada");
        funcionario = savedInstanceState.getInt("funcionario");
        Log.i("dataReg",dataReg);
        Log.i("Funcionario",""+funcionario);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

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
                    startActivity(it);
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


    public String retornaHora(String apont)
    {
        String hora = null;

        if(apont == null)
        {
            hora = "";
        }
        else
        {
            SimpleDateFormat entrada = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date c = null;
            SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss");
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


                Toast.makeText(getApplicationContext(),""+ROWID, LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
