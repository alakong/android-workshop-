package com.example.student.p260;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SecondActivity extends AppCompatActivity {
    TextView textView2;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView2=(TextView)findViewById(R.id.textView2);

        Intent intent=getIntent();
        num= intent.getIntExtra("num1",0);//앞에서 안날라오면 0을 넣겠다는 뜻
        textView2.setText(num+"");
    }

    public void clickBt2(View v){
        int result = num*3000;
        Intent intent= new Intent();
        intent.putExtra("result",result);
        setResult(RESULT_OK,intent);
        finish();

    }
}
