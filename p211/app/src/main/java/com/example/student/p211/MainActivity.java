package com.example.student.p211;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText id,pwd;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUI();
    }

    public void makeUI(){
        id=(EditText)findViewById(R.id.tx_id);
        pwd=(EditText)findViewById(R.id.tx_pwd);
        login=(Button)findViewById(R.id.bt_login);
    }
    public void clickLogin(View v){
        //editText에서 값을 가지고 온다.
        String tx_id=id.getText().toString();
        String tx_pwd=pwd.getText().toString();
        Intent intent=null;
        id.setText("");
        pwd.setText("");
        if(tx_id.equals("qq")&&tx_pwd.equals("11")){
            intent.putExtra("loginid",tx_id);
           intent=new Intent(MainActivity.this,loginok.class);

        }else{

            intent=new Intent(MainActivity.this,loginfail.class);

        }

        startActivity(intent);
    }
}
