package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Product;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDetail extends AppCompatActivity {
    Connecting_MSSQL connecting_mssql;
    Connection connection_deleteProduct = null;
    Connection connection_updateProduct = null;
    ImageView arrback;
    Product selectedProduct;
    EditText tv1;
    EditText tv2;
    EditText tv3;
    EditText tv4;
    EditText tv6;
    EditText tv8;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        tv1 = findViewById(R.id.product_productName);
        tv2 = findViewById(R.id.product_categoryName);
        tv3 = findViewById(R.id.product_productBrand);
        tv4 = findViewById(R.id.product_productPrice);
        tv6 = findViewById(R.id.product_productUnit);
        tv8 = findViewById(R.id.product_productQuantity);
        getSelectedProduct();
        setValues();

        btn1 = findViewById(R.id.deleteProduct);
        btn2 = findViewById(R.id.updateProduct);
        btn3 = findViewById(R.id.saveChangeProduct);
        btn4 = findViewById(R.id.cancelChangeProduct);
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ProductDetail.this);
                dialog.setContentView(R.layout.activity_custom_dialog);
                Button btnYes = dialog.findViewById(R.id.btnCustomDialogYes);
                Button btnNo = dialog.findViewById(R.id.btnCustomDialogNo);
                TextView msgDialog = dialog.findViewById(R.id.txtCustomDialogMessage);
                msgDialog.setText("Do you want to completely delete this product?");
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        connecting_mssql = new Connecting_MSSQL(connection_deleteProduct);
                        connection_deleteProduct = connecting_mssql.Connecting();

                        if (connection_deleteProduct != null) {
                            try {
                                Statement statement = connection_deleteProduct.createStatement();
                                ResultSet resultSet = statement.executeQuery("update [dbo].[PRODUCT]\n" +
                                        "set Status = 'inactive'\n" +
                                        "where Product_ID = '"+selectedProduct.getProduct_ID()+"'");
                            }catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("editTextValue", "value_here");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Edit Product." , Toast.LENGTH_SHORT).show();
                tv1.setFocusableInTouchMode(true);
                tv4.setFocusableInTouchMode(true);
                tv6.setFocusableInTouchMode(true);
                tv8.setFocusableInTouchMode(true);
                btn1.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.INVISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);

                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), tv1.getText() , Toast.LENGTH_SHORT).show();
                        connecting_mssql = new Connecting_MSSQL(connection_updateProduct);
                        connection_updateProduct = connecting_mssql.Connecting();
                        if (connection_updateProduct!=null)
                        {
                            try {
                                Statement statement = connection_updateProduct.createStatement();
                                ResultSet resultSet = statement.executeQuery("update [dbo].[PRODUCT]\n" +
                                        "set Name_of_Product = '" +tv1.getText()+"', " + "Quantity = '" + tv8.getText()+"', " +"OutPrice='"+tv4.getText()+"', "+"Unit='"+tv6.getText()+"'\n"+
                                        "where Product_ID = '"+selectedProduct.getProduct_ID()+"'");
                            }catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
                        }
                        tv1.setFocusableInTouchMode(false);
                        tv4.setFocusableInTouchMode(false);
                        tv6.setFocusableInTouchMode(false);
                        tv8.setFocusableInTouchMode(false);
                        tv1.clearFocus();
                        tv4.clearFocus();
                        tv6.clearFocus();
                        tv8.clearFocus();
                        btn3.setVisibility(View.INVISIBLE);
                        btn4.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                        Intent intent = new Intent();
                        intent.putExtra("editTextValue", "value_here");
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                });

                btn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv1.setFocusableInTouchMode(false);
                        tv4.setFocusableInTouchMode(false);
                        tv6.setFocusableInTouchMode(false);
                        tv8.setFocusableInTouchMode(false);
                        tv1.clearFocus();
                        tv4.clearFocus();
                        tv6.clearFocus();
                        tv8.clearFocus();
                        btn3.setVisibility(View.INVISIBLE);
                        btn4.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

    }
    private void getSelectedProduct() {
        Intent prevIntent = getIntent();
        String parseStringID = prevIntent.getStringExtra("id");

        for (int i = 0; i <  ManageProduct.productList.size(); i++) {
            if(ManageProduct.productList.get(i).getProduct_ID().equals(parseStringID)) {
                selectedProduct = ManageProduct.productList.get(i);
                return;
            }
        }
    }
    private void setValues() {


        ImageView iv = findViewById(R.id.product_productImg);
        tv1.setText(selectedProduct.getName_of_Product());
        tv2.setText(selectedProduct.getCategory_ID());
        tv3.setText(selectedProduct.getBranding());
        tv4.setText(String.valueOf(selectedProduct.getPrice()));
        tv6.setText(selectedProduct.getUnit());
        tv8.setText(String.valueOf(selectedProduct.getQuantity()));
        Picasso.get().load(selectedProduct.getImage_Product()).into(iv);
    }
}