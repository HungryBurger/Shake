package com.example.hjh.myapplication;

import android.icu.util.Output;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private final static String NOTES = "notes.txt";
    private EditText textUIData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textUIData = findViewById(R.id.txtUIData);
        Button btn = findViewById(R.id.close);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(NOTES,0));
            out.write(textUIData.getText().toString());
            out.close();
        }
        catch (IOException e){e.printStackTrace();}
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            InputStream in = openFileInput(NOTES);
            if(in != null){
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String str = "";
                StringBuffer buf = new StringBuffer();

                while((str = reader.readLine())!=null){
                    buf.append(str+"\n");
                }
                in.close();
                textUIData.setText(buf.toString());
            }
        }catch (FileNotFoundException e){}
        catch (Throwable y){}
    }
}
