package com.afmobi.palmcall.model;

import java.util.Date;

public class RechargePalmCoinFail {
    private int id;

    private String afid;

    private int amount;

    private String phonecountrycode;

    private Date addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAfid() {
        return afid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPhonecountrycode() {
        return phonecountrycode;
    }

    public void setPhonecountrycode(String phonecountrycode) {
        this.phonecountrycode = phonecountrycode;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "RechargePalmCoinFail{" +
                "id=" + id +
                ", afid='" + afid + '\'' +
                ", amount=" + amount +
                ", phonecountrycode='" + phonecountrycode + '\'' +
                ", addtime=" + addtime +
                '}';
    }

}
