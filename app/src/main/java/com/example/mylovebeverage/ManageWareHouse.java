package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.WarehouseAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Supplier;
import com.example.mylovebeverage.Models.Warehouse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ManageWareHouse extends AppCompatActivity {
    private ListView listView;
    Spinner supSpinner;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_supplier = null;
    private static Connection connection_warehouse = null;
    public static ArrayList<Warehouse> warehousesList = new ArrayList<>();

    public static ArrayList<String> suppliersList = new ArrayList<String>();
    Supplier supplier;
    ImageView arrback;
    Warehouse warehouse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ware_house);
        connecting_mssql = new Connecting_MSSQL(connection_supplier);
        connection_supplier = connecting_mssql.Connecting();
        connecting_mssql = new Connecting_MSSQL(connection_warehouse);
        connection_warehouse = connecting_mssql.Connecting();
        FloatingActionButton addWHBtn = (FloatingActionButton) findViewById(R.id.addWarehouse);
        supSpinner = (Spinner) findViewById(R.id.supplierSpinner);
        getAllSuppliers();
        ArrayAdapter supplierAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, suppliersList);
        supplierAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        supSpinner.setAdapter(supplierAdapter);
        supSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(28);
                Toast.makeText(getApplicationContext(), suppliersList.get(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addWHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Add Warehouse ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),AddNewWarehouse.class);
                startActivityForResult(intent, 1);
            }
        });
        getAllWarehouse();
        setUpList();
        setUpOnClickListener();
        FloatingActionButton addProductBtn = findViewById(R.id.addWarehouse);
        arrback = (ImageView) findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    protected void getAllSuppliers(){
        if (connection_supplier!=null)
        {
            try {
                Statement statement = connection_supplier.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from [dbo].[SUPPLIER] where Status = 'active';");
                suppliersList.clear();
                while (resultSet.next()) {
                    suppliersList.add(resultSet.getString(2).trim());
                }

            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to Supplier makes error." , Toast.LENGTH_SHORT).show();
        }
    }
    protected void getAllWarehouse(){
        if (connection_warehouse!=null)
        {
            try {
                Statement statement = connection_warehouse.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from [dbo].[WAREHOUSEINVOICE];");
                warehousesList.clear();
                while (resultSet.next()) {
                    warehouse = new Warehouse(resultSet.getString(1).trim(),resultSet.getString(2).trim(), resultSet.getString(3).trim(), resultSet.getDate(4), resultSet.getInt(5));
                    warehousesList.add(warehouse);
                }

            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to Warehouse makes error." , Toast.LENGTH_SHORT).show();
        }
    }
    private void setUpList() {

        listView = (ListView) findViewById(R.id.warehouseListView);
        WarehouseAdapter adapter = new WarehouseAdapter(getApplicationContext(), 0, warehousesList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setUpOnClickListener() {

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Warehouse selectWarehouse = (Warehouse) (listView.getItemAtPosition(i));
            Intent showDetail = new Intent(getApplicationContext(), WarehouseDetail.class);
            showDetail.putExtra("id",selectWarehouse.getWarehouse_ID());
            startActivityForResult(showDetail, 1);
        });

    }
}