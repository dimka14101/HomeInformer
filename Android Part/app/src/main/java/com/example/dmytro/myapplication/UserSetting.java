package com.example.dmytro.myapplication;

import java.io.Serializable;

/**
 * Created by Dmytro on 05.01.2017.
 */

public class UserSetting implements Serializable {
    private int timeUpdate=60000;
    private int settingTimePosition=0;

    private int CleanPeriod=1;
    private int  settingCleanPeriodPosition=0;

    private double freespace=5.0;
    private boolean dataType=false;

    private String UserLanguage="uk";
    private int settingUserLngPosition=1;

    private Integer CriticalTemp=60;

    public Integer getCriticalTemp() { return this.CriticalTemp; }
    public void setCriticalTemp(int CriticalTemp) { this.CriticalTemp = CriticalTemp; }

    public int getTimeUpdate() { return this.timeUpdate; }
    public void setTimeUpdate(int timeUpdate) { this.timeUpdate = timeUpdate; }

    public int getSttngTimeUpdPos() { return this.settingTimePosition; }
    public void setSttngTimeUpdPos(int settingTimePosition) { this.settingTimePosition = settingTimePosition; }


    public int getCleanPeriod() { return this.CleanPeriod; }
    public void setCleanPeriod(int CleanPeriod) { this.CleanPeriod = CleanPeriod; }

    public int getSttngClnPerdPos() { return this.settingCleanPeriodPosition; }
    public void setSttngClnPerdPos(int settingCleanPeriodPosition) { this.settingCleanPeriodPosition = settingCleanPeriodPosition; }

    public double getFreeSpace() { return this.freespace; }
    public void setFreeSpace(double freespace) { this.freespace = freespace; }

    public boolean getDataType() { return this.dataType; }
    public void setDataType(boolean dataType) { this.dataType = dataType; }

    public String getUserLanguage() { return this.UserLanguage; }
    public void setUserLanguage(String UserLanguage) { this.UserLanguage = UserLanguage; }

    public int getSttngLngPos() { return this.settingUserLngPosition; }
    public void setSttngLngPos(int settingUserLngPosition) { this.settingUserLngPosition = settingUserLngPosition; }



}
