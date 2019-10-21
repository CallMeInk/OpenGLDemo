package com.example.opengldemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyRendererV2 implements GLSurfaceView.Renderer {

    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private MalletV2 mallet;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private Puck puck;

    public AirHockeyRendererV2(Context context){
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        table = new Table();
        mallet = new MalletV2(0.08f, 0.15f, 32);
        puck = new Puck(0.06f, 0.02f, 32);

        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.texture);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

//        MatrixHelper.perspectiveM(projectionMatrix, 45, (float)width / (float) height, 1f, 10f);
//        Matrix.setIdentityM(modelMatrix, 0);
//        Matrix.translateM(modelMatrix, 0, 0f, 0f, -3f);
//        Matrix.rotateM(modelMatrix, 0, -60, 1, 0, 0);
//        final float[] temp = new float[16];
//        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

//        //draw the table
//        textureShaderProgram.useProgram();
//        textureShaderProgram.setUniforms(projectionMatrix, texture);
//        table.bindData(textureShaderProgram);
//        table.draw();
//
//        //draw the mallets.
//        colorShaderProgram.useProgram();
//        colorShaderProgram.setUniforms(projectionMatrix);
//        mallet.bindData(colorShaderProgram);
//        mallet.draw();

        //draw table
        positionTableInScene();
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(modelViewProjectionMatrix, texture);
        table.bindData(textureShaderProgram);
        table.draw();

        //draw the mallets
        positionObjectInScene(0f, mallet.height / 2f, -0.4f);
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

        positionObjectInScene(0f, mallet.height / 2f, 0.4f);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f);
        mallet.draw();

        //draw the puck.
        positionObjectInScene(0f, puck.height / 2f, 0f);
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        puck.bindData(colorShaderProgram);
        puck.draw();
    }

    private void positionTableInScene(){
        // the table is defined n terms of X & Y coordinates,
        // so we rotate it 90 degrees to lie flat on tbe XZ plane
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);

    }

    private void positionObjectInScene(float x, float y, float z){
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

}
