package com.example.student.hzmr_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static EditText editText1,editText2;
    public static boolean flag = true;
    private static AlertDialog.Builder alBuilder;
    private static AlertDialog dialog;
    public static String id,pwd;
    private Intent intent,intent2;
    LoginTask loginTask;
    SharedPreferences login;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUi();

        //이미 로그인정보가 있으면 자동로그인
        if(login.getString("id","")!=null&login.getString("pwd","")!=null){
            id=login.getString("id","");
            pwd=login.getString("pwd","");
            Log.d("mainacccccc",id+"  "+pwd);
            loginTask = new LoginTask("http://70.12.114.136/mv/login.do");
            try {
                loginTask.execute(id.trim(), pwd.trim()).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeUi(){
        editText1 = findViewById(R.id.editText1);
        editText1.setPrivateImeOptions("defaultInputmode=english;");
        editText2 = findViewById(R.id.editText2);
        alBuilder = new AlertDialog.Builder(MainActivity.this);
        intent=new Intent(MainActivity.this,CreateActivity.class);
        intent2=new Intent(MainActivity.this,Main2Activity.class);
        login= PreferenceManager.getDefaultSharedPreferences(this);
        editor=login.edit();
    }


//로그인기능   <자동로그인으로 구현>
    public void clickBt(View v){
        id = editText1.getText().toString();
        pwd = editText2.getText().toString();


        if (id == null || pwd == null || id.equals("") || pwd.equals("")) {
            return;
        }
        try {
            loginTask = new LoginTask("http://70.12.114.136/mv/login.do");
            loginTask.execute(id.trim(), pwd.trim()).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
    }
    }


    //회원가입 페이지 이동
    public void clickBt2(View v){
        startActivity(intent);
    }


//로그인테스크
    class LoginTask extends AsyncTask<String,Void,String> {

        String url;
        LoginTask() {
        }
        LoginTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            String pwd = strings[1];
            url += "?id="+id+"&pwd="+pwd;    //쿼리문

            //http request
            StringBuilder sb = new StringBuilder();
            URL url;
            HttpURLConnection con= null;
            BufferedReader reader = null;
            try{
                url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();

                if(con!=null){
                    con.setConnectTimeout(5000);   //connection 5초이상 길어지면 exepction
                    //con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept","*/*");
                    if(con.getResponseCode()!=HttpURLConnection.HTTP_OK)
                        return null;



                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while(true){
                        line = reader.readLine();
                        if(line == null){
                            break;
                        }
                        sb.append(line);
                    }
                }
            }catch(Exception e){
                return e.getMessage();   //리턴하면 post로
            }finally {

                try {
                    if (reader !=null){
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.disconnect();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.charAt(0)=='1'){
                editor.putString("id",id);
                editor.putString("pwd",pwd);
                editor.commit();
                String name= s.substring(2);
                Toast.makeText(MainActivity.this,name+"님 안녕하세요:)",Toast.LENGTH_SHORT).show();
                intent2.putExtra("name",name);
                intent2.putExtra("id",id);
                startActivity(intent2);

            }else if (s.trim().equals("0")){
                Toast.makeText(MainActivity.this,"로그인 해주세요!",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(MainActivity.this,""+s,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        alBuilder.setTitle("Alert");
        alBuilder.setMessage("끝낼거야?");
        alBuilder.setPositiveButton("응", new DialogInterface.OnClickListener() {
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
        alBuilder.setNegativeButton("아니", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        dialog=alBuilder.create();
        dialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}