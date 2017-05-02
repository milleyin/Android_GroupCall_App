package com.afmobi.palmcall.model;

import java.util.Date;

public class GiftMinutes {
    private int id;

    private String afid;

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

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        return "GiftMinutes{" +
                "id=" + id +
                ", afid='" + afid + '\'' +
                ", addtime=" + addtime +
                '}';
    }

}
