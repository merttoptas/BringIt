package com.merttoptas.bringit.Activity.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistrictDetail {

    @SerializedName("ilce_id")
    @Expose
    private String ilceId;
    @SerializedName("ilce_title")
    @Expose
    private String ilceTitle;
    @SerializedName("ilce_key")
    @Expose
    private String ilceKey;
    @SerializedName("ilce_sehirkey")
    @Expose
    private String ilceSehirkey;

    public String getIlceId() {
        return ilceId;
    }

    public void setIlceId(String ilceId) {
        this.ilceId = ilceId;
    }

    public String getIlceTitle() {
        return ilceTitle;
    }

    public void setIlceTitle(String ilceTitle) {
        this.ilceTitle = ilceTitle;
    }

    public String getIlceKey() {
        return ilceKey;
    }

    public void setIlceKey(String ilceKey) {
        this.ilceKey = ilceKey;
    }

    public String getIlceSehirkey() {
        return ilceSehirkey;
    }

    public void setIlceSehirkey(String ilceSehirkey) {
        this.ilceSehirkey = ilceSehirkey;
    }
}
