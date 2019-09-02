package com.example.pontoeletroniconew;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.widget.ExpandableListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.*;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import com.google.firebase.firestore.model.SnapshotVersion;
import com.google.firebase.firestore.model.mutation.MutationResult;
import com.google.firebase.firestore.remote.WriteStream;
import io.grpc.Status;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import static java.lang.Thread.sleep;


public class SearchListActivity extends Activity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    private HashMap<String,List<String>> listFireHash;
    private int qtdHeader;
    private ApontamentoDataSource source;
    private ImageButton floatButton;
    private List<Ponto> pontos = new ArrayList<Ponto>();
    private List<String> aux = new ArrayList<String>();
    private TreeSet<Date> cabs = new TreeSet<Date>();
    private ArrayList<String> ord = new ArrayList<String>();
    private SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.lista_apontamentos);
         swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.Swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },4000);
            }
        });

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        source = new ApontamentoDataSource(getApplicationContext());
        DisplayMetrics metrics = new DisplayMetrics();
        listView = (ExpandableListView)findViewById(R.id.lvExp);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        listView.setIndicatorBounds(width - GetDipsFromPixel(50), width - 40);




        initData();
        floatButton = (ImageButton) findViewById(R.id.btnAdd);
        Log.i("ListFirePos","antes");

        Log.i("ListFirePos",aux.toString());

        floatButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   Toast.makeText(getApplicationContext(),"Apontar",Toast.LENGTH_LONG).show();
                   Intent it = new Intent(SearchListActivity.this, Cadastro.class);
                   it.putExtra("tipo",0);
                   startActivity(it);
                }
            });


        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String data = listDataHeader.get(i);
                List<String> codFun = source.apontamentosDoDia(data);
                String aux = String.valueOf(codFun.get(i1).charAt(0)) + String.valueOf(codFun.get(i1).charAt(1)) + String.valueOf(codFun.get(i1).charAt(2));
                Log.i("HeaderChar0", String.valueOf(codFun.get(i1).charAt(0)));
                Log.i("HeaderChar1",String.valueOf(codFun.get(i1).charAt(1)));
                Log.i("HeaderChar2",String.valueOf(codFun.get(i1).charAt(2)));
                Log.i("HeaderFun",codFun.get(i1));
                Log.i("HeaderData",data);
                Log.i("HeaderChild",""+aux);
                int func = Integer.parseInt(aux);



               // Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                Intent it = new Intent(SearchListActivity.this, Apontamento.class);
                it.putExtra("formatada",data);
                it.putExtra("funcionario",func);
                startActivity(it);

                return false;
            }
        });

    }

    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void initData() {
        pontos.clear();
        aux.clear();
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        listFireHash = new HashMap<>();
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

        myRef.child("apontamentos").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Ponto p = objSnapshot.getValue(Ponto.class);
              //      Log.i("Filho",p.toString());
                      pontos.add(p);
                    final Calendar c = Calendar.getInstance();
                      Date d = null;
                    try {
                        d = f.parse(p.getDATA());
                        c.setTime(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cabs.add(c.getTime());


               //     Log.i("FilhosTam",""+pontos.size());
                }
                Iterator<Date> ite = cabs.descendingIterator();
                while(ite.hasNext()) {
                    ord.add(f.format(ite.next()));
                }
                qtdHeader = pontos.size();
                cabs.descendingIterator();
                Log.i("Cabeçalhos",ord.toString());
                Log.i("QtdCabeçalhos",""+cabs.size());
                Log.i("ListHeaderFire",""+qtdHeader);
                Log.i("ListFireHash4",listFireHash.toString());

                String dia1 = ord.get(0);

                //  if(pontos.size() > 0)
                //  {

                //  }
                //  else {
                //      qtdHeader = source.todasDatas().size();
                //     Log.i("ListHeaderSQLite",""+qtdHeader);
                // }



                for (int i = 0; i < 7; i++)
                {
                    // if(pontos.size() > 0) {
                    listDataHeader.add(ord.get(i));
                    Query q = null;
                    aux.clear();

                    q = FirebaseDatabase.getInstance().getReference().child("apontamentos").orderByChild("data").equalTo(ord.get(i));
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                                Ponto b = objSnapshot.getValue(Ponto.class);
                                Log.i("Pts",b.toString());
                                ContentValues valores;
                                long resultado;

                                SQLiteDatabase db = new DatabaseHelper(getApplicationContext()).getWritableDatabase();
                                Date d = null;
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    d = new SimpleDateFormat("dd/MM/yyyy").parse(b.getDATA());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            /*
                            valores = new ContentValues();
                            Log.i("Query",valores.toString());
                            Log.i("PInsert",b.toString());

                            valores = new ContentValues();
                            valores.put("DATA", format.format(d));
                            valores.put("CODFUNCIONATIO",b.getCODFUNCIONATIO());
                            valores.put("DESCRICAO",b.getDESCRICAO());

                            valores.put("APONT1", b.getAPONT1());
                            valores.put("GPS1", b.getGPS1());
                            valores.put("LOCAL1", b.getLOCAL1());


                            valores.put("APONT2", b.getAPONT2());
                            valores.put("GPS2", b.getGPS2());
                            valores.put("LOCAL2", b.getLOCAL2());

                            valores.put("APONT3", b.getAPONT3());
                            valores.put("GPS3", b.getGPS3());
                            valores.put("LOCAL3", b.getLOCAL3());

                            valores.put("APONT4", b.getAPONT4());
                            valores.put("GPS4", b.getGPS4());
                            valores.put("LOCAL4", b.getLOCAL4());


                            valores.put("APONTEXTRA", b.getAPONTEXTRA());
                            valores.put("GPSEXTRA", b.getGPSEXTRA());
                            valores.put("LOCALEXTRA", b.getLOCALEXTRA());


                            valores.put("APONTEXTRA2", b.getAPONTEXTRA2());
                            valores.put("GPSEXTRA2", b.getGPSEXTRA2());
                            valores.put("LOCALEXTRA2", b.getLOCALEXTRA2());

                            valores.put("ROWID",b.getROWID()); */
                                String replace = "INSERT OR REPLACE INTO apontamentos (data,codfuncionatio,descricao,apont1,local1,gps1,apont2,local2,gps2,apont3,local3,gps3,apont4,local4,gps4,apontextra,localextra,gpsextra,apontextra2,localextra2,gpsextra2,ROWID,DATACODFUNCIONATIO)\n" +
                                        "values('"+format.format(d)+" 00:00:00',"+b.getCODFUNCIONATIO()+",'"+b.getDESCRICAO()+"','"+b.getAPONT1()+"','"+b.getLOCAL1()+"','"+b.getGPS1()+"','"+b.getAPONT2()+"','"+b.getLOCAL2()+"','"+b.getGPS2()+"','"+b.getAPONT3()+"','"+b.getLOCAL3()+"','"+b.getGPS3()+"','"+b.getAPONT4()+"','"+b.getLOCAL4()+"','"+b.getGPS4()+"','"+b.getAPONTEXTRA()+"','"+b.getLOCALEXTRA()+"','"+b.getGPSEXTRA()+"','"+b.getAPONTEXTRA2()+"','"+b.getLOCALEXTRA2()+"','"+b.getGPSEXTRA2()+"',"+b.getROWID()+",'"+b.getDATACODFUNCIONATIO()+"');";
                                Log.i("QueryReplace",replace);
                                db.execSQL(replace);
                                db.close();
                            }
                            Log.i("ListFireHash3",aux.toString());


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //  }
                    //   else {
                    //     listDataHeader.add(source.todasDatas().get(i));
                    //   }
                    Log.i("ListHeaderText",ord.get(i));
                    listHash.put(ord.get(i),source.apontamentosDoDia(ord.get(i)));

                }
                Log.i("ListFire",pontos.toString());

                listAdapter = new ExpandableListAdapter(getApplicationContext(),listDataHeader,listHash);
                listView.setAdapter(listAdapter);
                Log.i("ListFireDps",aux.toString());
                Log.i("ListHeaderFire2",""+qtdHeader);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("The read failed: ",""+ databaseError.getCode());
            }
        });
        Log.i("ListFire2",pontos.toString());



    }

    public void apontamentoDoDiaFire(String dia)
    {

            //CountDownLatch done = new CountDownLatch(1);
            Query q = null;
            q = FirebaseDatabase.getInstance().getReference().child("apontamentos").orderByChild("data").equalTo(dia);
            Log.i("ListFireDay",dia);


            q.addValueEventListener (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> l = new ArrayList<>();
                    for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {

                        Ponto b = objSnapshot.getValue(Ponto.class);
                        Log.i("Pts",b.toString());
                        l.add(b.getDESCRICAO());

                    }

//                            Log.i("ListFireRd2",finalI+"Rd "+"Data:"+listDataHeader.get(finalI));
                    // listFireHash.put(ord.get(finalI),aux);

                            /*

                            if(finalI == 6)
                            {
                               Log.i("Chegou","Sim");
                                listAdapter = new ExpandableListAdapter(getApplicationContext(),listDataHeader,listFireHash);
                                listView.setAdapter(listAdapter);
                            }*/

                    aux.addAll(l);
                    Log.i("ListFireHash3",aux.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


        Log.i("ListFireHashDps",aux.toString());

    }

}
