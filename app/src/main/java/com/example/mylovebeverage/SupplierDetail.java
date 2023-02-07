package com.example.mylovebeverage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Supplier;
import com.example.mylovebeverage.Singleton.MySingleton;
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
    ImageButton img_chat;
    ImageButton img_call;
    private static final int PERMISSION_REQUEST_CODE_CALL = 123;
    private static final int PERMISSION_REQUEST_CODE_CHAT = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);
        arrback = findViewById(R.id.arrow_back);
        arrback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        img_chat = findViewById(R.id.chat);
        img_call = findViewById(R.id.call);
        tv1 = findViewById(R.id.supplier_supplierName);
        tv2 = findViewById(R.id.supplier_supplierAddress);
        tv3 = findViewById(R.id.supplier_supplierEmail);
        tv4 = findViewById(R.id.supplier_supplierPhoneNumber);
        tv5 = findViewById(R.id.supplier_supplierBill);
        tv6 = findViewById(R.id.supplier_supplierMoney);
        getSelectedSupplier();
        setValues();
        btn1 = findViewById(R.id.deleteSupplier);
        btn2 = findViewById(R.id.updateSupplier);
        btn3 = findViewById(R.id.saveChangeSupplier);
        btn4 = findViewById(R.id.cancelChangeSupplier);
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
                Dialog dialog = new Dialog(SupplierDetail.this);
                dialog.setContentView(R.layout.activity_custom_dialog);
                Button btnYes = dialog.findViewById(R.id.btnCustomDialogYes);
                Button btnNo = dialog.findViewById(R.id.btnCustomDialogNo);
                TextView msgDialog = dialog.findViewById(R.id.txtCustomDialogMessage);
                msgDialog.setText("Do you want to completely delete this supplier?");
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        connecting_mssql = new Connecting_MSSQL(connection_deleteSupplier);
                        connection_deleteSupplier = connecting_mssql.Connecting();

                        if (connection_deleteSupplier != null) {
                            try {
                                Statement statement = connection_deleteSupplier.createStatement();
                                ResultSet resultSet = statement.executeQuery("update [dbo].[Supplier]\n" +
                                        "set Status = 'inactive'\n" +
                                        "where Supplier_ID = '"+selectedSupplier.getSupplier_ID()+"'");
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }else {
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
                        if (connection_updateSupplier!=null) {
                            try {
                                Statement statement = connection_updateSupplier.createStatement();
                                ResultSet resultSet = statement.executeQuery("update [dbo].[SUPPLIER]\n" +
                                        "set Name_of_supplier = '" +tv1.getText()+"', " + "Address = '" + tv2.getText()+"', " +"Email='"+tv3.getText()+"', "+"PhoneNumber='"+tv4.getText()+"'\n"+
                                        "where Supplier_ID = '"+selectedSupplier.getSupplier_ID()+"'");
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }else {
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
        Chatting();
        Calling();

    }

    private void requestCallPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
                //provide explannation to user that s' why they must provide permission
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE_CALL);
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCalling();
            } else {
                Toast.makeText(this, "Calling Permission has denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_CHAT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openChatting();
            } else {
                Toast.makeText(this, "Chatting Permission has denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCalling() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent_call = new Intent();
            intent_call.setAction(Intent.ACTION_CALL);
            intent_call.setData(Uri.parse("tel: " + selectedSupplier.getPhoneNumber()));
            startActivity(intent_call);
        } else {
            // Permission has not been granted, request it
            requestCallPermission();
        }
    }

    private void Calling() {
        img_call.setOnClickListener(view -> {
            openCalling();
        });
    }

    private void requestChatPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                //provide explanation for user that s' why they need to chat
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE_CHAT);
        } else {
            //Permission has been granted
        }
    }

    private void openChatting() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + selectedSupplier.getPhoneNumber()));
            intent.putExtra("sms_body", "Hello " + selectedSupplier.getName_of_supplier());
            startActivity(intent);
        } else {
            requestChatPermission();
        }
    }

    private void Chatting() {
        img_chat.setOnClickListener(view -> {
            openChatting();
        });
    }

    private void getSelectedSupplier() {
        Intent prevIntent = getIntent();
        String parseStringID = prevIntent.getStringExtra("id");

        for (int i = 0; i < ManageSupplier.suppliersList.size(); i++) {
            if (ManageSupplier.suppliersList.get(i).getSupplier_ID().equals(parseStringID)) {
                selectedSupplier = ManageSupplier.suppliersList.get(i);
                return;
            }
        }
    }
    private void setValues() {
        ImageView iv = findViewById(R.id.supplier_supplierImg);
        tv1.setText(selectedSupplier.getName_of_supplier());
        tv2.setText(selectedSupplier.getAddress());
        tv3.setText(selectedSupplier.getEmail());
        tv4.setText(selectedSupplier.getPhoneNumber());
        tv5.setText(String.valueOf(selectedSupplier.getTotalBill()));
        tv6.setText(String.valueOf(selectedSupplier.getTotalMoney()));
        Picasso.get().load(selectedSupplier.getLogo()).into(iv);
    }
}