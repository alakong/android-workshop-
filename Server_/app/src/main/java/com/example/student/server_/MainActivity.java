package com.example.student.server_;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    TextView textView,textView2;
    EditText editText;
    String TAG="Server---";
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);
        editText=findViewById(R.id.editText);
        try{

            server=new Server();
            server.start();}

        catch(Exception e){

        }
    }

    public void click(View v){
        String str = editText.getText().toString();
        server.sendAll(str);
    }

    public void setSpeed(final String msg){
        Runnable r= new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(msg);
                    }
                });
            }
        };
        new Thread(r).start();
    }

    public void setConnect(final String ip,final String msg){
        Runnable r= new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (msg.equals("t")) {
                            textView2.setText(ip+" : Client Connected");
                        } else {
                            textView2.setText("Client Disconnected");
                        }
                    }
                });

            }
        };
        new Thread(r).start();
    }

    //HttpRequest Start...
    class SendHttp extends AsyncTask<Void,Void,Void>{

        String surl="http://70.12.114.142/ws/main.do?speed=";
        String speed;
        HttpURLConnection urlConn;
        URL url;

        public SendHttp(){
        }

        public SendHttp(String speed){
            this.speed=speed;
            surl+=speed;

            try {
                url = new URL(surl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                urlConn=(HttpURLConnection)url.openConnection();
                urlConn.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    //ServerSocket Start...

    public class Server extends Thread {

        ServerSocket serverSocket;
        boolean flag = true;
        boolean rflag = true;
        HashMap<String, DataOutputStream> map = new HashMap<>();

        public Server() throws IOException {
            // Create ServerSocket ...
            serverSocket = new ServerSocket(8888);
            Log.d(TAG,"Ready Server ..");
        }

        @Override
        public void run() {
            // Accept Client Connection ...
            try {
                while (flag) {
                    Log.d(TAG,"Ready Accept...");
                    Socket socket = serverSocket.accept();
                    String client= socket.getInetAddress().getHostAddress();
                    setConnect(client, "t");
                    new Receiver(socket).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        class Receiver extends Thread {//클라이언트별로 하나씩 생긴다. 클라이언트가 접속 할때마다 서버에서 하나씩 리시버만드는것

            InputStream in;
            DataInputStream din;
            OutputStream out;
            DataOutputStream dout;
            Socket socket;
            String ip;

            public Receiver(Socket socket) {
                try {
                    this.socket = socket;
                    in = socket.getInputStream();
                    din = new DataInputStream(in);
                    out = socket.getOutputStream();
                    dout = new DataOutputStream(out);
                    ip = socket.getInetAddress().getHostAddress();
                    map.put(ip, dout);
                    Log.d(TAG,"Connected Count:" + map.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } // end Recevier

            @Override
            public void run() {
                try {
                    while (rflag) {

                        if (socket.isConnected() && din != null && din.available() > 0) {
                            String str = din.readUTF();//사용자들의 메세지가 들어오면
                            setSpeed(str);
                            SendHttp sendHttp=new SendHttp(str);
                            sendHttp.execute();

                            }
                            //sendAll("[" + ip + "]" + str);
                        }
                    } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    setConnect(null,"f");

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if (dout != null) {
                        try {
                            dout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (din != null) {
                        try {
                            din.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }

        public void sendAll(String msg) {
            System.out.println(msg);//서버에서 한번 찍히고
            Sender sender = new Sender();
            sender.setMeg(msg);
            sender.start();
        }

        class Sender extends Thread {

            String msg;

            public void setMeg(String msg) {
                this.msg = msg;
            }

            @Override
            public void run() {
                try {
                    Collection<DataOutputStream> col = map.values();//맵에서 values를 꺼내서
                    Iterator<DataOutputStream> it = col.iterator();//iterator로 돌린다~
                    while (it.hasNext()) {
                        it.next().writeUTF(msg);
                    }

                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        }

        public void stopServer() {
            try {
                Thread.sleep(1000);
                rflag=false;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    //ServerSocket End...

}
