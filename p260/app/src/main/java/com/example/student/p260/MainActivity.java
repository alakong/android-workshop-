package com.example.student.p260;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView,textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.textView);
        textView3=(TextView)findViewById(R.id.textView3);

    }

    public void  clickBt(View v){
        Intent intent =new Intent(getApplicationContext(),SecondActivity.class);
        intent.putExtra("num1",1000);
        startActivityForResult(intent,100);
    }

    @Override //받아서 처리하는 메쏘드
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, " "+requestCode+" "+resultCode, Toast.LENGTH_SHORT).show();
        if(requestCode==100 && resultCode==RESULT_OK){
            int result=data.getIntExtra("result",0);
            textView.setText(result+"");
        }else if(requestCode==101 && resultCode==RESULT_OK){
            int result=data.getIntExtra("result",0);
            textView3.setText(result+"");}
    }

    public void  clickBt2(View v){
        Intent intent =new Intent(getApplicationContext(),ThirdActivity.class);
        intent.putExtra("num1",1000);
        startActivityForResult(intent,101);
    }

}
