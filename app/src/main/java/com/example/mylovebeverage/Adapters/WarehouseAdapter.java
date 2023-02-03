package com.example.mylovebeverage.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mylovebeverage.Models.Warehouse;
import com.example.mylovebeverage.R;

import java.util.List;

public class WarehouseAdapter extends ArrayAdapter<Warehouse>{

    public WarehouseAdapter(Context context, int resource, List<Warehouse> warehouseList)
    {
        super(context,resource,warehouseList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Warehouse warehouse = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_warehouse_cell, parent, false);
        }
        TextView tv1 = convertView.findViewById(R.id.warehouse_WarehouseIDItem);
        TextView tv2 = convertView.findViewById(R.id.warehouse_WarehouseDateItem);

        tv1.setText(warehouse.getWarehouse_ID());
        tv2.setText(String.valueOf(warehouse.getDateTime()));
        return convertView;
    }
}
