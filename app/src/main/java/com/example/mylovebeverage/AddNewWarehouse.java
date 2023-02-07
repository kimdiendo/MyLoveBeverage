package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.AddWarehouseAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Product;
import com.example.mylovebeverage.Models.ProductDataHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AddNewWarehouse extends AppCompatActivity {
    Spinner supSpinner;
    ListView listView;
    public static TextView totalWh;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_product = null;
    private static Connection connection_warehouse = null;
    private static Connection connection_detailWarehouse = null;
    private static Connection connection_supplier = null;
    private static Connection connection_supplier_2 = null;

    Product product;
    String selectedSupplier;
    public static ArrayList<String> productNameList = new ArrayList<>();
    public static ArrayList<ProductDataHolder> productHoldersList = new ArrayList<>();
    public static int TotalWH = 0;
    ImageView arrback;
    AddWarehouseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connecting_mssql = new Connecting_MSSQL(connection_product);
        connection_product = connecting_mssql.Connecting();

        connecting_mssql = new Connecting_MSSQL(connection_warehouse);
        connection_warehouse = connecting_mssql.Connecting();

        connecting_mssql = new Connecting_MSSQL(connection_detailWarehouse);
        connection_detailWarehouse = connecting_mssql.Connecting();

        connecting_mssql = new Connecting_MSSQL(connection_supplier);
        connection_supplier = connecting_mssql.Connecting();

        connecting_mssql = new Connecting_MSSQL(connection_supplier_2);
        connection_supplier_2 = connecting_mssql.Connecting();

        setContentView(R.layout.activity_add_new_warehouse);
        supSpinner = findViewById(R.id.warehouse_addNewWH_supplier);
        getAllProductName();
        setUpList();
        totalWh = findViewById(R.id.TotalWh);
        ArrayAdapter supplierAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ManageWareHouse.suppliersList);
        supplierAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        supSpinner.setAdapter(supplierAdapter);
        supSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) adapterView.getChildAt(0)).setTextSize(28);
                selectedSupplier = ManageWareHouse.suppliersList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   finish();
            }
        });
        Button cancelBtn = findViewById(R.id.cancelNewWH);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddNewWarehouse.this, productHoldersList.get(0).getProduct_ID() + " " + productHoldersList.get(0).getQuantity() + " " + productHoldersList.get(0).getUnit() + " " + productHoldersList.get(0).getIncome() + " " + productHoldersList.get(0).getProfit() + " " + productHoldersList.get(0).getOutcome(), Toast.LENGTH_SHORT).show();

            }
        });
        Button addWhBtn = findViewById(R.id.addNewWH);
        addWhBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String supID = "SP1";
                String WH_ID = "";
                int TotalBill = 0;
                int TotalMoney = 0;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                Integer numberOfWarehouse = 0;
                if (connection_supplier != null) {
                    try {
                        Statement statement = connection_supplier.createStatement();
                        ResultSet resultSet = statement.executeQuery("select Supplier_ID from [dbo].[SUPPLIER]\n" +
                                "where Name_of_supplier = '"+selectedSupplier+"';");
                        while (resultSet.next()) {
                            supID = resultSet.getString(1);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Connect to SupplierDB makes error.", Toast.LENGTH_SHORT).show();
                }
                if (connection_warehouse != null) {
                    try {
                        Statement statement = connection_warehouse.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT COUNT(WarehouseInvoice_ID)\n" +
                                "FROM [dbo].[WAREHOUSEINVOICE];");
                        while (resultSet.next()) {
                            numberOfWarehouse = resultSet.getInt(1) + 1;
                        }
                        if (numberOfWarehouse<10) {
                            WH_ID = "WH00" + numberOfWarehouse;
                        }
                        else if (numberOfWarehouse<100) {
                            WH_ID = "WH0" + numberOfWarehouse;
                        }
                        else {
                            WH_ID = "WH" + numberOfWarehouse;
                        }
                        Statement statement2 = connection_warehouse.createStatement();
                        ResultSet resultSet2 = statement2.executeQuery("INSERT INTO [dbo].[WAREHOUSEINVOICE] (WarehouseInvoice_ID, Staff_ID, Supplier_ID, DateTime_of_WarehouseInvoice, Price_of_WarehouseInvoice)\n" +
                                "VALUES ('"+WH_ID+"', 'NV01', '"+supID+"', '"+dtf.format(now)+"', '"+TotalWH+"');");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Connect to WarehouseDB makes error.", Toast.LENGTH_SHORT).show();
                }
                for (ProductDataHolder productDataHolder : productHoldersList) {
                    if (connection_detailWarehouse!=null) {
                        try {
                            Statement statement = connection_detailWarehouse.createStatement();
                            ResultSet resultSet = statement.executeQuery("INSERT INTO [dbo].[DETAILWAREHOUSEINVOICE] (WarehouseInvoice_ID, Product_ID, Quantity, IncomePrice, OutcomePrice, Profit, Unit)\n" +
                                    "VALUES ('"+WH_ID+"', '"+productDataHolder.getProduct_ID()+"', '"+productDataHolder.getQuantity()+"', '"+productDataHolder.getIncome()+"', '+"+productDataHolder.getOutcome()+"', '"+productDataHolder.getProfit()+"', '"+productDataHolder.getUnit()+"');");
                        }
                        catch (SQLException e) {
//                            Toast.makeText(AddNewWarehouse.this, "Connect to DetailWarehouseDB makes error." , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                if (connection_supplier_2 != null) {
                    try {
                        Statement statement = connection_supplier_2.createStatement();
                        ResultSet resultSet = statement.executeQuery("select * from [dbo].[SUPPLIER] where Supplier_ID = '"+supID+"'");
                        while (resultSet.next()) {
                            TotalBill = resultSet.getInt(8) + 1;
                            TotalMoney = resultSet.getInt(9) + TotalWH;
                        }
                        Statement statement1 = connection_supplier_2.createStatement();
                        ResultSet resultSet1 = statement1.executeQuery("update [dbo].[SUPPLIER]\n" +
                                "set TotalBill = "+TotalBill+"\n" +", TotalMoney= "+TotalMoney+
                                " where Supplier_ID = '"+supID+"'");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Connect to SupplierDB makes error.", Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent();
                intent.putExtra("editTextValue", "value_here");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void getAllProductName() {
        if (connection_product != null) {
            try {
                Statement statement = connection_product.createStatement();
                ResultSet resultSet = statement.executeQuery("select Name_of_Product from [dbo].[PRODUCT] where Status = 'active';");
                productNameList.clear();
                while (resultSet.next()) {
                    productNameList.add(resultSet.getString(1).trim());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpList() {
        productHoldersList.clear();
        ProductDataHolder data1 = new ProductDataHolder(this, "", "", "", 0, 0, 0, 0, "");
        ProductDataHolder data2 = new ProductDataHolder(this, "", "", "", 0, 0, 0, 0, "");
        ProductDataHolder data3 = new ProductDataHolder(this, "", "", "", 0, 0, 0, 0, "");
        ProductDataHolder data4 = new ProductDataHolder(this, "", "", "", 0, 0, 0, 0, "");
        ProductDataHolder data5 = new ProductDataHolder(this, "", "", "", 0, 0, 0, 0, "");
        productHoldersList.add(data1);
        productHoldersList.add(data2);
        productHoldersList.add(data3);
        productHoldersList.add(data4);
        productHoldersList.add(data5);
        listView = findViewById(R.id.warehouse_addDetailWH);
        adapter = new AddWarehouseAdapter(this, R.layout.activity_add_new_warehouse_cell, productHoldersList);
        listView.setAdapter(adapter);
    }

}

