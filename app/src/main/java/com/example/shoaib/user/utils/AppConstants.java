package com.example.shoaib.user.utils;


import io.socket.client.Socket;

public class AppConstants {


    public static final int initialTimeoutMs = 30000;

    //Urls
   // public static final String BASE_URL = "http://reqres.in/api/";172.20.52.43:3000  192.168.10.17
    public static final String ip_address="http://192.168.10.8:3000";
    public static final String signin_URL = ip_address+"/login";
    public static final String signup_URL = ip_address+"/register";
    public static final String sendmail_URL = ip_address+"/sendmail";
    public static final String connect_URL = ip_address+"/connect";
    public static final String getcamera_URL = ip_address+"/getcameras";
    public static final String setcamera_URL = ip_address+"/setcameras";
    public static final String getnotification_URL = ip_address+"/getnotification";
    public static  String loginemail="";
    public static  String connectemail="";
    public static final String POST_TAG = "post_request";
    public static Socket socket;
    public static long videoid=0;
    public static String reqid;





}
