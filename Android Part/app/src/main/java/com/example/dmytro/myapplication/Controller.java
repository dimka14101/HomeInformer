package com.example.dmytro.myapplication;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import history_tab_classes.ArchiveObject;
import history_tab_classes.HistoryObject;

/**
 * Created by Dmytro on 22.01.2017.
 */

public class Controller {



    void saveUserCredentials(Context context, User user)
    {
        try {
            File saveUserCredentials = new File(context.getCacheDir(),"User.hi");
            FileOutputStream fos = new FileOutputStream(saveUserCredentials);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User loadUserCredentials(Context context) {
       User userCredentials= new User();
        try {
            File fileLoadUser = new File(context.getCacheDir().getAbsolutePath() + "/User.hi");
            FileInputStream fis = new FileInputStream(fileLoadUser);
            ObjectInputStream is = new ObjectInputStream(fis);
            Boolean complete = false;
            while(complete != true) {
                userCredentials = (User) is.readObject();
                if(is.readObject() == null)
                    complete = true;
            }
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userCredentials;
    }


    void saveLastLoadedRecord(Context context, Details items)
    {
        try {
            File fileSaveLastRecord = new File(context.getCacheDir(),"LastRecord.hi");
            FileOutputStream fos = new FileOutputStream(fileSaveLastRecord);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(items);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Details loadLastLoadedRecord(Context context) {
        Details items = new Details();
        try {
            File fileLoadLastRec = new File(context.getCacheDir().getAbsolutePath() + "/LastRecord.hi");
            FileInputStream fis = new FileInputStream(fileLoadLastRec);
            ObjectInputStream is = new ObjectInputStream(fis);
            Boolean complete = false;
            while(complete != true) {
               items = (Details) is.readObject();
                if(is.readObject() == null)
                    complete = true;
            }
                is.close();
                fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }


   public UserSetting loadSettings(Context context) {
        UserSetting settings=new UserSetting();
        try {
            File fileLoadSettings = new File(context.getCacheDir().getAbsolutePath() + "/HomeInfSttngs.his");
            FileInputStream fis = new FileInputStream(fileLoadSettings);
            ObjectInputStream is = new ObjectInputStream(fis);
            Boolean complete = false;
            while(complete != true) {
                settings = (UserSetting) is.readObject();
                if(is.readObject() == null)
                    complete = true;
            }
                is.close();
                fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return settings;
    }

    void saveSettings(Context context,UserSetting settings){
        try {
            File fileSaveSettings = new File(context.getCacheDir(),"HomeInfSttngs.his");
            FileOutputStream fos = new FileOutputStream(fileSaveSettings);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(settings);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

protected void setLanguage(String language,Resources res){
        Locale mylocale=new Locale(language);
        Resources resources=res;
        DisplayMetrics dm=resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        conf.locale=mylocale;
        resources.updateConfiguration(conf,dm);
    }

    public void saveHistory(Context context, ArrayList<HistoryObject> list)
    {
        try {

            FileOutputStream fos = context.openFileOutput("History.his", context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(list);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HistoryObject> loadHistory(Context context) {
        ArrayList<HistoryObject> list = new ArrayList<HistoryObject>();
        try {
            FileInputStream fis = context.openFileInput("History.his");
            ObjectInputStream is = new ObjectInputStream(fis);
            list  = (ArrayList<HistoryObject>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

   public void saveArchive(Context context, ArrayList<ArchiveObject> list)
    {
        try {

            FileOutputStream fos = context.openFileOutput("Archive.his", context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(list);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<ArchiveObject>  loadArchive(Context context) {
        ArrayList<ArchiveObject> list=new ArrayList<ArchiveObject>();
        try {
            FileInputStream fis = context.openFileInput("Archive.his");
            ObjectInputStream is = new ObjectInputStream(fis);

            list  = (ArrayList<ArchiveObject>) is.readObject();
            is.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

public boolean checkWifiConnection(Context context){
    WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    if(wifi.isWifiEnabled() && isInternetAvailable(context))
return true;
    else return false;
}

    public boolean isInternetAvailable(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void showToastMessage(Context context,String msg)
    {
        Context cntxt = context;
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(cntxt, text, duration);
        toast.show();
    }




}
