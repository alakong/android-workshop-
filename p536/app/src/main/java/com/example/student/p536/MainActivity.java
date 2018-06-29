package com.example.student.p536;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);

    }

    public void clickBt(View v){
        t.start();
    }


  //스레드 객체 생성하는 방법 1
    class MyThread extends Thread{

    }

//스레드 객체 생성하는 방법 2
    Thread t = new Thread(new Runnable() {
        int i =1;
        @Override
        public void run() {
            while(i<=10){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                i++;
                //서브스레드에서는 메인스레드의 내용을 변경할 수 없다.
                //textView.setText(i+""); 그래서 얘가 실행오류뜸
                //이걸 실행시키려면 아래와 같은 메서드를 이용하여 실행가능
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textView.setText(i+"");

                    }
                });

            }

        }
    });

}
