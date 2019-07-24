package com.example.myfirstapp;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    ProgressBar androidProgressBar;
    int progressStatusCounter = 0;
    TextView textView;
    TextView textView2;
    Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        //Start progressing
        new Thread(new Runnable() {
            public void run() {
                while (progressStatusCounter < 6) {
                    progressStatusCounter += 1;
                    progressHandler.post(new Runnable() {
                        public void run() {
                            androidProgressBar.setProgress(progressStatusCounter);
                            //Status update in textview
                            textView.setText("Hunt Progress:                          " + progressStatusCounter + "/" + androidProgressBar.getMax());
                            textView2.setText(((double)progressStatusCounter/6*100)+"% Complete");
                        }
                    });
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}