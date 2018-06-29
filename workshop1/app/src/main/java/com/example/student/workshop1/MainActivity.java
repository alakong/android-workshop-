package com.example.student.workshop1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    WebView wv1,wv2;
    LinearLayout main;
    RelativeLayout login,signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeUi();
    }

    public void makeUi(){

        wv1=(WebView)findViewById(R.id.chart);
        wv2=(WebView)findViewById(R.id.naver);
        main=(LinearLayout)findViewById(R.id.main);
        login=(RelativeLayout)findViewById(R.id.login);
        signin=(RelativeLayout) findViewById(R.id.signin);

        wv1.setVisibility(View.INVISIBLE);
        wv2.setVisibility(View.INVISIBLE);
        main.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        signin.setVisibility(View.INVISIBLE);

        wv2.setWebViewClient(new WebViewClient());
        wv2.getSettings().setJavaScriptEnabled(true);
        wv1.setWebViewClient(new WebViewClient());
        wv1.getSettings().setJavaScriptEnabled(true);

    }

    public void invisible(){
        wv1.setVisibility(View.INVISIBLE);
        wv2.setVisibility(View.INVISIBLE);
        main.setVisibility(View.INVISIBLE);
        login.setVisibility(View.INVISIBLE);
        signin.setVisibility(View.INVISIBLE);
    }

    public void click(View v){
        if(v.getId()==R.id.btmain){
            invisible();
            main.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btsignin){
            invisible();
            signin.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btlogin){
            invisible();
            login.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btchart){
            invisible();
            wv1.loadUrl("http://70.12.114.142/mv");
            wv1.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btnaver){
            invisible();
            wv2.loadUrl("http://m.naver.com");
            wv2.setVisibility(View.VISIBLE);
        }
    }
}
