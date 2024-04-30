package com.example.pontoeletroniconew;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ExpandableListView;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.*;
import com.firebase.client.Firebase;
import com.firebase.client.annotations.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import static java.lang.Thread.sleep;


public class SearchListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


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
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Bundle b = new Bundle();

    private FirebaseAuthentication firebaseAuthentication;
    String dayLast = null;
    private static final String MY_PASSWORD_DIALOG_ID = "RHPONTOMASTER";
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Log.i("SLA1","SLA1");
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        Log.i("SLA2","SLA2");
        b = savedInstanceState;
        Log.i("SLA3","SLA3");
        setContentView(R.layout.lista_apontamentos);
        Log.i("SLA4","SLA4");
        setTitle("Ponto Eletrônico");

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.barraMain);

        setSupportActionBar(toolbar);
        Log.i("SLA5","SLA5");
        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        Log.i("SLA6","SLA6");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Log.i("SLA7","SLA7");


         swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.Swipe);
        Log.i("SLA8","SLA8");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                drawerLayout.refreshDrawableState();
                listAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Atualizando Tela Inicial ",Toast.LENGTH_LONG).show();
             /* Intent it = new Intent(SearchListActivity.this, SearchListActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY );
                startActivity(it); */
                Log.i("SLA9","SLA9");
                recreate();
                Log.i("SLA10","SLA10");
               //  initData();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("SLA11","SLA11");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },4000);
                Log.i("SLA12","SLA12");



            }
        });

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple);
        Log.i("SLA13","SLA13");
        source = new ApontamentoDataSource(getApplicationContext());
        DisplayMetrics metrics = new DisplayMetrics();
        listView = (ExpandableListView)findViewById(R.id.lvExp);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        Log.i("SLA14","SLA14");
        listView.setIndicatorBounds(width - GetDipsFromPixel(50), width - 40);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        listView.setLayoutAnimation(animation);
        Log.i("SLA15","SLA15");


        initData();
        floatButton = (ImageButton) findViewById(R.id.btnAdd);
        Log.i("ListFirePos","antes");
        Log.i("SLA16","SLA16");

        Log.i("ListFirePos",aux.toString());

        floatButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   Toast.makeText(getApplicationContext(),"Apontar",Toast.LENGTH_LONG).show();
                   Intent it = new Intent(SearchListActivity.this, Cadastro.class);
                   //incluir lista de pessoas apontadas no dia coomo put extra, verificar se to primeiro item da arvore é o dia atual mesmo
                   // cuidar para que esteja atualizado os aponts

                    it.putExtra("tipo",0);
                   SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                   List<String> a = new ArrayList<>();
                   a = source.apontamentosDoDia(format.format(Calendar.getInstance().getTime()));
                   it.putStringArrayListExtra ("funcApontados",(ArrayList<String>) a);
                   Log.i("funcApontados",format.format(Calendar.getInstance().getTime())+"   "+a.toString());
                   it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
                it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    private void initData() {
        pontos.clear();
        aux.clear();
        Log.i("SEARCH1","SEARCH1");
        Firebase.setAndroidContext(this);
        Log.i("SEARCH2","SEARCH2");
        FirebaseApp.initializeApp(getApplicationContext());
        Log.i("SEARCH3","SEARCH3");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.i("SEARCH4","SEARCH4");
        String userEmail = "pontoeletronicoaj@gmail.com";
        String userPassword = "2AJ@eletronico";
        firebaseAuthentication.authenticateUser(userEmail, userPassword);
        Log.i("SEARCH5","SEARCH5");
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        listFireHash = new HashMap<>();
        try {
            database.setPersistenceEnabled(true);
            Log.i("SEARCH6","SEARCH6");
        }
        catch (Exception e)
        {
          //  Toast.makeText(getApplicationContext(),"Fire Off",Toast.LENGTH_LONG).show();
        }
        final DatabaseReference myRef = database.getReference();
        Log.i("SEARCH7","SEARCH7");
        myRef.keepSynced(true);
        Log.i("SEARCH8",""+database.getReference());

        myRef.child("apontamentos").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Ponto p = objSnapshot.getValue(Ponto.class);
                    Log.i("Filho",p.toString());
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


                    Log.i("FilhosTam",""+pontos.size());
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
                    Log.i("ListFireHash44",listFireHash.toString());
                    // if(pontos.size() > 0) {
                    listDataHeader.add(ord.get(i));
                    Query q = null;
                    aux.clear();
                    Log.i("ListFireHash444",listFireHash.toString());
                    q = FirebaseDatabase.getInstance().getReference().child("apontamentos").orderByChild("data").equalTo(ord.get(i));
                    Log.i("ListFireHash4444",listFireHash.toString());
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                                Ponto b = objSnapshot.getValue(Ponto.class);
                                Log.i("ListFireHash44444",b.toString());
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
                               String l1;
                               String l2;
                               String l3;
                               String l4;
                               String le;
                               String le2;

                               try {
                                   l1 = b.getLOCAL1().replace("'", " ");
                               }
                               catch(Exception e)
                               {
                                   System.out.println(e);
                                   l1 = "";
                               }


                            try {
                                l2 = b.getLOCAL2().replace("'", " ");
                            }
                            catch(Exception e)
                            {
                                System.out.println(e);
                                l2 = "";
                            }
                            try {
                                l3 = b.getLOCAL3().replace("'", " ");
                            }
                            catch(Exception e)
                            {
                                System.out.println(e);
                                l3 = "";
                            }

                            try {
                                l4 = b.getLOCAL4().replace("'", " ");
                            }
                            catch(Exception e)
                            {
                                System.out.println(e);
                                l4 = "";
                            }
                                try {
                                    le = b.getLOCALEXTRA().replace("'", " ");
                                }
                                catch(Exception e)
                                {
                                    System.out.println(e);
                                    le = "";
                                }
                                try {
                                    le2 = b.getLOCALEXTRA2().replace("'", " ");
                                }
                                catch(Exception e)
                                {
                                    System.out.println(e);
                                    le2 = "";
                                }

                                String replace = "INSERT OR REPLACE INTO apontamentos (data,codfuncionatio,descricao,apont1,local1,gps1,apont2,local2,gps2,apont3,local3,gps3,apont4,local4,gps4,apontextra,localextra,gpsextra,apontextra2,localextra2,gpsextra2,ROWID,DATACODFUNCIONATIO)\n" +
                                        "values('"+format.format(d)+" 00:00:00',"+b.getCODFUNCIONATIO()+",'"+b.getDESCRICAO()+"','"+b.getAPONT1()+"','"+l1+"','"+b.getGPS1()+"','"+b.getAPONT2()+"','"+l2+"','"+b.getGPS2()+"','"+b.getAPONT3()+"','"+l3+"','"+b.getGPS3()+"','"+b.getAPONT4()+"','"+l4+"','"+b.getGPS4()+"','"+b.getAPONTEXTRA()+"','"+le+"','"+b.getGPSEXTRA()+"','"+b.getAPONTEXTRA2()+"','"+le2+"','"+b.getGPSEXTRA2()+"',"+b.getROWID()+",'"+b.getDATACODFUNCIONATIO()+"');";
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
                Log.i("FireRoundPre",""+listDataHeader.size());

                List<String> keys = new ArrayList<>(listHash.keySet());
                Log.i("FireRoundListDataHeader",""+ keys.get(0) );
                getUltimoDiaApont(new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        Log.i("FireRoundListMethod",""+value.toString());


                    }
                });

                listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listHash);
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

    public void getUltimoDiaApont(MyCallback myCallback)
    {


        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        try {
            database.setPersistenceEnabled(true);

        }
        catch (Exception e)
        {
            //  Toast.makeText(getApplicationContext(),"Fire Off",Toast.LENGTH_LONG).show();
        }
        final DatabaseReference myRef = database.getReference();
        myRef.keepSynced(true);
        Log.i("Referencia2",""+database.getReference());

        myRef.child("apontamentos").orderByChild("data").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Ponto p = objSnapshot.getValue(Ponto.class);
                    dayLast = p.getDATA();
                    Log.i("FireRoundDay:",dayLast);
                    final Calendar c = Calendar.getInstance();
                    Date d = null;
                    try {
                        d = f.parse(p.getDATA());
                        c.setTime(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cabs.add(c.getTime());
                }
                Iterator<Date> ite = cabs.descendingIterator();
                while(ite.hasNext()) {
                    ord.add(f.format(ite.next()));
                }
                dayLast = ord.get(0);
                for(int i = 0; i < 7; i++)
                {
                    listHash.put(ord.get(i),source.apontamentosDoDia(ord.get(i)));

                }
                myCallback.onCallback(dayLast);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    public boolean hashUpdated(String value, HashMap<String,List<String>> listAux)
    {
        boolean result = false;
        List<String> keys = new ArrayList<>(listAux.keySet());

        for(int i = 0; i < keys.size(); i++)
        {
            if(keys.get(i).equals(value) )
            {
                result = true;
                i = listAux.size();
            }
        }

        return result;
    }

    public List<String> apontamentoDoDiaFire(String dia)
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
        return aux;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_one: {
                Toast.makeText(this, "Funcionarios", Toast.LENGTH_SHORT).show();
              /*  Intent it = new Intent(SearchListActivity.this, MainActivity.class);
                startActivity(it);*/
                break;
            }
            case R.id.nav_item_two: {
                Toast.makeText(this, "Ex Funcionarios", Toast.LENGTH_SHORT).show();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_signin, (ViewGroup) findViewById(R.id.telaSenha) );
                final EditText password1 = (EditText) layout.findViewById(R.id.password);
                final TextView error = (TextView) layout.findViewById(R.id.TextView_PwdProblem);

                password1.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        String strPass1 = password1.getText().toString();

                        if (strPass1.equals(MY_PASSWORD_DIALOG_ID)) {
                            error.setText("Logar");
                        } else {
                            error.setText("Senha Incorreta");
                        }
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Senha");
                builder.setView(layout);

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeDialog(R.layout.dialog_signin);
                    }
                });

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String strPassword1 = password1.getText().toString();

                        if (strPassword1.equals(MY_PASSWORD_DIALOG_ID)) {
                            Toast.makeText(getApplicationContext(),
                                    "Logado Com Sucesso!!!", Toast.LENGTH_SHORT).show();
                        }
                        removeDialog(R.layout.dialog_signin);
                    }
                });

                AlertDialog passwordDialog = builder.create();
                passwordDialog.show();

                /*  Intent it = new Intent(SearchListActivity.this, MainActivity.class);
                startActivity(it);*/
                break;
            }
            case R.id.nav_item_three: {
                Toast.makeText(this, "Relatório", Toast.LENGTH_SHORT).show();

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.dialog_signin, (ViewGroup) findViewById(R.id.telaSenha) );
                final EditText password1 = (EditText) layout.findViewById(R.id.password);
                final TextView error = (TextView) layout.findViewById(R.id.TextView_PwdProblem);

                password1.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        String strPass1 = password1.getText().toString();

                        if (strPass1.equals(MY_PASSWORD_DIALOG_ID)) {
                        error.setText("Logar");
                        } else {
                            error.setText("Senha Incorreta");
                        }
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Senha");
                builder.setView(layout);

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeDialog(R.layout.dialog_signin);
                    }
                });

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String strPassword1 = password1.getText().toString();

                        if (strPassword1.equals(MY_PASSWORD_DIALOG_ID)) {
                            Toast.makeText(getApplicationContext(),
                                    "Logado Com Sucesso!!!", Toast.LENGTH_SHORT).show();
                        }
                        removeDialog(R.layout.dialog_signin);
                    }
                });

                AlertDialog passwordDialog = builder.create();
                passwordDialog.show();





                break;
            }
            default: {
                Toast.makeText(this, "Menu Default", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void recreate()
    {
        if (android.os.Build.VERSION.SDK_INT >= 11)
        {
            super.recreate();
        }
        else
        {
            startActivity(getIntent());
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public interface MyCallback {
        void onCallback(String value);
    }




}
