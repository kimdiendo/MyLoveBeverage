package com.example.mylovebeverage.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.AddNewWarehouse;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.ProductDataHolder;
import com.example.mylovebeverage.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AddWarehouseAdapter extends ArrayAdapter<ProductDataHolder>{
    private final Activity mContext;
    private static Connection connection_productID = null;
    Connecting_MSSQL connecting_mssql= new Connecting_MSSQL(connection_productID);

    String ProductID="";

    public AddWarehouseAdapter(Activity context, int resource, ArrayList<ProductDataHolder> objects)
    {
        super(context,resource,objects);
        mContext = context;
    }

    static class ViewHolder {
        protected ProductDataHolder data;
        protected EditText edt1;
        protected EditText edt2;
        protected EditText edt3;
        protected EditText edt4;
        protected TextView edt5;
        protected AutoCompleteTextView atctv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        connection_productID = connecting_mssql.Connecting();
        View view = null;
        ProductDataHolder productDataHolder = getItem(position);
        if (convertView==null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(R.layout.activity_add_new_warehouse_cell, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.edt1 = view.findViewById(R.id.warehouse_quantity);
            viewHolder.edt1.setText("0");
            viewHolder.edt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        productDataHolder.setQuantity(Integer.parseInt(viewHolder.edt1.getText().toString()));
                        setTotalWH();
                        AddNewWarehouse.totalWh.setText(AddNewWarehouse.TotalWH + "");
                    }
                }
            });
            viewHolder.edt2 = view.findViewById(R.id.warehouse_unit);
            viewHolder.edt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        productDataHolder.setUnit(viewHolder.edt2.getText().toString());
                    }
                }
            });
            viewHolder.edt3 = view.findViewById(R.id.warehouse_inprice);
            viewHolder.edt3.setText("0");
            viewHolder.edt3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        productDataHolder.setIncome(Integer.parseInt(viewHolder.edt3.getText().toString()));
                        viewHolder.edt5.setText(String.valueOf(productDataHolder.getIncome() + productDataHolder.getIncome() * productDataHolder.getProfit() / 100));
                        productDataHolder.setOutcome(Integer.parseInt(viewHolder.edt5.getText().toString()));
                        setTotalWH();
                        AddNewWarehouse.totalWh.setText(AddNewWarehouse.TotalWH + "");
                    }
                }
            });
            viewHolder.edt4 = view.findViewById(R.id.warehouse_profit);
            viewHolder.edt4.setText("0");
            viewHolder.edt4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        productDataHolder.setProfit(Integer.parseInt(viewHolder.edt4.getText().toString()));
                        viewHolder.edt5.setText(String.valueOf(productDataHolder.getIncome() + productDataHolder.getIncome() * productDataHolder.getProfit() / 100));
                        productDataHolder.setOutcome(Integer.parseInt(viewHolder.edt5.getText().toString()));
                    }
                }
            });
            viewHolder.edt5 = view.findViewById(R.id.warehouse_outprice);
            viewHolder.edt5.setText("0");
            viewHolder.data = new ProductDataHolder(mContext,"WH1", "PD01","Coca",Integer.parseInt(viewHolder.edt1.getText().toString()),Integer.parseInt(viewHolder.edt3.getText().toString()),0,Integer.parseInt(viewHolder.edt4.getText().toString()),viewHolder.edt2.getText().toString());
            viewHolder.atctv = view.findViewById(R.id.warehouse_ProductName);
            viewHolder.atctv.setAdapter(viewHolder.data.getAdapter());
            viewHolder.atctv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        productDataHolder.setProduct_Name(viewHolder.atctv.getText().toString());
                        getProductID(productDataHolder.getProduct_Name());
                        productDataHolder.setProduct_ID(ProductID);
                    }
                }
            });

        } else {
            view = convertView;
        }
        return view;
    }
    private void getProductID(String productName) {
        if (connection_productID!=null)
        {
            try {
                Statement statement = connection_productID.createStatement();
                ResultSet resultSet = statement.executeQuery("select Product_ID from [dbo].[PRODUCT] where Name_of_Product = '"+productName+"';");
                while (resultSet.next()) {
                    ProductID = resultSet.getString(1).trim();
                }
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(mContext, "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
        }
    }
    private void setTotalWH() {
        AddNewWarehouse.TotalWH = 0;
        for (ProductDataHolder productDataHolder1: AddNewWarehouse.productHoldersList){
            AddNewWarehouse.TotalWH += productDataHolder1.getIncome()* productDataHolder1.getQuantity();
        }
    }
}
