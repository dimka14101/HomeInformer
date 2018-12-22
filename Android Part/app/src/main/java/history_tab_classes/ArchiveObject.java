package history_tab_classes;

import java.io.Serializable;

/**
 * Created by Dmytro on 06.01.2017.
 */

public class ArchiveObject implements Serializable{
       /**
     * Created by Dmytro on 06.01.2017.
     */
        private int detailsIdA=0;
        private double temp1A=0;
        private double temp2A=0;
        private double humiditiA=0;
        private double luminosityA=0;
        private double pressureA=0;
        private double altitudeA=0;
        private String timeperiodA="";

    public ArchiveObject(int detailsIdA, double temp1A, double temp2A, double humiditiA,
                         double luminosityA, double pressureA, double altitudeA, String timeperiodA) {
        this.detailsIdA = detailsIdA;
        this.temp1A = temp1A;
        this.temp2A = temp2A;
        this.humiditiA = humiditiA;
        this.luminosityA = luminosityA;
        this.pressureA = pressureA;
        this.altitudeA = altitudeA;
        this.timeperiodA = timeperiodA;
    }

        public int getDetailsIdA() {
            return detailsIdA;
        }

        public void setDetailsIdA(int detailsIdA) {
            this.detailsIdA = detailsIdA;
        }

        public double getTemp1A() {
            return temp1A;
        }

        public void setTemp1A(double temp1A) {
            this.temp1A = temp1A;
        }

        public double getTemp2A() {
            return temp2A;
        }

        public void setTemp2A(double temp2A) {
            this.temp2A = temp2A;
        }

        public double getHumiditiA() {
            return humiditiA;
        }

        public void setHumiditiA(double humiditiA) {
            this.humiditiA = humiditiA;
        }

        public double getLuminosityA() {
            return luminosityA;
        }

        public void setLuminosityA(double luminosityA) {
            this.luminosityA = luminosityA;
        }

        public double getPressureA() {
            return pressureA;
        }

        public void setPressureA(double pressureA) {
            this.pressureA = pressureA;
        }

        public double getAltitudeA() {
            return altitudeA;
        }

        public void setAltitudeA(double altitudeA) {
            this.altitudeA = altitudeA;
        }

        public String getTimeperiodA() {
            return timeperiodA;
        }

        public void setTimeperiodA(String timeperiodA) {
            this.timeperiodA = timeperiodA;
        }
    }


