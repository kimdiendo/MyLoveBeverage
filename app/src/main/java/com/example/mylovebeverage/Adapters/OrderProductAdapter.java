package com.example.mylovebeverage.Adapters;

import static com.example.mylovebeverage.Order.totalOrderBill;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.mylovebeverage.Connecting_MSSQL;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Product;
import com.example.mylovebeverage.Order;
import com.example.mylovebeverage.R;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderProductAdapter extends BaseAdapter
{
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_product = null;
    Product product;

    Context context;
    int layout;
    List<Product> productList;

    LayoutInflater inflater;

    public OrderProductAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class VỉewHolder{
        ImageView imgProduct;
        TextView txtProductName;
        TextView txtProductPrice;
        Button btnOrderProduct;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        connecting_mssql = new Connecting_MSSQL(connection_product);
        connection_product = connecting_mssql.Connecting();

        VỉewHolder vỉewHolder;
        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null){
            convertView = inflater.inflate(layout, null);
            vỉewHolder = new VỉewHolder();

            vỉewHolder.imgProduct = convertView.findViewById(R.id.imgOrderProductImage);
            vỉewHolder.txtProductName = convertView.findViewById(R.id.txtOrderProductName);
            vỉewHolder.txtProductPrice = convertView.findViewById(R.id.txtOrderProductPrice);
            vỉewHolder.btnOrderProduct = convertView.findViewById(R.id.btnOrderProduct);

            convertView.setTag(vỉewHolder);
        }
        else {
            vỉewHolder = (VỉewHolder) convertView.getTag();
        }

        //imgProduct.setImageResource(productImage[position]);
        Picasso.get().load(productList.get(position).getImage_Product()).into(vỉewHolder.imgProduct);
        vỉewHolder.txtProductName.setText(productList.get(position).getName_of_Product());
        vỉewHolder.txtProductPrice.setText(productList.get(position).getPriceCustom() + " VND");
        vỉewHolder.btnOrderProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderProductDetail(productList.get(position).getProduct_ID());
            }
        });

        return convertView;
    }
    public void getOrderProductDetail(String a){
        boolean flag = false;
        for(Product product2 : Order.productArrayList2){
            if (a.equals(product2.getProduct_ID())){
                flag = true;
                break;
            }
        }
        if (flag == true){
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_custom_dialog_2);
            TextView txtMessage = dialog.findViewById(R.id.txtCustomDialog2Message);
            txtMessage.setText("This product has already been added in the current order!");
            dialog.show();
            Button btnOk = dialog.findViewById(R.id.btnCustomDialog2Ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        else {
            if (connection_product!=null)
            {
                try {
                    Statement statement = connection_product.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT WHERE Product_ID = '" + a + "';");
                    while (resultSet.next()) {
                        product = new Product(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getString(3).trim(),resultSet.getString(4).trim(),resultSet.getInt(7),resultSet.getString(8).trim(),resultSet.getInt(5),resultSet.getString(6));
                        Order.productArrayList2.add(product);
                        Order.orderDetailAdapter.notifyDataSetChanged();
                        totalOrderBill += product.getPrice();
                        Order.txtTotalOrder.setText(getPriceCustom(totalOrderBill) + " VND");
                    }
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(context, "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getPriceCustom(Integer b){
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        String a = en.format(b);
        return a;
    }
}