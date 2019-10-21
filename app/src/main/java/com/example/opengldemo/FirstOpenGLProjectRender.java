package com.example.opengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FirstOpenGLProjectRender implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 1, 0, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(100, 100, width - 100, height - 100);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //glclear   GL_COLOR_BUFFER_BIT    清空屏幕
        //会用glClearColor填充屏幕
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }
}
