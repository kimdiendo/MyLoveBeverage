package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.WarehouseDetailAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.DetailOfWarehouse;
import com.example.mylovebeverage.Models.Warehouse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class WarehouseDetail extends AppCompatActivity {
    public static ArrayList<DetailOfWarehouse> whDetailList = new ArrayList<DetailOfWarehouse>();
    Warehouse selectedWH;
    Connecting_MSSQL connecting_mssql;
    Connection connection_getSup = null;
    Connection connection_WhDetail = null;
    DetailOfWarehouse detailOfWarehouse;
    ListView listView;
    ImageView arrow_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_detail);
        getSelectedWH();
        setValues();
        getwhDetail();
        setUpList();
        Back_previous_activity();

    }
    private void Back_previous_activity()
    {
        arrow_back = findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(view -> finish());
    }
    private void getwhDetail() {
        connecting_mssql = new Connecting_MSSQL(connection_WhDetail);
        connection_WhDetail = connecting_mssql.Connecting();
        if (connection_WhDetail != null) {
            try {
                whDetailList.clear();
                Statement statement = connection_WhDetail.createStatement();
                Statement statement2 = connection_WhDetail.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM [dbo].[DETAILWAREHOUSEINVOICE] WHERE WarehouseInvoice_ID = '" + selectedWH.getWarehouse_ID() + "';");
                while (resultSet.next()) {
                    ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM [dbo].[PRODUCT] WHERE Product_ID = '" + resultSet.getString(2).trim() + "';");
                    while (resultSet2.next()) {
                        detailOfWarehouse = new DetailOfWarehouse(resultSet.getString(1).trim(), resultSet.getString(2).trim(), resultSet2.getString(3).trim(), resultSet.getInt(3), resultSet.getInt(4), 0, resultSet.getInt(6), resultSet.getString(7).trim());
                    }
                    whDetailList.add(detailOfWarehouse);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Connect to DetailWarehouseDB makes error.", Toast.LENGTH_SHORT).show();
        }
    }

    void setUpList() {
        listView = findViewById(R.id.whDetailListView);
        WarehouseDetailAdapter warehouseDetailAdapter = new WarehouseDetailAdapter(getApplicationContext(), 0, whDetailList);
        listView.setAdapter(warehouseDetailAdapter);
        warehouseDetailAdapter.notifyDataSetChanged();

    }

    private void getSelectedWH() {
        Intent prevIntent = getIntent();
        String parseStringID = prevIntent.getStringExtra("id");

        for (int i = 0; i < ManageWareHouse.warehousesList.size(); i++) {
            if (ManageWareHouse.warehousesList.get(i).getWarehouse_ID().equals(parseStringID)) {
                selectedWH = ManageWareHouse.warehousesList.get(i);
                return;
            }
        }
    }

    private void setValues() {

        connecting_mssql = new Connecting_MSSQL(connection_getSup);
        connection_getSup = connecting_mssql.Connecting();
        String SupplierName = "A";
        if (connection_getSup != null) {
            try {
                Statement statement = connection_getSup.createStatement();
                ResultSet resultSet = statement.executeQuery("select *\n" +
                        "from [dbo].[SUPPLIER]\n" +
                        "where Supplier_ID = '" + selectedWH.getSupplier_ID() + "'");

                while (resultSet.next()) {
                    SupplierName = resultSet.getString(2).trim();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Connect to Supplier makes error.", Toast.LENGTH_SHORT).show();
        }
        TextView tv1 = findViewById(R.id.txtDisplayWHcode);
        TextView tv2 = findViewById(R.id.txtDisplaySupplier);
        TextView tv3 = findViewById(R.id.txtDisplayWHDate);
        TextView tv4 = findViewById(R.id.txtDisplayTotalPrice);
        tv1.setText(selectedWH.getWarehouse_ID());
        tv2.setText(SupplierName);
        tv3.setText(selectedWH.getDateTime().toString());
        tv4.setText(selectedWH.getPriceCustom() + " VND");
    }
}