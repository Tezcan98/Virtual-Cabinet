package com.example.virtual_cabinet_16011089;

public class Clothes {
    private Integer ID;
    private String name;
    private String type;
    private String color;
    private String pattern;
    private Float cost;
    private String image_path;
    private String date;


    public Clothes() {
    }

    public Clothes(String name, String type, String color, String pattern, float cost, String image_path, String date) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.pattern = pattern;
        this.cost = cost;
        this.image_path = image_path;
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

    public String getImagePath() {
        return image_path;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
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

    public void setImage_path(String image_path) {
        this.image_path = image_path;
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
}
