package com.example.student.p227;

import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    WebView wv;
    LinearLayout ll;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUi();
    }

    public void makeUi(){
        wv=(WebView)findViewById(R.id.wv);
        ll=(LinearLayout)findViewById(R.id.ll);
        iv=(ImageView)findViewById(R.id.iv);
        wv.setVisibility(View.INVISIBLE);
        ll.setVisibility(View.INVISIBLE);
        iv.setVisibility(View.INVISIBLE);

        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
    }

    public void clickBt(View v){
        if(v.getId()==R.id.bt1){
            wv.setVisibility(View.VISIBLE);
            wv.loadUrl("http://m.naver.com");
            ll.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.INVISIBLE);

        }else if(v.getId()==R.id.bt2){
            wv.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.VISIBLE);
            iv.setVisibility(View.INVISIBLE);

        }else if(v.getId()==R.id.bt3){
            wv.setVisibility(View.INVISIBLE);
            ll.setVisibility(View.INVISIBLE);
            iv.setVisibility(View.VISIBLE);


        }
    }
}
