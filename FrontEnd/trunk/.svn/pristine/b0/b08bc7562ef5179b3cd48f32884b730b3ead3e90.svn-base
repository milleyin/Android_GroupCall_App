package com.afmobi.custom.filter;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.afmobi.utils.VisionFilterType;
import com.afmobi.vision.R;

import gpuimage.GPUImageLookupFilter;


/**
 * Created by daniel@afmobi on 2016/6/27.
 */
public class IFLookupFilter extends GPUImageLookupFilter {
    public IFLookupFilter() {
        super();
    }


    public void setFilterType(Context context, VisionFilterType type) {

        int mapResId = 0;

        switch (type) {
            case A_BAO_MAP:
                mapResId = R.raw.filter_map_colorfull;
                break;

            case COLD_MAP:
                mapResId = R.raw.filter_map_yellow_blue;
                break;

            case BLUE_MAP:
                mapResId = R.raw.filter_map_blue;
                break;

            case COLOR_ENHANCE_MAP:
                mapResId = R.raw.filter_map_color_enhance;
                break;

            case COLOR_LIGHT_MAP:
                mapResId = R.raw.filter_map_light;
                break;

            case LOOKUP_MAP_INTENSE:
                mapResId = R.raw.filter_map_lookup_01;
                break;
            case LOOKUP_MAP_BALANCE:
                mapResId = R.raw.filter_map_lookup_02;
                break;
            case LOOKUP_MAP_FRESH:
                mapResId = R.raw.filter_map_lookup_03;
                break;
            case LOOKUP_MAP_INDOOR:
                mapResId = R.raw.filter_map_lookup_04;
                break;
            case LOOKUP_MAP_WARM:
                mapResId = R.raw.filter_map_lookup_05;
                break;
            case LOOKUP_MAP_AUTO:
                mapResId = R.raw.filter_map_lookup_06;
                break;
            default:
                break;
        }

        setBitmap(BitmapFactory.decodeStream(context.getResources().openRawResource(mapResId)));
    }

}
