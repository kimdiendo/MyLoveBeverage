package com.example.mylovebeverage.Adapters;

import static com.example.mylovebeverage.Order.productArrayList2;
import static com.example.mylovebeverage.Order.totalOrderBill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Models.Invoice;
import com.example.mylovebeverage.Models.Product;
import com.example.mylovebeverage.Order;
import com.example.mylovebeverage.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailAdapter extends ArrayAdapter<Product>
{
    public static Integer totalPrice;

    public OrderDetailAdapter(Context context, int resource, List<Product> productList)
    {
        super(context,resource,productList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Product product = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_order_detail_cell, parent, false);
        }

        TextView tv1 = convertView.findViewById(R.id.txtOrderDetailProductName);
        Button btn1 = convertView.findViewById(R.id.btnOrderDetailDecreaseAmount);
        EditText editText = convertView.findViewById(R.id.editTextOrderDetailProductAmount);
        Button btn2 = convertView.findViewById(R.id.btnOrderDetailIncreaseAmount);
        TextView tv3 = convertView.findViewById(R.id.txtOrderDetailProductTotalPrice);

        tv1.setText(product.getName_of_Product());
        editText.setText(product.getOrderAmount().toString());

        totalPrice = product.getPrice() * product.getOrderAmount();
        tv3.setText(getPriceCustom(totalPrice));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((product.getOrderAmount() - 1) != 0){
                    product.setOrderAmount(product.getOrderAmount() - 1);
                    editText.setText(product.getOrderAmount().toString());
                    totalPrice = product.getPrice() * product.getOrderAmount();
                    tv3.setText(getPriceCustom(totalPrice));
                    totalOrderBill -= product.getPrice();
                    Order.txtTotalOrder.setText(getPriceCustom(totalOrderBill) + " VND");
                }
                else{
                    Order.productArrayList2.remove(Order.productArrayList2.get(position));
                    Order.orderDetailAdapter.notifyDataSetChanged();
                    totalOrderBill -= product.getPrice();
                    Order.txtTotalOrder.setText(getPriceCustom(totalOrderBill) + " VND");
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Integer amount = Integer.parseInt(editText.getText().toString()) + 1;
                product.setOrderAmount(product.getOrderAmount() + 1);
                editText.setText(product.getOrderAmount().toString());
                totalPrice = product.getPrice() * product.getOrderAmount();
                tv3.setText(getPriceCustom(totalPrice));
                totalOrderBill += product.getPrice();
                Order.txtTotalOrder.setText(getPriceCustom(totalOrderBill) + " VND");
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    product.setOrderAmount(Integer.parseInt(editText.getText().toString()));
                    totalPrice = product.getPrice() * product.getOrderAmount();
                    tv3.setText(getPriceCustom(totalPrice));
                    totalOrderBill = 0;
                    for (Product product1 : productArrayList2){
                        totalOrderBill += product1.getPrice() * product1.getOrderAmount();
                    }
                    Order.txtTotalOrder.setText(getPriceCustom(totalOrderBill) + " VND");
                }
            }
        });

        return convertView;
    }

    public static Integer getTotalPrice() {
        return totalPrice;
    }

    public static void setTotalPrice(Integer totalPrice) {
        OrderDetailAdapter.totalPrice = totalPrice;
    }

    public String getPriceCustom(Integer b){
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(b);
        return a;
    }
}