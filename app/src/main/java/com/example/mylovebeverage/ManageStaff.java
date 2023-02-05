package com.example.mylovebeverage;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
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

import com.example.mylovebeverage.R;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Detail_Human_Resource;
import com.example.mylovebeverage.Adapters.HumanResourceAdapter;
import com.example.mylovebeverage.databinding.ActivityManageStaffBinding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ManageStaff extends AppCompatActivity {
    private ActivityManageStaffBinding binding;
    Connection connection_manage_staff;
    String selectedFilter ="";
    private ArrayList<Detail_Human_Resource> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageStaffBinding.inflate(getLayoutInflater());
        arrayList = new ArrayList<>();
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection_manage_staff = new Connecting_MSSQL(connection_manage_staff).Connecting();
        Back_previous_activity();
        SetUp();
        SetUpList();
        SetItemWhenClicked();
        Adding();
        Filter();
    }
    private void filterProductList(String status) {
        selectedFilter = status;
        ArrayList<Detail_Human_Resource> filteredItem = new ArrayList<>();
        for(Detail_Human_Resource item: arrayList) {
            if (item.getPosition().contains(status)) {
                filteredItem.add(item);
            }
        }
        HumanResourceAdapter humanResourceAdapter = new HumanResourceAdapter(filteredItem);
        humanResourceAdapter.notifyDataSetChanged();
        binding.gidview.setAdapter(humanResourceAdapter);
    }
    private void setBackGroundButton(String status) {
        binding.allCategoryBtn.setBackgroundResource(R.drawable.login_rbcircle_4);
        binding.ManagerBtn.setBackgroundResource(R.drawable.login_rbcircle_4);
        binding.OrderBtn.setBackgroundResource(R.drawable.login_rbcircle_4);
        binding.WaitressBtn.setBackgroundResource(R.drawable.login_rbcircle_4);
        binding.SecurityBtn.setBackgroundResource(R.drawable.login_rbcircle_4);
        binding.Bartendertn.setBackgroundResource(R.drawable.login_rbcircle_4);
        switch ( status ) {
            case  "All":
                binding.allCategoryBtn.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;
            case  "Manager":
                binding.ManagerBtn.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;

            case "Order":
                binding.OrderBtn.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;
            case "Security":
                binding.SecurityBtn.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;
            case "Waitress":
                binding.WaitressBtn.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;
            case  "Bartender":
                binding.Bartendertn.setBackgroundResource(R.drawable.login_rbcircle_1);
                break;
        }

    }
    private void Filter() {
        setBackGroundButton("All");
        HumanResourceAdapter humanResourceAdapter = new HumanResourceAdapter(arrayList);
        humanResourceAdapter.notifyDataSetChanged();
        binding.gidview.setAdapter(humanResourceAdapter);
        binding.allCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundButton("All");
                HumanResourceAdapter humanResourceAdapter = new HumanResourceAdapter(arrayList);
                humanResourceAdapter.notifyDataSetChanged();
                binding.gidview.setAdapter(humanResourceAdapter);

            }
        });
        binding.ManagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundButton("Manager");
                filterProductList("Manager");
            }
        });
        binding.OrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundButton("Order");
                filterProductList("Order");

            }
        });
        binding.SecurityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundButton("Security");
                filterProductList("Security");

            }
        });
        binding.WaitressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundButton("Waitress");
                filterProductList("Waitress");
            }
        });
        binding.Bartendertn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundButton("Bartender");
                filterProductList("Bartender");
            }
        });
    }

    private void Handling_Selection_Events(Spinner spinner, EditText txt) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txt.setText(adapterView.getAdapter().getItem(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void Adding() {
        binding.floatingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ManageStaff.this);
                dialog.setContentView(R.layout.activity_staff_edit_dialog);
                //create spinner position
                ArrayAdapter<CharSequence> adapter_position = ArrayAdapter.createFromResource(ManageStaff.this, R.array.type_position, android.R.layout.simple_spinner_item);
                adapter_position.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                /// create spinner gender
                ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(ManageStaff.this, R.array.type_gender, android.R.layout.simple_spinner_item);
                adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // create spinner status
                ArrayAdapter<CharSequence> adapter_status = ArrayAdapter.createFromResource(ManageStaff.this, R.array.type_status_working, android.R.layout.simple_spinner_item);
                adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner spinner_position = dialog.findViewById(R.id.img_position);
                Spinner spinner_gender = dialog.findViewById(R.id.img_gender);
                Spinner spinner_status_working = dialog.findViewById(R.id.img_status);
                spinner_position.setAdapter(adapter_position);
                spinner_gender.setAdapter(adapter_gender);
                spinner_status_working.setAdapter(adapter_status);
                TextView title = dialog.findViewById(R.id.textfunction);
                ImageView btnClose = dialog.findViewById(R.id.close);
                Button btnSave = dialog.findViewById(R.id.save);
                EditText txt_ID = dialog.findViewById(R.id.ID);
                EditText txt_name = dialog.findViewById(R.id.Name);
                EditText txt_status = dialog.findViewById(R.id.status); //thêm spinner
                EditText txt_position = dialog.findViewById(R.id.position); // thêm spinner
                EditText txt_gender = dialog.findViewById(R.id.gender); //thêm spinner
                EditText txt_phonenumber = dialog.findViewById(R.id.PhoneNumber);
                EditText txt_email = dialog.findViewById(R.id.email);
                EditText txt_salary = dialog.findViewById(R.id.Salary);
                EditText txt_image = dialog.findViewById(R.id.image);
                title.setText("Fill Staff Information");
                Handling_Selection_Events(spinner_position, txt_position);
                Handling_Selection_Events(spinner_gender, txt_gender);
                Handling_Selection_Events(spinner_status_working, txt_status);
                dialog.setCanceledOnTouchOutside(false);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                           if(CheckFormValidation(txt_ID.getText().toString().trim(),txt_name.getText().toString().trim() ,txt_status.getText().toString().trim(),txt_position.getText().toString().trim() , txt_gender.getText().toString().trim(),txt_phonenumber.getText().toString().trim(), txt_email.getText().toString().trim() , txt_salary.getText().toString().trim(),txt_image.getText().toString().trim()))
                           {
                               if(connection_manage_staff!= null)
                               {
                                   try {

                                       Statement statement = connection_manage_staff.createStatement();
                                       ResultSet resultSet = statement.executeQuery("SELECT *\n" +
                                               "FROM STAFF\n" +
                                               "WHERE Staff_ID="+"'"+txt_ID.getText().toString().trim()+"'"+";");
                                       if (resultSet.next())
                                       {
                                           Toast.makeText(getApplicationContext(), "The ID has exited in StaffDB.You can not add the staff with ID" , Toast.LENGTH_LONG).show();
                                       }else
                                       {
                                           statement.execute("INSERT INTO STAFF\n" +
                                                   "VALUES("+"'"+txt_ID.getText().toString().trim()+"'"+","+"'"+txt_name.getText().toString().trim()+"'"+","+"'"+txt_position.getText().toString().trim()+"'"+","+"'"+txt_gender.getText().toString().trim()+"'"+","+"'"+txt_phonenumber.getText().toString().trim()+"'"+","+"'"+txt_salary.getText().toString().trim()+"'"+","+"'"+txt_image.getText().toString().trim()+"'"+","+"'"+txt_email.getText().toString().trim()+"'"+","+"'"+txt_status.getText().toString().trim()+"'"+")");
                                           dialog.dismiss();
                                           Toast.makeText(getApplicationContext(), "Adding successfully" , Toast.LENGTH_LONG).show();
                                           onStart();
                                       }
                                   }catch (SQLException e)
                                   {
                                       e.printStackTrace();
                                   }
                               }else
                               {
                                   Toast.makeText(getApplicationContext(), "Connect to StaffDB makes error." , Toast.LENGTH_LONG).show();
                               }
                           }
                    }
                });
                dialog.show();
            }
        });
    }
    private boolean CheckFormValidation(String ID, String name , String status , String position ,String gender ,String phonenumber , String email , String salary , String image)
    {
        if (ID.isEmpty() || ID.length()>10)
        {
            Toast.makeText(getApplicationContext(), "ID is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(name.isEmpty() || name.length()>20)
        {
            Toast.makeText(getApplicationContext(), "Name is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(status.isEmpty() || status.length()> 20)
        {
            Toast.makeText(getApplicationContext(), "Status is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(position.isEmpty() || position.length() > 10)
        {
            Toast.makeText(getApplicationContext(), "Position is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(gender.isEmpty() || gender.length()>7)
        {
            Toast.makeText(getApplicationContext(), "Gender is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(phonenumber.isEmpty()||phonenumber.length()> 10)
        {
            Toast.makeText(getApplicationContext(), "Phone is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(email.isEmpty()||email.length()>30)
        {
            Toast.makeText(getApplicationContext(), "Email is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(salary.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Salary is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(image.isEmpty()|| image.length()>1000)
        {
            Toast.makeText(getApplicationContext(), "Image is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void Back_previous_activity()
    {
        binding.arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void SetUp()
    {
        if (connection_manage_staff != null)//ket noi csdl
        {
            try
            {
                Statement statement = connection_manage_staff.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT *FROM STAFF WHERE Status ='active';");//duyet database
                arrayList.clear();
                while(resultSet.next())
                {
                    arrayList.add(new Detail_Human_Resource
                            (resultSet.getString(1).trim() , resultSet.getString(2).trim(),resultSet.getString(3).trim(),resultSet.getString(4).trim(),resultSet.getString(5).trim(),resultSet.getInt(6),resultSet.getString(7).trim(), resultSet.getString(8).trim(),resultSet.getString(9).trim()));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(), "Connect makes error.Please hotline with the administrator!" , Toast.LENGTH_SHORT).show();
        }
    }
    private void SetUpList()
    {
        HumanResourceAdapter humanResourceAdapter = new HumanResourceAdapter(arrayList);
        humanResourceAdapter.notifyDataSetChanged();
        binding.gidview.setAdapter(humanResourceAdapter);
    }
    private void SetItemWhenClicked()
    {
        binding.gidview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                       Detail_Human_Resource detail_human_resource =(Detail_Human_Resource) binding.gidview.getItemAtPosition(position);
                       Intent intent = new Intent(getApplicationContext() , StaffInformation.class);
                       intent.putExtra("ID" , detail_human_resource.getID());
                       intent.putExtra("Name" , detail_human_resource.getName());
                       intent.putExtra("Position" ,detail_human_resource.getPosition());
                       intent.putExtra("Gender" , detail_human_resource.getGender());
                       intent.putExtra("PhoneNumber" , detail_human_resource.getPhoneNumber());
                       intent.putExtra("Salary" , detail_human_resource.getSalary());
                       intent.putExtra("Image" , detail_human_resource.getImageview());
                       intent.putExtra("Email" , detail_human_resource.getEmail());
                       intent.putExtra("Status", detail_human_resource.getStatus());
                       startActivity(intent);
            }
        });
    }
}