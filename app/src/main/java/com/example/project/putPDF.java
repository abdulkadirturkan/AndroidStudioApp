package com.example.project;

import android.os.StrictMode;

import java.lang.reflect.Constructor;

public class putPDF {
    public String name;
    public String url;

    public putPDF(){
    }
    public putPDF(String name,String url){
        this.name=name;
        this.url=url;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url=url;
    }
}
