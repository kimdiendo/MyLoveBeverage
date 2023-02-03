package com.example.mylovebeverage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mylovebeverage.Models.DetailOfInvoice;
import com.example.mylovebeverage.R;

import java.util.List;

public class InvoiceDetailAdapter extends ArrayAdapter<DetailOfInvoice>
{
    public InvoiceDetailAdapter(Context context, int resource, List<DetailOfInvoice> invoiceDetailList)
    {
        super(context,resource,invoiceDetailList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DetailOfInvoice invoiceDetail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_invoice_detail_cell, parent, false);
        }

        TextView tv1 = convertView.findViewById(R.id.txtProductName);
        TextView tv3 = convertView.findViewById(R.id.txtProductAmount);
        TextView tv4 = convertView.findViewById(R.id.txtProductUnit);
        TextView tv5 = convertView.findViewById(R.id.txtProductPrice);

        tv1.setText(invoiceDetail.getProductName());
        tv3.setText("x" + invoiceDetail.getQuantity().toString());
        tv4.setText(invoiceDetail.getUnit());
        tv5.setText(invoiceDetail.getPrice());

        return convertView;
    }
}
