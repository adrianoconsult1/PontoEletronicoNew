package com.example.pontoeletroniconew;

public class Ponto {



    private String APONT1;
    private String APONT2;
    private String APONT3;
    private String APONT4;
    private String APONTEXTRA;
    private String APONTEXTRA2;
    private String DATA;
    private String DESCRICAO;
    private long CODFUNCIONATIO;
    private String GPS1;
    private String GPS2;
    private String GPS3;
    private String GPS4;
    private String GPSEXTRA;
    private String GPSEXTRA2;
    private String LOCAL1;
    private String LOCAL2;
    private String LOCAL3;
    private String LOCAL4;
    private String LOCALEXTRA;
    private String LOCALEXTRA2;
    private long ROWID;


    public Ponto()
    {


        this.APONT1 = null;
        this.APONT2 = null;
        this.APONT3 = null;
        this.APONT4 = null;
        this.APONTEXTRA = null;
        this.APONTEXTRA2 = null;
        this.CODFUNCIONATIO = 23;
        this.DATA = null;
        this.DESCRICAO = null;
        this.GPS1 = null;
        this.GPS2 = null;
        this.GPS3 = null;
        this.GPS4 = null;
        this.GPSEXTRA = null;
        this.GPSEXTRA2 = null;
        this.LOCAL1 = null;
        this.LOCAL2 = null;
        this.LOCAL3 = null;
        this.LOCAL4 = null;
        this.LOCALEXTRA = null;
        this.LOCALEXTRA2 = null;
        this.ROWID = 0;

    }

    @Override
    public String toString() {
        return "Ponto{" +
                "APONT1='" + APONT1 + '\'' +
                ", APONT2='" + APONT2 + '\'' +
                ", APONT3='" + APONT3 + '\'' +
                ", APONT4='" + APONT4 + '\'' +
                ", APONTEXTRA='" + APONTEXTRA + '\'' +
                ", APONTEXTRA2='" + APONTEXTRA2 + '\'' +
                ", DATA='" + DATA + '\'' +
                ", DESCRICAO='" + DESCRICAO + '\'' +
                ", CODFUNCIONATIO=" + CODFUNCIONATIO +
                ", GPS1='" + GPS1 + '\'' +
                ", GPS2='" + GPS2 + '\'' +
                ", GPS3='" + GPS3 + '\'' +
                ", GPS4='" + GPS4 + '\'' +
                ", GPSEXTRA='" + GPSEXTRA + '\'' +
                ", GPSEXTRA2='" + GPSEXTRA2 + '\'' +
                ", LOCAL1='" + LOCAL1 + '\'' +
                ", LOCAL2='" + LOCAL2 + '\'' +
                ", LOCAL3='" + LOCAL3 + '\'' +
                ", LOCAL4='" + LOCAL4 + '\'' +
                ", LOCALEXTRA='" + LOCALEXTRA + '\'' +
                ", LOCALEXTRA2='" + LOCALEXTRA2 + '\'' +
                ", ROWID=" + ROWID +
                '}';
    }

    public Ponto(String APONT1, String APONT2, String APONT3, String APONT4, long CODFUNCIONATIO, String DATA , String DESCRICAO , String GPS1, String GPS2, String GPS3, String GPS4, String GPSEXTRA, String GPSEXTRA2, String APONTEXTRA, String APONTEXTRA2, String LOCAL1, String LOCAL2, String LOCAL3, String LOCAL4, String LOCALEXTRA, String LOCALEXTRA2, long ROWID) {


        this.APONT1 = APONT1;
        this.APONT2 = APONT2;
        this.APONT3 = APONT3;
        this.APONT4 = APONT4;
        this.APONTEXTRA = APONTEXTRA;
        this.APONTEXTRA2 = APONTEXTRA2;
        this.CODFUNCIONATIO = CODFUNCIONATIO;
        this.DATA = DATA;
        this.DESCRICAO = DESCRICAO;
        this.GPS1 = GPS1;
        this.GPS2 = GPS2;
        this.GPS3 = GPS3;
        this.GPS4 = GPS4;
        this.GPSEXTRA = GPSEXTRA;
        this.GPSEXTRA2 = GPSEXTRA2;
        this.LOCAL1 = LOCAL1;
        this.LOCAL2 = LOCAL2;
        this.LOCAL3 = LOCAL3;
        this.LOCAL4 = LOCAL4;
        this.LOCALEXTRA = LOCALEXTRA;
        this.LOCALEXTRA2 = LOCALEXTRA2;
        this.ROWID = ROWID;

    }

