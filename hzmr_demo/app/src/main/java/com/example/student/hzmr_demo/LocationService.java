package com.example.student.hzmr_demo;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;

public class LocationService extends Service{

   DataTask dataTask;
   private static Intent intent2;
   private static String id,code,ur;
    // flag for GPS status
    private static boolean isGPSEnabled = false;
    // flag for GPS status
   private static boolean canGetLocation = false;
    // The minimum distance to change Updates in meters

    // The minimum time between updates in milliseconds

    LocationListener lis;
    Location lastLocation;
    LocationManager manager;
    SharedPreferences login;
    NotificationManager notificationmanager;
    Context context=this;


    public LocationService() {
    }

    @Override
    public void onCreate() {
        Log.d("----------", "on create----------@@@");
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("----------", "on startcommand----------@@@");

        if(intent==null){
            return Service.START_STICKY;
        }else{
            login= PreferenceManager.getDefaultSharedPreferences(this);//누구인지 로그인 아이디
            id=login.getString("id","");
            //SharedPreferences.Editor editor=login.edit();
            //id=intent.getStringExtra("id");
            //몇시인지 구분하는 코드넘버
            code=Integer.toString(intent.getIntExtra("code",0));
            Log.d("------LocationService", id+" ");
            Log.d("------LocationService", code+" ");
            requestMyLocation(intent);
        }
        return Service.START_NOT_STICKY;
    }

    // 현재 나의 위도 경도 구하기
    public void requestMyLocation(Intent intent) {
        Log.d("----------", "requestmyLocation----------@@@");
        try {
            manager =
                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // getting network status
            //isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled) {
                // no network provider is enabled
                Log.d("----------", "no provider----------@@@");
            } else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                    try {
                        long minTime = 10000;
                        float minDistance = 0;
                        manager.requestLocationUpdates(//로케이션 매니저에 바뀔 때 마다 아래 수행되도록.
                                LocationManager.NETWORK_PROVIDER,
                                minTime,
                                minDistance,
                                lis=new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) { //위치바뀌면 바뀐위치객체 넘겨줌
                                        lastLocation=location;
                                    }

                                    @Override
                                    public void onStatusChanged(String provider, int status, Bundle extras) {

                                    }

                                    @Override
                                    public void onProviderEnabled(String provider) {

                                    }

                                    @Override
                                    public void onProviderDisabled(String provider) {

                                    }
                                }
                        );

                       lastLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (lastLocation != null) {
                            showCurrentLocation(lastLocation);
                            Log.d("--------------", "gottttttttttttlastlocation1");
                        }else{

                        }
                        //if (currentLocation == null) {//연습으로 구현할 때는 텀이 짧으므로 얘를 없애줘야 계속 받음


                        Log.d("GPS Enabled", "GPS Enabled");
                /*        if (manager != null) {
                            if (currentLocation != null) {
                                Log.d("--------------", "gottttttttttttlastlocation2");
                                showCurrentLocation(lastLocation);
                            }
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    //나의 위치가 멀캠 출석 인정 범위 내에 있는지 여부 구하기
    private void showCurrentLocation(Location location) {
        String att="";
        double lat=location.getLatitude();

        Log.d("--------------",Double.toString(lat));
        double lon=location.getLongitude();
        Log.d("--------------",Double.toString(lon));
        location.setLatitude(lat);
        location.setLongitude(lon);
//멀캠위치 셋팅
        Location targetLoc = new Location("point B");
        targetLoc.setLatitude(37.501388);
        targetLoc.setLongitude(127.039567);
//현재 위치와 멀캠 위치 간의 거리 구하기
        float distance;
        distance = location.distanceTo(targetLoc);
//거리가 100m이내이면 true,100m 이상이면 false
        if(distance>=100){
            att="0";
        }else{
            att="1";
        }
        intent2= new Intent(LocationService.this,Main2Activity.class);
        intent2.putExtra("lat",Double.toString(lat));
        intent2.putExtra("lon",Double.toString(lon));
        intent2.putExtra("dis",Float.toString(distance));
        intent2.putExtra("att",att);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);//p307
        startActivity(intent2);
        sendData(att,code);
    }

    private void sendData(String att,String code){
        //코드값을 받아서 9시,2시.6시 값 별로 부를 컨트롤러설정
        if(code.equals("9")){
           // ur="http://70.12.114.136/mv/insertData9.do?time_nine="
             ur="http://70.12.114.136/mv/insertData9.do?time_nine=";}
        else if(code.equals("14")){
            ur="http://70.12.114.136/mv/updateData2.do?time_two=";
        }else if(code.equals("18")){
        ur="http://70.12.114.136/mv/updateData6.do?time_six=";}
        dataTask=new DataTask(ur);//시간대 별로 URL다름
        dataTask.execute(att,id);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

class DataTask extends AsyncTask<String, String, String> {

        String url;

        public DataTask() {}
        public DataTask(String url) {
            this.url = url;
        }
        @Override
        protected void onPreExecute() {

        }
        @Override
        protected String doInBackground(String... strings) {

            String att= strings[0];
            String id= strings[1];

            //화면이 꺼져있어도 출석과 부재 알람으로 전달
            notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intentP=new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentP, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            if(att.equals("1")){
                builder.setSmallIcon(R.drawable.gps).setTicker("HETT").setWhen(System.currentTimeMillis())
                        .setNumber(1).setContentTitle("GPS 확인").setContentText("출석이 확인되었습니다:)")
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
            }else if(att.equals("0")){
                builder.setSmallIcon(R.drawable.gps).setTicker("HETT").setWhen(System.currentTimeMillis())
                        .setNumber(1).setContentTitle("GPS 확인").setContentText("부재가 확인되었습니다.")
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
            }
            notificationmanager.notify(1, builder.build());


            //HTTPrequest 서버로 데이터 전달
            url += att+"&id="+id;
            StringBuilder sb = new StringBuilder();
            URL url = null;
            HttpURLConnection con = null;
            BufferedReader reader=null;
            try {
                url = new URL(this.url);
                con = (HttpURLConnection) url.openConnection();
                if (con != null) {
                    con.setConnectTimeout(5000);
                    //con.setReadTimeout(10000);
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Accept", "*/*");
                    if (con.getResponseCode() != HttpURLConnection.HTTP_OK){
                        return null;
                    }
                    reader =new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line="";
                    while(true){
                        line=reader.readLine();
                        if(line==null){
                            break;
                        }
                        sb.append(line);
                    }
                }
            } catch (Exception e) {
                return e.getMessage();
            } finally {
                con.disconnect();
                try {
                    if(reader!=null){
                        reader.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    @Override
    public void onDestroy() {
        Log.d("-----------","service destroyed");
        super.onDestroy();
    }
}

