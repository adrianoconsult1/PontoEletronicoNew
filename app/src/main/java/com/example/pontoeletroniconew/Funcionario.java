package com.example.pontoeletroniconew;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Funcionario {

    private int CODFUNCIONATIO;
    private String NOME;
    private String EMAIL;
    private long TELEFONE;
    private String ATIVO;

    public Funcionario()
    {
        this.CODFUNCIONATIO = 0;
        this.NOME = null;
        this.EMAIL = null;
        this.TELEFONE = 0;
        this.ATIVO = "N";
    }

    public Funcionario(int CODFUNCIONATIO, String NOME, String EMAIL, long TELEFONE, String ATIVO) {
        this.CODFUNCIONATIO = CODFUNCIONATIO;
        this.NOME = NOME;
        this.EMAIL = EMAIL;
        this.TELEFONE = TELEFONE;
        this.ATIVO = ATIVO;
    }

    public Funcionario(String codNome)
    {
        String aux = ""+codNome.charAt(0)+codNome.charAt(1)+codNome.charAt(2);
        this.CODFUNCIONATIO = (int) Integer.parseInt(aux);
        this.NOME = codNome.substring(5,(codNome.length()-4));
    }

    public int getCODFUNCIONATIO() {
        return CODFUNCIONATIO;
    }

    public void setCODFUNCIONATIO(int CODFUNCIONATIO) {
        this.CODFUNCIONATIO = CODFUNCIONATIO;
    }

    public String getNOME() {
        return NOME;
    }

    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public long getTELEFONE() {
        return TELEFONE;
    }

    public void setTELEFONE(long TELEFONE) {
        this.TELEFONE = TELEFONE;
    }

    public String getCodNome()
    {
        return getCODFUNCIONATIO() +": " +getNOME();
    }

    public String getATIVO() { return ATIVO; }

    public void setATIVO(String ATIVO) {  this.ATIVO = ATIVO;  }



    @Override
    public String toString() {
        return "Funcionario{" +
                "CODFUNCIONATIO=" + CODFUNCIONATIO +
                ", NOME='" + NOME + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", TELEFONE='" + TELEFONE + '\'' +
                ", ATIVO='" + ATIVO + '\'' +
                '}';
    }
}
