package com.example.mylovebeverage.Models;

public class Other_Expense_Invoice extends Expense_Invoice {
       String Kind_of_invoice = "";
       String month ="";
       String year ="";
       int icon;

    public Other_Expense_Invoice(String ID_invoice, int price, String ID_staff, String payment_method, String paydate, String status, String kind_of_invoice, String month, String year, int icon) {
        super(ID_invoice, price, ID_staff, payment_method, paydate, status);
        Kind_of_invoice = kind_of_invoice;
        this.month = month;
        this.year = year;
        this.icon = icon;
    }

    public String getKind_of_invoice() {
        return Kind_of_invoice;
    }

    public void setKind_of_invoice(String kind_of_invoice) {
        Kind_of_invoice = kind_of_invoice;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
