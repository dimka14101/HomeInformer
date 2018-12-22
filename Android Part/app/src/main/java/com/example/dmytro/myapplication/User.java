package com.example.dmytro.myapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Dmytro on 22.03.2017.
 */

public class User implements Serializable {

    @SerializedName("access_token")
    @Expose
    private String accessToken="empty";
    @SerializedName("token_type")
    @Expose
    private String tokenType="bearer";
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn=1209599;
    @SerializedName("userName")
    @Expose
    private String userName="userName";
    @SerializedName(".issued")
    @Expose
    private String issued="date";
    @SerializedName(".expires")
    @Expose
    private String expires="date";

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

}
