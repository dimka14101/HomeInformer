package com.example.dmytro.myapplication;

import java.util.ArrayList;

import history_tab_classes.ArchiveObject;
import history_tab_classes.HistoryObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Dmytro on 09.03.2017.
 */

public interface ManagerApi {

    @GET("api/getlastrecord")
    Call<Details> getLastRecord(@Header("Authorization") String authHeader);

    @GET("api/getall")
    Call<ArrayList<HistoryObject>> getHistory(@Header("Authorization") String authHeader);

    @GET("api/getarchived")
    Call<ArrayList<ArchiveObject>> getArchive(@Header("Authorization") String authHeader);

    @GET("api/getdbsize")
    Call<DBSize> getDBSize(@Header("Authorization") String authHeader);

    //Not tested
    @GET("api/delete")
    Call<ResponseBody> deleteData(@Query("period")int cleanPeriod, @Header("Authorization") String authHeader);

    //Not tested
    @GET("api/archiveddata")
    Call<ResponseBody> archiveData(@Query("period")int cleanPeriod,@Header("Authorization") String authHeader);

    //Not tested
    @GET("api/updatesetting")
    Call<ResponseBody> updateCriticalTemp(@Query("description")String value, @Query("value")int criticalTemperature,@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/token")
    Call<User> login(@Field("username") String userEmail, @Field("Password") String password, @Field("grant_type") String grantType);

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("api/Account/ChangePassword")
    Call<ResponseBody> changePassword(@Field("oldpassword") String oldPassword, @Field("newpassword") String newPassword,@Field("confirmpassword") String confirmPassword, @Header("Authorization") String authHeader);

    @FormUrlEncoded
    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("api/Account/Register")
    Call<ResponseBody> userRegister(@Field("email") String email, @Field("password") String password,@Field("confirmpassword") String confirmPassword);

}
