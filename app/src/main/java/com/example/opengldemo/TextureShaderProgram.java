package com.example.opengldemo;

import android.content.Context;
import android.opengl.GLES20;

public class TextureShaderProgram extends ShaderProgram{

    //Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    //Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordiantesLocation;

    public TextureShaderProgram(Context context){
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        //Retrive uniform locations for the sjader program
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);

        //Retrive attribute locations for the shader program
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordiantesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId){
        // pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        //set the active texture unit to texture unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // bind the texture to this unit
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        //tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation(){
        return aTextureCoordiantesLocation;
    }

}
