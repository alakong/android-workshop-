package com.example.student.hzmr_demo;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {
    private static  TextView textView, textView2,logininfo;
    private static String lat,lon,distance,att;
    public static boolean flag = true;
    public static AlertDialog.Builder alBuilder;
    private static AlertDialog dialog;
    WebView selfView,mngView;
    LinearLayout proto,calendarView;
    Calendar calendar;
    Intent intent,intentS,intentGet,intentCheck;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    private static String id,name;

    //달력
    GridView monthView;
    MonthAdapter monthViewAdapter;
    TextView monthText;
    TextView info;
    int curYear;
    int curMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // 권한 물어서 권한안되어있으면 권한 셋팅해주기
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionCheck1 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);

        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck2 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck3 == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        makeUi();
        calendarSet();
    }

    public void makeUi(){
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        selfView =findViewById(R.id.selfView);
        mngView=findViewById(R.id.mngView);
        calendarView=findViewById(R.id.calendarView);
        proto=findViewById(R.id.proto);
        alBuilder = new AlertDialog.Builder(Main2Activity.this);
        logininfo=findViewById(R.id.logininfo);
        intentS = new Intent(Main2Activity.this, LocationService.class);
        intent = new Intent(this,AlarmReceiver.class);

        intentGet=getIntent();
        id=intentGet.getStringExtra("id");
        name=intentGet.getStringExtra("name");
        //Log.d("----------------------",id);//앱 종료하면 아이디값이 보존이 안돼서 에러남

        selfView.setWebViewClient(new WebViewClient());
        selfView.getSettings().setJavaScriptEnabled(true);
        mngView.setWebViewClient(new WebViewClient());
        mngView.getSettings().setJavaScriptEnabled(true);

       intentCheck = new Intent(this, AlarmReceiver.class);
       PendingIntent sender;

       //이미 알람셋팅이 되어있는지 확인->하나라도 안돼있으면 메소드호출
      if(PendingIntent.getBroadcast(this, 9, intent, PendingIntent.FLAG_NO_CREATE)==null||
          PendingIntent.getBroadcast(this, 14, intent, PendingIntent.FLAG_NO_CREATE)==null||
          PendingIntent.getBroadcast(this, 18, intent, PendingIntent.FLAG_NO_CREATE)==null){

          setTimer();
       }



        hide();
        //위도경도 들어오는지 확인하는 액티비티 켜짐
        proto.setVisibility(View.VISIBLE);
        logininfo.setText("데이터를 알아보는 테스트 페이지입니다.");
    }

    public void calendarSet(){
        //달력세팅
        monthView = (GridView) findViewById(R.id.monthView);
        monthViewAdapter = new MonthAdapter(this,id);
        monthView.setAdapter(monthViewAdapter);
        info = findViewById(R.id.info);
        monthText = (TextView) findViewById(R.id.monthText);

        // 리스너 설정
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 현재 선택한 일자 정보 표시
                MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                int day = curItem.getDay();
                String state = curItem.getState();

                Log.d("MainActivity", "Selected : " + day);
                //Toast.makeText(Main2Activity.this, day+"일: "+state, Toast.LENGTH_SHORT).show();
                if(state==null){
                info.setText((monthViewAdapter.getCurMonth()+1)+"월 "+day+"일");
                }else{
                info.setText((monthViewAdapter.getCurMonth()+1)+"월 "+day+"일"+" "+state);}
            }
        });

        setMonthText();
        // 이전 월로 넘어가는 이벤트 처리
        Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                setMonthText();
            }
        });
        // 다음 월로 넘어가는 이벤트 처리
        Button monthNext = (Button) findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                setMonthText();
            }
        });
        /**
         * 월 표시 텍스트 설정
         */
    }

    private void setMonthText(){
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();
        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }

    //프레임 레이아웃 가리기
    public void hide(){
        proto.setVisibility(View.INVISIBLE);
        selfView.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        mngView.setVisibility(View.INVISIBLE);
    }

    public void clickBt(View v){
        if(v.getId()==R.id.btself){
            hide();
            logininfo.setText(name+"님의 출석 조회 페이지입니다.");
            selfView.loadUrl("http://70.12.114.136/mv/webview.do?id="+id);
            selfView.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btcal){
            hide();
            logininfo.setText(name+"님의 달력 페이지입니다.");
            calendarView.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.btmng){
            hide();
            logininfo.setText(name+"님의 통합 조회 페이지입니다.");
            mngView.loadUrl("http://70.12.114.136/mv/view/android2.jsp");
            mngView.setVisibility(View.VISIBLE);
        }
    }



//경도 위도값 확인하는 화면용
    @Override
    protected void onNewIntent(Intent intent) {
        lat = intent.getStringExtra("lat");
        lon = intent.getStringExtra("lon");
        distance = intent.getStringExtra("dis");
        att = intent.getStringExtra("att");

        textView.setText(lat + "  " + lon);
        textView2.setText(att + "   " + distance);
    }



//알람 셋하는 메서드
    public void setTimer(){

       // int diff = Calendar.getInstance().getTimeInMilis() - targetCal.getTimeInMillis();
        //If diff is greater than 0, then add a day to your calendar (targetCal)


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
        long diff;
        int time;
        time=31;


        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,time);
        calendar.set(Calendar.SECOND,0);
         diff=Calendar.getInstance().getTimeInMillis()-calendar.getTimeInMillis();
        if(diff>0){
            calendar.add(Calendar.DAY_OF_WEEK,1);
        }
        intent.putExtra("id",id);
        intent.putExtra("code",9);
        pendingIntent = PendingIntent.getBroadcast(this,9,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);


        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,time+1);
        calendar.set(Calendar.SECOND,0);
        diff=Calendar.getInstance().getTimeInMillis()-calendar.getTimeInMillis();
        if(diff>0){
            calendar.add(Calendar.DAY_OF_WEEK,1);
        }
        intent.putExtra("id",id);
        intent.putExtra("code",14);
        pendingIntent = PendingIntent.getBroadcast(this,14,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);


        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,13);
        calendar.set(Calendar.MINUTE,time+2);
        calendar.set(Calendar.SECOND,0);
        diff=Calendar.getInstance().getTimeInMillis()-calendar.getTimeInMillis();
        if(diff>0){
            calendar.add(Calendar.DAY_OF_WEEK,1);
        }
        intent.putExtra("id",id);
        intent.putExtra("code",18);
        pendingIntent = PendingIntent.getBroadcast(this,18,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);

    }

}