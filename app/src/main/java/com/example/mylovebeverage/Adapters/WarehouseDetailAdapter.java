package com.example.mylovebeverage.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mylovebeverage.Models.DetailOfWarehouse;
import com.example.mylovebeverage.R;

import java.util.List;

public class WarehouseDetailAdapter extends ArrayAdapter<DetailOfWarehouse>{

    public WarehouseDetailAdapter(Context context, int resource, List<DetailOfWarehouse> whDetailList)
    {
        super(context,resource,whDetailList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetailOfWarehouse whDetail = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_warehouse_detail_cell, parent, false);
        }
        TextView tv1 = convertView.findViewById(R.id.wh_detail_cell_ProductName);
        TextView tv2 = convertView.findViewById(R.id.wh_detail_cell_ProductAmount);
        TextView tv3 = convertView.findViewById(R.id.wh_detail_cell_ProductInprice);
        TextView tv4 = convertView.findViewById(R.id.wh_detail_cell_ProductProfit);

        tv1.setText(whDetail.getProduct_Name());
        tv2.setText(String.valueOf(whDetail.getQuantity()));
        tv3.setText(whDetail.getIncomeCustom());
        tv4.setText(whDetail.getProfit() + "%");
        return convertView;
    }
}
