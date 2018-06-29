package com.example.student.p260;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Intent intent=getIntent();
        num= 2000*(intent.getIntExtra("num1",0));//앞에서 안날라오면 0을 넣겠다는 뜻
        intent.putExtra("result",num);
        setResult(RESULT_OK,intent);
        finish();
    }
}
