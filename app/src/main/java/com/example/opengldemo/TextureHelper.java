package com.example.opengldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureHelper {

    public static int loadTexture(Context context, int resourceId){
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0){
            Log.e("yk", "could not generate a new openGL texture object");
            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null){
            Log.e("yk", "ResourceId::" + resourceId + "could not be decoded");
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        //告诉openGL这应该被作为一个二维纹理对待，第二个参数告诉openGL绑定到哪个纹理对象的ID
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);

        //缩小和放大的纹理过滤参数
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        //读入bitmap定义的位图数据，复制到当前绑定的纹理对象
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectIds[0];

    }

}
