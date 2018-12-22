package com.example.dmytro.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    TextView TV_ID;
    TextView TV_T1;
    TextView TV_T2;
    TextView TV_HMDT;
    TextView TV_LMNST;
    TextView TV_PRSSR;
    TextView TV_ALTTD;
    TextView TV_DATE;
    TextView TV_status;
    TextView TV_DataUpdPer;
    ImageView IV_status;
    Button dataCaptureButton;
    //menu buttons
    MenuItem settingButton;

    Details items=new Details();

    Thread receiveDataThread;
    Thread CheckFilesThread;
    UserSetting settings= new UserSetting();
    User user=new User();
    int temp=0;
    int ReceiveDataDelay=1000;
    boolean _StartStopCapturing=true;
    Call<Details> jsonCall;
    boolean isThreadAlive=false;

Controller controller=new Controller();
      File fileLastRecord ;
      File fileUserCredentials;
    File fileUserSettings ;
      File fileHistory ;
      File fileArchive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        stopService(new Intent(this, AlertControllerService.class));
        fileLastRecord = new File(getApplicationContext().getCacheDir(),"LastRecord.hi");
        fileUserSettings = new File(getApplicationContext().getCacheDir(),"HomeInfSttngs.his");
        fileUserCredentials = new File(getApplicationContext().getCacheDir(),"User.hi");
        fileHistory = new File(getApplicationContext().getFilesDir(),"History.his");
        fileArchive = new File(getApplicationContext().getFilesDir(),"Archive.his");
        CheckFilesThread = new Thread() {
            @Override
            public void run() {
                try {
                    if(!fileLastRecord.exists())    fileLastRecord.createNewFile();//will do nothing if file exist
                    if(!fileUserSettings.exists())  fileUserSettings.createNewFile();
                    if(!fileUserCredentials.exists())  fileUserCredentials.createNewFile();
                    if(!fileHistory.exists())       fileHistory.createNewFile();
                    if(!fileArchive.exists())       fileArchive.createNewFile();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Login. Some troubles while check files"+e.getMessage());
                }
            }
        };
        CheckFilesThread.start();

        if(fileLastRecord.length()!=0) items=controller.loadLastLoadedRecord(getApplicationContext());
        if(fileUserSettings.length()!=0) settings=controller.loadSettings(getApplicationContext());
        if(fileUserCredentials.length()!=0) user=controller.loadUserCredentials(getApplicationContext());

        System.out.println("Main TOKEN="+user.getAccessToken());
        controller.setLanguage(settings.getUserLanguage(),getResources());
        setContentView(R.layout.activity_main);
    ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);


        dataCaptureButton = (Button) findViewById(R.id.button);
        dataCaptureButton.setOnClickListener(TestStart);

        TV_ID = (TextView) findViewById(R.id.valueID);
        TV_T1 = (TextView) findViewById(R.id.valueT1);
        TV_T2 = (TextView) findViewById(R.id.valueT2);
        TV_HMDT = (TextView) findViewById(R.id.valueHMDT);
        TV_LMNST = (TextView) findViewById(R.id.valueLMNST);
        TV_PRSSR = (TextView) findViewById(R.id.valuePRSSR);
        TV_ALTTD = (TextView) findViewById(R.id.valueALTTD);
        TV_DATE = (TextView) findViewById(R.id.valueDATE);
        TV_status = (TextView) findViewById(R.id.textViewStatus);
        TV_DataUpdPer = (TextView) findViewById(R.id.valueDataUpdPer);
        IV_status = (ImageView) findViewById(R.id.imageViewStatus);
        //menu buttons
        settingButton = (MenuItem) findViewById(R.id.setting_cart);

        TV_ID.setText(items.getDetailsId()+"");
        TV_T1.setText(items.getTemperature1()+"℃");
        TV_T2.setText(items.getTemperature2()+"℃");
        TV_HMDT.setText(items.getHumidity()+"%");
        TV_LMNST.setText(items.getLuminosity()+"lx");
        TV_PRSSR.setText(items.getPressure()+"hPa");
        TV_ALTTD.setText(items.getAltitude()+"m");
        TV_DATE.setText(items.getDatetime()+"");



       ReceiveDataDelay=settings.getTimeUpdate();
        TV_DataUpdPer.setText(TimeUnit.MILLISECONDS.toMinutes(settings.getTimeUpdate())+getApplicationContext().getString(R.string.MainMinutes));
       Log.d("Loaded Settings", "LNG POS=" + settings.getSttngLngPos());
      Log.d("Loaded Settings", "DEL POS=" + settings.getSttngClnPerdPos());
       Log.d("Loaded Settings", "UPD POS=" + settings.getSttngTimeUpdPos());
        System.out.println("Main TOKEN1="+user.getAccessToken());
        if(controller.isInternetAvailable(getApplicationContext()))
        {
            IV_status.setImageResource(R.mipmap.ie_online);
            TV_status.setText( getApplicationContext().getString(R.string.MainTVConnection));
           // new Connection().execute();
        }
        else
        {
            IV_status.setImageResource(R.mipmap.ie_offline);
            TV_status.setText(getApplicationContext().getString(R.string.MainTVNOConnection));

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_cart:
                Intent setting = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(setting);
               // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case R.id.history_cart:
                Intent history = new Intent(getApplicationContext(),HistoryActivity.class);
                startActivity(history);
                //overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case R.id.statistic_cart:
                Intent statistics = new Intent(getApplicationContext(),StatisticActivity.class);
                startActivity(statistics);
               // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case R.id.about_cart:
                Intent about = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(about);
                // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case R.id.signout_cart:
                Intent signout = new Intent(getApplicationContext(),LoginActivity.class);
                signout.putExtra("showLoginActivity",false);
                signout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signout);
                // overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
       // receiveData();
    }


    public View.OnClickListener TestStart = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
       try {

            if (_StartStopCapturing) {
                if (controller.isInternetAvailable(getApplicationContext())) {
                    dataCaptureButton.setText(getApplicationContext().getString(R.string.MainBTNStopReceiveData));
                    IV_status.setImageResource(R.mipmap.ie_online);
                    TV_status.setText(getApplicationContext().getString(R.string.MainTVConnection));
                   receiveData();
                    Log.d("Thread Status", "Thread is alive ");
                    _StartStopCapturing = false;
                } else {
                    controller.showToastMessage(getApplicationContext(),getString(R.string.MainToastNoInternet));
                    IV_status.setImageResource(R.mipmap.ie_offline);
                    TV_status.setText(getApplicationContext().getString(R.string.MainTVNOConnection));
                }
            } else {
             if(isThreadAlive) receiveDataThread.interrupt();
                isThreadAlive=false;
                //receiveDataThread=null;
                Log.d("Thread Status", "Thread is dead ");
                _StartStopCapturing = true;
                dataCaptureButton.setText(getApplicationContext().getString(R.string.MainBTNReceiveData));
            }
        }
        catch(Exception w)
        {
            Log.d("Exc In Thread", "SOME ERROR="+w);
            w.printStackTrace();
        }

        }
    };





