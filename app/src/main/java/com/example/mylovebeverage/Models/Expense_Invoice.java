package com.example.mylovebeverage.Models;
public class Expense_Invoice {
       String ID_invoice = "";
       int Price;
       String ID_staff = "";
       String Payment_method ="";
       String Paydate ="";
       String status ="";

    public Expense_Invoice(String ID_invoice, int price, String ID_staff, String payment_method, String paydate, String status) {
        this.ID_invoice = ID_invoice;
        Price = price;
        this.ID_staff = ID_staff;
        Payment_method = payment_method;
        Paydate = paydate;
        this.status = status;
    }
    public String getID_invoice() {
        return ID_invoice;
    }

    public void setID_invoice(String ID_invoice) {
        this.ID_invoice = ID_invoice;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getID_staff() {
        return ID_staff;
    }

    public void setID_staff(String ID_staff) {
        this.ID_staff = ID_staff;
    }

    public String getPayment_method() {
        return Payment_method;
    }

    public void setPayment_method(String payment_method) {
        Payment_method = payment_method;
    }

    public String getPaydate() {
        return Paydate;
    }

    public void setPaydate(String paydate) {
        Paydate = paydate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
