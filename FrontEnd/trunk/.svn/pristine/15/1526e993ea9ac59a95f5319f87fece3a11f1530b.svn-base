/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gpuimage;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GPUImageHistogramFilter extends GPUImageFilter {

    private final int HISTOGRAM_TYPE_RED = 0;
    private final int HISTOGRAM_TYPE_GREEN = 1;
    private final int HISTOGRAM_TYPE_BLUE = 2;
    private final int HISTOGRAM_TYPE_LUMINACE = 3;

    private final int PIC_WIDTH = 512;
    private final int PIC_HEIGHT = 512;

    private FloatBuffer mVertexBuffer;


    public static final String RED_HISTOGRAM_FILTER_VERTEX_SHADER = "" +
            " attribute vec4 position;\n" +
            " \n" +
            " varying vec3 colorFactor;\n" +
            "\n" +
            " void main()\n" +
            " {\n" +
            "     colorFactor = vec3(1.0, 0.0, 0.0);\n" +
            "     gl_Position = vec4(-1.0 + (position.x * 0.0078125), 0.0, 0.0, 1.0);\n" +
            "     gl_PointSize = 1.0;\n" +
            " }\n";


    public static final String GREEN_HISTOGRAM_FILTER_VERTEX_SHADER = "" +
            " attribute vec4 position;\n" +
            " \n" +
            " varying vec3 colorFactor;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     colorFactor = vec3(0.0, 1.0, 0.0);\n" +
            "     gl_Position = vec4(-1.0 + (position.y * 0.0078125), 0.0, 0.0, 1.0);\n" +
            "     gl_PointSize = 1.0;\n" +
            " }\n";


    public static final String BLUE_HISTOGRAM_FILTER_VERTEX_SHADER = "" +
            " attribute vec4 position;\n" +
            " \n" +
            " varying vec3 colorFactor;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     colorFactor = vec3(0.0, 0.0, 1.0);\n" +
            "     gl_Position = vec4(-1.0 + (position.z * 0.0078125), 0.0, 0.0, 1.0);\n" +
            "     gl_PointSize = 1.0;\n" +
            " }\n";


    public static final String LUMINACE_HISTOGRAM_FILTER_VERTEX_SHADER = "" +
            " attribute vec4 position;\n" +
            " \n" +
            " varying vec3 colorFactor;\n" +
            " \n" +
            " const vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     float luminance = dot(position.xyz, W);\n" +
            "\n" +
            "     colorFactor = vec3(1.0, 1.0, 1.0);\n" +
            "     gl_Position = vec4(-1.0 + (luminance * 0.0078125), 0.0, 0.0, 1.0);\n" +
            "     gl_PointSize = 1.0;\n" +
            " }\n";


    public static final String FILTER_FRAGMENT_SHADER = "" +
            "\n" +
            " const lowp float scalingFactor = 1.0 / 256.0;\n" +
            "\n" +
            " varying lowp vec3 colorFactor;\n" +
            "\n" +
            " void main()\n" +
            " {\n" +
            "     gl_FragColor = vec4(colorFactor * scalingFactor , 1.0);\n" +
            " }\n";


    public static final String HISTOGRAM_FILTER_VERTEX_SHADER = "" +
            "uniform sampler2D inputImageTexture;\n" +
            "attribute vec4 position;\n" +
            "const float SCREEN_WIDTH = 512.0;\n" +
            "\n" +
            "void main()\n" +
            "{	\n" +
            "	vec2 TexCoord = position.xy / SCREEN_WIDTH;\n" +
            "	vec3 color = texture2D(inputImageTexture, TexCoord.st).rgb;\n" +
            " 	\n" +
            "	float greyscale = 0.299 * color.r + 0.587 * color.g + 0.114 * color.b;\n" +
            "\n" +
            "	float Xposition = greyscale * 255.0;\n" +
            "	\n" +
            "	vec2 Vertex = vec2(0);\n" +
            "	Vertex.x = (Xposition - SCREEN_WIDTH / 2.0) / (SCREEN_WIDTH / 2.0);\n" +
            "\n" +
            "	gl_Position = vec4(Vertex.x, -1.0, 0.0, 1.0);\n" +
    "}\n";

    public static final String HISTOGRAM_FRAGMENT_SHADER = "" +
            "void main()\n" +
            "{	\n" +
            "	// TODO: Ein Pixel mit der Helligkeit eines einzelnen Grauwerts ausgeben.\n" +
            "	gl_FragColor = vec4(1.0/255.0, 1.0/255.0, 1.0/255.0, 1.0);\n" +
            "}\n";


    public GPUImageHistogramFilter() {
        // this(HISTOGRAM_FILTER_VERTEX_SHADER, HISTOGRAM_FRAGMENT_SHADER);
        this(HISTOGRAM_FILTER_VERTEX_SHADER, HISTOGRAM_FRAGMENT_SHADER);
    }


    public GPUImageHistogramFilter(String vert, String frag) {
        super(vert, frag);
        initVertexBuffer();
    }


    private void initVertexBuffer() {

        mVertexBuffer = ByteBuffer.allocateDirect((PIC_HEIGHT * PIC_WIDTH) * 4*3).order(ByteOrder.nativeOrder()).asFloatBuffer();


        for (int y = 0; y<PIC_HEIGHT* PIC_WIDTH; y++){

            float m = y%PIC_WIDTH;
            float n = y/PIC_WIDTH;

            mVertexBuffer.put(m);
            mVertexBuffer.put(n);
           // Log.v("AFMOBI", "OUTPUT VETEX" + "("+m+", "+n+")");
        }
    }

    public void onDraw(final int textureId, final FloatBuffer cubeBuffer,
                       final FloatBuffer textureBuffer) {

        GLES20.glUseProgram(mGLProgId);
        runPendingOnDrawTasks();
        if (!isInitialized()) {
            return;
        }


        GLES20.glClearColor(0f, 0f, 0f, 0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
        GLES20.glEnable(GLES20.GL_BLEND);


        cubeBuffer.position(0);
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }
       // onDrawArraysPre();
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 400);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, PIC_HEIGHT* PIC_WIDTH);

        GLES20.glDisable(GLES20.GL_BLEND);



        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        onDrawArraysAfter();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

   //     GLES20.glUseProgram(0);
        getHistogramDataBuffer();
    }


    public void getHistogramDataBuffer() {
        ByteBuffer ib0 = ByteBuffer.allocate(256 * 4);

        IntBuffer ib = IntBuffer.allocate(256);


        GLES20.glReadPixels(0, 0, 256, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib0);
        byte[] ia = ib0.array();

        Log.v("AFMOBI", ib.toString());

        int count = 0;


        for (int i = 0; i < 256 ; i++) {

            int result0 = ia[4*i + 0] & 0xff;
            int result1 = ia[4*i + 1]& 0xff;
            int result2 = ia[4*i + 2]& 0xff;
            int result3 = ia[4*i + 3]& 0xff;


            if (result0<255){
                count += result0;
            }



            Log.v("AFMOBI", "index: " + i + "  result : " + "("+result0+","+result1+"," + result2+"," + result3+")");


        }

        Log.v("AFMOBI", "index: "  + " count result : " + "("+count+")");
    }


}
