package com.example.opengldemo;

import android.content.Context;
import android.opengl.GLES20;

public class ColorShaderProgramCP08 extends ShaderProgram {

    private final int uMatrixLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgramCP08(Context context){
        super(context, R.raw.simple_vertex_shader_cp6, R.raw.simple_fragment_shader_cp6);

        //retrive uniform locations for the shader program
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

        //retrive attribute locations for the shader program
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        aColorLocation = GLES20.glGetUniformLocation(program, A_COLOR);
    }

    public void setUniforms(float[] matrix){
        //pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getColorAttributeLocation(){
        return aColorLocation;
    }

}
