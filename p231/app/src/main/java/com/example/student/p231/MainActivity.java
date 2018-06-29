package com.example.student.p231;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    CheckBox cb1,cb2;
    RadioButton rb1,rb2;
    Switch switch1;
    ToggleButton togglebutton;
    RadioGroup rg;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUi();
    }

    public void makeUi(){
        cb1=(CheckBox)findViewById(R.id.cb1);
        cb2=(CheckBox)findViewById(R.id.cb2);
        rg=(RadioGroup)findViewById(R.id.rg);
        rb1=(RadioButton) findViewById(R.id.rb1);
        rb2=(RadioButton)findViewById(R.id.rb2);
        switch1=(Switch)findViewById(R.id.switch1);
        togglebutton=(ToggleButton)findViewById(R.id.toggleButton);
        editText=(EditText)findViewById(R.id.editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str=editText.getText().toString();
                Toast.makeText(MainActivity.this, i+' '+i1+" "+i2+" "+str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(MainActivity.this, "결과"+b, Toast.LENGTH_SHORT).show();
                if(b==true){
                    rb1.setVisibility(View.VISIBLE);
                    rb2.setVisibility(View.VISIBLE);
                }else{
                    rb1.setVisibility(View.INVISIBLE);
                    rb2.setVisibility(View.INVISIBLE);
                }
            }
        });

        togglebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    rg.setVisibility(View.VISIBLE);
                }else{
                    rg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public void clickBt(View v){
        String str="";
        if(cb1.isChecked()){
            str+="아이스크림";
        }
        if(cb2.isChecked()){
            str+="케이크";
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void clickBt2(View v){
        String str="";
        if(rb1.isChecked()){
            str+="아이폰";
        }else if(rb2.isChecked()){
            str+="안드로이드";
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
