package com.example.mylovebeverage.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovebeverage.Models.Salary_Staff_Expense_Invoice;
import com.example.mylovebeverage.R;
import java.util.ArrayList;

public class StaffSalaryAdapter extends BaseAdapter
{
    final ArrayList<Salary_Staff_Expense_Invoice> invoiceSalaryStaffArrayList;
    public StaffSalaryAdapter(ArrayList<Salary_Staff_Expense_Invoice> invoiceSalaryStaffArrayList) {
           this.invoiceSalaryStaffArrayList = invoiceSalaryStaffArrayList;
    }
    @Override
    public int getCount() {
        return invoiceSalaryStaffArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return invoiceSalaryStaffArrayList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private  class ViewHolder
    {
        TextView edt1 , edt2 , edt3;
        TextView txt_date_invoice  , txt_invoice_ID , txt_invoice_salary , txt_invoice_status;
        ImageView img; //icon salary
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        View viewproduct ;//View hien thi san pham tu arrayList
        StaffSalaryAdapter.ViewHolder viewHolder ;//lưu thông tin ánh xạ
        if(view == null)
        {
            //viewproduct= View.inflate(viewGroup.getContext() , R.layout.actionn jj njn , null);
            viewproduct = View.inflate(viewGroup.getContext(), R.layout.expense_invoice_cell, null);
            viewHolder = new StaffSalaryAdapter.ViewHolder();
            viewHolder.edt1 = viewproduct.findViewById(R.id.edit1);
            viewHolder.edt2 = viewproduct.findViewById(R.id.edit2);
            viewHolder.edt3 = viewproduct.findViewById(R.id.edit3);
            viewHolder.img = viewproduct.findViewById(R.id.img_icon);
            viewHolder.txt_date_invoice = viewproduct.findViewById(R.id.datetime_of_invoice);
            viewHolder.txt_invoice_ID = viewproduct.findViewById(R.id.ID);
            viewHolder.txt_invoice_salary = viewproduct.findViewById(R.id.salary);
            viewHolder.txt_invoice_status = viewproduct.findViewById(R.id.Status);
            viewproduct.setTag(viewHolder); // Tạo tag để nắm viewholder mà lưu trữ các thông tin ánh xạ để dùng cho lần sau.

        }else
        {
            viewproduct = view;
            viewHolder = (StaffSalaryAdapter.ViewHolder) viewproduct.getTag();
        }
        Salary_Staff_Expense_Invoice product = (Salary_Staff_Expense_Invoice) getItem(position);
        viewHolder.img.setImageResource(R.drawable.salary_icon);
        viewHolder.edt1.setText("ID");
        viewHolder.edt2.setText("Salary");
        viewHolder.edt3.setText("Status");
        viewHolder.txt_date_invoice.setText(product.getPaydate());
        viewHolder.txt_invoice_ID.setText(product.getID_invoice());
        viewHolder.txt_invoice_salary.setText(String.valueOf(product.getPrice()));
        viewHolder.txt_invoice_status.setText(product.getStatus());
        return viewproduct;
    }
    }
