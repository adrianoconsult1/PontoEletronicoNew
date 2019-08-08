package com.example.pontoeletroniconew;

import java.io.*;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper
{

    static final String NOME_BANCO = "bancoApontApp.db";

    static final String TABELA = "apontamentos";
    static final String DATA = "DATA";
    static final String CODIGOFUNCIONARIO = "CODFUNCIONATIO";
    static final String APONT1 = "APONT1";
    static final String LOCAL1 = "LOCAL1";
    static final String GPS1 = "GPS1";
    static final String APONT2 = "APONT2";
    static final String LOCAL2 = "LOCAL2";
    static final String GPS2 = "GPS2";
    static final String APONT3 = "APONT3";
    static final String LOCAL3 = "LOCAL3";
    static final String GPS3 = "GPS3";
    static final String APONT4 = "APONT4";
    static final String LOCAL4 = "LOCAL4";
    static final String GPS4 = "GPS4";
    static final String APONTEXTRA = "APONTEXTRA";
    static final String LOCALEXTRA = "LOCALEXTRA";
    static final String GPSEXTRA = "GPSEXTRA";
    static final String APONTEXTRA2 = "APONTEXTRA2";
    static final String LOCALEXTRA2 = "LOCALEXTRA2";
    static final String GPSEXTRA2 = "GPSEXTRA2";


    /**
     * Este é o endereço onde o android salva os bancos de dados criado pela aplicação,
     * /data/data/<namespace da aplicacao>/databases/
     */
    private static String DBPATH = "/data/data/com.example.pontoeletroniconew/databases/";
    private static String CREATE_TABLE = "CREATE TABLE [apontamentos](\n" +
            "  [DATA] DATE NOT NULL, \n" +
            "  [CODFUNCIONATIO] INT NOT NULL, \n" +
            "  [DESCRICAO] TEXT, \n" +
            "  [APONT1] DATETIME, \n" +
            "  [LOCAL1] TEXT, \n" +
            "  [GPS1] TEXT, \n" +
            "  [APONT2] DATETIME, \n" +
            "  [LOCAL2] TEXT, \n" +
            "  [GPS2] TEXT, \n" +
            "  [APONT3] DATETIME, \n" +
            "  [LOCAL3] TEXT, \n" +
            "  [GPS3] TEXT, \n" +
            "  [APONT4] DATETIME, \n" +
            "  [LOCAL4] TEXT, \n" +
            "  [GPS4] TEXT, \n" +
            "  [APONTEXTRA] DATETIME, \n" +
            "  [LOCALEXTRA] TEXT, \n" +
            "  [GPSEXTRA] TEXT, \n" +
            "  [APONTEXTRA2] DATETIME, \n" +
            "  [LOCALEXTRA2] TEXT, \n" +
            "  [GPSEXTRA2] TEXT, \n" +
            "  [UGMODIFICADOREG] DATE, \n" +
            "  [UGINSERIDOREG] DATE, \n" +
            "  [UGUSERINSREG] VARCHAR(20), \n" +
            "  [UGUSERMODIFREG] VARCHAR(20), \n" +
            "  PRIMARY KEY([DATA], [CODFUNCIONATIO]));";


    // Este é o nome do banco de dados que iremos utilizar
    private static String DBNAME ="bancoApontApp";


    private static final int VERSION = 1;
    private final Context context;



/**
 * O construtor necessita do contexto da aplicação*/
 public DatabaseHelper(Context context)
 {
    super(context, DBNAME, null, VERSION);
    this.context = context;

 }



/**
 * Os métodos onCreate e onUpgrade precisam ser sobreescrito */
 @Override
 public void onCreate(SQLiteDatabase db)
 {

 }



@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    /*
     * Estamos criando a primeira versão do nosso banco de dados,
     * então não precisamos fazer nenhuma alteração neste método.
     *
     */

}


/**
 * Método auxiliar que verifica a existencia do banco
 * da aplicação.
 */
private boolean checkDataBase() {



        SQLiteDatabase db = null;

        try {
        String path = DBPATH + DBNAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        db.close();


        } catch (SQLiteException e)
        {
        // O banco não existe
        }

// Retorna verdadeiro se o banco existir, pois o ponteiro irá existir,
// se não houver referencia é porque o banco não existe
        return db != null;


        }



private void createDataBase()
        throws Exception {




// Primeiro temos que verificar se o banco da aplicação
// já foi criado
        boolean exists = checkDataBase();

        if(!exists)
        {
        // Chamaremos esse método para que o android
        // crie um banco vazio e o diretório onde iremos copiar
        // no banco que está no assets.
        this.getReadableDatabase();
        this.close();
        // Se o banco de dados não existir iremos copiar o nosso
        // arquivo em /assets para o local onde o android os salva
        try
        {
            copyDatabase();
        } catch (IOException e)
        {
        throw new Error("Não foi possível copiar o arquivo");
        }

        }


        }



/**
 * Esse método é responsável por copiar o banco do diretório
 * assets para o diretório padrão do android.
 */
private void copyDatabase()
        throws IOException
{



        String dbPath = DBPATH + DBNAME;

// Abre o arquivo o destino para copiar o banco de dados
        OutputStream dbStream = new FileOutputStream(dbPath);

// Abre Stream do nosso arquivo que esta no assets
        InputStream dbInputStream =
        context.getAssets().open("bancoApontApp.db");

        byte[] buffer = new byte[1024];
        int length;
        while((length = dbInputStream.read(buffer)) > 0)
        {
        dbStream.write(buffer, 0, length);
        }

        dbInputStream.close();

        dbStream.flush();
        dbStream.close();


        }



        public SQLiteDatabase getDatabase()
        {



        try
        {
        // Verificando se o banco já foi criado e se não foi o
        // mesmo é criado.
        createDataBase();

        // Abrindo database
        String path =  DBPATH + DBNAME;

        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        }catch (Exception e)
        {
        // Se não conseguir copiar o banco um novo será retornado
        return getWritableDatabase();
        }


        }
        }
