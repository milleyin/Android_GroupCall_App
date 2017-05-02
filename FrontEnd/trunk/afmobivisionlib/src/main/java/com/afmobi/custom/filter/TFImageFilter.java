package com.afmobi.custom.filter;

/**
 * Created by daniel@afmobi on 2016/5/26.
 */
public interface TFImageFilter {
    /*
    * type and extra
    *
    *if type or extra not used, keep it 0, and dont care aboat it;
    *them only use for composite params
    */
    public void setValue(int type, int value);
    public int getValue(int type);
    public void setValueExtra(int type,int value);
    public int getValueExtra(int type);
}
