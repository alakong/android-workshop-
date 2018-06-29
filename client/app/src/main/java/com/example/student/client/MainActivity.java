package com.example.student.client;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;

        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="CLIENT";

    EditText ip, content;
    Button submit;
    boolean flag = true;
    String address="192.168.0.64";
    Socket socket;
    boolean cflag=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setting();
    }

    public void setting() {
        ip = findViewById(R.id.ip);
        content = findViewById(R.id.content);
        submit = findViewById(R.id.button);
        submit.setEnabled(false);
        connect.start();
    }


    Thread connect = new Thread(new Runnable() {
        @Override
        public void run() {
            while(cflag) {
                try {
                    socket = new Socket(address,8888);
                    Log.d(TAG,"Connected Server ..");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            submit.setEnabled(true);
                        }
                    });
                    cflag=false;
                    break;
                }catch(IOException e) {
                    Log.d(TAG,"Retry...");            }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }



        }

    });

    public void clickBt(View v){

        String msg=content.getText().toString();
        try {
            Sender sender = new Sender(socket);
            Thread t = new Thread(sender);
            sender.setSendMsg(msg);
            t.start();
        }catch(Exception e) {
        }

    }



    class Sender implements Runnable {

        Socket socket;
        OutputStream out;
        DataOutputStream outw;
        String sendMsg;


        public Sender(Socket socket) throws IOException {
            Log.d(TAG,"Sender Constructed ..");
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
}




