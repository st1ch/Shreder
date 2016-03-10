package com.example.st1ch.nook;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityChooseFile extends ListActivity {

    private List<String> directoryEntries = new ArrayList<String>();

    private File currentDirectory = new File("/media/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);

        browseTo(currentDirectory);
    }

    private void browseTo(final File dirPath){
        if(dirPath.isDirectory()){
            this.currentDirectory = dirPath;
            fill(dirPath.listFiles());

            TextView titleManager = (TextView) findViewById(R.id.titleManager);
            titleManager.setText(dirPath.getAbsolutePath());
        } else{

            Intent intent = new Intent();
            intent.putExtra("fileName", dirPath.getName());
            intent.putExtra("fullFileName", dirPath.getAbsolutePath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void fill(File[] files){
        this.directoryEntries.clear();
        if(this.currentDirectory.getParent() != null){
            this.directoryEntries.add("..");
        }

        for(File file: files){
            this.directoryEntries.add(file.getAbsolutePath());
        }

        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, R.layout.row, this.directoryEntries);
        this.setListAdapter(directoryList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int selectionRowId = position;
        String selectedFileString = this.directoryEntries.get(selectionRowId);

        if(selectedFileString.equals("..")){
            this.upOnLevel();
        } else {
            File clickedFile = new File(selectedFileString);
            this.browseTo(clickedFile);
        }
    }

    @Override
    public void onBackPressed() {
        if(this.currentDirectory.getParent()==null){
            super.onBackPressed();
        } else {
            upOnLevel();
        }
    }

    private void upOnLevel(){
        if(this.currentDirectory.getParent() != null){
            this.browseTo(this.currentDirectory.getParentFile());
        }
    }
}
