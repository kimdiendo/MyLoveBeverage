package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.InvoiceDetailAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.DetailOfInvoice;
import com.example.mylovebeverage.Models.Invoice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PostOrder extends AppCompatActivity {
    public  static ArrayList<DetailOfInvoice> invoicePostOrderDetailList = new ArrayList<DetailOfInvoice>();
    public ListView listView;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_invoiceDetail = null;
    String currentInvoiceId;
    Invoice selectedInvoice;
    DetailOfInvoice detailOfInvoice;
    Button btnComplete, btnReprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_order);

        Dialog dialog = new Dialog(PostOrder.this);
        dialog.setContentView(R.layout.activity_custom_dialog_2);
        TextView txtMessage = dialog.findViewById(R.id.txtCustomDialog2Message);
        txtMessage.setText("The invoice has just been printed!");
        dialog.show();
        Button btnOk = dialog.findViewById(R.id.btnCustomDialog2Ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        connecting_mssql = new Connecting_MSSQL(connection_invoiceDetail);
        connection_invoiceDetail = connecting_mssql.Connecting();

        btnComplete = findViewById(R.id.btnPostOrderComplete);
        btnReprint = findViewById(R.id.btnPostOrderReprint);

        getCurrentInvoice();
        setValue();

        getInvoiceDetails();
        setUpList();

        btnReprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyReprint();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void NotifyReprint(){
        Dialog dialog = new Dialog(PostOrder.this);
        dialog.setContentView(R.layout.activity_custom_dialog_2);
        TextView txtMessage = dialog.findViewById(R.id.txtCustomDialog2Message);
        txtMessage.setText("The invoice has just been reprinted!");
        dialog.show();
        Button btnOk = dialog.findViewById(R.id.btnCustomDialog2Ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getInvoiceDetails() {
        if (connection_invoiceDetail!=null)
        {
            try {
                Statement statement = connection_invoiceDetail.createStatement();
                Statement statement2 = connection_invoiceDetail.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM DETAILINVOICE WHERE Invoice_ID = '" + selectedInvoice.getInvoice_ID() + "';");
                invoicePostOrderDetailList.removeAll(invoicePostOrderDetailList);
                while (resultSet.next()) {
                    ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM PRODUCT WHERE Product_ID = '" + resultSet.getString(2).trim() + "';");
                    while (resultSet2.next()){
                        detailOfInvoice = new DetailOfInvoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getInt(3),resultSet2.getInt(7),resultSet2.getString(8).trim(),resultSet2.getString(3).trim());
                    }
                    invoicePostOrderDetailList.add(detailOfInvoice);
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
        listView = findViewById(R.id.invoicePostOrderDetailListView);
        InvoiceDetailAdapter invoiceDetailAdapter = new InvoiceDetailAdapter(getApplicationContext(),0,invoicePostOrderDetailList);
        listView.setAdapter(invoiceDetailAdapter);
    }

    private void getCurrentInvoice() {
        Intent getIntent = getIntent();
        currentInvoiceId = getIntent.getStringExtra("invoiceId");

        if (connection_invoiceDetail!=null)
        {
            try {
                Statement statement = connection_invoiceDetail.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE WHERE Invoice_ID = '" + currentInvoiceId + "';");
                while (resultSet.next()){
                    selectedInvoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                }
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
        }
    }

    private void setValue() {
        TextView tv1 = findViewById(R.id.txtPostOrderDisplayInvoiceCode);
        TextView tv2 = findViewById(R.id.txtPostOrderDisplayStaffCode);
        TextView tv3 = findViewById(R.id.txtPostOrderDisplayInvoiceDate);
        TextView tv4 = findViewById(R.id.txtPostOrderDisplayTotalPrice);
        TextView tv5 = findViewById(R.id.txtPostOrderDisplayMoneyReceived);
        TextView tv6 = findViewById(R.id.txtPostOrderDisplayMoneyReturned);

        tv1.setText(selectedInvoice.getInvoice_ID());
        tv2.setText(selectedInvoice.getStaff_ID());
        tv3.setText(selectedInvoice.getDateTime_Invoice());
        tv4.setText(selectedInvoice.getPriceCustom() + " VND");
        tv5.setText(selectedInvoice.getMoneyReceivedCustom() + " VND");
        tv6.setText(selectedInvoice.getMoneyReturnedCustom() + " VND");
    }
}