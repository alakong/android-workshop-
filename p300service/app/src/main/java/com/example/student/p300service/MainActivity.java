package com.example.student.p300service;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Intent intent;
    TextView textView;
    ProgressBar progressBar;
    ImageView imageView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView=(TextView)findViewById(R.id.textView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(100);
        imageView=(ImageView)findViewById(R.id.imageView);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
    }
//버튼1에 이름 서비스로 전달하는 메소드
    public void clickBt(View v) {
        String name = editText.getText().toString();
        intent = new Intent(this, MyService.class);
        intent.putExtra("command", "start");
        intent.putExtra("name", name);
        startService(intent);
    }
    //버튼2로 프로그레스 다이알로그 뜨게하는 메소드
    public  void clickBt2(View v){
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("진행중");
        progressDialog.show();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();

    }



    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
    }
    private void processIntent(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra("command");
            int cnt = intent.getIntExtra("cnt", 0);
            textView.setText(""+command+"  "+cnt);
            progressBar.setProgress(cnt*10);
            if(cnt%2==0){
                imageView.setImageResource(R.drawable.hain3);
            }else{
                imageView.setImageResource(R.drawable.hain2);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            stopService(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("이거");
        builder.setMessage("끝낼꺼에요?");
        builder.setIcon(R.drawable.hain4);
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });

        builder.show();
    }
}
