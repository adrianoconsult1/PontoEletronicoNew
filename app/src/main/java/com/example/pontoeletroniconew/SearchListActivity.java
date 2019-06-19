package com.example.pontoeletroniconew;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.ImageButton;



public class SearchListActivity extends Activity {



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
        // Recupera do banco as informações que serão uitlizados em nosso adapter
        apontamentos = source.todosApontamentos();

        // Passamos a lista de exemplo para gerar nosso adpater
        adapter = new ApontamentoArrayAdapter(getApplicationContext(), R.layout.principal, apontamentos);

        // Buscando o elemento Listview da nossa interface principal interface
        listaApontamentos = (ListView) findViewById(R.id.Lista);
        // Setando o adapter em nossa ListView
        listaApontamentos.setAdapter(adapter);

        // Setando callback ao selecionar um item da lista
        listaApontamentos.setOnItemClickListener(new OnItemClickListener()
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

        });

    }


}
