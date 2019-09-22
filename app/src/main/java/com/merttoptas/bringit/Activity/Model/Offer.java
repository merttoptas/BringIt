package com.merttoptas.bringit.Activity.Model;

public class Offer {
    private String offerNameSurname;
    private String title;
    private String transport;
    private String numberOfFloors;
    private String province;
    private String district;
    private String targetProvince;
    private String targetDistrict;
    private String toFloors;
    private double latitude;
    private double longitude;
    private String dateTime;
    private String explanation;

    public Offer(){ }

    public Offer(String title, String transport, String numberOfFloors, String province, String district,
                 String targetProvince, String targetDistrict, String toFloors, double latitude, double longitude,
                 String dateTime, String explanation, String offerNameSurname) {
        this.title = title;
        this.transport = transport;
        this.numberOfFloors = numberOfFloors;
        this.province = province;
        this.district = district;
        this.targetProvince = targetProvince;
        this.targetDistrict = targetDistrict;
        this.toFloors = toFloors;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.explanation=explanation;
        this.offerNameSurname =offerNameSurname;

    }


    public String getTitle() { return title; }

    public void setTitle(String etBaslik) {
        this.title = etBaslik;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(String numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTargetProvince(){ return  targetProvince;
    }
    public void setTargetProvince(String targetProvince) {
        this.targetProvince = targetProvince;
    }

    public String getTargetDistrict() {
        return targetDistrict;
    }

    public void setTargetDistrict(String targetDistrict) {
        this.targetDistrict = targetDistrict;
    }

    public String getToFloors() {
        return toFloors;
    }

    public void setToFloors(String toFloors) {
        this.toFloors = toFloors;
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String aciklama) {
        this.explanation = aciklama;
    }

    public String getOfferNameSurname() {
        return offerNameSurname;
    }

    public void setOfferNameSurname(String offerNameSurname) { this.offerNameSurname = offerNameSurname; }
}
