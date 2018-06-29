package com.example.student.server;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.TextView;

        import java.io.DataInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.ServerSocket;
        import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    TextView textView,textView2;
    private static final String TAG="SERVER";
    boolean rflag = true;
    boolean flag = true;
    Server server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting();
    }

    public void setting() {
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        server=new Server();
        server.start();
    }


    public class Server extends Thread{

        ServerSocket serverSocket;

        public Server(){
            try {
                serverSocket = new ServerSocket(8888);
            } catch (IOException e) {
            }
            Log.d(TAG,"Ready Server...");

        }

        @Override
        public void run() {
            try {
                while (flag) {
                    Log.d(TAG,"Ready Accept");
                    Socket socket = serverSocket.accept();
                    new Receiver(socket).start();

                }
            }catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


    class Receiver extends Thread{

        InputStream in;
        DataInputStream din;
        Socket socket;
        String ip;

        public Receiver(Socket socket){
            Log.d(TAG,"Receiver Thread Start----------");

            try {
                this.socket = socket;
                in = socket.getInputStream();
                din = new DataInputStream(in);
                ip = socket.getInetAddress().getHostAddress();


            }catch(Exception e){

            }
        }

        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(ip);
                    }
                });
                while (rflag) {

                    if (socket.isConnected() && din != null && din.available() > 0) {
                        final String str = din.readUTF();//사용자들의 메세지가 들어오면

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView2.setText(str);
                            }
                        });

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
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

    @Override
    protected void onDestroy() {
        flag=false;
        rflag=false;
        super.onDestroy();
    }
}
