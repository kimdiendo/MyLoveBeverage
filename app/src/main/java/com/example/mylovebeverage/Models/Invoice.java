package com.example.mylovebeverage.Models;

import net.sourceforge.jtds.jdbc.DateTime;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Invoice {
    private String Invoice_ID;
    private String Staff_ID;
    private Date DateTime_Invoice;
    private Integer Price_of_Invoice;
    private Integer Money_Received;
    private Integer Money_Returned;

    public Invoice(String invoice_ID, String staff_ID, Date dateTime_Invoice, Integer price_of_Invoice, Integer money_Received, Integer money_Returned) {
        Invoice_ID = invoice_ID;
        Staff_ID = staff_ID;
        DateTime_Invoice = dateTime_Invoice;
        Price_of_Invoice = price_of_Invoice;
        Money_Received = money_Received;
        Money_Returned = money_Returned;
    }

    public Integer getMoney_Received() {
        return Money_Received;
    }

    public void setMoney_Received(Integer money_Received) {
        Money_Received = money_Received;
    }

    public Integer getMoney_Returned() {
        return Money_Returned;
    }

    public void setMoney_Returned(Integer money_Returned) {
        Money_Returned = money_Returned;
    }

    public String getInvoice_ID() {
        return Invoice_ID;
    }

    public void setInvoice_ID(String invoice_ID) {
        Invoice_ID = invoice_ID;
    }

    public String getStaff_ID() {
        return Staff_ID;
    }

    public void setStaff_ID(String staff_ID) {
        Staff_ID = staff_ID;
    }

    public String getDateTime_Invoice()
    {
        DateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return dateFormat.format(DateTime_Invoice);
    }

    public void setDateTime_Invoice(Date dateTime_Invoice) {
        DateTime_Invoice = dateTime_Invoice;
    }

    public Integer getPrice_of_Invoice()
    {
        //DecimalFormat df = new DecimalFormat("###.###.###");
        //return df.format(Price_of_Invoice);
        return Price_of_Invoice;
    }

    public void setPrice_of_Invoice(Integer price_of_Invoice) {
        Price_of_Invoice = price_of_Invoice;
    }

    public String getPriceCustom(){
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(Price_of_Invoice);
        return a;
    }

    public String getMoneyReceivedCustom(){
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(Money_Received);
        return a;
    }

    public String getMoneyReturnedCustom(){
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(Money_Returned);
        return a;
    }
}