package com.example.pontoeletroniconew;

import android.widget.ExpandableListView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.*;
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
    private int qtdHeader;
    private ListView listaApontamentos;
    private List<JSONObject> apontamentos;
    private ApontamentoArrayAdapter adapter;
    private ApontamentoDataSource source;
    private ImageButton floatButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_apontamentos);
        source = new ApontamentoDataSource(getApplicationContext());
        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        floatButton = (ImageButton) findViewById(R.id.btnAdd);
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);
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
        // Recupera do banco as informações que serão uitlizados em nosso adapter

        /*
        // Passamos a lista de exemplo para gerar nosso adpater
        adapter = new ApontamentoArrayAdapter(getApplicationContext(), R.layout.principal, apontamentos);

        // Buscando o elemento Listview da nossa interface principal interface
        listaApontamentos = (ListView) findViewById(R.id.Lista);
        // Setando o adapter em nossa ListView
        listaApontamentos.setAdapter(adapter);*/

        // Setando callback ao selecionar um item da lista
       /* listView.setOnItemClickListener(new OnItemClickListener()
        {

             public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                try
                {
                    JSONObject apontamento = apontamentos.get(position);
                    String data = apontamento.getString("DATA");
                    int func = Integer.parseInt(apontamento.getString("CODFUNCIONATIO"));


                    Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                    Intent it = new Intent(SearchListActivity.this, Apontamento.class);
                    it.putExtra("formatada",data);
                    it.putExtra("funcionario",func);
                    startActivity(it);
                }
                catch (JSONException e)
                {

                }
            }

        });*/

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


                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
                Intent it = new Intent(SearchListActivity.this, Apontamento.class);
                it.putExtra("formatada",data);
                it.putExtra("funcionario",func);
                startActivity(it);
                return false;
            }
        });

    }

    private void initData() {

        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        qtdHeader = source.todasDatas().size();

        for (int i = 0; i < qtdHeader; i++)
        {
            listDataHeader.add(source.todasDatas().get(i));
            Log.i("ListHeaderText",listDataHeader.get(i));
            listHash.put(listDataHeader.get(i),source.apontamentosDoDia(listDataHeader.get(i)));
        }



       /* listDataHeader.add("EDMTDev");
        listDataHeader.add("Android");
        listDataHeader.add("Xamarin");
        listDataHeader.add("UWP");

        List<String> edmtDev = new ArrayList<>();
        edmtDev.add("This is Expandable ListView");

        List<String> androidStudio = new ArrayList<>();
        androidStudio.add("Expandable ListView");
        androidStudio.add("Google Map");
        androidStudio.add("Chat Application");
        androidStudio.add("Firebase ");

        List<String> xamarin = new ArrayList<>();
        xamarin.add("Xamarin Expandable ListView");
        xamarin.add("Xamarin Google Map");
        xamarin.add("Xamarin Chat Application");
        xamarin.add("Xamarin Firebase ");

        List<String> uwp = new ArrayList<>();
        uwp.add("UWP Expandable ListView");
        uwp.add("UWP Google Map");
        uwp.add("UWP Chat Application");
        uwp.add("UWP Firebase ");

        listHash.put(listDataHeader.get(0),edmtDev);
        listHash.put(listDataHeader.get(1),androidStudio);
        listHash.put(listDataHeader.get(2),xamarin);
        listHash.put(listDataHeader.get(3),uwp); */
    }


}
