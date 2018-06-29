package com.example.student.p246;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView view1,view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUi();
    }

    public void makeUi(){
        view1=(ImageView)findViewById(R.id.imageView);
        view2=(ImageView)findViewById(R.id.imageView2);
    }

    public void up_down(View v){
        if(v.getId()==R.id.up){
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.INVISIBLE);

        }else if(v.getId()==R.id.down){
            view2.setVisibility(View.VISIBLE);
            view1.setVisibility(View.INVISIBLE);

        }
    }
}
