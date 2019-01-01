package com.example.shubham.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

Button androidB,pcB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidB = (Button)findViewById(R.id.androidMode);
        pcB = (Button)findViewById(R.id.pcMode);
    }


    public void androidMode(View view) {
        Intent i = new Intent(MainActivity.this,AndroidMode.class);
        startActivity(i);
    }

    public void offliceMode(View view) {
        Intent i = new Intent(MainActivity.this,SheetsPage.class);
        startActivity(i);
    }
}
