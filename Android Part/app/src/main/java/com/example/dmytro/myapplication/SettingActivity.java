package com.example.dmytro.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    Spinner dataUpdateSpinner;
    Spinner dataDeleteSpinner;
    Spinner dataLanguage;
    TextView dataArchiveProposition;
    TextView userFreeSpace;
    TextView criticalTempTV;
    Button clearData;
    Button resetAll;
    Button changePasswordBTN;
    SeekBar seekBar;
    ToggleButton dataTypeToggleBtn;

    UserSetting settings=new UserSetting();
    User user=new User();
    Controller controller=new Controller();
    DBSize dbsize=new DBSize();
    int cleanPeriod=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTitle(R.string.SttgActivName);
        user=controller.loadUserCredentials(getApplicationContext());
        settings = controller.loadSettings(getApplicationContext());
        controller.setLanguage(settings.getUserLanguage(), getResources());

        Call<DBSize> jsonCall = ApiModule.getClient().getDBSize(user.getTokenType()+" "+user.getAccessToken());
        jsonCall.enqueue(new Callback<DBSize>() {


                             @Override
                             public void onResponse(Call<DBSize> call, Response<DBSize> response) {
                                 if (response.code() == 200) {
                                     dbsize = response.body();
                                     controller.saveSettings(getApplicationContext(), settings);
                                     if (dbsize.getDbsize() != null) {
                                         settings.setFreeSpace(dbsize.getDbsize());
                                         Log.d("LOGGGS", "Retrofit FreeSpace " + dbsize.getDbsize());
                                     } else {
                                         settings.setFreeSpace(0.0);
                                     }
                                 } else {
                                     controller.showToastMessage(getApplicationContext(), "Мережева помилка! Перевірте з'єднання з інтернетом");
                                 }
                             }
                             @Override
                             public void onFailure(Call<DBSize> call, Throwable t) {

                             }
                         });

        clearData = (Button) findViewById(R.id.buttonClearDB);
        clearData.setOnClickListener(clearDB);
        resetAll = (Button) findViewById(R.id.resetAllBtn);
        resetAll.setOnClickListener(ResetAll);
        changePasswordBTN = (Button) findViewById(R.id.changePasswordBtn);
        changePasswordBTN.setOnClickListener(changePassword);

        dataUpdateSpinner = (Spinner) findViewById(R.id.spinnerDataUpdate);
        dataDeleteSpinner = (Spinner) findViewById(R.id.spinnerDataDelete);
        dataLanguage = (Spinner) findViewById(R.id.spinnerLanguageChoose);
        dataArchiveProposition = (TextView) findViewById(R.id.textViewArchivePropos);
        dataArchiveProposition.setOnClickListener(archiveDB);
        userFreeSpace = (TextView) findViewById(R.id.valueFreeSpace);
        criticalTempTV = (TextView) findViewById(R.id.TV_CriticTemp);
        criticalTempTV.setOnClickListener(editTextClickListener);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        dataTypeToggleBtn = (ToggleButton) findViewById(R.id.dataTypeToggleButton);

        ArrayAdapter<?> updateDataList = ArrayAdapter.createFromResource(this, R.array.updateTime, android.R.layout.simple_spinner_item);
        updateDataList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<?> deleteDataList = ArrayAdapter.createFromResource(this, R.array.cleaningPeriod, android.R.layout.simple_spinner_item);
        deleteDataList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<?> chooseLanguageList = ArrayAdapter.createFromResource(this, R.array.languageList, android.R.layout.simple_spinner_item);
        chooseLanguageList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataUpdateSpinner.setAdapter(updateDataList);
        dataDeleteSpinner.setAdapter(deleteDataList);
        dataLanguage.setAdapter(chooseLanguageList);
        //dataArchiveProposition.setText(R.string.SttgTVdataArchivePropos +"1"+R.string.Sttg1Day);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataTypeToggleBtn.setChecked(settings.getDataType());

    userFreeSpace.setText(String.format("%.1f", settings.getFreeSpace()) + "/32mb");
        dataUpdateSpinner.setSelection(settings.getSttngTimeUpdPos());
        dataDeleteSpinner.setSelection(settings.getSttngClnPerdPos());
        dataLanguage.setSelection(settings.getSttngLngPos());


        criticalTempTV.setText(settings.getCriticalTemp()+"℃");
        seekBar.setProgress(settings.getCriticalTemp() - 25);


        dataUpdateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int timer = 0;
                switch (position) {
                    case 0:
                        timer = 60000;
                        break;
                    case 1:
                        timer = 120000;
                        break;
                    case 2:
                        timer = 300000;
                        break;
                    case 3:
                        timer = 600000;
                        break;
                    case 4:
                        timer = 1800000;
                        break;
                    case 5:
                        timer = 3600000;
                        break;
                }
                settings.setTimeUpdate(timer);
                settings.setSttngTimeUpdPos(position);
                controller.saveSettings(getApplicationContext(), settings);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        dataDeleteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String[] array = getResources().getStringArray(R.array.cleaningPeriod);
                dataArchiveProposition.setText(getApplicationContext().getString(R.string.SttgTVDataArchivePropos) + array[position]);
                switch (position) {
                    case 0:
                        cleanPeriod = 1;
                        break;
                    case 1:
                        cleanPeriod = 7;
                        break;
                    case 2:
                        cleanPeriod = 10;
                        break;
                    case 3:
                        cleanPeriod = 15;
                        break;
                    case 4:
                        cleanPeriod = 31;
                        break;
                    case 5:
                        cleanPeriod = 90;
                        break;
                    case 6:
                        cleanPeriod = 365;
                        break;
                }
                settings.setCleanPeriod(cleanPeriod);
                settings.setSttngClnPerdPos(position);
                controller.saveSettings(getApplicationContext(), settings);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        dataLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String languagePos = "";
                switch (position) {
                    case 0:
                        languagePos = "en";
                        break;
                    case 1:
                        languagePos = "uk";
                        break;
                    case 2:
                        languagePos = "ru";
                        break;
                    case 3:
                        languagePos = "de";
                        break;
                    case 4:
                        languagePos = "pl";
                        break;
                    case 5:
                        languagePos = "cs";
                        break;

                }
                settings.setUserLanguage(languagePos);
                settings.setSttngLngPos(position);
                controller.saveSettings(getApplicationContext(), settings);
                controller.setLanguage(languagePos, getResources());
                // showToastMessage(getApplicationContext().getString(R.string.SttgRebootRecomendation));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress + 25;
               criticalTempTV.setText(progressChanged + "℃");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                settings.setCriticalTemp(progressChanged);
                controller.saveSettings(getApplicationContext(), settings);
                if(controller.isInternetAvailable(getApplicationContext()))
                {
                    Log.d("LOGGGS", "TEMP_UPDATE READY ");
                    //new UpdateCriticalTemp().execute();
                    Call<ResponseBody> callUpdateTemp = ApiModule.getClient().updateCriticalTemp("criticalTemperature",settings.getCriticalTemp(), user.getTokenType() + " " + user.getAccessToken());
                    callUpdateTemp.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ResponseBody answer = response.body();
                            Log.d("LOGGGS", "TEMP_UPDATE ЗАПИТ ВІДПРАВЛЕНО " + answer.toString());
                            if(response.code()==200)  Log.d("LOGGGS", "TEMP_UPDATE ЗАПИТ Підтверджено ");
                            else Log.d("LOGGGS", "TEMP_UPDATE ЗАПИТ Провалено ");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("LOGGGS", "TEMP_UPDATE ЗАПИТ Провалено "+ t.getMessage());
                        }
                    });
                }
            }
        });
        dataTypeToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    settings.setDataType(true);
                    controller.saveSettings(getApplicationContext(),settings);
                }
                else
                {
                    settings.setDataType(false);
                    controller.saveSettings(getApplicationContext(),settings);

                }

            }
        });



    }





    View.OnClickListener editTextClickListener = new View.OnClickListener(){
        public void onClick(View v) {
            criticalTempTV.setText("");
            if (v.getId() == criticalTempTV.getId())
            {
                criticalTempTV.setCursorVisible(true);

            }

        }
    };
    public View.OnClickListener changePassword = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent changePass = new Intent(getApplicationContext(),ChangePasswordActivity.class);
                startActivity(changePass);
            }
            catch(Exception w)
            {
                Log.d("Change Pass", "Error");
                w.printStackTrace();
            }
        }
    };

    public View.OnClickListener ResetAll = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                ResetAll();
            }
            catch(Exception w)
            {
                Log.d("Reset Sttng", "Reset");
                w.printStackTrace();
            }
        }
    };
    public View.OnClickListener clearDB = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
               DeleteData();
            }
            catch(Exception w)
            {
                Log.d("Exc In Thread", "SOME ERROR="+w);
                w.printStackTrace();
            }
        }
    };
    public View.OnClickListener archiveDB = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                ArchiveData();
            }
            catch(Exception w)
            {
                Log.d("Exc In Thread", "SOME ERROR="+w);
                w.printStackTrace();
            }
        }
    };

    void ResetAll()
    {

        final AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AppTheme_NoActionBar));
        alert.setTitle(getApplicationContext().getString(R.string.SttgResetAllMsgTitle)); //Set Alert dialog title here
        alert.setMessage(R.string.SttgResetAllAlrtMsg);
        alert.setPositiveButton(R.string.DialogOkBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                settings.setTimeUpdate(60000);
                settings.setSttngTimeUpdPos(0);
                settings.setSttngClnPerdPos(1);
                settings.setSttngClnPerdPos(0);
                settings.setUserLanguage("uk");
                settings.setSttngLngPos(1);
                settings.setCriticalTemp(60);
                controller.saveSettings(getApplicationContext(),settings);
            }
        });

        alert.setNegativeButton(R.string.DialogCancelBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }
    void DeleteData()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AppTheme_NoActionBar));
        alert.setTitle(getApplicationContext().getString(R.string.SttgDltDataMsgTitle)+cleanPeriod+getApplicationContext().getString(R.string.SttgDltDataMsgTitlePeriod)); //Set Alert dialog title here
        alert.setMessage(R.string.SttgDltDataAlrtMsg);
        alert.setPositiveButton(R.string.DialogOkBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //new DeleteRequestTask().execute();
                Log.d("LOGGGS", "DELETE READY ");
                Call<ResponseBody> callDeleteData = ApiModule.getClient().deleteData(cleanPeriod,user.getTokenType() + " " + user.getAccessToken());
                callDeleteData.enqueue(new Callback<ResponseBody>() {


                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody answer = response.body();
                        Log.d("LOGGGS", "DELETE ЗАПИТ ВІДПРАВЛЕНО " + answer.toString());
                        if(response.code()==200)  Log.d("LOGGGS", "DELETE ЗАПИТ Підтверджено ");
                        else Log.d("LOGGGS", "DELETE ЗАПИТ Провалено ");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("LOGGGS", "DELETE ЗАПИТ Провалено "+ t.getMessage());
                    }
                });
            }});
                alert.setNegativeButton(R.string.DialogCancelBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                alert.show();

        }
    void ArchiveData()
    {

        final AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AppTheme_NoActionBar));
        alert.setTitle(getApplicationContext().getString(R.string.SttgArchDataMsgTitle)+cleanPeriod+getApplicationContext().getString(R.string.SttgArchDataMsgTitlePeriod)); //Set Alert dialog title here
        alert.setMessage(R.string.SttgArchDataAlrtMsg);
        alert.setPositiveButton(R.string.DialogOkBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               // new ArchiveRequestTask().execute();
                Log.d("LOGGGS", "ARCHIVE READY ");
                Call<ResponseBody> callArchiveData = ApiModule.getClient().archiveData(cleanPeriod, user.getTokenType() + " " + user.getAccessToken());
                callArchiveData.enqueue(new Callback<ResponseBody>() {


                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody answer = response.body();
                        Log.d("LOGGGS", "ARCHIVE ЗАПИТ ВІДПРАВЛЕНО " + answer.toString());
                        if(response.code()==200)  Log.d("LOGGGS", "ARCHIVE ЗАПИТ Підтверджено ");
                        else Log.d("LOGGGS", "ARCHIVE ЗАПИТ Провалено ");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("LOGGGS", "ARCHIVE ЗАПИТ Провалено "+ t.getMessage());
                    }
                });
            }
        });

        alert.setNegativeButton(R.string.DialogCancelBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

 }

