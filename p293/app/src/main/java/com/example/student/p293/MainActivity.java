package com.example.student.p293;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

        SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("pref",Activity.MODE_PRIVATE);

    }

  /*  @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();

        }*/

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        restoreState();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        saveState();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();

    }

    protected void restoreState(){
        if(sp!=null){
          if(sp.contains("cnt")){
              int rcnt= sp.getInt("cnt",0);
              Toast.makeText(this, rcnt+"", Toast.LENGTH_SHORT).show();
          }
        }

    }

    protected void saveState(){
        SharedPreferences.Editor editor=sp.edit();
        if(sp!=null){
            if(sp.contains("cnt")){
                int rcnt=sp.getInt("cnt",0);
                editor.putInt("cnt",++rcnt);


            }else{
                int cnt=0;
                editor.putInt("cnt",++cnt);
            }

            editor.commit();

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);//this:현재 나의 액티비티 위에 다이알로그를 띄우겠음
        builder.setTitle("Alert Message !!");
        builder.setMessage("Do you want to exit & clear");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor=sp.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        });
        builder.show();
    }

    /*
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onReStart", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this,
                "onStop", Toast.LENGTH_SHORT).show();

    }

   */
}
