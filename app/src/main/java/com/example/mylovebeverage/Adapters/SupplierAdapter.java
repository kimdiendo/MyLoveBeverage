package com.example.mylovebeverage.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovebeverage.Models.Supplier;
import com.example.mylovebeverage.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SupplierAdapter extends ArrayAdapter<Supplier>{

    public SupplierAdapter(Context context, int resource, List<Supplier> suppliersList)
    {
        super(context,resource,suppliersList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Supplier supplier = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_supplier_cell, parent, false);
        }
        TextView tv1 = convertView.findViewById(R.id.supplier_supplierNameItem);
        TextView tv2 = convertView.findViewById(R.id.supplier_supplierAddress);

        ImageView iv = convertView.findViewById(R.id.supplier_supplierLogo);


        tv1.setText(supplier.getName_of_supplier());
        tv2.setText(supplier.getAddress());
        Picasso.get().load(supplier.getLogo()).into(iv);
        return convertView;
    }
}
