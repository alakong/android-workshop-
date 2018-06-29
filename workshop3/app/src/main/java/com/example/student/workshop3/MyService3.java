package com.example.student.workshop3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService3 extends Service {
    private static String TAG= "----My Service3----";
    public MyService3() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            return Service.START_STICKY;
        }else{
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void processCommand(Intent intent) {

        final Intent sintent=new Intent(getApplicationContext(),MainActivity.class);
        sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);//p307
        sintent.putExtra("act", 2);
        Runnable run= new Runnable() {

            @Override
            public void run() {
                for(int i=0;i<11;i++) {
                    Log.d(TAG, "Process:" + i);
                    sintent.putExtra("cnt", i);
                    startActivity(sintent);

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
