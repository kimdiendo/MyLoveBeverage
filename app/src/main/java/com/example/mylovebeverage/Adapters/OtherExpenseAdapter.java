package com.example.mylovebeverage.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovebeverage.Models.Invoice_Other;
import com.example.mylovebeverage.R;

import java.util.ArrayList;

public class OtherExpenseAdapter extends BaseAdapter {
    final ArrayList<Invoice_Other> invoice_otherArrayList;
    public OtherExpenseAdapter(ArrayList<Invoice_Other>invoice_otherArrayList) {
           this.invoice_otherArrayList = invoice_otherArrayList;
    }
    @Override
    public int getCount() {
        return invoice_otherArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return  invoice_otherArrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder
    {
        TextView edt1 , edt2 , edt3;
        TextView txt_date_invoice  , txt_invoice_ID , txt_invoice_price , txt_invoice_status;
        ImageView img; //icon salary
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewproduct ;//View hien thi san pham tu arrayList
        OtherExpenseAdapter.ViewHolder viewHolder ;//lưu thông tin ánh xạ
        if(view == null)
        {
            //viewproduct= View.inflate(viewGroup.getContext() , R.layout.actionn jj njn , null);
            viewproduct = View.inflate(viewGroup.getContext(), R.layout.expense_invoice_cell, null );
            viewHolder = new OtherExpenseAdapter.ViewHolder();
            viewHolder.edt1 =(TextView)viewproduct.findViewById(R.id.edit1);
            viewHolder.edt2 =(TextView)viewproduct.findViewById(R.id.edit2);
            viewHolder.edt3 =(TextView)viewproduct.findViewById(R.id.edit3);
            viewHolder.img = (ImageView)viewproduct.findViewById(R.id.img_icon);
            viewHolder.txt_date_invoice = (TextView) viewproduct.findViewById(R.id.datetime_of_invoice);
            viewHolder.txt_invoice_ID = (TextView) viewproduct.findViewById(R.id.ID);
            viewHolder.txt_invoice_price = (TextView) viewproduct.findViewById(R.id.salary);
            viewHolder.txt_invoice_status = (TextView) viewproduct.findViewById(R.id.Status);
            viewproduct.setTag(viewHolder); // Tạo tag để nắm viewholder mà lưu trữ các thông tin ánh xạ để dùng cho lần sau.

        }else
        {
            viewproduct = view;
            viewHolder = (OtherExpenseAdapter.ViewHolder) viewproduct.getTag();
        }
        Invoice_Other product = (Invoice_Other) getItem(position);
        viewHolder.img.setImageResource(product.getIcon());
        viewHolder.edt1.setText("ID");
        viewHolder.edt2.setText("Price");
        viewHolder.edt3.setText("Status");
        viewHolder.txt_date_invoice.setText(product.getPaydate());
        viewHolder.txt_invoice_ID.setText(product.getID_invoice());
        viewHolder.txt_invoice_price.setText(String.valueOf(product.getPrice()));
        viewHolder.txt_invoice_status.setText(product.getStatus());
        return viewproduct;
    }
    }