package com.example.student.carinput;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    EditText id, speed,id2,pwd,num,id3,pwd2;
    InputTask inputTask;
    LoginTask loginTask;
    CreateTask createTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id=findViewById(R.id.id);
        speed=findViewById(R.id.speed);
        id2=findViewById(R.id.editText);
        pwd=findViewById(R.id.editText2);
        num=findViewById(R.id.editText3);
        id3=findViewById(R.id.editText4);
        pwd2=findViewById(R.id.editText5);
    }

    public void click(View v){
        String carId= id.getText().toString();
        String carSpeed=speed.getText().toString();

        if (carId == null || carSpeed== null || carId.equals("") ||  carSpeed.equals("")) {
            return;
        }
        try {
            inputTask = new InputTask("http://70.12.114.142/first/common/basicLog.do");
            inputTask.execute(carId,carSpeed).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void signin(View v){

        String Idinput= id2.getText().toString();
        String pwdinput=pwd.getText().toString();
        String carid=num.getText().toString();

        if (Idinput == null || pwdinput== null || pwdinput.equals("") ||  Idinput.equals("")) {
            return;
        }
        try {
            createTask = new CreateTask("http://70.12.114.142/first/common/signin.do");
            createTask.execute(Idinput,pwdinput,carid).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




    }

    public void login(View v){

        String Idlogin= id3.getText().toString();
        String pwdlogin=pwd2.getText().toString();

        if (Idlogin == null || pwdlogin== null || Idlogin.equals("") ||  pwdlogin.equals("")) {
            return;
        }
        try {
           loginTask = new LoginTask("http://70.12.114.142/first/common/login.do");
            loginTask.execute(Idlogin,pwdlogin).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




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

        }

        @Override
        protected String doInBackground(String... strings) {
            String id= strings[0];
            String pwd = strings[1];
            String car = strings[2];

            url += "?id="+id+"&pwd="+pwd+"&carid="+car;    //쿼리문

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
            if (s.trim().equals("1")){
                Toast.makeText(MainActivity.this, "Create OK:)", Toast.LENGTH_SHORT).show();
            }else if (s.trim().equals("0")){
                Toast.makeText(MainActivity.this,"Create FAIL:("+s,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,""+s,Toast.LENGTH_SHORT).show();
            }
        }

    }

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
            url += "?id=" + id + "&pwd=" + pwd;    //쿼리문

            //http request
            StringBuilder sb = new StringBuilder();
            URL url;
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();

                if (con != null) {
                    con.setConnectTimeout(5000);   //connection 5초이상 길어지면 exepction
                    //con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "*/*");
                    if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                        return null;


                    reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    while (true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                    }
                }
            } catch (Exception e) {
                return e.getMessage();   //리턴하면 post로
            } finally {

                try {
                    if (reader != null) {
                        reader.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.disconnect();
            }
            return sb.toString();
        }
    }


        class InputTask extends AsyncTask<String, Void, String> {

            String url;

            InputTask() {
            }

            InputTask(String url) {
                this.url = url;
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(String... strings) {
                String id = strings[0];
                String speed = strings[1];
                url += "?id=" + id + "&accel=" + speed;    //쿼리문

                //http request
                StringBuilder sb = new StringBuilder();
                URL url;
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {
                    url = new URL(this.url);
                    con = (HttpURLConnection) url.openConnection();

                    if (con != null) {
                        con.setConnectTimeout(5000);   //connection 5초이상 길어지면 exepction
                        //con.setReadTimeout(10000);
                        con.setRequestMethod("GET");
                        con.setRequestProperty("Accept", "*/*");
                        if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                            return null;


                        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String line = null;
                        while (true) {
                            line = reader.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                        }
                    }
                } catch (Exception e) {
                    return e.getMessage();   //리턴하면 post로
                } finally {

                    try {
                        if (reader != null) {
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

            }
        }

    }




