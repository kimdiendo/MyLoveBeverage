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
import com.example.mylovebeverage.Models.Warehouse;
import com.example.mylovebeverage.Singleton.MySingleton;
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
    ImageView arrback;
    Warehouse warehouse;
    Boolean signal_adding = false;
    FloatingActionButton addWHBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ware_house);
        connecting_mssql = new Connecting_MSSQL(connection_supplier);//kết nối với csdl supplier
        connection_supplier = connecting_mssql.Connecting();
        connecting_mssql = new Connecting_MSSQL(connection_warehouse);//kết nối với csdl supplier
        connection_warehouse = connecting_mssql.Connecting();
        addWHBtn = findViewById(R.id.addWarehouse);
        supSpinner = findViewById(R.id.supplierSpinner);
        getAllSuppliers();
        ArrayAdapter supplierAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, suppliersList);
        supplierAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        supSpinner.setAdapter(supplierAdapter);
        arrback = findViewById(R.id.arrow_back);
        listView = findViewById(R.id.warehouseListView);
        getAllWarehouse();

    }

    @Override
    protected void onStart() {
        super.onStart();
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addWHBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signal_adding = true;
                Intent intent = new Intent(getApplicationContext(), AddNewWarehouse.class);
                startActivity(intent);
                getAllWarehouse();
            }
        });
        getAllWarehouse();
        ShowInvoicebySupplier();
        setUpOnClickListener();

    }
    private String Search(String nameofsupplier) {
        String Supplier_ID = "";
        if (connection_supplier != null) {
            try {
                Statement statement = connection_supplier.createStatement();
                ResultSet resultSet = statement.executeQuery("select Supplier_ID \n" +
                        "from SUPPLIER \n" +
                        "WHERE Name_of_supplier =" + "'" + nameofsupplier + "'");
                while (resultSet.next()) {
                    Supplier_ID = resultSet.getString(1).toString().trim();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Connect to Supplier makes error.", Toast.LENGTH_SHORT).show();
        }
        return Supplier_ID;
    }

    private Integer Counting_total_money(String Supplier_ID) {
        Integer total_money = 0;
        if (connection_warehouse != null) {
            try {
                Statement statement = connection_warehouse.createStatement();
                ResultSet resultSet = statement.executeQuery("select SUM(Price_of_WarehouseInvoice) as TotalMoney\n" +
                        "from WAREHOUSEINVOICE\n" +
                        "WHERE Supplier_ID =" + "'" + Supplier_ID + "'" + "\n" +
                        "GROUP BY Supplier_ID");
                while (resultSet.next()) {
                    total_money = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Connect to Warehouse makes error.", Toast.LENGTH_SHORT).show();
        }
        return total_money;
    }

    private void Update_TotalBill_TotalMoney(String SupplierID, Integer totalbill, Integer totalmoney) {
        if (connection_supplier != null) {
            try {
                Statement statement = connection_supplier.createStatement();
                Boolean resultSet = statement.execute("UPDATE SUPPLIER\n" +
                        "SET TotalBill=" + totalbill + "," + "TotalMoney=" + totalmoney + "\n" +
                        "WHERE Supplier_ID =" + "'" + SupplierID + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Connect to Supplier makes error.", Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowInvoicebySupplier() {
        supSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE); //set thêm color và textsize cho item khi bấm vào
                ((TextView) adapterView.getChildAt(0)).setTextSize(28);
                ArrayList<Warehouse> warehouselist_eachSupplier = new ArrayList<>();
                String Supplier_ID = Search(adapterView.getAdapter().getItem(i).toString().trim());
                for (int j = 0; j < warehousesList.size(); j++) {
                    if (warehousesList.get(j).getSupplier_ID().equals(Supplier_ID)) {
                        warehouselist_eachSupplier.add(warehousesList.get(j));
                    }
                }

                WarehouseAdapter adapter = new WarehouseAdapter(getApplicationContext(), 0, warehouselist_eachSupplier);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (signal_adding) {
                    Update_TotalBill_TotalMoney(Supplier_ID, warehouselist_eachSupplier.size(), Counting_total_money(Supplier_ID));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    protected void getAllSuppliers() {
        if (connection_supplier != null) {
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

    private void setUpOnClickListener() {
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Warehouse selectWarehouse = (Warehouse) (listView.getItemAtPosition(i));
            Intent showDetail = new Intent(getApplicationContext(), WarehouseDetail.class);
            showDetail.putExtra("id", selectWarehouse.getWarehouse_ID());
            startActivity(showDetail);
        });

    }
}