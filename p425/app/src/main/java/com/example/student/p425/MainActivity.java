package com.example.student.p425;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());//default로 셋팅된 브라우저엔진을 사용하겠다
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void clickBt(View v){
        if(v.getId()==R.id.button){
            webView.loadUrl("http://m.naver.com");
        }else if(v.getId()==R.id.button2){
            webView.loadUrl("http://www.nate.com");

        }
    }




}
