package com.example.student.hzmr_demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateActivity extends AppCompatActivity {

    private static  EditText editText4, editText5, editText6, editText3;
    private static Intent intent;
    private static ProgressDialog progressDialog;
    CreateTask createTask;
    private static boolean checkConf=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        makeUi();

        //비밀번호 CONFIRM확인하는 부분
        editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password=editText5.getText().toString();
                String confirm=editText6.getText().toString();
                if(confirm.equals(password)){
                    editText6.setTextColor(Color.parseColor("#000000"));
                    checkConf=true;
                }else{
                    editText6.setTextColor(Color.parseColor("#ff5050"));
                    checkConf=false;
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });

    }

    public void makeUi(){
        editText3 = findViewById(R.id.editText3);
        editText3.setPrivateImeOptions("defaultInputmode=english;");
        editText4 = findViewById(R.id.editText4);
        editText4.setPrivateImeOptions("defaultInputmode=english;");
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        progressDialog = new ProgressDialog(CreateActivity.this);
        intent=new Intent(CreateActivity.this,MainActivity.class);
    }

    //회원가입 데이터 전송하기
    public void clickBt3(View v) {
        String name = editText3.getText().toString();
        String id = editText4.getText().toString();
        String pwd = editText5.getText().toString();
        String conpwd = editText6.getText().toString();
        if (name == null || id == null || pwd == null || conpwd == null || name.equals("") || id.equals("") || pwd.equals("") || conpwd.equals("")||checkConf==false) {
            return;
        }
        createTask = new CreateTask("http://70.12.114.136/mv/join.do");
        createTask.execute(name.trim(),id.trim(),pwd.trim());
    }

    class CreateTask extends AsyncTask<String,Void,String> {

        String url;

        CreateTask() {
        }

        CreateTask(String url) {
            this.url = url;
        }


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("create");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String id = strings[1];
            String pwd = strings[2];

            url += "?name="+name+"&id="+id+"&pwd="+pwd;    //쿼리문

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
                progressDialog.dismiss();
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
            progressDialog.dismiss();
            if (s.trim().equals("1")){
                Toast.makeText(CreateActivity.this, "Create OK:)", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }else if (s.trim().equals("2")){
                Toast.makeText(CreateActivity.this,"Create FAIL:("+s,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(CreateActivity.this,""+s,Toast.LENGTH_SHORT).show();
            }
        }

    }

}


