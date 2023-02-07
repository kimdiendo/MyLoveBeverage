package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.OrderProductAdapter;
import com.example.mylovebeverage.Adapters.OrderDetailAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Product;
import com.example.mylovebeverage.SharedPreferences.MyPreferences;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Order extends AppCompatActivity {

    Connecting_MSSQL connecting_mssql;
    private static Connection connection_product = null;
    private static Connection connection_invoice = null;
    private static Connection connection_invoice_detail = null;
    private static Connection connection_staff = null;
    GridView gridView;
    public static ListView listView;
    public static OrderDetailAdapter orderDetailAdapter;
    public  static ArrayList<Product> productArrayList = new ArrayList<Product>();
    public  static ArrayList<Product> productArrayList2 = new ArrayList<Product>();
    Product product;
    Button btnOrderFilterAll, btnOrderFilterCoffee, btnOrderFilterMilktea, btnOrderFilterTea,
            btnOrderFilterFreeze, btnOrderFilterOthers, btnOrderFilterCake,
            btnOrderCancel, btnOrderConfirm;
    TextView txtOrderStaffId, txtOrderDate;
    EditText editTextOrderDetailMoneyReceived;
    SearchView searchView;
    public static  TextView txtTotalOrder;
    Integer a = 0;
    public static Integer totalOrderBill = 0;
    String getStaffId = "";
    //Logout
    Button img_logout;
    MyPreferences myPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        myPreferences = new MyPreferences(getApplicationContext());
        connecting_mssql = new Connecting_MSSQL(connection_product);
        connection_product = connecting_mssql.Connecting();

        connecting_mssql = new Connecting_MSSQL(connection_invoice);
        connection_invoice = connecting_mssql.Connecting();


        connecting_mssql = new Connecting_MSSQL(connection_invoice_detail);
        connection_invoice_detail = connecting_mssql.Connecting();


        btnOrderFilterAll = findViewById(R.id.btnOrderFilterAll);
        btnOrderFilterCoffee = findViewById(R.id.btnOrderFilterCoffee);
        btnOrderFilterMilktea = findViewById(R.id.btnOrderFilterMilktea);
        btnOrderFilterTea = findViewById(R.id.btnOrderFilterTea);
        btnOrderFilterFreeze = findViewById(R.id.btnOrderFilterFreeze);
        btnOrderFilterOthers = findViewById(R.id.btnOrderFilterOthers);
        btnOrderFilterCake = findViewById(R.id.btnOrderFilterCake);
        btnOrderCancel = findViewById(R.id.btnOrderCancel);
        btnOrderConfirm = findViewById(R.id.btnOrderConfirm);
        txtOrderStaffId = findViewById(R.id.txtOrderStaffId);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtTotalOrder = findViewById(R.id.txtOrderDetailTotalDisplay);

        editTextOrderDetailMoneyReceived = findViewById(R.id.editTextOrderDetailMoneyReceived);
        editTextOrderDetailMoneyReceived.addTextChangedListener(onTextChangedListener());

        searchView = findViewById(R.id.searchViewOrder);
        searchView.setFocusable(false);

        txtTotalOrder.setText(totalOrderBill.toString() + " VND");
        img_logout = findViewById(R.id.logout);
        Intent getIntent = getIntent();
        getStaffId = getIntent.getStringExtra("Staff Id");
        Date date = new Date();
        DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");

        txtOrderStaffId.setText("Staff ID: " + getStaffId);
        txtOrderDate.setText("Date: " + dateFormat.format(date));

        initSearchWidgets();
        Logout();
        getAllProducts();
        setUpGridview();
        setBackGroundButton("");

        orderDetailAdapter = new OrderDetailAdapter(getApplicationContext(),0,productArrayList2);

        btnOrderFilterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("");
                getAllProducts();
                setUpGridview();
            }
        });

        btnOrderFilterCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT1");
                getFilterProducts("CT1");
                setUpGridview();
            }
        });

        btnOrderFilterMilktea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT2");
                getFilterProducts("CT2");
                setUpGridview();
            }
        });

        btnOrderFilterTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT3");
                getFilterProducts("CT3");
                setUpGridview();
            }
        });

        btnOrderFilterFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT4");
                getFilterProducts("CT4");
                setUpGridview();
            }
        });

        btnOrderFilterOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT5");
                getFilterProducts("CT5");
                setUpGridview();
            }
        });

        btnOrderFilterCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGroundButton("CT6");
                getFilterProducts("CT6");
                setUpGridview();
            }
        });

        setUpList();

        btnOrderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productArrayList2.isEmpty()){
                    Dialog dialog = new Dialog(Order.this);
                    dialog.setContentView(R.layout.activity_custom_dialog_2);
                    TextView txtMessage = dialog.findViewById(R.id.txtCustomDialog2Message);
                    txtMessage.setText("The current order is empty!");
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
                    Dialog dialog = new Dialog(Order.this);
                    dialog.setContentView(R.layout.activity_custom_dialog);
                    TextView txtMessage = dialog.findViewById(R.id.txtCustomDialogMessage);
                    txtMessage.setText("Do you want to completely delete the current order?");
                    dialog.show();
                    Button btnYes = dialog.findViewById(R.id.btnCustomDialogYes);
                    Button btnNo = dialog.findViewById(R.id.btnCustomDialogNo);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productArrayList2.removeAll(productArrayList2);
                            orderDetailAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            totalOrderBill = 0;
                            txtTotalOrder.setText(totalOrderBill + " VND");
                            editTextOrderDetailMoneyReceived.setText("");
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        btnOrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productArrayList2.isEmpty()){
                    Dialog dialog = new Dialog(Order.this);
                    dialog.setContentView(R.layout.activity_custom_dialog_2);
                    TextView txtMessage = dialog.findViewById(R.id.txtCustomDialog2Message);
                    txtMessage.setText("The current order is empty!");
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
                    if (editTextOrderDetailMoneyReceived.getText().toString().matches("")){
                        Dialog dialog = new Dialog(Order.this);
                        dialog.setContentView(R.layout.activity_custom_dialog_2);
                        TextView txtMessage = dialog.findViewById(R.id.txtCustomDialog2Message);
                        txtMessage.setText("You must enter the amount of money the customer gives!");
                        dialog.show();
                        Button btnOk = dialog.findViewById(R.id.btnCustomDialog2Ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                    else{
                        if(Integer.parseInt(editTextOrderDetailMoneyReceived.getText().toString().replaceAll(",","")) < totalOrderBill){
                            Dialog dialog = new Dialog(Order.this);
                            dialog.setContentView(R.layout.activity_custom_dialog_2);
                            TextView txtMessage = dialog.findViewById(R.id.txtCustomDialog2Message);
                            txtMessage.setText("The amount of money received must be equal to or greater than the total amount of the order!");
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
                            Dialog dialog = new Dialog(Order.this);
                            dialog.setContentView(R.layout.activity_custom_dialog);
                            TextView txtMessage = dialog.findViewById(R.id.txtCustomDialogMessage);
                            txtMessage.setText("Do you want to complete the current order?");
                            dialog.show();
                            Button btnYes = dialog.findViewById(R.id.btnCustomDialogYes);
                            Button btnNo = dialog.findViewById(R.id.btnCustomDialogNo);
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String numberOfInvoiceFormat = "";
                                    if (connection_invoice != null) {
                                        try {
                                            Integer numberOfInvoice = 0;
                                            Date curentTime = new Date();
                                            String timeFormat = "";
                                            Statement statement = connection_invoice.createStatement();
                                            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM INVOICE;");
                                            while (resultSet.next()) {
                                                numberOfInvoice = resultSet.getInt(1) + 1;
                                            }
                                            if (numberOfInvoice < 10){
                                                numberOfInvoiceFormat = "0" + numberOfInvoice;
                                            }
                                            else {
                                                numberOfInvoiceFormat = numberOfInvoice + "";
                                            }
                                            Statement statement2 = connection_invoice.createStatement();
                                            ResultSet resultSet2 = statement2.executeQuery("SELECT CURRENT_TIMESTAMP;");
                                            while (resultSet2.next()) {
                                                curentTime = resultSet2.getTimestamp(1);
                                            }
                                            timeFormat = getDateTimeCustom(curentTime);
                                            a = Integer.parseInt(editTextOrderDetailMoneyReceived.getText().toString().replaceAll(",","")) - totalOrderBill;
                                            Statement statement3 = connection_invoice.createStatement();
                                            ResultSet resultSet3 = statement3.executeQuery("INSERT INTO [dbo].[INVOICE] (Invoice_ID, Staff_ID, DateTime_Invoice, Price_of_Invoice, Money_Received, Money_Returned)\n" +
                                                    "VALUES ('I" + numberOfInvoiceFormat + "', '" + getStaffId + "', '" + timeFormat + "', " + totalOrderBill + ", " + editTextOrderDetailMoneyReceived.getText().toString().replaceAll(",","") + ", " + a + ");");
                                        }catch (SQLException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }else
                                    {
                                        Toast.makeText(Order.this, "Connect to InvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                                    }
                                    for (Product product : productArrayList2){
                                        if (connection_invoice_detail!=null)
                                        {
                                            try {
                                                Statement statement = connection_invoice_detail.createStatement();
                                                ResultSet resultSet = statement.executeQuery("INSERT INTO [dbo].[DETAILINVOICE] (Invoice_ID, Product_ID, Quantity)\n" +
                                                        "VALUES ('I" + numberOfInvoiceFormat + "', '" + product.getProduct_ID() + "', '" + product.getOrderAmount() + "');");
                                            }catch (SQLException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }else
                                        {
                                            Toast.makeText(Order.this, "Connect to DetailInvoiceDB makes error." , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    dialog.dismiss();
                                    productArrayList2.removeAll(productArrayList2);
                                    orderDetailAdapter.notifyDataSetChanged();
                                    totalOrderBill = 0;
                                    txtTotalOrder.setText(totalOrderBill + " VND");
                                    editTextOrderDetailMoneyReceived.setText("");
                                    Intent intent = new Intent(getApplicationContext(), PostOrder.class);
                                    intent.putExtra("invoiceId","I" + numberOfInvoiceFormat);
                                    startActivity(intent);
                                }
                            });
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }

                }
            }
        });
    }
    private void Logout()
    {
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection_staff = new Connecting_MSSQL(connection_staff).Connecting();
                Dialog dialog = new Dialog(Order.this);
                dialog.setContentView(R.layout.activity_log_out);
                Button btnYes = dialog.findViewById(R.id.btnCustomDialogYes);
                Button btnNo = dialog.findViewById(R.id.btnCustomDialogNo);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (connection_staff != null)
                        {
                            try {
                                Statement statement = connection_staff.createStatement();
                                statement.execute("UPDATE ACCOUNT\n" +
                                        "SET Status ='inactive'\n" +
                                        "WHERE Account_name =" + "'" + getStaffId + "'");
                                dialog.dismiss();
                                finish();
                                myPreferences.saveKeyCheck(false);
                                myPreferences.saveUsername("");
                                myPreferences.savePassword("");
                                myPreferences.savePosition("");
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Connect makes error.Please hotline with the administrator!" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private TextWatcher onTextChangedListener(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextOrderDetailMoneyReceived.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    editTextOrderDetailMoneyReceived.setText(formattedString);
                    editTextOrderDetailMoneyReceived.setSelection(editTextOrderDetailMoneyReceived.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                editTextOrderDetailMoneyReceived.addTextChangedListener(this);
            }
        };
    }

    private  void initSearchWidgets(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Product> searchedProductArrayList = new ArrayList<Product>();
                for (Product product : productArrayList){
                    if(product.getName_of_Product().toLowerCase().contains(newText.toLowerCase())){
                        searchedProductArrayList.add(product);
                    }
                }

                OrderProductAdapter searchProductAdapter = new OrderProductAdapter(getApplicationContext(),R.layout.activity_grid_order_product_item,searchedProductArrayList);
                gridView.setAdapter(searchProductAdapter);

                return false;
            }
        });
    }

    private void getAllProducts() {
        if (connection_product!=null)
        {
            try {
                Statement statement = connection_product.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT;");
                productArrayList.removeAll(productArrayList);
                while (resultSet.next()) {
                    product = new Product(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getString(3).trim(),resultSet.getString(4).trim(),resultSet.getInt(7),resultSet.getString(8).trim(),resultSet.getInt(5),resultSet.getString(6));
                    productArrayList.add(product);
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

    private void setUpGridview() {
        gridView = findViewById(R.id.gridViewOrderProduct);
        OrderProductAdapter orderProductAdapter = new OrderProductAdapter(this,R.layout.activity_grid_order_product_item,productArrayList);
        gridView.setAdapter(orderProductAdapter);
        orderProductAdapter.notifyDataSetChanged();
    }

    private void getFilterProducts(String a) {
        if (connection_product!=null)
        {
            try {
                Statement statement = connection_product.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT WHERE Category_ID = '" + a + "';");
                productArrayList.removeAll(productArrayList);
                while (resultSet.next()) {
                    product = new Product(resultSet.getString(1).trim(),resultSet.getString(2).trim(),resultSet.getString(3).trim(),resultSet.getString(4).trim(),resultSet.getInt(7),resultSet.getString(8).trim(),resultSet.getInt(5),resultSet.getString(6));
                    productArrayList.add(product);
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

    private void setBackGroundButton(String status) {
        btnOrderFilterAll.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterCoffee.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterMilktea.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterTea.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterFreeze.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterOthers.setBackgroundResource(R.drawable.custom_order_filter_button);
        btnOrderFilterCake.setBackgroundResource(R.drawable.custom_order_filter_button);
        switch ( status ) {
            case  "CT1":
                btnOrderFilterCoffee.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT2":
                btnOrderFilterMilktea.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT3":
                btnOrderFilterTea.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT4":
                btnOrderFilterFreeze.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;

            case  "CT5":
                btnOrderFilterOthers.setBackgroundResource(R.drawable.custom_selected_order_filter_button);

                break;
            case  "CT6":
                btnOrderFilterCake.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
                break;
            default:
                btnOrderFilterAll.setBackgroundResource(R.drawable.custom_selected_order_filter_button);
        }

    }
    public void setUpList() {
        listView = findViewById(R.id.orderDetailListView);
        listView.setAdapter(orderDetailAdapter);
        orderDetailAdapter.notifyDataSetChanged();
    }

    public String getDateTimeCustom(Date a)
    {
        String temp = "";
        String hour = "";
        String result = "";
        DateFormat dateFormat = null;
        dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        temp = dateFormat.format(a);
        hour = temp.charAt(9) + "" + temp.charAt(10);
        result = temp.substring (0,9) + (Integer.parseInt(hour) + 7) + temp.substring(11);
        return result;
    }
}