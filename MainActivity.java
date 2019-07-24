package com.example.myfirstapp;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    ProgressBar androidProgressBar;
    int progressStatusCounter = 0;
    TextView textView;
    TextView textView2;
    TextView textView3;
    Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        //Start progressing
        new Thread(new Runnable() {
            public void run() {
                            //Status update in textview
                androidProgressBar.setProgress(6);
                textView.setText("Hunt Progress:                          6/6");
                textView2.setText("100% Complete");
                textView3.setVisibility(TextView.VISIBLE);
            }
        }).start();


    }


}