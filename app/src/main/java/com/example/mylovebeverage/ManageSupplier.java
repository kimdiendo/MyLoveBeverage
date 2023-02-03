package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.SupplierAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Supplier;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ManageSupplier extends AppCompatActivity {

    private ListView listView;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_supplier = null;
    ImageView arrback;

    Supplier supplier;
    public static ArrayList<Supplier> suppliersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_supplier);
        connecting_mssql = new Connecting_MSSQL(connection_supplier);
        connection_supplier = connecting_mssql.Connecting();
        getAllSuppliers();
        setUpList();
        setUpOnClickListener();
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FloatingActionButton addSuppliertBtn = findViewById(R.id.addSupplier);
        addSuppliertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewSupplier.class);
//                startActivity(intent);
                startActivityForResult(intent, 1);
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
                    supplier = new Supplier(resultSet.getString(1).trim(),resultSet.getString(2).trim(), resultSet.getString(3).trim(), resultSet.getString(4).trim(), resultSet.getString(5).trim(), resultSet.getString(7).trim(),resultSet.getInt(8), resultSet.getInt(9));
                    suppliersList.add(supplier);
                }

            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
        }
    }
    private void setUpList() {
        listView = findViewById(R.id.suppliersListView);
        SupplierAdapter adapter = new SupplierAdapter(getApplicationContext(), 0, suppliersList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void setUpOnClickListener() {

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Supplier selectSupplier = (Supplier) (listView.getItemAtPosition(i));
            Intent showDetail = new Intent(getApplicationContext(), SupplierDetail.class);
            showDetail.putExtra("id",selectSupplier.getSupplier_ID());
            startActivityForResult(showDetail, 1);
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                getAllSuppliers();
                setUpList();
            }
        }
    }
}