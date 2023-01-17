package com.example.mylovebeverage.Models;

import com.example.mylovebeverage.ManageStaff;

public class Detail_Human_Resource extends ManageStaff {
    private String ID ="";
    private String Name="";
    private String Position ="";
    private String Email ="";
    private String Gender ="";
    private String PhoneNumber="";
    private int Salary;
    private String imageview;
    private String status;

    public Detail_Human_Resource(String ID, String name, String position, String gender, String phoneNumber, int salary, String imageview , String email , String Status) {
        this.ID = ID;
        Name = name;
        Position = position;
        Email = email;
        Gender = gender;
        PhoneNumber = phoneNumber;
        Salary = salary;
        this.imageview = imageview;
        this.status = Status;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getSalary() {
        return Salary;
    }

    public void setSalary(int salary) {
        Salary = salary;
    }

    public String getImageview() {
        return imageview;
    }

    public void setImageview(String imageview) {
        this.imageview = imageview;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
