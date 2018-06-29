package com.example.student.p553;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView,imageView2,imageView3;
    MyHandler myHandler;
    int[] resID;
    Intent intent;

    //int imgs[]={R.drawable.hain1,R.drawable.hain2,R.drawable.hain3,R.drawable.hain4,R.drawable.hain5,R.drawable.hain6,R.drawable.hain7};
    //이렇게 넣으면 밑에서 imgs[i-1]로 부르면됨


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.imageView);
        imageView2=findViewById(R.id.imageView2);
        imageView3=findViewById(R.id.imageView3);
        myHandler=new MyHandler();
        resID= new int[8];
        resID[1]=R.drawable.hain1;
        resID[2]=R.drawable.hain2;
        resID[3]=R.drawable.hain3;
        resID[4]=R.drawable.hain4;
        resID[5]=R.drawable.hain5;
        resID[6]=R.drawable.hain6;
        resID[7]=R.drawable.hain7;
        intent=new Intent(MainActivity.this,MyService.class);

    }

    Runnable r1= new Runnable() {
        @Override
        public void run() {
            for(int i =1;i<=7;i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int j=i;
                //Image switch
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(resID[j]); // 이미지뷰의 이미지를 설정한다;
                    }
                });

            }
        }
    };

   /* Thread t1 = new Thread(new Runnable() {//스레드는 한번 돌고 죽어버림
        @Override
        public void run() {


            for(int i =1;i<=7;i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int j=i;
                //Image switch
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(resID[j]); // 이미지뷰의 이미지를 설정한다;
                    }
                });

            }
        }
    });*/

Runnable r2= new Runnable() {
    @Override
    public void run() {


        for(int i =1;i<=7;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Image switch handler
            Message msg = new Message();
            //번들이라는 바구니에 메세지를 집어넣는 것
            Bundle bundle = msg.getData();
            bundle.putInt("data", i);

            //*여기서 메세지를 보내면
            myHandler.sendMessage(msg);
        }
    }
};/*
    Thread t2= new Thread(new Runnable() {
        @Override
        public void run() {


            for(int i =1;i<=7;i++){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Image switch handler
                Message msg = new Message();
                //번들이라는 바구니에 메세지를 집어넣는 것
                Bundle bundle = msg.getData();
                bundle.putInt("data", i);

                //*여기서 메세지를 보내면
                myHandler.sendMessage(msg);
            }
        }
    });*/

    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int result = bundle.getInt("data");
            String  packName;
            String resName;
            packName= "com.example.student.p553";
            resName= "@drawable/hain" + result;
            int resID = getResources().getIdentifier(resName, "drawable", packName);
            imageView2.setImageResource(resID); // 이미지뷰의 이미지를 설정한다;
        }
    }

    public void clickBt(View view){

        if(view.getId()==R.id.stop){
            stopService(intent);
        }else{
        Thread t1= new Thread(r1);//버튼을 두번 눌러도 오류안나고 실행시키기 위해서는 계속 스레드를 새롭게 객체생성 해줘야함
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        startService(intent);}

    }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int result =intent.getIntExtra("act",0);
        String  packName;
        String resName;
        packName= "com.example.student.p553";
        resName= "@drawable/hain" + result;
        int resID = getResources().getIdentifier(resName, "drawable", packName);
        imageView3.setImageResource(resID); // 이미지뷰의 이미지를 설정한다;
    }
}
