package com.example.pontoeletroniconew;

import android.support.annotation.NonNull;
import android.widget.ExpandableListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.*;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.*;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;


public class SearchListActivity extends Activity {


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
        source = new ApontamentoDataSource(getApplicationContext());
        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        floatButton = (ImageButton) findViewById(R.id.btnAdd);

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

    private void initData() {
        pontos.clear();
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
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

        myRef.child("apontamentos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                    Ponto p = objSnapshot.getValue(Ponto.class);
              //      Log.i("Filho",p.toString());
                      pontos.add(p);
                    Calendar c = Calendar.getInstance();
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
                listDataHeader = new ArrayList<>();
                listHash = new HashMap<>();
                listFireHash = new HashMap<>();
                Query q = null;
                //  if(pontos.size() > 0)
                //  {

                //  }
                //  else {
                //      qtdHeader = source.todasDatas().size();
                //     Log.i("ListHeaderSQLite",""+qtdHeader);
                // }
                Log.i("ListHeaderFire2",""+qtdHeader);
                for (int i = 0; i < source.todasDatas().size(); i++)
                {
                    // if(pontos.size() > 0) {
                    listDataHeader.add(source.todasDatas().get(i));
                    //  }
                    //   else {
                    //     listDataHeader.add(source.todasDatas().get(i));
                    //   }
                    Log.i("ListHeaderText",listDataHeader.get(i));
                    listHash.put(listDataHeader.get(i),source.apontamentosDoDia(listDataHeader.get(i)));
                q = FirebaseDatabase.getInstance().getReference().child("apontamentos").orderByChild("data").equalTo(listDataHeader.get(i));
                    final int finalI = i;

                    q.addValueEventListener (new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot objSnapshot:dataSnapshot.getChildren()) {
                                Ponto b = objSnapshot.getValue(Ponto.class);
                                Log.i("Pts",b.toString());
                                aux.add(b.getDESCRICAO());

                            }
                            Log.i("ListF","Data:"+listDataHeader.get(finalI)+"  "+aux.toString());
                            listFireHash.put(listDataHeader.get(finalI),aux);
                            Log.i("ListFireHash",listFireHash.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });
                }
                Log.i("ListFire",pontos.toString());

                listAdapter = new ExpandableListAdapter(getApplicationContext(),listDataHeader,listHash);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("The read failed: ",""+ databaseError.getCode());
            }
        });
        Log.i("ListFire2",pontos.toString());



    }


}
