package com.afmobi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.afmobi.custom.filter.GPUImageColorPencilFilter;
import com.afmobi.custom.filter.GPUImageSkinWhiteFilter;

import com.afmobi.custom.filter.IFImageFilter;
import com.afmobi.custom.filter.IFInkwellFilter;

import com.afmobi.custom.filter.IFSketchFilter;

import com.afmobi.custom.filter.IFLookupFilter;

import com.afmobi.vision.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gpuimage.GPUImageFilter;
import gpuimage.GPUImageKuwaharaFilter;


/**
 * Created by daniel@afmobi on 2016/5/27.
 */

public class VisionConst {

    public final static int TOOL_FILTER_TYPE_SHADOW = 0;
    public final static int TOOL_FILTER_TYPE_HIGHLIGHT = 1;

    public final static int TOOL_FILTER_TILT_CLOSED = 0;
    public final static int TOOL_FILTER_TILT_RADIO = 1;
    public final static int TOOL_FILTER_TILT_LINEAR = 2;


    public final static int TOOL_FILTER_TRANSFROM_VERTICAL = 0;
    public final static int TOOL_FILTER_TRANSFROM_ROTATE = 1;
    public final static int TOOL_FILTER_TRANSFROM_HORIZONTAL = 2;


    public static final int[] COLORS_TYPE_SHADOWCOLORS = new int[]{
            0xff000000,

            0xffc7c22e,  //yellow
            0xffc78b2e,
            0xffc72e2e, //red
            0xffc4417e,

            0xff852ec7, //purple
            0xff2e3cc7, //blue
            0xff2eabc7, //2eabc7
            0xff2ec73c  //green
    };

    public static final int[] COLORS_TYPE_HIGLIGHTCOLORS = new int[]{
            0xff000000,

            0xffe6e377,  //yellow
            0xffe6bb77,
            0xffe67777, //red
            0xffea8cb8,

            0xffb677e6, //purple
            0xff7782e6, //blue
            0xff77d2e6, //2eabc7
            0xff77e681  //green
    };

    private static final HashMap<VisionFilterType, Class<?>> mTypeFilterMap = new HashMap<VisionFilterType, Class<?>>();
    private static final HashMap<VisionFilterType, Integer> mTypeStringIDMap = new HashMap<VisionFilterType, Integer>();

    private static final List<VisionFilterType> mTopFilterTypeList = new ArrayList<>();

    private static final HashMap<VisionFilterType, Integer> mFilterDrawableIDMap = new HashMap<VisionFilterType, Integer>();

    private static final HashMap<VisionFilterType, String> mFilterNameForStatistics = new HashMap<VisionFilterType, String>();



