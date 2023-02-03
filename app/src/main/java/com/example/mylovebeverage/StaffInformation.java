package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Detail_Human_Resource;
import com.example.mylovebeverage.Adapters.InformationAdapter;
import com.example.mylovebeverage.Models.Detail_Profile;
import com.example.mylovebeverage.databinding.ActivityStaffInformationBinding;
import com.squareup.picasso.Picasso;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StaffInformation extends AppCompatActivity {
    private ActivityStaffInformationBinding binding;
    Connection connection_staff_information;
    Detail_Human_Resource selectedItem;
    InformationAdapter informationAdapter;
    private ArrayList<Detail_Profile> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStaffInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        selectedItem = new Detail_Human_Resource(intent.getStringExtra("ID"), intent.getStringExtra("Name"), intent.getStringExtra("Position"), intent.getStringExtra("Gender"),intent.getStringExtra("PhoneNumber"),intent.getIntExtra("Salary" , 0) , intent.getStringExtra("Image"), intent.getStringExtra("Email"), intent.getStringExtra("Status"));
    }
    @Override
    protected void onStart() {
        super.onStart();
        connection_staff_information = new Connecting_MSSQL(connection_staff_information).Connecting();
        CheckAccount();
        Back_previous_activity();
        ShowItem();
        Chatting();
        Delete();
        Edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    private void CheckAccountStatus()
    {
        String status ="";
        try {
            Statement statement = connection_staff_information.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Status FROM ACCOUNT WHERE Account_name ="+"'"+selectedItem.getID()+"';");
            while(resultSet.next())
            {
                status = resultSet.getString(1).trim();
            }
            if(status.equals("active"))
            {
                binding.radioStatus.setVisibility(View.VISIBLE);
                binding.radioStatus.setText("Active");
                binding.radioStatus.setChecked(true);
            }else
            {
                binding.radioStatus.setVisibility(View.VISIBLE);
                binding.radioStatus.setText("Away");
                binding.radioStatus.setChecked(false);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private  void CheckAccount()
    {
            if(connection_staff_information!= null)
            {
                try{
                    Statement statement = connection_staff_information.createStatement();
                    ResultSet resultSet  = statement.executeQuery("SELECT *FROM ACCOUNT WHERE Account_name ="+"'"+selectedItem.getID()+"';");
                    if (resultSet.next())
                    {
                        CheckAccountStatus();
                        binding.accountCheck.setVisibility(View.VISIBLE);
                        binding.accountAdding.setVisibility(View.INVISIBLE);
                        binding.accountCheck.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(StaffInformation.this);
                                dialog.setContentView(R.layout.activity_account_management_dialog);
                                EditText txt_ID = dialog.findViewById(R.id.ID);
                                EditText txt_pass = dialog.findViewById(R.id.pass);
                                EditText txt_role = dialog.findViewById(R.id.role);
                                ImageView btnClose = dialog.findViewById(R.id.close);
                                Button btnSave = dialog.findViewById(R.id.save);
                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            Statement statement1 = connection_staff_information.createStatement();
                                            statement1.execute("UPDATE ACCOUNT\n" +
                                                    "SET Account_name =" + "'" + txt_ID.getText().toString().trim() + "'" + "," + "Pass=" + "'" + txt_pass.getText().toString().trim() + "'" + "," + "Position=" + "'" + txt_role.getText().toString().trim() + "'" + "\n" +
                                                    "WHERE Account_name=" + "'" + selectedItem.getID() + "'");
                                            dialog.dismiss();
                                                  Toast.makeText(getApplicationContext(), "Update successfully" , Toast.LENGTH_LONG).show();
                                                  onStart();

                                              }catch (SQLException e)
                                              {
                                                  e.printStackTrace();
                                              }

                                       }
                                   });
                                   btnClose.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                              dialog.dismiss();
                                       }

                                   });
                                   try {
                                       Statement statement1 = connection_staff_information.createStatement();
                                       ResultSet resultSet = statement1.executeQuery("SELECT *FROM ACCOUNT WHERE Account_name ="+"'"+selectedItem.getID()+"';");
                                       while(resultSet.next()) {
                                           txt_ID.setText(resultSet.getString(1).trim());
                                           txt_pass.setText(resultSet.getString(2).trim());
                                           txt_role.setText(resultSet.getString(3).trim());
                                       }
                                       resultSet.close();
                                       statement1.close();
                                   }catch (SQLException e)
                                   {
                                       e.printStackTrace();
                                   }
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            }
                        });
                    }
                    else
                    {
                        binding.radioStatus.setVisibility(View.INVISIBLE);
                        binding.accountAdding.setVisibility(View.VISIBLE);
                        binding.accountCheck.setVisibility(View.INVISIBLE);
                        binding.accountAdding.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Dialog dialog = new Dialog(StaffInformation.this);
                                dialog.setContentView(R.layout.activity_account_management_dialog);
                                EditText txt_ID = dialog.findViewById(R.id.ID);
                                EditText txt_pass = dialog.findViewById(R.id.pass);
                                EditText txt_role = dialog.findViewById(R.id.role);
                                txt_ID.setText(selectedItem.getID());
                                txt_role.setText(selectedItem.getPosition().toUpperCase());
                                ImageView btnClose = dialog.findViewById(R.id.close);
                                Button btnSave = dialog.findViewById(R.id.save);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                                btnClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                       public void onClick(View view) {
                                           if (CheckFormAccount(txt_ID.getText().toString().trim(), txt_pass.getText().toString().trim(), txt_role.getText().toString().trim()))
                                           {
                                               try {
                                                   Statement statement1 = connection_staff_information.createStatement();
                                                   statement1.execute("INSERT INTO ACCOUNT(Account_name , Pass , Position , Status)\n" +
                                                           "VALUES("+"'"+txt_ID.getText().toString().trim()+"'"+","+"'"+txt_pass.getText().toString().trim()+"'"+","+"'"+txt_role.getText().toString().trim()+"'"+","+"'inactive'"+")");
                                                   dialog.dismiss();
                                                   Toast.makeText(getApplicationContext(), "Adding successfully" , Toast.LENGTH_LONG).show();
                                                   onStart();
                                               }catch (SQLException e)
                                               {
                                                   e.printStackTrace();
                                               }

                                           }

                                       }
                                   });
                            }
                        });
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
    private boolean CheckFormAccount(String ID, String password , String role)
    {
        if (ID.isEmpty() || ID.length()>10)
        {
            Toast.makeText(getApplicationContext(), "ID is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(password.isEmpty()||password.length()>10)
        {
            Toast.makeText(getApplicationContext(), "Password is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(role.isEmpty()||role.length()>10)
        {
            Toast.makeText(getApplicationContext(), "Position is error." , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private void Chatting()
    {
        binding.chat.setOnClickListener(view -> {
            Intent intent_call = new Intent();
            intent_call.setAction(Intent.ACTION_CALL);
            intent_call.setData(Uri.parse("tel: "+selectedItem.getPhoneNumber()));
            startActivity(intent_call);
        });
    }
    private void Delete()
    {
        binding.delete.setOnClickListener(view -> {
            Dialog dialog = new Dialog(StaffInformation.this);
            dialog.setContentView(R.layout.activity_staff_delete_dialog);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            Button btnYes = dialog.findViewById(R.id.yesDeleteBtn);
            Button btnNo = dialog.findViewById(R.id.noDeleteBtn);
            btnYes.setOnClickListener(view1 -> {
                if (connection_staff_information != null) {
                    try {
                        Statement statement = connection_staff_information.createStatement();
                        boolean check = statement.execute("update STAFF\n" +
                                "set Status = 'inactive'\n" +
                                "where Staff_ID = '" + selectedItem.getID() + "'");
                        if (check) {
                            Toast.makeText(getApplicationContext(), "Delete unsuccessfully" , Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }else
                        {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Delete successfully" , Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "Connect to StaffDB makes error." , Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

            });
            btnNo.setOnClickListener(view12 -> dialog.dismiss());
        });

    }
    private void Edit()
    {
        binding.edit.setOnClickListener(view -> {
            Dialog dialog = new Dialog(StaffInformation.this);
            dialog.setContentView(R.layout.activity_staff_edit_dialog);
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            ImageView btnClose = dialog.findViewById(R.id.close);
            Button btnSave = dialog.findViewById(R.id.save);
            EditText txt_ID = dialog.findViewById(R.id.ID);
            EditText txt_name = dialog.findViewById(R.id.Name);
            EditText txt_status = dialog.findViewById(R.id.status);
            EditText txt_position = dialog.findViewById(R.id.position);
            EditText txt_gender = dialog.findViewById(R.id.gender);
            EditText txt_phonenumber = dialog.findViewById(R.id.PhoneNumber);
            EditText txt_email = dialog.findViewById(R.id.email);
            EditText txt_salary = dialog.findViewById(R.id.Salary);
            EditText txt_image = dialog.findViewById(R.id.image);
            txt_ID.setText(selectedItem.getID());
            txt_name.setText(selectedItem.getName());
            txt_status.setText(selectedItem.getStatus());
            txt_position.setText(selectedItem.getPosition());
            txt_gender.setText(selectedItem.getGender());
            txt_phonenumber.setText(selectedItem.getPhoneNumber());
            txt_email.setText(selectedItem.getEmail());
            txt_salary.setText(String.valueOf(selectedItem.getSalary()));
            txt_image.setText(selectedItem.getImageview());
            btnClose.setOnClickListener(view1 -> dialog.dismiss());
            btnSave.setOnClickListener(view12 -> {
                    if (CheckFormValidation(txt_ID.getText().toString().trim(),txt_name.getText().toString().trim() ,txt_status.getText().toString().trim(),txt_position.getText().toString().trim() , txt_gender.getText().toString().trim(),txt_phonenumber.getText().toString().trim(), txt_email.getText().toString().trim() , txt_salary.getText().toString().trim(),txt_image.getText().toString().trim()))
                    {
                        if (connection_staff_information!= null)
                        {
                            try
                            {
                                Statement statement = connection_staff_information.createStatement();
                                boolean check = statement.execute("UPDATE STAFF\n" +
                                        "SET Name_of_staff ="+"'"+txt_name.getText().toString().trim()+"'"+","+"Position ="+"'"+txt_position.getText().toString().trim()+"'"+","+"Gender ="+"'"+txt_gender.getText().toString().trim()+"'"+","+"PhoneNumber="+"'"+txt_phonenumber.getText().toString().trim()+"'"+","+"Salary_of_staff="+"'"+txt_salary.getText().toString().trim()+"'"+","+"ProvivedImage="+"'"+txt_image.getText().toString().trim()+"'"+","+"Email="+"'"+txt_email.getText().toString().trim()+"'"+","+"Status="+"'"+txt_status.getText().toString().trim()+"'"+"\n"+
                                        "WHERE Staff_ID ="+"'"+selectedItem.getID()+"';");
                                if(check==false)
                                {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext() , "Update successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }else
                                {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Update unsuccessfully" , Toast.LENGTH_LONG).show();
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
            });
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
        binding.arrowBack.setOnClickListener(view -> finish());
    }
    private void ShowItem() {
        binding.textView2.setText(selectedItem.getName());
        Picasso.get().load(selectedItem.getImageview()).fit().into(binding.roundedImageViewpersonal);
        arrayList = new ArrayList<>();
        arrayList.add(new Detail_Profile(R.drawable.accountname, "ID:", selectedItem.getID()));
        arrayList.add(new Detail_Profile(R.drawable.role, "Position:", selectedItem.getPosition()));
        arrayList.add(new Detail_Profile(R.drawable.gender, "Gender:", selectedItem.getGender()));
        arrayList.add(new Detail_Profile(R.drawable.phonenumber, "Phone Number:", selectedItem.getPhoneNumber()));
        arrayList.add(new Detail_Profile(R.drawable.salary, "Salary:", String.valueOf(selectedItem.getSalary())));
        arrayList.add(new Detail_Profile(R.drawable.email, "Email:", selectedItem.getEmail()));
        arrayList.add(new Detail_Profile(R.drawable.status , "Status:", selectedItem.getStatus()));
        informationAdapter = new InformationAdapter(arrayList);
        informationAdapter.notifyDataSetChanged();
        binding.listItem.setAdapter(informationAdapter);
    }
}