package com.example.mylovebeverage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mylovebeverage.Models.Invoice;
import com.example.mylovebeverage.R;

import java.util.List;

public class InvoiceAdapter extends ArrayAdapter<Invoice>
{
    public InvoiceAdapter(Context context, int resource, List<Invoice> invoiceList)
    {
        super(context,resource,invoiceList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Invoice invoice = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_invoice_cell, parent, false);
        }

        TextView tv1 = convertView.findViewById(R.id.txtInvoiceCode);
        TextView tv2 = convertView.findViewById(R.id.txtInvoicePrice);
        TextView tv3 = convertView.findViewById(R.id.txtInvoiceDate);

        tv1.setText(invoice.getInvoice_ID());
        tv2.setText("Total: " + invoice.getPriceCustom() + " VND");
        tv3.setText(invoice.getDateTime_Invoice());

        return convertView;
    }
}
