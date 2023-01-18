package com.example.mylovebeverage.Models;

import java.text.NumberFormat;
import java.util.Locale;

public class Product {
    private String Product_ID ="";
    private String Category_ID ="";
    private String Name_of_Product ="";
    private String Branding ="";
    private int Price =0;
    private String Unit ="";
    private int Quantity =0;
    private String Image_Product ="";
    private Integer OrderAmount = 1;

    public Product(String product_ID, String category_ID, String name_of_Product, String branding, int price, String unit, int quantity, String image_Product) {
        Product_ID = product_ID;
        Category_ID = category_ID;
        Name_of_Product = name_of_Product;
        Branding = branding;
        Price = price;
        Unit = unit;
        Quantity = quantity;
        Image_Product = image_Product;
    }

    public Integer getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public String getCategory_ID() {
        return Category_ID;
    }

    public void setCategory_ID(String category_ID) {
        Category_ID = category_ID;
    }

    public String getName_of_Product() {
        return Name_of_Product;
    }

    public void setName_of_Product(String name_of_Product) {
        Name_of_Product = name_of_Product;
    }

    public String getBranding() {
        return Branding;
    }

    public void setBranding(String branding) {
        Branding = branding;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getImage_Product() {
        return Image_Product;
    }

    public void setImage_Product(String image_Product) {
        Image_Product = image_Product;
    }

    public String getPriceCustom(){
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(Price);
        return a;
    }

}