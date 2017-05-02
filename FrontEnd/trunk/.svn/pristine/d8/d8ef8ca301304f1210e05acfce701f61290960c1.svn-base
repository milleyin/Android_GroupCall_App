package com.afmobi.custom.filter;

import android.opengl.GLES20;

import gpuimage.GPUImage3x3TextureSamplingFilter;

/**
 * Created by daniel@afmobi on 2016/6/22.
 */
public class GPUImageColorPencilFilter extends GPUImage3x3TextureSamplingFilter {

    public static final String FRAGMENT_SHADER = "" +
            "precision mediump float;\n"+
            "\n"+
            " varying vec2 textureCoordinate;\n"+
            " varying vec2 leftTextureCoordinate;\n"+
            " varying vec2 rightTextureCoordinate;\n"+
            " \n"+
            " varying vec2 topTextureCoordinate;\n"+
            " varying vec2 topLeftTextureCoordinate;\n"+
            " varying vec2 topRightTextureCoordinate;\n"+
            " \n"+
            " varying vec2 bottomTextureCoordinate;\n"+
            " varying vec2 bottomLeftTextureCoordinate;\n"+
            " varying vec2 bottomRightTextureCoordinate;\n"+
            "\n"+
            " uniform sampler2D inputImageTexture;\n"+
            " uniform float edgeStrength;\n"+
            " \n"+
            " \n"+
            " const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n"+
            " \n"+
            " void main()\n"+
            " {\n"+
            " \n"+
            "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n"+
            " \n"+
            "    float bottomLeftIntensity = dot(texture2D(inputImageTexture, bottomLeftTextureCoordinate).rgb,W);\n"+
            "    float topRightIntensity = dot(texture2D(inputImageTexture, topRightTextureCoordinate).rgb,W);\n"+
            "    float topLeftIntensity = dot(texture2D(inputImageTexture, topLeftTextureCoordinate).rgb,W);\n"+
            "    float bottomRightIntensity = dot(texture2D(inputImageTexture, bottomRightTextureCoordinate).rgb,W);\n"+
            "    float leftIntensity = dot(texture2D(inputImageTexture, leftTextureCoordinate).rgb,W);\n"+
            "    float rightIntensity = dot(texture2D(inputImageTexture, rightTextureCoordinate).rgb,W);\n"+
            "    float bottomIntensity = dot(texture2D(inputImageTexture, bottomTextureCoordinate).rgb,W);\n"+
            "    float topIntensity = dot(texture2D(inputImageTexture, topTextureCoordinate).rgb,W);\n"+
            "    float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n"+
            "    float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n"+
            "    \n"+
            "    float mag = length(vec2(h, v)) * edgeStrength;\n"+
            "          \n"+
            "    gl_FragColor = vec4(vec3( mag), 1.0);\n"+
            "    \n"+
            "    gl_FragColor = vec4(vec3( mag), 1.0);\n"+
            "    gl_FragColor.r = textureColor.r<=0.5? 2.0*textureColor.r*(1.0 - mag):1.0-  2.0*(1.0 - textureColor.r)*mag;\n"+
            "    gl_FragColor.g = textureColor.g<=0.5? 2.0*textureColor.g*(1.0 - mag):1.0 - 2.0*(1.0 - textureColor.g)*mag;\n"+
            "    gl_FragColor.b = textureColor.b<=0.5? 2.0*textureColor.b*(1.0 - mag):1.0 - 2.0*(1.0 - textureColor.b)*mag;\n"+
            " }\n";


    private float mEdgeStrength;
    private int mEdgeStrengthLocation;

    public GPUImageColorPencilFilter(){
        super(FRAGMENT_SHADER);
        mEdgeStrength = 1.0f;

    }

    @Override
    public void onInit() {
        super.onInit();
        mEdgeStrengthLocation = GLES20.glGetUniformLocation(getProgram(), "edgeStrength");
        setEdgeStrength(mEdgeStrength);
    }

    public void setEdgeStrength(float edgeStrength){
        mEdgeStrength = edgeStrength;
        setFloat(mEdgeStrengthLocation, mEdgeStrength);
    }




}
