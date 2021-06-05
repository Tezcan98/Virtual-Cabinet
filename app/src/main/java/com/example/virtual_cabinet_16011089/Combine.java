package com.example.virtual_cabinet_16011089;

import android.util.Log;

import java.util.List;

public class Combine {
    private String Tag;
    private Integer ID;
    private Integer[] clothesList;

    public Combine(int id, String Tag) {
        this.Tag = Tag;
        ID = id;
        clothesList = new Integer[5];
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

}
