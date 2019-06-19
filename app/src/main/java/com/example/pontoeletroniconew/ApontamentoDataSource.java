package com.example.pontoeletroniconew;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ApontamentoDataSource
{



    private SQLiteDatabase db;
    private DatabaseHelper helper;



    public ApontamentoDataSource(Context context)
    {
        helper = new DatabaseHelper(context);
        db = helper.getDatabase();
    }



    /*
     * Recuperando todas a linguagens cadastradas no nosso
     * banco de dados.
     * Iremos retorna-los em List<JSONObject>, pois é o
     * formato que o nosso adapter espera.
     */
    public List<JSONObject> todosApontamentos() {
           List<JSONObject> result = new ArrayList<JSONObject>();



// Iremos buscar todas as linguagens cadastradas no banco
// As colunas que iremos selecionar serão nome e descricao
// O objeto de retorno contém a referencias das linhas retornadas
        // String query = "select strftime('%d/%m/%Y',DATA), CODFUNCIONATIO from apontamentos order by data desc";
        String query = "select strftime('%d/%m/%Y',a.DATA), a.CODFUNCIONATIO,b.nome from apontamentos a inner join funcionarios b on (b.codfuncionatio = a.codfuncionatio) order by a.data desc";
        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            JSONObject obj = new JSONObject();

            try{
                // As colunas são recuperadas na ordem que foram selecionadas
                obj.put("DATA", cursor.getString(0));
                obj.put("CODFUNCIONATIO",cursor.getString(1));
                obj.put("nome",cursor.getString(2));
            }catch (JSONException e) {
            }

            result.add(obj);

            cursor.moveToNext();
        }

        cursor.close();
        return result;


    }

    public List<JSONObject> todosFuncionarios()
    {
        List<JSONObject> result = new ArrayList<JSONObject>();



    // Iremos buscar todas as linguagens cadastradas no banco
    // As colunas que iremos selecionar serão nome e descricao
    // O objeto de retorno contém a referencias das linhas retornadas
        String query = "select nome,codfuncionatio from funcionarios order by nome asc";
        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            JSONObject obj = new JSONObject();

            try
            {
                // As colunas são recuperadas na ordem que foram selecionadas
                obj.put( "Nome",cursor.getString(0));
                obj.put("Cod",cursor.getString(1));

            }
            catch (JSONException e)
            {

            }

            result.add(obj);

            cursor.moveToNext();
        }

        cursor.close();
        return result;


    }

    public List<JSONObject> todosFuncionarios(int func)
    {
        List<JSONObject> result = new ArrayList<JSONObject>();



        // Iremos buscar todas as linguagens cadastradas no banco
        // As colunas que iremos selecionar serão nome e descricao
        // O objeto de retorno contém a referencias das linhas retornadas
        String query = "select nome,codfuncionatio from funcionarios where codfuncionatio = "+func + " order by nome asc" ;
        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            JSONObject obj = new JSONObject();

            try
            {
                // As colunas são recuperadas na ordem que foram selecionadas
                obj.put( "Nome",cursor.getString(0));
                obj.put("Cod",cursor.getString(1));

            }
            catch (JSONException e)
            {

            }

            result.add(obj);

            cursor.moveToNext();
        }

        cursor.close();
        return result;


    }
}

