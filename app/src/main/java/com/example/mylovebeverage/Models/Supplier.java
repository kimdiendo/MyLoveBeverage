package com.example.mylovebeverage.Models;

public class Supplier {
    private String Supplier_ID ="";
    private String Name_of_supplier ="";
    private String Address ="";
    private String Email ="";
    private String PhoneNumber ="";
    private String Logo ="";
    private int TotalBill = 0;
    private int TotalMoney = 0;

    public Supplier(String supplier_ID, String name_of_supplier, String address, String email, String phoneNumber, String logo, int totalBill, int totalMoney) {
        Supplier_ID = supplier_ID;
        Name_of_supplier = name_of_supplier;
        Address = address;
        Email = email;
        PhoneNumber = phoneNumber;
        Logo = logo;
        TotalBill = totalBill;
        TotalMoney = totalMoney;
    }

    public String getSupplier_ID() {
        return Supplier_ID;
    }

    public void setSupplier_ID(String supplier_ID) {
        Supplier_ID = supplier_ID;
    }

    public String getName_of_supplier() {
        return Name_of_supplier;
    }

    public void setName_of_supplier(String name_of_supplier) {
        Name_of_supplier = name_of_supplier;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public int getTotalBill() {
        return TotalBill;
    }

    public void setTotalBill(int totalBill) {
        TotalBill = totalBill;
    }

    public int getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        TotalMoney = totalMoney;
    }
}
