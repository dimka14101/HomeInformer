package com.example.dmytro.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dmytro on 22.01.2017.
 */

public class AlertControllerService extends Service {
    Thread receiveDataThread;
    Controller controller=new Controller();
    Details items=new Details();
    UserSetting settings=new UserSetting();
    Call<Details> jsonCall;
    User user=new User();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        receiveDataThread.interrupt();
        Runtime.getRuntime().gc();
       // stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        user=controller.loadUserCredentials(getApplicationContext());
        settings=controller.loadSettings(getApplicationContext());
        Log.d("SERVICE TASK", "I`m here1" );
        Log.d("SERVICE TASK", "Token="+user.getAccessToken() );
        receiveDataThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sleep(10000);//ПОТІМ ПОСТАВИТИ ТУТ ЗМІННУ ПАУЗИ ReceiveDataDelay
                        if(controller.isInternetAvailable(getApplicationContext())) {
                            jsonCall = ApiModule.getClient().getLastRecord(user.getTokenType() + " " + user.getAccessToken());
                            jsonCall.enqueue(new Callback<Details>() {
                                @Override
                                public void onResponse(Call<Details> call, Response<Details> response) {
                                    Log.i("RETROFIT", "Back Service " + response);
                                    if (response.code() == 200) {
                                        items = response.body();
                                        controller.saveLastLoadedRecord(getApplicationContext(), items);
                                        if (items.getTemperature1() != null && items.getTemperature2() != null && settings.getCriticalTemp() != null) {
                                            if (items.getTemperature1() >= settings.getCriticalTemp()
                                                    || items.getTemperature2() >= settings.getCriticalTemp()) {
                                                sendNotification();
                                                Log.i("RETROFIT", "Response servise. MUST BE NOTIFICATION " + response);
                                            }
                                            else {
                                                Log.i("RETROFIT", "Back Service T1<Crit or T2<Crit T1=" + items.getTemperature1()+" T2="+items.getTemperature2()+" CRIT="+settings.getCriticalTemp());
                                            }
                                        } else {
                                            System.out.println("Values in service is null ="+items.getTemperature1()+" ="+items.getTemperature2()+" ="+settings.getCriticalTemp());
                                        }
                                    } else {
                                        System.out.println("Code in service is not 200");
                                    }

                                }

                                @Override
                                public void onFailure(Call<Details> call, Throwable t) {
                                    //controller.showToastMessage(getApplicationContext(),"Something went wrong");
                                    Log.i("RETROFIT", "Service on Failure");
                                }
                            });
                        }

                        Runtime.getRuntime().gc();
                    }
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Log.i("RETROFIT", "Service catch " + e.getMessage());

                }
            }
        };
        receiveDataThread.start();
        return super.onStartCommand(intent, flags, startId);
    }


    public void sendNotification() {
        controller.setLanguage(settings.getUserLanguage(),getResources());
        // Use NotificationCompat.Builder to set up our notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //icon appears in device notification bar and right hand corner of notification
        builder.setSmallIcon(R.mipmap.ic_launcher);

        // This intent is fired when notification is clicked
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Large icon appears on the left of the notification
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.fire_icon));

        // Content title, which appears in large type at the top of the notification
        builder.setContentTitle("Home Informer: "+getApplicationContext().getString(R.string.ServiceAlert));

        // Content text, which appears in smaller text below the title
        builder.setContentText(getApplicationContext().getString(R.string.ServiceContentText)+settings.getCriticalTemp()+" ℃");

        // The subtext, which appears under the text on newer devices.
        // This will show-up in the devices with Android 4.2 and above only
        builder.setSubText(getApplicationContext().getString(R.string.ServiceSubText));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm);
        builder.setSound(alarmSound);
        //Vibration
        builder.setVibrate(new long[] { 1000, 1000 });
        //LED
        builder.setLights(Color.BLUE, 3000, 3000);
        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());

    }

}
