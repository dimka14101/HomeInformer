package history_tab_classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dmytro on 06.01.2017.
 */

public class HistoryObject implements Serializable {
    @SerializedName("detailsId")
    @Expose
    private int detailsId=0;

    @SerializedName("temperature1")
    @Expose
    private double temperature1=0;

    @SerializedName("temperature2")
    @Expose
    private double temperature2=0;

    @SerializedName("humidity")
    @Expose
    private double humidity=0;

    @SerializedName("luminosity")
    @Expose
    private double luminosity=0;

    @SerializedName("pressure")
    @Expose
    private double pressure=0;

    @SerializedName("altitude")
    @Expose
    private double altitude=0;

    @SerializedName("datetime")
    @Expose
    private String datetime="";

    public HistoryObject(int detailsId, double temperature1, double temperature2,
                   double humidity, double luminosity, double pressure, double altitude, String datetime) {
        this.detailsId = detailsId;
        this.temperature1 = temperature1;
        this.temperature2 = temperature2;
        this.humidity = humidity;
        this.luminosity = luminosity;
        this.pressure = pressure;
        this.altitude = altitude;
        this.datetime = datetime;
    }


    @Override
    public String toString() {
        return "T1="+temperature1+" T2="+temperature2+" HMDT="+humidity+" LMNST="+luminosity+" PRSR="+pressure+" ALTTD="+altitude+" DATE="+datetime;
    }

    public int getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(int detailsId) {
        this.detailsId = detailsId;
    }

    public double getTemperature1() {
        return temperature1;
    }

    public void setTemperature1(double temperature1) {
        this.temperature1 = temperature1;
    }

    public double getTemperature2() {
        return temperature2;
    }

    public void setTemperature2(double temperature2) {
        this.temperature2 = temperature2;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(double luminosity) {
        this.luminosity = luminosity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
        Date date = null;
        try {
            date = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat print = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return  print.format(date); }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
