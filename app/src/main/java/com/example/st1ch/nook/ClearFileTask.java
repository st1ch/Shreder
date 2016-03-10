package com.example.st1ch.nook;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by st1ch on 18.01.2016.
 * Package: ${PACKAGE_NAME}
 */
public class ClearFileTask extends AsyncTask<Void, Double, Void> {

    final String LOG_TAG = "myLogs";
    private String fullFileName;
    private String fileName;

    public ClearFileTask(String fullFileName, String fileName){
        this.fullFileName = fullFileName;
        this.fileName = fileName;
    }

    @Override
    protected Void doInBackground(Void... params) {

        File file = new File(fullFileName);
        long length = file.length();
        int maxLength = 10485760;
        Log.d(LOG_TAG, "Удаляем файл: " + fullFileName + " ; длина файла: " + length + " байт");

        Log.d(LOG_TAG, "Очищаем файл");
        totalCleaning(file, length, maxLength);

        Log.d(LOG_TAG, String.format("Файл %1$s очищен", fileName));
        file.delete();
        Log.d(LOG_TAG, String.format("Файл %1$s удален", fileName));


        return null;
    }

    public void totalCleaning(File file, long length, int maxLength){
        byte zero = (byte) 0;
        byte one = (byte) 1;
        for(int i = 0; i < 3; i++){
            if(i % 2 == 0){
                cleanFullFile(file, length, maxLength, zero);
            } else cleanFullFile(file, length, maxLength, one);
        }
    }

    public void cleanFullFile(File file, long length, int maxLength, byte value){
        try {
            FileOutputStream os = new FileOutputStream(file);
            if(length > maxLength){
                while(length > maxLength){
                    try {
                        os.write(clean(maxLength, value));
                        length = length - maxLength;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    os.write(clean(length, value));
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public byte[] clean(long length, byte value){
        byte[] arr = new byte[(int)length];
        for(int i = 0; i < length; i++){
            arr[i] = value;
        }
        return arr;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MainActivity.getTvFileName().setText(String.format("Файл: %1$s был полностью удален", fileName));
        MainActivity.getmProgressDialog().dismiss();
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
    }
}
