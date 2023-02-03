package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
public class AddNewProduct extends AppCompatActivity {
    ImageView arrback;
    Spinner spinnerCategory;
    String categoryID = "";
    ArrayList<String> arrCategory;
    EditText edt1;
    EditText edt2;
    EditText edt3;
    EditText edt4;
    EditText edt5;
    EditText edt6;
    Button btn1;
    Button btn2;
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_addProduct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        connecting_mssql = new Connecting_MSSQL(connection_addProduct);
        connection_addProduct = connecting_mssql.Connecting();
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinnerCategory = findViewById(R.id.product_addCategoryName);
        arrCategory = new ArrayList<String>();
        arrCategory.add("Coffee");
        arrCategory.add("Milktea");
        arrCategory.add("Tea");
        arrCategory.add("Freeze");
        arrCategory.add("Others");
        arrCategory.add("Cake");

        edt1 = findViewById(R.id.product_addProductName);
        edt2 = findViewById(R.id.product_addProductImage);
        edt3 = findViewById(R.id.product_addProductBrand);
        edt4 = findViewById(R.id.product_addProductPrice);
        edt5 = findViewById(R.id.product_addProductUnit);
        edt6 = findViewById(R.id.product_addProductQuantity);

        btn1 = findViewById(R.id.addNewProduct);
        btn2 = findViewById(R.id.cancelNewProduct);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrCategory);
        arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(arrayAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), arrCategory.get(i), Toast.LENGTH_SHORT).show();
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) adapterView.getChildAt(0)).setTextSize(30);
                categoryID = arrCategory.get(i);
                getCategoryID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = edt1.getText().toString();
                String s2 = edt2.getText().toString();
                String s3 = edt3.getText().toString();
                String s4 = edt4.getText().toString();
                String s5 = edt5.getText().toString();
                String s6 = edt6.getText().toString();
                int i = 1;
                if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("") || s6.equals("") || categoryID.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill all information", Toast.LENGTH_SHORT).show();
                } else {
                    if (connection_addProduct != null) {
                        try {
                            Statement statement = connection_addProduct.createStatement();
                            ResultSet resultSet = statement.executeQuery("select * from [dbo].[PRODUCT];");
                            while (resultSet.next()) {
                                i += 1;
                            }

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error.", Toast.LENGTH_SHORT).show();
                    }

                    String ProductID = "PD" + i;
                    try {
                        Statement statement2 = connection_addProduct.createStatement();
                        ResultSet resultSet = statement2.executeQuery("INSERT INTO [dbo].[PRODUCT] (Product_ID, Category_ID, Name_of_Product, Branding, Quantity, Image_Product, OutPrice, Unit, Status)\n" +
                                "VALUES ('"+ProductID+"', '"+categoryID+"', '"+s1+"', '"+s3+"', '"+s6+"', '"+s2+"', '"+s4+"', '"+s5+"', 'active');");
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
    }

    private void getCategoryID() {
        switch (categoryID) {
            case "Coffee":
                categoryID = "CT1";
                break;
            case "Milktea":
                categoryID = "CT2";
                break;
            case "Tea":
                categoryID = "CT3";
                break;
            case "Freeze":
                categoryID = "CT4";
                break;
            case "Others":
                categoryID = "CT5";
                break;
            case "Cake":
                categoryID = "CT6";
                break;
            default:
                categoryID = "";
        }
    }
}