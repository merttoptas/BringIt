package com.merttoptas.bringit.Activity.Model;

public class Offer {

    private String etBaslik;
    private String etEsyaSekli;
    private String etKatSayisi;
    private String etIl;
    private String etIlce;
    private String etToIl;
    private String etToIlce;
    private String etKat;
    private double latitude;
    private double longitude;
    private String dateTime;
    private String aciklama;

    public Offer(){

    }

    public Offer(String etBaslik, String etEsyaSekli, String etKatSayisi, String etIl, String etIlce, String etToIl, String etToIlce, String etKat, double latitude, double longitude, String dateTime, String aciklama) {
        this.etBaslik = etBaslik;
        this.etEsyaSekli = etEsyaSekli;
        this.etKatSayisi = etKatSayisi;
        this.etIl = etIl;
        this.etIlce = etIlce;
        this.etToIl = etToIl;
        this.etToIlce = etToIlce;
        this.etKat = etKat;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.aciklama=aciklama;

    }


    public String getEtBaslik() {


        return etBaslik;
    }

    public void setEtBaslik(String etBaslik) {
        this.etBaslik = etBaslik;
    }

    public String getEtEsyaSekli() {
        return etEsyaSekli;
    }

    public void setEtEsyaSekli(String etEsyaSekli) {
        this.etEsyaSekli = etEsyaSekli;
    }

    public String getEtKatSayisi() {
        return etKatSayisi;
    }

    public void setEtKatSayisi(String etKatSayisi) {
        this.etKatSayisi = etKatSayisi;
    }

    public String getEtIl() {
        return etIl;
    }

    public void setEtIl(String etIl) {
        this.etIl = etIl;
    }

    public String getEtIlce() {
        return etIlce;
    }

    public void setEtIlce(String etIlce) {
        this.etIlce = etIlce;
    }

    public String etToIl() {
        return etToIl;
    }

    public void setEtToIl(String etToIl) {
        this.etToIl = etToIl;
    }

    public String getEtToIl(){
        return  etToIl;
    }

    public String getEtToIlce() {
        return etToIlce;
    }

    public void setEtToIlce(String etToIlce) {
        this.etToIlce = etToIlce;
    }

    public String getEtKat() {
        return etKat;
    }

    public void setEtKat(String etKat) {
        this.etKat = etKat;
    }

    public double getLatitude() { return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude;
    }

    public void setLongitude(double longitude) { this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
