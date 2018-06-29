package com.example.student.p555asynctask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText editText,editText2;
    TextView textView;
    LoginTask loginTask;
    ProgressDialog progressDialog;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.id);
        editText2=findViewById(R.id.pwd);
        textView=findViewById(R.id.textView);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Progress...");
        progressDialog.setCancelable(false);
        button=findViewById(R.id.button);
    }


    public void clickLogin(View v) throws ExecutionException, InterruptedException {
        loginTask=new LoginTask();
        String id= editText.getText().toString();
        String pwd= editText2.getText().toString();
        String result="";
        loginTask.execute(id,pwd);

    }

    class LoginTask extends AsyncTask<String,Void,String>{


        @Override //전처리
        protected void onPreExecute() {

            progressDialog.show();
            button.setEnabled(false);
        }

        @Override//스레드
        protected String doInBackground(String... strings) {//파라미터의 strings은 배열
            String id= strings[0];
            String pwd=strings[1];
            String result="";
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (id.equals("qq") && pwd.equals("11")) {
                result="1";
            } else {
                result="0";
            }
            return result;
        }

        @Override//후처리
        protected void onPostExecute(String s) {

            AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);

            progressDialog.dismiss();
            button.setEnabled(true);
            if(s.equals("1")){
                textView.setText("Login OK:)");
                builder.setTitle("Alert");
                builder.setMessage("Login OK!!");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editText.setText("");
                        editText2.setText("");
                        return;
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }else{
                textView.setText("Login FAIL:(");
                builder.setTitle("Alert");
                builder.setMessage("Login Fail!!");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editText.setText("");
                        editText2.setText("");
                        return;
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        }

    }


}
