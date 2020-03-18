package com.example.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class AirHockeyRendererCp06 implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";
    private final FloatBuffer vertexData;
    private final Context context;
    private int program;
    private int aPositionLocation;
    private int uColorLocation;
    private int aColorLocation;
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;
    private final float[] modelMatrix = new float[16];


    public AirHockeyRendererCp06(Context context){
        float[] tableVertices = {
            0f, 0f,
            0f, 14f,
            9f, 14f,
            9f, 0f
        };

        float[] tableVerticesWithTriangles = {
                //Triangle Fan
                0, 0, 0, 1.5f,              1f, 1f, 1f,
                -0.5f, -0.8f, 0, 1f,        0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0, 1f,         0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0, 2f,          0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0, 2f,         0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0, 1f,        0.7f, 0.7f, 0.7f,

                //Line 1
                -0.5f, 0f, 0, 1.5f,         1f, 0f, 0f,
                0.5f, 0f, 0, 1.5f,          1f, 0f, 0f,

                //mallets
                0f, -0.4f, 0, 1.25f,        0f, 0f, 1f,
                0f, 0.4f, 0, 1.75f,         1f, 0f, 0f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);

        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0);
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader_cp5);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader_cp5);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        //check 是否低效率、能运行
        ShaderHelper.validateProgram(program);
        //告诉OpenGL在绘制任何东西到屏幕上到时候要使用这里定义的程序
        GLES20.glUseProgram(program);

        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0, 0, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60, 1, 0, 0);
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //glclear   GL_COLOR_BUFFER_BIT    清空屏幕
        //会用glClearColor填充屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        //draw triangles
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        //draw lines
        glDrawArrays(GL_LINES, 6, 2);

        //draw first mallet blue
        glDrawArrays(GL_POINTS, 8, 1);

        //draw second mallet blue
        glDrawArrays(GL_POINTS, 9, 1);
    }

}
