package com.example.pontoeletroniconew;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static android.os.SystemClock.sleep;

public class ApontamentoDataSource
{



    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private int qtdHeader;
    private int qtdAptDia;



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

    public ArrayList<String> todasDatas() {
        ArrayList<String> result = new ArrayList<String>();



// Iremos buscar todas as linguagens cadastradas no banco
// As colunas que iremos selecionar serão nome e descricao
// O objeto de retorno contém a referencias das linhas retornadas
        // String query = "select strftime('%d/%m/%Y',DATA), CODFUNCIONATIO from apontamentos order by data desc";
        String query = "select distinct strftime('%d/%m/%Y',a.DATA) from apontamentos a order by a.Data desc";
        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String obj = cursor.getString(0);


            result.add(obj);

            cursor.moveToNext();
        }

        cursor.close();
        return result;


    }

    public List<String> apontamentosDoDia(String dia) {
        ArrayList<String> result = new ArrayList<String>();

        SimpleDateFormat saida = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat entrada = new SimpleDateFormat("dd/MM/yyyy");
        Date newDate = null;
        try
        {
            newDate = entrada.parse(dia);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        String dataFormatada = saida.format(newDate);
        Log.i("DataHeader",""+dataFormatada);


        // Iremos buscar todas as linguagens cadastradas no banco
        // As colunas que iremos selecionar serão nome e descricao
        // O objeto de retorno contém a referencias das linhas retornadas
        // String query = "select strftime('%d/%m/%Y',DATA), CODFUNCIONATIO from apontamentos order by data desc";
        String query = "select descricao from apontamentos a where a.Data = '"+dataFormatada+" 00:00:00' order by a.Data desc";
        Log.i("DiaQuery",query);
        Cursor cursor = db.rawQuery(query,null);
        sleep(5);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String obj = new String(cursor.getString(0));


            result.add(obj);

            cursor.moveToNext();
        }

        cursor.close();
        return result;


    }

    public int getQtdAptDia() {
        return qtdAptDia;
    }

    public void setQtdAptDia(int qtdAptDia) {
        this.qtdAptDia = qtdAptDia;
    }

    public int getQtdHeader() {
        return qtdHeader;
    }

    public void setQtdHeader(int qtdHeader) {
        this.qtdHeader = qtdHeader;
    }
}

