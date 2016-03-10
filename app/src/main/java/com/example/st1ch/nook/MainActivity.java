package com.example.st1ch.nook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    private static TextView tvFileName;
    private String fullFileName;
    private String fileName;
    private static ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvFileName = (TextView) findViewById(R.id.tvFile);

    }

    public void onclick(View v){
        switch(v.getId()){
            case R.id.btnChooseFile:
                chooseFile();
                break;
            case R.id.btnDeleteFile:
                deleteFile();
                break;
        }
    }

    private void chooseFile(){
        Log.d(LOG_TAG, "Выбирается файл для удаления");
        Intent intent = new Intent(this, ActivityChooseFile.class);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(data == null){ return;}
                fullFileName = data.getStringExtra("fullFileName");
                fileName = data.getStringExtra("fileName");
                String formatString = this.getString(R.string.file_name);
                String subsString = String.format(formatString, fileName);
                Log.d(LOG_TAG, "Выбран файл: " + fileName);
                tvFileName.setText(subsString);
                tvFileName.setVisibility(View.VISIBLE);
                break;
        }

    }


    private void deleteFile(){
        try{
            if(new File(fullFileName).exists()){
                ClearFileTask cft = new ClearFileTask(fullFileName, fileName);
                cft.execute();
                createProgressDialog();
            } else {
                Log.d(LOG_TAG, String.valueOf(R.string.open_delete_error));
                Toast.makeText(this, R.string.open_delete_error, Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Log.d(LOG_TAG, String.valueOf(R.string.open_delete_error));
            Toast.makeText(this, R.string.open_delete_error, Toast.LENGTH_LONG).show();
        }
    }

    private void createProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getString(R.string.deleting_file_progress));
        mProgressDialog.show();
    }

    public static TextView getTvFileName() {
        return tvFileName;
    }

    public static ProgressDialog getmProgressDialog() {
        return mProgressDialog;
    }
}
