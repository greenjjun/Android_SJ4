package com.example.sunrin_08.myapplication;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textView;
    Button button;
    int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String fFilename;
    private Context context = this;

    private List<String> fItem = null;
    private List<String> fPath = null;
    private String root = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        textView = findViewById(R.id.txt);
        button = findViewById(R.id.btn_getDir);

        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDir(root);
            }
        });

    }

    private void getDir(String dirPath) {
        textView.setText(String.format("Loaction: %s", dirPath));

        fItem = new ArrayList<>();
        fPath = new ArrayList<>();

        File f = new File(dirPath);
        File[] files = ((File) f).listFiles();

        if (!dirPath.equals(root)) {
            fItem.add("../");
            fPath.add(f.getParent());
        }


        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            fPath.add(file.getAbsolutePath());

            if (file.isDirectory())
                fItem.add(file.getName() + "/");
            else
                fItem.add(file.getName());
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fItem);
        listView.setAdapter(fileList);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(fPath.get(position));

                if (file.isDirectory()) {
                    if (file.canRead())
                        getDir(fPath.get(position));
                    else {
                        Toast.makeText(context, "No files in this folder", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    fFilename = file.getName();
                }
            }
        });
    }
}
