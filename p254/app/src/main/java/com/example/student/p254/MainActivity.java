package com.example.student.p254;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    LinearLayout container,scontainer;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUi();
    }

    private void makeUi() {
        container =findViewById(R.id.container);

        inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);//컨테이너에 다른 xml을 붙이려면 필요한 객체
    }

    public void clickBt(View view){
        View v1= inflater.inflate(R.layout.sub,container,true);
        TextView stv=container.findViewById(R.id.stv);//컨테이너에서 서브에 있는 애들 꺼낼 수 있음
        stv.setText("Button Click");
        Button sbt1=container.findViewById(R.id.sbt1);
        Button sbt2=container.findViewById(R.id.sbt2);
        scontainer=container.findViewById(R.id.scontainer);
        sbt1.setText("Seb Button 1");
        sbt2.setText("Seb Button 2");

        sbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scontainer.removeAllViews();
              inflater.inflate(R.layout.sub2,scontainer,true);


            }
        });

        sbt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              scontainer.removeAllViews();
             inflater.inflate(R.layout.sub3,scontainer,true);


            }
        });

    }
}
