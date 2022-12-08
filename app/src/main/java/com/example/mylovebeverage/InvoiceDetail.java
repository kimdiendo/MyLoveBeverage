package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.InvoiceAdapter;
import com.example.mylovebeverage.Adapters.InvoiceDetailAdapter;
import com.example.mylovebeverage.Models.DetailOfInvoice;
import com.example.mylovebeverage.Models.Invoice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InvoiceDetail extends AppCompatActivity {
    public  static ArrayList<DetailOfInvoice> invoiceDetailList = new ArrayList<DetailOfInvoice>();
    public ListView listView;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_invoiceDetail = null;
    Invoice selectedInvoice;
    DetailOfInvoice detailOfInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        connecting_mssql = new Connecting_MSSQL(connection_invoiceDetail);
        connection_invoiceDetail = connecting_mssql.Connecting();

        getSelectedInvoice();
        setValue();

        getInvoiceDetails();
        setUpList();
    }

    private void getInvoiceDetails() {
        if (connection_invoiceDetail!=null)
        {
            try {
                Statement statement = connection_invoiceDetail.createStatement();
                Statement statement2 = connection_invoiceDetail.createStatement();
                Intent getIntent = getIntent();
                Integer getInvoice = getIntent.getIntExtra("postion",0);
                selectedInvoice = ManageInvoice.invoiceList.get(getInvoice);
                ResultSet resultSet = statement.executeQuery("SELECT * FROM DETAILINVOICE WHERE Invoice_ID = '" + selectedInvoice.getInvoice_ID() + "';");
                invoiceDetailList.removeAll(invoiceDetailList);
                while (resultSet.next()) {
                    ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM PRODUCT WHERE Product_ID = '" + resultSet.getString(2).trim() + "';");
                    while (resultSet2.next()){
                        detailOfInvoice = new DetailOfInvoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getInt(3),resultSet2.getInt(7),resultSet2.getString(8).trim(),resultSet2.getString(3).trim());
                    }
                    invoiceDetailList.add(detailOfInvoice);
                }

            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to DetailInvoiceDB makes error." , Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpList() {
        listView = (ListView) findViewById(R.id.invoiceDetailListView);
        InvoiceDetailAdapter invoiceDetailAdapter = new InvoiceDetailAdapter(getApplicationContext(),0,invoiceDetailList);
        listView.setAdapter(invoiceDetailAdapter);
    }

    private void getSelectedInvoice() {
        Intent getIntent = getIntent();
        Integer getInvoice = getIntent.getIntExtra("postion",0);
        //String getId = getInvoice.substring(1);
        selectedInvoice = ManageInvoice.invoiceList.get(getInvoice);
    }

    private void setValue() {
        TextView tv1 = (TextView) findViewById(R.id.txtDisplayInvoiceCode);
        TextView tv2 = (TextView) findViewById(R.id.txtDisplayStaffCode);
        TextView tv3 = (TextView) findViewById(R.id.txtDisplayInvoiceDate);
        TextView tv4 = (TextView) findViewById(R.id.txtDisplayTotalPrice);

        tv1.setText(selectedInvoice.getInvoice_ID());
        tv2.setText(selectedInvoice.getStaff_ID());
        tv3.setText(selectedInvoice.getDateTime_Invoice().toString());
        tv4.setText(selectedInvoice.getPriceCustom() + " VND");
    }
}