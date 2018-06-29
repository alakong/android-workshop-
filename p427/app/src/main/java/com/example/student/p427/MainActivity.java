package com.example.student.p427;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


     WebView webView;
     TextView textView;
     Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView)findViewById(R.id.textView);
        webView=(WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new JS(),"js");//자바스크립트 인터페이스 이름을 설정하는 것
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        handler= new Handler();
    }

    public void clickBt(View v){
        webView.loadUrl("http://70.12.114.142/ad/sample.html");

    }

    public void clickBt2(View v){
       handler.post(new Runnable() {//웹뷰일 경우 신규 스레드로는 불가하여 핸들러를 사용함
            @Override
            public void run() {
                webView.loadUrl("javascript:changeImage()");
            }
        });
    }



    final class JS{
        JS(){
        }
        @android.webkit.JavascriptInterface //이걸 붙여야 웹에서 호출했을 때 실행됨
        public void clickJS(String i){
            textView.setText(i);
            Log.d("[ JS ]","Event Process..." +i);
        }
    }
}
