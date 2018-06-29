package com.example.student.p554asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ProgressBar progressBar;
    MyTask myTask;
    Button button;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.textView);
        progressBar=findViewById(R.id.progressBar);
        button=findViewById(R.id.button);
        progressDialog=new ProgressDialog(this);




    }

    public void clickBt(View v){
        myTask=new MyTask("12121324145364576");
     /*   try {

            //int result=myTask.execute().get();
            //get으로 하면 얘가 일단 실행하고 내려가므로 로그인과 같은 기능 구현시 필요하다.
            //execute로 그냥하면 스레드로 시작하고 안끝나도 바로 아래로 구문이 내려가는 데 그거 막아줄 수 있음

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        myTask.execute();//thread이므로 얘가 실행돼도 아래로 내려가서 진행.
        Log.d("click.....","@@@@@@@@@@@@@@@@@");


    }

    class MyTask extends AsyncTask<String,Integer,Integer>{//<메인스레드 동작아규먼트, doInBackground에서 onProgressUpdate로 넘겨주는 값,리턴값>
        String msg;

        public MyTask(){

        }

        public MyTask(String msg){
            this.msg=msg;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setMax(55);
            textView.setText("start Thread.......");
            button.setEnabled(false);
            progressDialog.setTitle("Progress");
            progressDialog.setMessage("Ing.......");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(String...Strings) {//여기 안에서는 UI건드릴 수 없음 그래서 아래onProgressUpdate를 통해 접근하는 것
            //thread의 run부분,arguments 넣을 수 있다.
            Log.d("doInBackground.....",msg+"start~~~~~~~~~~~");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int sum=0;
            for(int i=1;i<=10;i++){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sum+=i;
                publishProgress(sum);
            }
            Log.d("doInBackground.....","end~~~~~~~~~~~");



            return sum;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Integer result) {
            textView.setText("End Thread: "+result);
            button.setEnabled(true);
            progressDialog.dismiss();
        }
    }
}
