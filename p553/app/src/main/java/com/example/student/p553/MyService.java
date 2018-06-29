package com.example.student.p553;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    public static boolean isRun=true;
    public MyService() {
    }


    @Override
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

        Runnable run= new Runnable() {

            @Override
            public void run() {


                    for (int i = 1; i <= 7&isRun; i++) {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //sintent.putExtra("cnt",i);
                        sintent.putExtra("act", i);
                        startActivity(sintent);
                    }

            }
        };
        new Thread(run).start();}

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun=false;


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
