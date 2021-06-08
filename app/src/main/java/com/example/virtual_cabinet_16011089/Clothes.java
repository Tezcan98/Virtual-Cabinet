package com.example.virtual_cabinet_16011089;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;

public class Clothes {
    private Integer ID;
    private String name;
    private String type;
    private String color;
    private String pattern;
    private Float cost;
    private String imagePath;
    private String date;



    public Clothes(String name, String type, String color, String pattern, float cost, String imagePath, String date) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.pattern = pattern;
        this.cost = cost;
        this.imagePath = imagePath;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getPattern() {
        return pattern;
    }
    public float getCost() {
        return cost;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return      "Başlık             " + name +
                "\n\nTür                " + type +
                "\n\nRenk               " + color+
                "\n\nDesen              " + pattern +
                "\n\nFiyat              " + cost +
                "\n\nAlınma Tarihi      " + date ;

    }


    public void setFeatures(String type, String color, String pattern, Float cost) {
        this.type = type;
        this.color = color;
        this.pattern = pattern;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public String getImagePath() {
        return imagePath;
    }
    public Uri getUriFromStringPath(Context context) {
        File file = new File(this.imagePath);
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
    }


    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void returnImageBitmap() {

    }
}
