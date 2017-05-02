package com.afmobi.custom.filter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.afmobi.vision.R;

import java.util.ArrayList;
import java.util.List;

import gpuimage.GPUImageBilateralFilter;
import gpuimage.GPUImageFilter;
import gpuimage.GPUImageFilterGroup;
import gpuimage.GPUImageGrayscaleFilter;

import gpuimage.GPUImageSketchFilter;
import gpuimage.OpenGlUtils;

/**
 * Created by daniel@afmobi on 2016/6/27.
 */
public class IFSketchFilter extends GPUImageFilterGroup {
    public static final String VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "}";
    public static final String FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            "uniform sampler2D inputImageTexture2;\n" +
            "uniform sampler2D inputImageTexture3;\n" +
            "uniform sampler2D inputImageTexture4;\n" +
            "uniform sampler2D inputImageTexture5;\n" +
            "uniform sampler2D inputImageTexture6;\n" +
            "uniform sampler2D inputImageTexturePencil;\n" +

            "const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     vec4 texel0 = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     vec4 texel2 = texture2D(inputImageTexture2, textureCoordinate);\n" +
            "     vec4 texel3 = texture2D(inputImageTexture3, textureCoordinate);\n" +
            "     vec4 texel4 = texture2D(inputImageTexture4, textureCoordinate);\n" +
            "     vec4 texel5 = texture2D(inputImageTexture5, textureCoordinate);\n" +
            "     vec4 texel6 = texture2D(inputImageTexture6, textureCoordinate);\n" +

            " \n" +
            " \n" +
            "    gl_FragColor = texel4*(texel5*0.1+ vec4(0.8) + texel2*0.1 );\n" +
//            "    //gl_FragColor = (gray*0.1 + 0.9)*texel4;\n" +
//            "  //   gl_FragColor = vec4(vec3(hightColor.r*0.5+ gray*0.4 + lowColor*0.1)*texel4.rgb, 1.0);\n" +
//            " //   gl_FragColor = vec4(vec3(lowColor*0.1 + hightColor*0.5 + gray*0.4)*texel4.rgb, 1.0);\n" +
//            " //   gl_FragColor = vec4(vec3(dot(texel5.rgb, W)*0.06 + dot(texel3.rgb, W)*0.57 + lum*0.37)*((vec3(1.0) -texel6.rgb)), 1.0);\n" +
//            " //    gl_FragColor = texel0*0.2 + texel2*0.2 + texel3*0.2+ texel4*0.2 + texel5*0.2;\n" +
//            "//     gl_FragColor = (ve4(1.0) - texel6) * texel4;//texel4;//*0.5 + (vec4(1.0)- texel6)*0.5;\n" +


            "}";

    public static final String COLOR_DODGE_BLEND_FRAGMENT_SHADER = "precision mediump float;\n" +
            " \n" +
            " varying highp vec2 textureCoordinate;\n" +
            " varying highp vec2 textureCoordinate2;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2;\n" +
            " const vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     vec4 base = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     vec4 overlay = texture2D(inputImageTexture2, textureCoordinate);\n" +
            "     base  = vec4(vec3(dot(base.rgb, W)), base.a);  \n" +
            "     \n" +
            "     vec3 baseOverlayAlphaProduct = vec3(overlay.a * base.a);\n" +
            "     vec3 rightHandProduct = overlay.rgb * (1.0 - base.a) + base.rgb * (1.0 - overlay.a);\n" +
            "     \n" +
            "     vec3 firstBlendColor = baseOverlayAlphaProduct + rightHandProduct;\n" +
            "     vec3 overlayRGB = clamp((overlay.rgb / clamp(overlay.a, 0.01, 1.0)) * step(0.0, overlay.a), 0.0, 0.99);\n" +
            "     \n" +
            "     vec3 secondBlendColor = (base.rgb * overlay.a) / (1.0 - overlayRGB) + rightHandProduct;\n" +
            "     \n" +
            "     vec3 colorChoice = step((overlay.rgb * base.a + base.rgb * overlay.a), baseOverlayAlphaProduct);\n" +
            "     \n" +
            "     gl_FragColor = vec4(mix(firstBlendColor, secondBlendColor, colorChoice), 1.0);\n" +
            "    // gl_FragColor = base;\n" +
            " }";



    private int mUniformTexelWidthLocation;
    private int mUniformTexelHeightLocation;

    private Bitmap mBitmap;


    public IFSketchFilter(){
        super();

        addFilter(new GPUImageGrayscaleFilter());
       //addFilter(new GPUImageKuwaharaFilter());

        addFilter(new GPUImageBilateralFilter());
        addFilter(new GPUImageSketchFilter());
       // addFilter(new GPUImageGaussianBlurFilter());

//
//        //texture2
//        GPUImageFilterGroup grpFilter = new GPUImageFilterGroup();
//
//        grpFilter.addFilter(new GPUImageGrayscaleFilter());
//        grpFilter.addFilter(new GPUImageColorInvertFilter());
//        //grpFilter.addFilter(new GPUImageKuwaharaFilter());
//        grpFilter.addFilter(new GPUImageWeakPixelInclusionFilter());
//
//        addRenderFilter(grpFilter);
//
//        //texture3
//        GPUImageFilterGroup grpGrayFilter = new GPUImageFilterGroup();
//        grpGrayFilter.addFilter(new GPUImageGrayscaleFilter());
//        grpGrayFilter.addFilter(new GPUImageContrastFilter());
//        addRenderFilter(grpGrayFilter);
//
//        ////texture4
//
//        GPUImageFilterGroup grpSektchFilter = new GPUImageFilterGroup();
//        grpSektchFilter.addFilter(new GPUImageGrayscaleFilter());
//        grpSektchFilter.addFilter(new GPUImageSketchFilter());
//        addRenderFilter(grpSektchFilter);
//
//        ////texture5
//        GPUImageFilterGroup grpLaplacianFilter = new GPUImageFilterGroup();
//        grpLaplacianFilter.addFilter(new GPUImageGrayscaleFilter());
//        grpLaplacianFilter.addFilter(new GPUImageGammaFilter());
//        addRenderFilter(grpLaplacianFilter);






    }

    public void onInit() {
        super.onInit();
//        mUniformTexelWidthLocation = GLES20.glGetUniformLocation(getProgram(), "texelWidth");
//        mUniformTexelHeightLocation = GLES20.glGetUniformLocation(getProgram(), "texelHeight");
    }

    @Override
    public void onOutputSizeChanged(final int width, final int height) {
        super.onOutputSizeChanged(width, height);
//        setFloat(mUniformTexelWidthLocation, 2f/getBitmapWidth());
//        setFloat(mUniformTexelHeightLocation, 2f/getBitmapHeigh());
    }



}
