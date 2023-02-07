package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class InvoiceDetail extends AppCompatActivity {
    public static ArrayList<DetailOfInvoice> invoiceDetailList = new ArrayList<DetailOfInvoice>();
    public ListView listView;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_invoiceDetail = null;
    Invoice selectedInvoice;
    DetailOfInvoice detailOfInvoice;
    ImageView arrback;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        tv1 = findViewById(R.id.txtDisplayInvoiceCode);
        tv2 = findViewById(R.id.txtDisplayStaffCode);
        tv3 = findViewById(R.id.txtDisplayInvoiceDate);
        tv4 = findViewById(R.id.txtDisplayTotalPrice);
        tv5 = findViewById(R.id.txtDisplayMoneyReceived);
        tv6 = findViewById(R.id.txtDisplayMoneyReturned);
        connecting_mssql = new Connecting_MSSQL(connection_invoiceDetail);
        connection_invoiceDetail = connecting_mssql.Connecting();
        getSelectedInvoice();
        setValue();

        getInvoiceDetails();
        setUpList();

        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void CheckStaffofInvoice() {
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connection_invoiceDetail != null) {
                    Intent intent = new Intent(getApplicationContext(), StaffInformation.class);
                    try {
                        Statement statement = connection_invoiceDetail.createStatement();
                        ResultSet resultSet = statement.executeQuery("\n" +
                                "select * from STAFF WHERE Staff_ID =" + "'" + tv2.getText().toString().trim() + "'");
                        while (resultSet.next()) {
                            intent.putExtra("ID", resultSet.getString(1).trim());
                            intent.putExtra("Name", resultSet.getString(2).trim());
                            intent.putExtra("Position", resultSet.getString(3).trim());
                            intent.putExtra("Gender", resultSet.getString(4).trim());
                            intent.putExtra("PhoneNumber", resultSet.getString(5).trim());
                            intent.putExtra("Salary", resultSet.getInt(6));
                            intent.putExtra("Image", resultSet.getString(7).trim());
                            intent.putExtra("Email", resultSet.getString(8).trim());
                            intent.putExtra("Status", resultSet.getString(9).trim());
                        }
                        startActivity(intent);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckStaffofInvoice();

    }
    private void getInvoiceDetails() {
        if (connection_invoiceDetail != null) {
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
        listView = findViewById(R.id.invoiceDetailListView);
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
        tv1.setText(selectedInvoice.getInvoice_ID());
        tv2.setText(selectedInvoice.getStaff_ID());
        tv3.setText(selectedInvoice.getDateTime_Invoice());
        tv4.setText(selectedInvoice.getPriceCustom() + " VND");
        tv5.setText(selectedInvoice.getMoneyReceivedCustom() + " VND");
        tv6.setText(selectedInvoice.getMoneyReturnedCustom() + " VND");
    }

}