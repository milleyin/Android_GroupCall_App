package com.afmobi.palmchat.wheel.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class CountryAdapter extends AbstractWheelTextAdapter {
    // Countries names
    private String [] countries;
    // Countries flags
    /**
     * Constructor
     */
    public CountryAdapter(Context context , String [] countries) {
        super(context);
        this.countries = countries;
        this.setTextSize(18);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        return super.getItem(index, cachedView, parent);
    }
    
    @Override
    public int getItemsCount() {
        return countries.length;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        return countries[index];
    }
}
