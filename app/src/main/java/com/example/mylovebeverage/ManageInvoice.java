package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.datastore.core.Data;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.InvoiceAdapter;
//import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Fragments.FragmentFilterInvoice;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Invoice;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Date;

public class ManageInvoice extends FragmentActivity
        implements FragmentFilterInvoice.FragmentFilterInvoiceListener
{

    public  static ArrayList<Invoice> invoiceList = new ArrayList<Invoice>();
    public ListView listView;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_invoice = null;
    Invoice invoice;
    Button btnFilter;
    Button btnResetFilter;
    FragmentFilterInvoice fragmentFilterInvoice;
    String getDataFromFragment = null;
    ImageView arrback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invoice);

        btnFilter = findViewById(R.id.btnFilterInvoice);
        fragmentFilterInvoice = new FragmentFilterInvoice();

        btnResetFilter = findViewById(R.id.btnResetFilter);
        btnResetFilter.setVisibility(View.GONE);

        connecting_mssql = new Connecting_MSSQL(connection_invoice);
        connection_invoice = connecting_mssql.Connecting();

        getAllInvoices();
        setUpList();
        setUpOnClickListener();

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fragmentFilterInvoice.isAdded()){
                    addFragment(fragmentFilterInvoice);
                }else{
                    if(!fragmentFilterInvoice.isHidden()){
                        hideFragment(fragmentFilterInvoice);
                    }
                    else {
                        showFragment(fragmentFilterInvoice);
                    }
                }
            }
        });

        btnResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllInvoices();
                setUpList();
                btnResetFilter.setVisibility(View.GONE);
            }
        });
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void FilterInvoiceList(String getDataFromFragment) {
        if (getDataFromFragment!=null){
            hideFragment(fragmentFilterInvoice);
            btnResetFilter.setVisibility(View.VISIBLE);
            switch (getDataFromFragment){
                case "Price1":
                    if (connection_invoice!=null)
                    {
                        try {
                            Statement statement = connection_invoice.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE WHERE Price_of_Invoice <= 100000;");
                            invoiceList.removeAll(invoiceList);
                            while (resultSet.next()) {
                                invoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                                invoiceList.add(invoice);
                            }

                        }catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                    }
                    setUpList();
                    break;
                case "Price2":
                    if (connection_invoice!=null)
                    {
                        try {
                            Statement statement = connection_invoice.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE WHERE Price_of_Invoice > 100000 AND Price_of_Invoice <= 1000000;");
                            invoiceList.removeAll(invoiceList);
                            while (resultSet.next()) {
                                invoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                                invoiceList.add(invoice);
                            }

                        }catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                    }
                    setUpList();
                    break;
                case "Price3":
                    if (connection_invoice!=null)
                    {
                        try {
                            Statement statement = connection_invoice.createStatement();
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE WHERE Price_of_Invoice > 1000000;");
                            invoiceList.removeAll(invoiceList);
                            while (resultSet.next()) {
                                invoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                                invoiceList.add(invoice);
                            }

                        }catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                    }
                    setUpList();
                    break;
                case "Date1":
                    if (connection_invoice!=null)
                    {
                        try {
                            Statement statement = connection_invoice.createStatement();
                            LocalDate date = LocalDate.now();
                            LocalDate lastWeek = date.minusWeeks(1);
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE WHERE DateTime_Invoice >= '" + lastWeek.getYear() + "-" + lastWeek.getMonth() + "-" + lastWeek.getDayOfMonth() + "';");
                            invoiceList.removeAll(invoiceList);
                            while (resultSet.next()) {
                                invoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                                invoiceList.add(invoice);
                            }

                        }catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                    }
                    setUpList();
                    break;
                case "Date2":
                    if (connection_invoice!=null)
                    {
                        try {
                            Statement statement = connection_invoice.createStatement();
                            LocalDate date = LocalDate.now();
                            LocalDate lastMonth = date.minusMonths(1);
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE WHERE DateTime_Invoice >= '" + lastMonth.getYear() + "-" + lastMonth.getMonth() + "-" + lastMonth.getDayOfMonth() + "';");
                            invoiceList.removeAll(invoiceList);
                            while (resultSet.next()) {
                                invoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                                invoiceList.add(invoice);
                            }

                        }catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                    }
                    setUpList();
                    break;
                case "Date3":
                    if (connection_invoice!=null)
                    {
                        try {
                            Statement statement = connection_invoice.createStatement();
                            LocalDate date = LocalDate.now();
                            LocalDate lastYear = date.minusYears(1);
                            ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE WHERE DateTime_Invoice >= '" + lastYear.getYear() + "-" + lastYear.getMonth() + "-" + lastYear.getDayOfMonth() + "';");
                            invoiceList.removeAll(invoiceList);
                            while (resultSet.next()) {
                                invoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                                invoiceList.add(invoice);
                            }

                        }catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                    }
                    setUpList();
                    break;
            }
        }
    }

    public void onButtonClick(String text) {
        getDataFromFragment = text;
        FilterInvoiceList(getDataFromFragment);
    }

    protected void addFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_filter_invoice_container, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    protected void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    protected void showFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().show(fragment)
                .commit();
    }

    protected void hideFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().hide(fragment)
                .commit();
    }

    protected void getAllInvoices(){
        if (connection_invoice!=null)
        {
            try {
                Statement statement = connection_invoice.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM INVOICE;");
                invoiceList.removeAll(invoiceList);
                while (resultSet.next()) {
                    invoice = new Invoice(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getTimestamp(3),resultSet.getInt(4),resultSet.getInt(5),resultSet.getInt(6));
                    invoiceList.add(invoice);
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

    private void setUpList()
    {
        listView = findViewById(R.id.invoiceListView);
        InvoiceAdapter invoiceAdapter = new InvoiceAdapter(getApplicationContext(),0,invoiceList);
        listView.setAdapter(invoiceAdapter);
        invoiceAdapter.notifyDataSetChanged();
    }

    private void setUpOnClickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Invoice selectInvoice = (Invoice) (listView.getItemAtPosition(i));
                Intent showDetail = new Intent(getApplicationContext(), InvoiceDetail.class);
                showDetail.putExtra("postion",i);
                startActivity(showDetail);
            }
        });
    }
}