package com.example.virtual_cabinet_16011089;

import android.location.Location;

import java.util.Date;

public class Activity {

    private Integer id;
    private String name;
    private String type;
    private String date;
    private String targetLocation;
    private Combine selectedCombine;

    public Activity(String name, String type, String date, String targetLocation, Combine selectedCombine) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.targetLocation = targetLocation;
        this.selectedCombine = selectedCombine;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public int getSelectedCombineId() {
        return selectedCombine.getID();
    }

    public void setSelectedCombine(Combine selectedCombine) {
        this.selectedCombine = selectedCombine;
    }

    @Override
    public String toString() {
        return
                "İsmi=              " + name  +
                "\n\nTipi=          " + type  +
                "\n\nTarihi=        " + date  +
                "\n\nLokasyonu=     " + targetLocation +
                "\n\nKombin İsmi =  " + this.selectedCombine.getTag();
    }
}
