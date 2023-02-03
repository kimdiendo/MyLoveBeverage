package com.example.mylovebeverage.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovebeverage.Models.Product;
import com.example.mylovebeverage.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product>{

    public ProductAdapter(Context context, int resource, List<Product> productList)
    {
        super(context,resource,productList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_product_cell, parent, false);
        }
        TextView tv1 = convertView.findViewById(R.id.product_productNameItem);
        TextView tv2 = convertView.findViewById(R.id.product_productPrice);

        ImageView iv = convertView.findViewById(R.id.product_productImage);


        tv1.setText(product.getName_of_Product());
        tv2.setText(product.getPriceCustom() + " VND");
        Picasso.get().load(product.getImage_Product()).into(iv);
        return convertView;
    }
}