    static {
        {

            mTypeFilterMap.put(VisionFilterType.ORIGIN, GPUImageFilter.class);

            //first stage filter
            mTypeFilterMap.put(VisionFilterType.LOOKUP_MAP_INDOOR, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.LOOKUP_MAP_WARM, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.MOON, IFInkwellFilter.class);
            mTypeFilterMap.put(VisionFilterType.LOOKUP_MAP_INTENSE, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.BLUE_MAP, IFLookupFilter.class);   //blue

            mTypeFilterMap.put(VisionFilterType.LOOKUP_MAP_FRESH, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.SKIN_WHITE, GPUImageSkinWhiteFilter.class); //skin white
            mTypeFilterMap.put(VisionFilterType.LOOKUP_MAP_AUTO, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.A_BAO_MAP, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.COLOR_ENHANCE_MAP, IFLookupFilter.class);


            mTypeFilterMap.put(VisionFilterType.COLD_MAP, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.LOOKUP_MAP_BALANCE, IFLookupFilter.class);
            mTypeFilterMap.put(VisionFilterType.COLOR_LIGHT_MAP, IFLookupFilter.class); //light
            mTypeFilterMap.put(VisionFilterType.SKETCH, IFSketchFilter.class);
            mTypeFilterMap.put(VisionFilterType.OIL_PAINTING, GPUImageKuwaharaFilter.class);

            mTypeFilterMap.put(VisionFilterType.COLOR_PENCIL, GPUImageColorPencilFilter.class);






/*



            mTypeFilterMap.put(VisionFilterType.GINHAM, IF1977Filter.class);
            mTypeFilterMap.put(VisionFilterType.CLARENDON, IFBrannanFilter.class);
            mTypeFilterMap.put(VisionFilterType.REYES, IFEarlybirdFilter.class);
            mTypeFilterMap.put(VisionFilterType.JUNO, IFHefeFilter.class);

            mTypeFilterMap.put(VisionFilterType.LUDWIG, IFLomoFilter.class);
            mTypeFilterMap.put(VisionFilterType.PERPETUA, IFLordKelvinFilter.class);
            mTypeFilterMap.put(VisionFilterType.SLUMBER, IFNashvilleFilter.class);
            mTypeFilterMap.put(VisionFilterType.AMARO, IFAmaroFilter.class);
            mTypeFilterMap.put(VisionFilterType.MAYFAIR, IFSutroFilter.class);
            mTypeFilterMap.put(VisionFilterType.RISE, IFRiseFilter.class);
            mTypeFilterMap.put(VisionFilterType.HUDSON, IFHudsonFilter.class);
            mTypeFilterMap.put(VisionFilterType.VALENCIA, IFValenciaFilter.class);
            mTypeFilterMap.put(VisionFilterType.X_PRO_II, IFXprollFilter.class);
            mTypeFilterMap.put(VisionFilterType.SIERRA, IFSierraFilter.class);
            mTypeFilterMap.put(VisionFilterType.WILLOW, IFWaldenFilter.class);
            mTypeFilterMap.put(VisionFilterType.LO_FI, GPUImageGlassSphereFilter.class);
            mTypeFilterMap.put(VisionFilterType.INKWELL, GPUImageHalftoneFilter.class);
            mTypeFilterMap.put(VisionFilterType.HEFE, GPUImageHardLightBlendFilter.class);
            mTypeFilterMap.put(VisionFilterType.NASHVILLE, GPUImageHazeFilter.class);
            mTypeFilterMap.put(VisionFilterType.MAVEN, GPUImageHighlightShadowFilter.class);
            mTypeFilterMap.put(VisionFilterType.GINZA, GPUImageHueBlendFilter.class);
            mTypeFilterMap.put(VisionFilterType.SKYLINE, GPUImageHueBlendFilter.class);





            mTypeFilterMap.put(VisionFilterType.TONE_CURVE, GPUImageToneCurveFilter.class);

            //TOOTLS
            mTypeFilterMap.put(VisionFilterType.TOOL_BRIGHTNESS, TFBrightnessFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_COLOR_BALANCE, TFColorBalanceFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_COLOR_CONTRAST, TFContrastFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_WHITE_BALANCE, TFWhiteBalanceFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_VIGNETTE, TFVignetteFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_SHARPEN, TFSharpenFilter.class);

            mTypeFilterMap.put(VisionFilterType.TOOL_SHADOW, TFHighlightShadowFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_HIGHLIGHT, TFHighlightShadowFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_TILT_SHIFT, TFTiltShiftFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_SATURATION, TFSaturationFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_FADE, TFFadeFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_TRANSFORM, TFTransformFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_STRUCTURE, TFStructureFilter.class);
            mTypeFilterMap.put(VisionFilterType.TOOL_GAUSS_SELECTIVE_BLUR, TFGaussSelectiveBlurFilter.class);
*/

            //string to type
            mTypeStringIDMap.put(VisionFilterType.ORIGIN, R.string.top_filter_type_orgin);
            mTypeStringIDMap.put(VisionFilterType.GINHAM, R.string.top_filter_type_ginham);
            mTypeStringIDMap.put(VisionFilterType.CLARENDON, R.string.top_filter_type_clarendon);
            mTypeStringIDMap.put(VisionFilterType.MOON, R.string.top_filter_type_black_white);
            mTypeStringIDMap.put(VisionFilterType.REYES, R.string.top_filter_type_reyes);
            mTypeStringIDMap.put(VisionFilterType.JUNO, R.string.top_filter_type_juno);
            mTypeStringIDMap.put(VisionFilterType.SLUMBER, R.string.top_filter_type_slumber);
            mTypeStringIDMap.put(VisionFilterType.LUDWIG, R.string.top_filter_type_ludwig);
            mTypeStringIDMap.put(VisionFilterType.PERPETUA, R.string.top_filter_type_perpetua);
            mTypeStringIDMap.put(VisionFilterType.AMARO, R.string.top_filter_type_amaro);
            mTypeStringIDMap.put(VisionFilterType.MAYFAIR, R.string.top_filter_type_mayfair);
            mTypeStringIDMap.put(VisionFilterType.RISE, R.string.top_filter_type_rise);
            mTypeStringIDMap.put(VisionFilterType.HUDSON, R.string.top_filter_type_hudson);
            mTypeStringIDMap.put(VisionFilterType.VALENCIA, R.string.top_filter_type_valencia);
            mTypeStringIDMap.put(VisionFilterType.X_PRO_II, R.string.top_filter_type_x_pro_ii);
            mTypeStringIDMap.put(VisionFilterType.SIERRA, R.string.top_filter_type_sierra);
            mTypeStringIDMap.put(VisionFilterType.WILLOW, R.string.top_filter_type_willow);
            mTypeStringIDMap.put(VisionFilterType.LO_FI, R.string.top_filter_type_lo_fi);
            mTypeStringIDMap.put(VisionFilterType.INKWELL, R.string.top_filter_type_inkwell);
            mTypeStringIDMap.put(VisionFilterType.HEFE, R.string.top_filter_type_hefe);
            mTypeStringIDMap.put(VisionFilterType.NASHVILLE, R.string.top_filter_type_nashville);
            mTypeStringIDMap.put(VisionFilterType.MAVEN, R.string.top_filter_type_maven);
            mTypeStringIDMap.put(VisionFilterType.GINZA, R.string.top_filter_type_ginza);
            mTypeStringIDMap.put(VisionFilterType.SKYLINE, R.string.top_filter_type_skyline);
            mTypeStringIDMap.put(VisionFilterType.CRAYON, R.string.top_filter_type_crayon);
            mTypeStringIDMap.put(VisionFilterType.COLOR_PENCIL, R.string.top_filter_type_color_pencil);


            mTypeStringIDMap.put(VisionFilterType.A_BAO_MAP, R.string.top_filter_type_colorful);
            mTypeStringIDMap.put(VisionFilterType.BLUE_MAP, R.string.top_filter_type_color_blue);
            mTypeStringIDMap.put(VisionFilterType.COLOR_ENHANCE_MAP, R.string.top_filter_type_color_enhance);
            mTypeStringIDMap.put(VisionFilterType.COLOR_LIGHT_MAP, R.string.top_filter_type_color_light);
            mTypeStringIDMap.put(VisionFilterType.COLD_MAP, R.string.top_filter_type_color_yellow_blue);

            mTypeStringIDMap.put(VisionFilterType.LOOKUP_MAP_INTENSE, R.string.top_filter_type_lookup_map_intense);
            mTypeStringIDMap.put(VisionFilterType.LOOKUP_MAP_BALANCE, R.string.top_filter_type_lookup_map_balance);
            mTypeStringIDMap.put(VisionFilterType.LOOKUP_MAP_FRESH, R.string.top_filter_type_lookup_map_fresh);
            mTypeStringIDMap.put(VisionFilterType.LOOKUP_MAP_INDOOR, R.string.top_filter_type_lookup_map_indoor);
            mTypeStringIDMap.put(VisionFilterType.LOOKUP_MAP_WARM, R.string.top_filter_type_lookup_map_warm);
            mTypeStringIDMap.put(VisionFilterType.LOOKUP_MAP_AUTO, R.string.top_filter_type_lookup_map_auto);
            mTypeStringIDMap.put(VisionFilterType.SKIN_WHITE, R.string.top_filter_type_skin_white);
            mTypeStringIDMap.put(VisionFilterType.OIL_PAINTING, R.string.top_filter_type_oil_painting);
            mTypeStringIDMap.put(VisionFilterType.SKETCH, R.string.top_filter_type_sketch);

            mTopFilterTypeList.add(VisionFilterType.ORIGIN);


            //drawable
            mFilterDrawableIDMap.put(VisionFilterType.COLOR_PENCIL, R.drawable.filter_sampe_colorpencil);
            mFilterDrawableIDMap.put(VisionFilterType.BLUE_MAP, R.drawable.filter_sampe_blue);
            mFilterDrawableIDMap.put(VisionFilterType.COLD_MAP, R.drawable.filter_sampe_cold);
            mFilterDrawableIDMap.put(VisionFilterType.A_BAO_MAP, R.drawable.filter_sampe_abao);
            mFilterDrawableIDMap.put(VisionFilterType.LOOKUP_MAP_BALANCE, R.drawable.filter_sampe_balance);

            mFilterDrawableIDMap.put(VisionFilterType.LOOKUP_MAP_INDOOR, R.drawable.filter_sampe_indoor);
            mFilterDrawableIDMap.put(VisionFilterType.LOOKUP_MAP_AUTO, R.drawable.filter_sampe_auto);
            mFilterDrawableIDMap.put(VisionFilterType.SKIN_WHITE, R.drawable.filter_sampe_skinwhite);
            mFilterDrawableIDMap.put(VisionFilterType.SKETCH, R.drawable.filter_sampe_sketch);
            mFilterDrawableIDMap.put(VisionFilterType.ORIGIN, R.drawable.filter_sampe_original);

            mFilterDrawableIDMap.put(VisionFilterType.COLOR_LIGHT_MAP, R.drawable.filter_sampe_light);
            mFilterDrawableIDMap.put(VisionFilterType.LOOKUP_MAP_WARM, R.drawable.filter_sampe_warm);
            mFilterDrawableIDMap.put(VisionFilterType.MOON, R.drawable.filter_sampe_blackwhite);
            mFilterDrawableIDMap.put(VisionFilterType.OIL_PAINTING, R.drawable.filter_sampe_oilpainting);
            mFilterDrawableIDMap.put(VisionFilterType.LOOKUP_MAP_INTENSE, R.drawable.filter_sampe_intense);

            mFilterDrawableIDMap.put(VisionFilterType.COLOR_ENHANCE_MAP, R.drawable.filter_sampe_enhance);
            mFilterDrawableIDMap.put(VisionFilterType.LOOKUP_MAP_FRESH, R.drawable.filter_sampe_fresh);


            //first stage
            mTopFilterTypeList.add(VisionFilterType.LOOKUP_MAP_INDOOR);
            mTopFilterTypeList.add(VisionFilterType.LOOKUP_MAP_WARM);
            mTopFilterTypeList.add(VisionFilterType.MOON);  //black white
            mTopFilterTypeList.add(VisionFilterType.LOOKUP_MAP_INTENSE);
            mTopFilterTypeList.add(VisionFilterType.BLUE_MAP);  //blue

            mTopFilterTypeList.add(VisionFilterType.LOOKUP_MAP_FRESH);
            mTopFilterTypeList.add(VisionFilterType.SKIN_WHITE); //skin white
            mTopFilterTypeList.add(VisionFilterType.LOOKUP_MAP_AUTO);
            mTopFilterTypeList.add(VisionFilterType.A_BAO_MAP); //a bao
            mTopFilterTypeList.add(VisionFilterType.COLOR_ENHANCE_MAP);

            mTopFilterTypeList.add(VisionFilterType.COLD_MAP);
            mTopFilterTypeList.add(VisionFilterType.LOOKUP_MAP_BALANCE);
            mTopFilterTypeList.add(VisionFilterType.COLOR_LIGHT_MAP); //light
            mTopFilterTypeList.add(VisionFilterType.SKETCH);
            mTopFilterTypeList.add(VisionFilterType.OIL_PAINTING);

            mTopFilterTypeList.add(VisionFilterType.COLOR_PENCIL);








            /*
            mTopFilterTypeList.add(VisionFilterType.CRAYON);
            mTopFilterTypeList.add(VisionFilterType.GINHAM);
            mTopFilterTypeList.add(VisionFilterType.CLARENDON);

            mTopFilterTypeList.add(VisionFilterType.REYES);
            mTopFilterTypeList.add(VisionFilterType.JUNO);
            mTopFilterTypeList.add(VisionFilterType.SLUMBER);
            mTopFilterTypeList.add(VisionFilterType.LUDWIG);
            mTopFilterTypeList.add(VisionFilterType.PERPETUA);
            mTopFilterTypeList.add(VisionFilterType.AMARO);
            mTopFilterTypeList.add(VisionFilterType.MAYFAIR);
            mTopFilterTypeList.add(VisionFilterType.RISE);
            mTopFilterTypeList.add(VisionFilterType.HUDSON);
            mTopFilterTypeList.add(VisionFilterType.VALENCIA);
            mTopFilterTypeList.add(VisionFilterType.X_PRO_II);
            mTopFilterTypeList.add(VisionFilterType.SIERRA);
            mTopFilterTypeList.add(VisionFilterType.WILLOW);
            mTopFilterTypeList.add(VisionFilterType.LO_FI);
            mTopFilterTypeList.add(VisionFilterType.INKWELL);
            mTopFilterTypeList.add(VisionFilterType.HEFE);
            mTopFilterTypeList.add(VisionFilterType.NASHVILLE);
            mTopFilterTypeList.add(VisionFilterType.MAVEN);
            mTopFilterTypeList.add(VisionFilterType.GINZA);
            mTopFilterTypeList.add(VisionFilterType.SKYLINE);
*/


            mFilterNameForStatistics.put(VisionFilterType.ORIGIN, "Original");
            mFilterNameForStatistics.put(VisionFilterType.MOON, "BlackWhite");
            mFilterNameForStatistics.put(VisionFilterType.COLOR_PENCIL, "Pencil");
            mFilterNameForStatistics.put(VisionFilterType.A_BAO_MAP, "ABao");
            mFilterNameForStatistics.put(VisionFilterType.BLUE_MAP, "Blue");
            mFilterNameForStatistics.put(VisionFilterType.COLOR_ENHANCE_MAP, "Enhance");
            mFilterNameForStatistics.put(VisionFilterType.COLOR_LIGHT_MAP,"Light");
            mFilterNameForStatistics.put(VisionFilterType.COLD_MAP,"Cold");
            mFilterNameForStatistics.put(VisionFilterType.LOOKUP_MAP_INTENSE, "Intense");
            mFilterNameForStatistics.put(VisionFilterType.LOOKUP_MAP_BALANCE, "Balance");
            mFilterNameForStatistics.put(VisionFilterType.LOOKUP_MAP_FRESH, "Fresh");
            mFilterNameForStatistics.put(VisionFilterType.LOOKUP_MAP_INDOOR, "Indoor");
            mFilterNameForStatistics.put(VisionFilterType.LOOKUP_MAP_WARM, "Warm");
            mFilterNameForStatistics.put(VisionFilterType.LOOKUP_MAP_AUTO, "Auto");
            mFilterNameForStatistics.put(VisionFilterType.SKIN_WHITE, "SkinWhite");
            mFilterNameForStatistics.put(VisionFilterType.OIL_PAINTING, "Paint");
            mFilterNameForStatistics.put(VisionFilterType.SKETCH, "Sketch");
        }
    }


