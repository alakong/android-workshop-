package com.example.student.workshop3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService1 extends Service {

 private static String TAG="----My Service1----";

    public MyService1() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"----Service onStart----");
        if(intent==null){
            return Service.START_STICKY;
        }else{
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void processCommand(Intent intent) {

        Log.d(TAG,"Service processCommand-----");
        final Intent sintent=new Intent(getApplicationContext(),MainActivity.class);
        sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);//p307

        Runnable run= new Runnable() {

            @Override
            public void run() {
                for(int i=0;i<6;i++) {
                    Log.d(TAG, "Process:" + i);
                    //sintent.putExtra("cnt",i);
                    if (i == 5) {
                        Log.d(TAG, "service1 complete");
                        sintent.putExtra("act", 1);
                        startActivity(sintent);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(run).start();}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"----Service onDestroy----");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
