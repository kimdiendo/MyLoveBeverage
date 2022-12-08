package com.example.mylovebeverage.Models;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class DetailOfInvoice
{
    private String Invoice_ID;
    private String Product_ID;
    private Integer Quantity;
    private Integer Price;
    private String Unit;
    private String ProductName;

    public DetailOfInvoice(String invoice_ID, String product_ID, Integer quantity, Integer price, String unit, String productName) {
        Invoice_ID = invoice_ID;
        Product_ID = product_ID;
        Quantity = quantity;
        Price = price;
        Unit = unit;
        ProductName = productName;
    }

    public String getInvoice_ID() {
        return Invoice_ID;
    }

    public void setInvoice_ID(String invoice_ID) {
        Invoice_ID = invoice_ID;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public String getPrice()
    {
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(Price);
        return a;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }
}
