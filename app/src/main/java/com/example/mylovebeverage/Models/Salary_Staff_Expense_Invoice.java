package com.example.mylovebeverage.Models;


public class Salary_Staff_Expense_Invoice extends Expense_Invoice {
      String LinkExcel ="";
      String month = "";
      String year = "";

    public Salary_Staff_Expense_Invoice(String ID_invoice, int price, String ID_staff, String payment_method, String paydate, String status, String linkExcel, String month, String year) {
        super(ID_invoice, price, ID_staff, payment_method, paydate, status);
        LinkExcel = linkExcel;
        this.month = month;
        this.year = year;
    }

    public String getLinkExcel() {
        return LinkExcel;
    }

    public void setLinkExcel(String linkExcel) {
        LinkExcel = linkExcel;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
