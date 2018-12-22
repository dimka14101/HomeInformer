package com.example.dmytro.myapplication;
import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Callback;

/**
 * Created by Dmytro on 02.01.2017.
 */

public class Details implements Serializable {

    @SerializedName("detailsId")
    @Expose
    private int detailsId=0;
    @SerializedName("temperature1")
    @Expose
    private Double temperature1=0.0;
    @SerializedName("temperature2")
    @Expose
    private Double temperature2=0.0;
    @SerializedName("humidity")
    @Expose
    private double humidity=0.0;
    @SerializedName("luminosity")
    @Expose
    private double luminosity=0.0;
    @SerializedName("datetime")
    @Expose
    private String datetime="2017-01-10T10:10:10.10";
    @SerializedName("pressure")
    @Expose
    private double pressure=0.0;
    @SerializedName("altitude")
    @Expose
    private double altitude=0.0;

    public int getDetailsId() { return this.detailsId; }
    public void setDetailsId(int detailsId) { this.detailsId = detailsId; }

    public Double getTemperature1() { return this.temperature1; }
    public void setTemperature1(double temperature1) { this.temperature1 = temperature1; }

    public Double getTemperature2() { return this.temperature2; }
    public void setTemperature2(double temperature2) { this.temperature2 = temperature2; }

    public double getHumidity() { return this.humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public double getLuminosity() { return this.luminosity; }
    public void setLuminosity(double luminosity) { this.luminosity = luminosity; }

    public double getPressure() { return this.pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }

    public double getAltitude() { return this.altitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }

    public String getDatetime()   {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return  print.format(date); }
    public void setDatetime(String datetime){this.datetime =datetime; }



}