    public static Class<?> getFilterClassByType(VisionFilterType type) {
        return mTypeFilterMap.get(type);
    }


    public static Map<VisionFilterType, Class<?>> getTopFilterClassMap() {
        return mTypeFilterMap;
    }

    public static List<VisionFilterType> getTopFilterTypeList() {
        return mTopFilterTypeList;
    }

    public static int getFilterDrawableByType(VisionFilterType type) {
        return mFilterDrawableIDMap.get(type);
    }



    public static int getStringIDbyFilterType(VisionFilterType type) {

        int id = R.string.app_name;
        if (mTypeStringIDMap.containsKey(type)) {
            id = mTypeStringIDMap.get(type);
        }
        return id;
    }
    public static String getStringFilterNameForStatistics(String strPreFilter,VisionFilterType type){
         String name="";
        if (mFilterNameForStatistics.containsKey(type)) {
            name = mFilterNameForStatistics.get(type);
            if(!TextUtils.isEmpty(strPreFilter)&&!strPreFilter.equals("Default")&&type==VisionFilterType.ORIGIN){
                //如果之前是有设置过滤镜的  并且新滤镜要设置为原图  那任然用之前设置过的滤镜
                name=strPreFilter;
            }
        }
        return name;

    }
    public static List<Bitmap> getTopBitmapList(Context context, Bitmap bitmap) {
        List<Bitmap> bmpList = new ArrayList<Bitmap>();

        for (int i = 0; i < mTopFilterTypeList.size(); i++) {
            Class<?> clazz = mTypeFilterMap.get(mTopFilterTypeList.get(i));
            if (clazz != null) {
                GPUImageFilter filter = null;
                try {

                    if (clazz.getSuperclass() == IFImageFilter.class) {

                        Constructor<?> ctor = (Constructor<Context>) clazz.getConstructor(Context.class);
                        filter = (GPUImageFilter) ctor.newInstance(context);
                    } else {
                        filter = (GPUImageFilter) clazz.newInstance();
                        if (filter.getClass() == IFLookupFilter.class) {
                            IFLookupFilter ifLookupFilter = (IFLookupFilter) filter;
                            ifLookupFilter.setFilterType(context, mTopFilterTypeList.get(i));
                        }
                    }


                    Bitmap bm = VisionUtils.bitmapFilterFactory(context, bitmap, filter);
                    bmpList.add(bm);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return bmpList;
    }


    public static GPUImageFilter creatFilterByType(Context context, VisionFilterType filterType) {

        Class<?> filterClass = VisionConst.getFilterClassByType(filterType);


        GPUImageFilter filter = null;

        try {

            if (filterClass.getSuperclass() == IFImageFilter.class) {

                Constructor<?> ctor = (Constructor<Context>) filterClass.getConstructor(Context.class);
                filter = (GPUImageFilter) ctor.newInstance(context);
            } else {
                filter = (GPUImageFilter) filterClass.newInstance();

                if (filter.getClass() == IFLookupFilter.class) {
                    IFLookupFilter tfLookupFilter = (IFLookupFilter) filter;
                    tfLookupFilter.setFilterType(context, filterType);
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return filter;
    }


}
