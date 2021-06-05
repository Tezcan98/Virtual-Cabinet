package com.example.virtual_cabinet_16011089;

import java.util.List;

public class Wardrop {
    private Integer ID;
    private String Name;
    private Integer clothesCount;
    private List<Clothes> clothesList;

    public Wardrop(Integer id, String name) {
        ID = id;
        Name = name;
        this.clothesCount = 0;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getClothesCount() {
        return clothesCount;
    }

    public void setClothesCount(Integer clothesCount) {
        this.clothesCount = clothesCount;
    }

    public List<Clothes> getClothesList() {
        return clothesList;
    }

    public void setClothesList(List<Clothes> clothesList) {
        this.clothesList = clothesList;
    }

    public void addClothes(){
        this.clothesCount++;
    }

    public Integer getID() {
        return ID;
    }
}
