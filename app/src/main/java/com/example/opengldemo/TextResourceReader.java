package com.example.opengldemo;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resourceId){
        StringBuilder body = new StringBuilder();
        try{
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;

            while((nextLine = bufferedReader.readLine()) != null){
                body.append(nextLine);
                body.append('\n');
            }
        }catch (Exception e){
            Log.e("yk", "read resource file failed");
            throw new RuntimeException("could not open resource file");
        }

        return body.toString();
    }

}
