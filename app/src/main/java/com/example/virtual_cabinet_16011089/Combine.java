package com.example.virtual_cabinet_16011089;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

public class Combine {
    private String Tag;
    private Integer ID;
    private String facePath;
    private Integer[] clothesList;

    public Combine(String Tag, String facePath) {
        this.Tag = Tag;
        this.facePath = facePath;
        clothesList = new Integer[4];
    }


    public String getTag() {
        return Tag;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void addClothes(Integer id, Integer clothesID){
        this.clothesList[id] = clothesID;
    }
    public Integer getClothesID(Integer id){
        return this.clothesList[id];
    }

    public String getFacePath() {
        return facePath;
    }

    public Uri getFaceUri(Context context) {
        if(this.facePath == null)
            return null;
        File file = new File(this.facePath);
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
    }


    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }
}
