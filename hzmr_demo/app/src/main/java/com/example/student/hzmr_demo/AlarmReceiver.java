package com.example.student.hzmr_demo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    String id;
    int code;


    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = Calendar.getInstance();
        int curTime = calendar.get(Calendar.SECOND);
        //id=login.getString("id","");
        code=intent.getIntExtra("code",0);
        //Log.d("-------------onReceive", code+"");
        //Log.d("-------------onReceive", id);


        //로케이션 서비스로 이동하는 인텐트
        Intent locService=new Intent(context,LocationService.class);
        //locService.putExtra("id",id);
        locService.putExtra("code",code);
        context.startService(locService);

        //리시버가 알람받을 때 울리는 토스트
        //Toast.makeText(context, "알람!", Toast.LENGTH_SHORT).show();
        Log.d("알람","로그"+curTime+"초");
        Intent intentR= new Intent(context,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context,code,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP,1000 * 60 * 60 * 24 * 7,pendingIntent);
        //알람울리고 3분후에 다시 울리게 세팅해놈
        alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis()+1000*60*3,pendingIntent);
    }
}