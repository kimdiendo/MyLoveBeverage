package com.example.mylovebeverage;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.mylovebeverage.Adapters.ProductAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;

public class ManageProduct extends AppCompatActivity {

    private ListView listView;
    String selectedFilter = "all";
    Connecting_MSSQL connecting_mssql;
    private static Connection connection_product = null;
    ImageView arrback;

    Product product;
    public static ArrayList<Product> productList = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
        connecting_mssql = new Connecting_MSSQL(connection_product);
        connection_product = connecting_mssql.Connecting();
        getAllProducts();
        setUpList();
        setUpOnClickListener();
        FloatingActionButton addProductBtn = findViewById(R.id.addProduct);
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewProduct.class);
//                startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });

    }

    protected void getAllProducts(){
        if (connection_product!=null)
        {
            try {
                Statement statement = connection_product.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from [dbo].[PRODUCT] where Status = 'active';");
                productList.clear();
                while (resultSet.next()) {
                        product = new Product(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getString(3).trim(),resultSet.getString(4).trim(),resultSet.getInt(7),resultSet.getString(8).trim(),resultSet.getInt(5),resultSet.getString(6));
                        productList.add(product);
                }


            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect to ProductDB makes error." , Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpList() {
        listView = findViewById(R.id.productsListView);
        ProductAdapter adapter = new ProductAdapter(getApplicationContext(), 0, productList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setUpOnClickListener() {

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Product selectProduct = (Product) (listView.getItemAtPosition(i));
            Intent showDetail = new Intent(getApplicationContext(), ProductDetail.class);
            showDetail.putExtra("id",selectProduct.getProduct_ID());
            startActivityForResult(showDetail, 1);
        });

}
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                getAllProducts();
                setUpList();
            }
        }
    }


    private void filterProductList(String status) {
        selectedFilter = status;
        ArrayList<Product> filteredProduct = new ArrayList<Product>();
        for(Product product: productList) {
            if (product.getCategory_ID().contains(status)) {
                filteredProduct.add(product);
            }
        }
        ProductAdapter adapter = new ProductAdapter(getApplicationContext(), 0, filteredProduct);
        listView.setAdapter(adapter);
    }

    private void setBackGroundButton(String status) {
        Button btn1 = findViewById(R.id.allCategoryBtn);
        Button btn2 = findViewById(R.id.coffeeBtn);
        Button btn3 = findViewById(R.id.milkteaBtn);
        Button btn4 = findViewById(R.id.teaBtn);
        Button btn5 = findViewById(R.id.freezeBtn);
        Button btn6 = findViewById(R.id.othersBtn);
        Button btn7 = findViewById(R.id.cakeBtn);
        btn1.setBackgroundResource(R.drawable.login_rbcircle_4);
        btn2.setBackgroundResource(R.drawable.login_rbcircle_4);
        btn3.setBackgroundResource(R.drawable.login_rbcircle_4);
        btn4.setBackgroundResource(R.drawable.login_rbcircle_4);
        btn5.setBackgroundResource(R.drawable.login_rbcircle_4);
        btn6.setBackgroundResource(R.drawable.login_rbcircle_4);
        btn7.setBackgroundResource(R.drawable.login_rbcircle_4);
        switch (status) {
            case "CT1":
                btn2.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;

            case  "CT2":
                btn3.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;

            case  "CT3":
                btn4.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;

            case  "CT4":
                btn5.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;

            case  "CT5":
                btn6.setBackgroundResource(R.drawable.login_rbcircle_1);

                break;
            case  "CT6":
                btn7.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;
            default:
                btn1.setBackgroundResource(R.drawable.login_rbcircle_1);
        }

    }

    public void showAllProducts(View view) {

        ProductAdapter adapter = new ProductAdapter(getApplicationContext(), 0, productList);
        setBackGroundButton("all");
        listView.setAdapter(adapter);
    }

    public void showCoffeeProducts(View view) {

        filterProductList("CT1");
        setBackGroundButton("CT1");
    }

    public void showMilkteaProducts(View view) {

        filterProductList("CT2");
        setBackGroundButton("CT2");

    }

    public void showTeaProducts(View view) {

        filterProductList("CT3");
        setBackGroundButton("CT3");

    }

    public void showFreezeProducts(View view) {
        filterProductList("CT4");
        setBackGroundButton("CT4");

    }

    public void showOthersProducts(View view) {

        filterProductList("CT5");
        setBackGroundButton("CT5");
    }

    public void showCakeProducts(View view) {

        filterProductList("CT6");
        setBackGroundButton("CT6");

    }

}