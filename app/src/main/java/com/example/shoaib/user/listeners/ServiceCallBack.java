package com.example.shoaib.user.listeners;


import java.io.UnsupportedEncodingException;

public interface ServiceCallBack {

    public void onSuccess(String requestTag, Object data) throws UnsupportedEncodingException;

    public void onFailure(String requestTag,String message);

}
