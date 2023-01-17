package com.example.mylovebeverage.Models;
public class Detail_Profile {
     private String name1 ="";
     private String name2 ="";
     private int imageView;

    public Detail_Profile(int imageView , String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
        this.imageView = imageView;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }
}
