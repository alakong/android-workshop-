package com.example.student.client_;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {


    EditText speed;
    ImageView imageView;
    Client client;
    String TAG = "Client---";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speed = findViewById(R.id.speed);
        imageView = findViewById(R.id.imageView);
        client = new Client();
        client.start();

    }

    public void click(View v) {
        String msg = speed.getText().toString();
        client.sendMsg(msg);

    }

    @Override
    protected void onDestroy() {//앱이 종료될 때 실행
        client.stopClient();
        super.onDestroy();

    }

    //Client Socket Start...........
    public class Client extends Thread {

        String address = "192.168.0.64";
        Socket socket;
        boolean cflag = true;
        boolean flag = true;

        public Client() {
        }

        @Override
        public void run() {// 안드로이드에서 통신을 하는 모듈은 반드시 thread 안에서 동작
            while (cflag) {
                try {
                    Log.d(TAG, "Connected Server ..");
                    socket = new Socket(address, 8888);
                   //socket.setSoTimeout(5000);

                    Log.d(TAG, "Connected Server ..");
                    cflag = false;
                    break;
                } catch (IOException e) {
                    Log.d(TAG, "Retry...");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } // while끝

            // After Connected...
            try {
                new Receiver(socket).start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        public void sendMsg(String msg) {
            try {

                Sender sender = new Sender(socket);
                sender.setSendMsg(msg);
                new Thread(sender).start();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


        class Sender implements Runnable {
            Socket socket;
            OutputStream out;
            DataOutputStream outw;
            String sendMsg;

            public Sender(Socket socket) throws IOException {
                this.socket = socket;
                out = socket.getOutputStream();
                outw = new DataOutputStream(out);
            }

            public void setSendMsg(String sendMsg) {
                this.sendMsg = sendMsg;
            }

            @Override
            public void run() {
                try {
                    if (outw != null) {
                        outw.writeUTF(sendMsg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        class Receiver extends Thread {
            Socket socket;
            InputStream in;
            DataInputStream inr;

            public Receiver(Socket socket) throws IOException {
                this.socket = socket;
                in = socket.getInputStream();
                inr = new DataInputStream(in);
            }

            @Override
            public void run() {

                try {

                    while (flag && inr != null) {

                        String str = inr.readUTF();
                        Log.d(TAG, str);
                        convertImg(str);//서버에서 받은 숫자에 따라 이미지를 변경하는 메서드

                    }
                } catch (Exception e) {
                    Log.d(TAG, "Server Closed...");
                } finally {
                    if (inr != null) {
                        try {
                            inr.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

        }


        //Client Socket End.............

        public void convertImg(final String str) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (str.equals("1")) {
                                imageView.setImageResource(R.drawable.car1);
                            } else if (str.equals("2")) {
                                imageView.setImageResource(R.drawable.car2);
                            } else if (str.equals("3")) {
                                imageView.setImageResource(R.drawable.car3);
                            }
                        }
                    });
                }
            };
            new Thread(r).start();
        }

        public void stopClient() {
            try {
                Thread.sleep(1000);
                flag = false;
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


