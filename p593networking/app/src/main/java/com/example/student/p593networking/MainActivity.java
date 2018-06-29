package com.example.student.p593networking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText, editText2;
    LoginTask loginTask;
    LocationTask locationTask;
    ProgressDialog progressDialog;
    boolean flag=true;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        builder=new AlertDialog.Builder(MainActivity.this);
        new Thread(r).start();
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(30000);//30초에 한번씩, true일때 무한루프
                    //좌표를 가지고 온다.
                    locationTask=new LocationTask("http://70.12.114.142/android/location.jsp");
                    locationTask.execute(37.12,127.123);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void clickBt(View v) {
        String id = editText.getText().toString();
        String pwd = editText2.getText().toString();
        if (id == null || pwd == null || id.equals("") || pwd.equals("")) {
            return;
        }
        loginTask = new LoginTask("http://70.12.114.142/android/login.jsp");
        loginTask.execute(id.trim(), pwd.trim());

    }

    class LoginTask extends AsyncTask<String, String, String> {

        String url;

        public LoginTask() {}
        public LoginTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Login...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String id = strings[0];
            String pwd = strings[1];

            //HTTPrequest
            url += "?id=" + id + "&pwd=" + pwd;
            StringBuilder sb = new StringBuilder();
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader reader=null;
            try {
                url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(5000);//5초지나면 실행 타임아웃  서버에 연결되는 시간이 delay를 초과햇을때 Exception
                    //con.setReadTimeout(10000);  // inputStream으로 읽어올때 delay를 초과하면 Exception
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "*/*");
                    if (con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    reader =new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line="";
                while(true){
                    line=reader.readLine();
                    if(line==null){
                        break;
                    }
                    sb.append(line);
                }
                }

            } catch (Exception e) {
                progressDialog.dismiss();
                return e.getMessage();
            } finally {
                con.disconnect();
                try {
                    if(reader!=null){
                    reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if(s.trim().equals("1")){
                Toast.makeText(MainActivity.this, "Login OK"+s , Toast.LENGTH_SHORT).show();
            }else if(s.trim().equals("0")){
                Toast.makeText(MainActivity.this, "Login FAIL"+s , Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "" + s, Toast.LENGTH_SHORT).show();

            }
        }
    }


//LocationTask 내 위치 전송하기
    class LocationTask extends AsyncTask<Double, Void, String> {

        String url;

        public LocationTask() {
        }

        public LocationTask(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(Double... doubles) {
            double lat=doubles[0];
            double log=doubles[1];

            //HTTPrequest
            url += "?lat=" + lat + "&log=" + log;
            StringBuilder sb = new StringBuilder();
            URL url = null;
            HttpURLConnection con = null;
            try {
                url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(5000);//5초지나면 실행 타임아웃-서버에 연결되는 시간이 delay를 초과햇을때 Exception
                    //con.setReadTimeout(10000);  // inputStream으로 읽어올때 delay를 초과하면 Exception
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "*/*");
                    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                        return null;
                }

            } catch (Exception e) {
                return e.getMessage();
            } finally {
                con.disconnect();
            }
            return null;
        }
    }

    //뒤로가기 눌렀을 때 실행되는 메서드
    @Override
    public void onBackPressed() {
        builder.setTitle("Alert");
        builder.setMessage("끝낼거야?");
        builder.setPositiveButton("응", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flag=false;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        builder.setNegativeButton("아니", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