void receiveData()
{
    receiveDataThread = new Thread() {
        @Override
        public void run() {
            try {
                while (true) {
                    sleep(2000);//ПОТІМ ПОСТАВИТИ ТУТ ЗМІННУ ПАУЗИ ReceiveDataDelay
                    isThreadAlive=true;
                    jsonCall = ApiModule.getClient().getLastRecord(user.getTokenType()+" "+user.getAccessToken());
                    jsonCall.enqueue(new Callback<Details>() {


                        @Override
                        public void onResponse(Call<Details> call, Response<Details> response) {
                            try {
                                Log.i("RETROFIT", "FUCK " + response);
                                if(response.code()==200) {
                                    items = response.body();
                                    TV_ID.setText(items.getDetailsId() + "");
                                    TV_T1.setText(items.getTemperature1() + "℃");
                                    TV_T2.setText(items.getTemperature2() + "℃");
                                    TV_HMDT.setText(items.getHumidity() + "%");
                                    TV_LMNST.setText(items.getLuminosity() + "lx");
                                    TV_PRSSR.setText(items.getPressure() + "hPa");
                                    TV_ALTTD.setText(items.getAltitude() + "m");
                                    TV_DATE.setText(items.getDatetime());
                                    controller.saveLastLoadedRecord(getApplicationContext(), items);
                                    if (items.getTemperature1() != null && items.getTemperature2() != null) {
                                        if (items.getTemperature1() >= settings.getCriticalTemp())
                                            TV_T1.setTextColor(Color.RED);
                                        else
                                            TV_T1.setTextColor(Color.parseColor("#737373"));

                                        if (items.getTemperature2() >= settings.getCriticalTemp())
                                            TV_T2.setTextColor(Color.RED);
                                        else
                                            TV_T2.setTextColor(Color.parseColor("#737373"));
                                    } else {
                                        System.out.println("Values in main is null ");
                                    }
                                } else
                                {
                                    System.out.println("Code in main is not 200");
                                }
                                System.out.println("Main Received items ");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Main Error " + ex);

                            }
                        }

                        @Override
                        public void onFailure(Call<Details> call, Throwable t) {
                            //controller.showToastMessage(getApplicationContext(),"Something went wrong");
                            System.out.println("Main Error onFailure");
                        }
                    });
                    Runtime.getRuntime().gc();
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
                System.out.println("Main Error catch" + e.getMessage());
            }
        }
    };
    receiveDataThread.start();
}









    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("PROGRAM STATUS", "IM DESTROYED onDestroy" );
            //receiveDataThread.interrupt();
           // receiveDataThread=null;
        if(isThreadAlive) receiveDataThread.interrupt();
        Runtime.getRuntime().gc();
            startService(new Intent(this, AlertControllerService.class));

            Log.d("Service STATUS", "SERVISE CREATED" );

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PROGRAM STATUS", "IM DESTROYED onResume");
        stopService(new Intent(this, AlertControllerService.class));
    }


}









