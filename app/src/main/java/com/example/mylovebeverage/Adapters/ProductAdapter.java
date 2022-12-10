package com.example.mylovebeverage.Adapters;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.ManageProduct;
import com.example.mylovebeverage.Models.Product;
import com.example.mylovebeverage.R;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product>{

    public ProductAdapter(Context context, int resource, List<Product> productList)
    {
        super(context,resource,productList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_product_cell, parent, false);
        }
        TextView tv1 = (TextView) convertView.findViewById(R.id.product_productNameItem);
        TextView tv2 = (TextView) convertView.findViewById(R.id.product_productPrice);

        ImageView iv = (ImageView) convertView.findViewById(R.id.product_productImage);


        tv1.setText(product.getName_of_Product());
        tv2.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(product.getImage_Product()).into(iv);
        return convertView;
    }
}
