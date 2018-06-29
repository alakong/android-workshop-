package com.example.student.p540thread;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    MyHandler myHandler;
    MyHandler2 myHandler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        myHandler = new MyHandler();
        myHandler2=new MyHandler2();

    }

    public void clickBt(View v) {
        t.start();
    }

    Thread t = new Thread(new Runnable() {
        int i = 0;

        @Override
        public void run() {
            while (i <= 10) {
                i++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                //번들이라는 바구니에 메세지를 집어넣는 것
                Bundle bundle = msg.getData();
                bundle.putInt("data", i);

                //*여기서 메세지를 보내면
                myHandler.sendMessage(msg);
            }
        }
    });



    class MyHandler extends Handler {
        public  void handleMessage(Message msg){
            Message msg2 =new Message();
            Bundle bundle= msg2.getData();
            bundle.putInt("data2",msg.getData().getInt("data"));
            myHandler2.sendMessage(msg2);
        }
    }

    class MyHandler2 extends Handler {
        //*여기서 받는 것
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int result = bundle.getInt("data2");
            if (result == 11) {
                textView.setText("Finish");
            } else {
                textView.setText(bundle.getInt("data2") + "");
            }
        }
    }

}
