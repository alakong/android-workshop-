package com.example.student.p182;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       view= (TextView)findViewById(R.id.textView);
    }
    public void click(View v) {
        Button b=(Button)v;
       view.setText((b.getText()));
    }
}