    public String getGPS1() {
        return GPS1;
    }

    public void setGPS1(String GPS1) {
        this.GPS1 = GPS1;
    }

    public String getGPS2() {
        return GPS2;
    }

    public void setGPS2(String GPS2) {
        this.GPS2 = GPS2;
    }

    public String getGPS3() {
        return GPS3;
    }

    public void setGPS3(String GPS3) {
        this.GPS3 = GPS3;
    }

    public String getGPS4() {
        return GPS4;
    }

    public void setGPS4(String GPS4) {
        this.GPS4 = GPS4;
    }

    public String getGPSEXTRA() {
        return GPSEXTRA;
    }

    public void setGPSEXTRA(String GPSEXTRA) {
        this.GPSEXTRA = GPSEXTRA;
    }

    public String getGPSEXTRA2() {
        return GPSEXTRA2;
    }

    public void setGPSEXTRA2(String GPSEXTRA2) {
        this.GPSEXTRA2 = GPSEXTRA2;
    }



    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public long getROWID() {
        return ROWID;
    }

    public void setROWID(long ROWID) {
        this.ROWID = ROWID;
    }

    public String getAPONT1() {
        return APONT1;
    }

    public void setAPONT1(String APONT1) {
        this.APONT1 = APONT1;
    }

    public String getAPONT2() {
        return APONT2;
    }

    public void setAPONT2(String APONT2) {
        this.APONT2 = APONT2;
    }

    public String getAPONT3() {
        return APONT3;
    }

    public void setAPONT3(String APONT3) {
        this.APONT3 = APONT3;
    }

    public String getAPONT4() {
        return APONT4;
    }

    public void setAPONT4(String APONT4) {
        this.APONT4 = APONT4;
    }

    public String getAPONTEXTRA() {
        return APONTEXTRA;
    }

    public void setAPONTEXTRA(String APONTEXTRA) {
        this.APONTEXTRA = APONTEXTRA;
    }

    public String getAPONTEXTRA2() {
        return APONTEXTRA2;
    }

    public void setAPONTEXTRA2(String APONTEXTRA2) {
        this.APONTEXTRA2 = APONTEXTRA2;
    }

    public String getLOCAL1() {
        return LOCAL1;
    }

    public void setLOCAL1(String LOCAL1) {
        this.LOCAL1 = LOCAL1;
    }

    public String getLOCAL2() {
        return LOCAL2;
    }

    public void setLOCAL2(String LOCAL2) {
        this.LOCAL2 = LOCAL2;
    }

    public String getLOCAL3() {
        return LOCAL3;
    }

    public void setLOCAL3(String LOCAL3) {
        this.LOCAL3 = LOCAL3;
    }

    public String getLOCAL4() {
        return LOCAL4;
    }

    public void setLOCAL4(String LOCAL4) {
        this.LOCAL4 = LOCAL4;
    }

    public String getLOCALEXTRA() {
        return LOCALEXTRA;
    }

    public void setLOCALEXTRA(String LOCALEXTRA) {
        this.LOCALEXTRA = LOCALEXTRA;
    }

    public String getLOCALEXTRA2() {
        return LOCALEXTRA2;
    }

    public void setLOCALEXTRA2(String LOCALEXTRA2) {
        this.LOCALEXTRA2 = LOCALEXTRA2;
    }

    public long getCODFUNCIONATIO() {
        return CODFUNCIONATIO;
    }

    public void setCODFUNCIONATIO(long CODFUNCIONATIO) {
        this.CODFUNCIONATIO = CODFUNCIONATIO;
    }

    public String getDESCRICAO() { return DESCRICAO;  }

    public void setDESCRICAO(String DESCRICAO) { this.DESCRICAO = DESCRICAO; }
}
