package com.example.dmytro.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dmytro on 10.03.2017.
 */

public class DBSize {

        @SerializedName("dbsize")
        @Expose
        private Double dbsize;

        public Double getDbsize() {
            return dbsize;
        }

        public void setDbsize(Double dbsize) {
            this.dbsize = dbsize;
        }


}
