package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.mylovebeverage.Data.Connecting_MSSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddNewSupplier extends AppCompatActivity {
    ImageView arrback;
    EditText edt1;
    EditText edt2;
    EditText edt3;
    EditText edt4;
    EditText edt5;

    Button btn1;
    Button btn2;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_addSupplier = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_supplier);

        connecting_mssql = new Connecting_MSSQL(connection_addSupplier);
        connection_addSupplier = connecting_mssql.Connecting();
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edt1 = findViewById(R.id.supplier_addSupplierName);
        edt2 = findViewById(R.id.supplier_addSupplierLogo);
        edt3 = findViewById(R.id.supplier_addSupplierAddress);
        edt4 = findViewById(R.id.supplier_addSupplierEmail);
        edt5 = findViewById(R.id.supplier_addSupplierPhone);

        btn1 = findViewById(R.id.addNewSupplier);
        btn2 = findViewById(R.id.cancelNewSupplier);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = edt1.getText().toString();
                String s2 = edt2.getText().toString();
                String s3 = edt3.getText().toString();
                String s4 = edt4.getText().toString();
                String s5 = edt5.getText().toString();

                int i = 1;
                if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all information", Toast.LENGTH_SHORT).show();
                } else {
                    if (connection_addSupplier != null) {
                        try {
                            Statement statement = connection_addSupplier.createStatement();
                            ResultSet resultSet = statement.executeQuery("select * from [dbo].[Supplier];");
                            while (resultSet.next()) {
                                i += 1;
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Connect to Supplier makes error.", Toast.LENGTH_SHORT).show();
                    }

                    String Supplier_ID = "SP" + i;
                    try {
                        Statement statement2 = connection_addSupplier.createStatement();
                        ResultSet resultSet = statement2.executeQuery("INSERT INTO [dbo].[SUPPLIER] (Supplier_ID, Name_of_supplier, Address, Email, PhoneNumber, Status, Logo, TotalBill, TotalMoney)\n" +
                                "VALUES ('"+Supplier_ID+"', '"+s1+"', '"+s3+"', '"+s4+"', '"+s5+"', 'active', '"+s2+"', '0', '0');");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("editTextValue", "value_here");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}