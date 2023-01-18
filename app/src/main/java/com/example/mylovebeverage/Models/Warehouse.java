package com.example.mylovebeverage.Models;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;

public class Warehouse {
    private String Warehouse_ID ="";
    private String Staff_ID ="";
    private String Supplier_ID ="";
    private Date DateTime;
    private int Price =0;

    public Warehouse(String warehouse_ID, String staff_ID, String supplier_ID, Date dateTime, int price) {
        Warehouse_ID = warehouse_ID;
        Staff_ID = staff_ID;
        Supplier_ID = supplier_ID;
        DateTime = dateTime;
        Price = price;
    }

    public String getWarehouse_ID() {
        return Warehouse_ID;
    }

    public void setWarehouse_ID(String warehouse_ID) {
        Warehouse_ID = warehouse_ID;
    }

    public String getStaff_ID() {
        return Staff_ID;
    }

    public void setStaff_ID(String staff_ID) {
        Staff_ID = staff_ID;
    }

    public String getSupplier_ID() {
        return Supplier_ID;
    }

    public void setSupplier_ID(String supplier_ID) {
        Supplier_ID = supplier_ID;
    }
    public Date getDateTime() {
        return DateTime;
    }

    public void setDateTime(Date dateTime) {
        DateTime = dateTime;
    }

    public int getPrice() {
        return Price;
    }
    public String getPriceCustom(){
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(Price);
        return a;
    }
    public void setPrice(int price) {
        Price = price;
    }
}
