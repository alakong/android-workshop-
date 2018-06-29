package com.example.student.workshop3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    Intent intent, intent2, intent3;
    FrameLayout container;
    ProgressBar progressBar;
    ImageView imageView;
    ProgressDialog progressDialog;
    Button bt;
    String packName;
    String resName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUi();
    }

    public void makeUi() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.imageView);
        bt = (Button) findViewById(R.id.start2);
        container=(FrameLayout)findViewById(R.id.container) ;

        progressBar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        bt.setVisibility(View.INVISIBLE);
        container.setVisibility(View.INVISIBLE);
    }

    public void clickBt(View v) {
        intent = new Intent(this, MyService1.class);
        startService(intent);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("로딩중:)");
        progressDialog.show();
        // progressDialog.dismiss();
    }

    public void clickBt2(View v) {
        intent2 = new Intent(this, MyService2.class);
        startService(intent2);
        intent3 = new Intent(this, MyService3.class);
        startService(intent3);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        int act = intent.getIntExtra("act", 0);

        if (intent != null) {
            if (act == 1) {
                stopService(intent);
                progressDialog.dismiss();
                container.setVisibility(View.VISIBLE);
                bt.setVisibility(View.VISIBLE);


            } else if (act == 2) {
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                int cnt = intent.getIntExtra("cnt", 0);
                progressBar.setProgress(cnt * 10);
                 packName= this.getPackageName();
                 resName= "@drawable/hain" + cnt;
                 int resID = getResources().getIdentifier(resName, "drawable", packName);
                 imageView.setImageResource(resID); // 이미지뷰의 이미지를 설정한다;

               /* if (cnt % 2 == 0) {
                    imageView.setImageResource(R.drawable.hain4);
                } else {
                    imageView.setImageResource(R.drawable.hain2);
                }*/
            }
        }


    }

    protected void onDestroy() {
        super.onDestroy();
        if (intent != null) {
            stopService(intent);
        }
        if (intent2 != null) {
            stopService(intent);
        }
        if (intent3 != null) {
            stopService(intent);
        }
    }
}
