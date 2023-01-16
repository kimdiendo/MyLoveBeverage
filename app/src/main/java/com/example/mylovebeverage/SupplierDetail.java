package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mylovebeverage.Models.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Supplier;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SupplierDetail extends AppCompatActivity {
    Connecting_MSSQL connecting_mssql;
    Connection connection_deleteSupplier = null;
    Connection connection_updateSupplier = null;
    ImageView arrback;
    Supplier selectedSupplier;
    EditText tv1;
    EditText tv2;
    EditText tv3;
    EditText tv4;
    EditText tv5;
    EditText tv6;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);
        arrback = (ImageView) findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv1 = (EditText) findViewById(R.id.supplier_supplierName);
        tv2 = (EditText) findViewById(R.id.supplier_supplierAddress);
        tv3 = (EditText) findViewById(R.id.supplier_supplierEmail);
        tv4 = (EditText) findViewById(R.id.supplier_supplierPhoneNumber);
        tv5 = (EditText) findViewById(R.id.supplier_supplierBill);
        tv6 = (EditText) findViewById(R.id.supplier_supplierMoney);

        getSelectedSupplier();
        setValues();

        btn1 = (Button) findViewById(R.id.deleteSupplier);
        btn2 = (Button) findViewById(R.id.updateSupplier);
        btn3 = (Button) findViewById(R.id.saveChangeSupplier);
        btn4 = (Button) findViewById(R.id.cancelChangeSupplier);
        arrback = (ImageView) findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(SupplierDetail.this);
                dialog.setContentView(R.layout.activity_supplier_delete_dialog);
                Button btnYes = (Button) dialog.findViewById(R.id.yesDeleteBtn);
                Button btnNo = (Button) dialog.findViewById(R.id.noDeleteBtn);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        connecting_mssql = new Connecting_MSSQL(connection_deleteSupplier);
                        connection_deleteSupplier = connecting_mssql.Connecting();

                        if (connection_deleteSupplier!=null)
                        {
                            try {
                                Statement statement = connection_deleteSupplier.createStatement();
                                ResultSet resultSet = statement.executeQuery("update [dbo].[Supplier]\n" +
                                        "set Status = 'inactive'\n" +
                                        "where Supplier_ID = '"+selectedSupplier.getSupplier_ID()+"'");
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
                tv2.setFocusableInTouchMode(true);
                tv3.setFocusableInTouchMode(true);
                tv4.setFocusableInTouchMode(true);
                btn1.setVisibility(View.INVISIBLE);
                btn2.setVisibility(View.INVISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);

                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), tv1.getText() , Toast.LENGTH_SHORT).show();
                        connecting_mssql = new Connecting_MSSQL(connection_updateSupplier);
                        connection_updateSupplier = connecting_mssql.Connecting();
                        if (connection_updateSupplier!=null)
                        {
                            try {
                                Statement statement = connection_updateSupplier.createStatement();
                                ResultSet resultSet = statement.executeQuery("update [dbo].[SUPPLIER]\n" +
                                        "set Name_of_supplier = '" +tv1.getText()+"', " + "Address = '" + tv2.getText()+"', " +"Email='"+tv3.getText()+"', "+"PhoneNumber='"+tv4.getText()+"'\n"+
                                        "where Supplier_ID = '"+selectedSupplier.getSupplier_ID()+"'");
                            }catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
                        }
                        tv1.setFocusableInTouchMode(false);
                        tv2.setFocusableInTouchMode(false);
                        tv3.setFocusableInTouchMode(false);
                        tv4.setFocusableInTouchMode(false);
                        tv1.clearFocus();
                        tv2.clearFocus();
                        tv3.clearFocus();
                        tv4.clearFocus();
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
                        tv2.setFocusableInTouchMode(false);
                        tv3.setFocusableInTouchMode(false);
                        tv4.setFocusableInTouchMode(false);
                        tv1.clearFocus();
                        tv2.clearFocus();
                        tv3.clearFocus();
                        tv4.clearFocus();
                        btn3.setVisibility(View.INVISIBLE);
                        btn4.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }
    private void getSelectedSupplier() {
        Intent prevIntent = getIntent();
        String parseStringID = prevIntent.getStringExtra("id");

        for (int i = 0; i <  ManageSupplier.suppliersList.size(); i++) {
            if(ManageSupplier.suppliersList.get(i).getSupplier_ID().equals(parseStringID)) {
                selectedSupplier = ManageSupplier.suppliersList.get(i);
                return;
            }
        }
    }
    private void setValues() {
        ImageView iv = (ImageView) findViewById(R.id.supplier_supplierImg);
        tv1.setText(selectedSupplier.getName_of_supplier());
        tv2.setText(selectedSupplier.getAddress());
        tv3.setText(selectedSupplier.getEmail());
        tv4.setText(selectedSupplier.getPhoneNumber());
        tv5.setText(String.valueOf(selectedSupplier.getTotalBill()));
        tv6.setText(String.valueOf(selectedSupplier.getTotalMoney()));
        Picasso.get().load(selectedSupplier.getLogo()).into(iv);
    }
}