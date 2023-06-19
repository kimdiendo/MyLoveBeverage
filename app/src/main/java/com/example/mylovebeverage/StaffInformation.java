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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Detail_Human_Resource;
import com.example.mylovebeverage.Adapters.InformationAdapter;
import com.example.mylovebeverage.Models.Detail_Profile;
import com.example.mylovebeverage.databinding.ActivityStaffInformationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
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
    private static final int PERMISSION_REQUEST_CODE_CALL = 123;
    private static final int PERMISSION_REQUEST_CODE_CHAT = 110;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStaffInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        auth = FirebaseAuth.getInstance();
        selectedItem = new Detail_Human_Resource(intent.getStringExtra("ID"), intent.getStringExtra("Name"), intent.getStringExtra("Position"), intent.getStringExtra("Gender"), intent.getStringExtra("PhoneNumber"), intent.getIntExtra("Salary", 0), intent.getStringExtra("Image"), intent.getStringExtra("Email"), intent.getStringExtra("Status"));
    }
    @Override
    protected void onStart() {
        super.onStart();
        connection_staff_information = new Connecting_MSSQL(connection_staff_information).Connecting();
        CheckAccount();
        Back_previous_activity();
        ShowItem();
        Calling();
        Chatting();
        Register_FirebaseAuth();
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


    private void Register_FirebaseAuth() {
        binding.registerFirebaseAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(StaffInformation.this);
                dialog.setContentView(R.layout.activity_firebaseauth_dialog);
                ImageView btn_close = dialog.findViewById(R.id.close);
                dialog.show();
                auth.fetchSignInMethodsForEmail(selectedItem.getEmail().trim()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            boolean account_exist = result != null && result.getSignInMethods() != null
                                    && !result.getSignInMethods().isEmpty();
                            if (account_exist) {
                                //đã có tài khoản trong firebase authenticate
                                TextView txt_checked = dialog.findViewById(R.id.checked);
                                txt_checked.setVisibility(View.VISIBLE);
                            } else {
                                //chưa có tài khoản
                                TextView txt_email = dialog.findViewById(R.id.textView3);
                                txt_email.setVisibility(View.VISIBLE);
                                TextView txt_password = dialog.findViewById(R.id.textView);
                                txt_password.setVisibility(View.VISIBLE);
                                TextView txt_note = dialog.findViewById(R.id.textView7);
                                txt_note.setVisibility(View.VISIBLE);
                                EditText editText_mail = dialog.findViewById(R.id.ID);
                                editText_mail.setVisibility(View.VISIBLE);
                                EditText editText_password = dialog.findViewById(R.id.pass);
                                editText_password.setVisibility(View.VISIBLE);
                                editText_mail.setText(selectedItem.getEmail().trim());
                                Button btn_register = dialog.findViewById(R.id.Register);
                                btn_register.setVisibility(View.VISIBLE);
                                btn_register.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        auth.createUserWithEmailAndPassword(editText_mail.getText().toString().trim(), editText_password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Register Firebase Authentication successfully", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Register Firebase Authentication Failed", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Cannot connect App with Firebase Authentication", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Cannot connect App with Firebase Authentication", Toast.LENGTH_LONG).show();
                    }
                });
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    private void CheckAccountStatus() {
        String status = "";
        try {
            Statement statement = connection_staff_information.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Status FROM ACCOUNT WHERE Account_name =" + "'" + selectedItem.getID() + "';");
            while (resultSet.next()) {
                status = resultSet.getString(1).toString().trim();
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
        if(password.isEmpty()||password.length()>10) {
            Toast.makeText(getApplicationContext(), "Password is error.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (role.isEmpty() || role.length() > 10) {
            Toast.makeText(getApplicationContext(), "Position is error.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void requestCallPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
                //provide explannation to user that s' why they must provide permission
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE_CALL);
        } else {
            // Permission has already been granted
        }
    }

    public void requestChatPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                //provide explanation for user that s' why they need to chat
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE_CHAT);
        } else {
            //Permission has been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //calling
            } else {
                Toast.makeText(this, "Calling Permission has denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE_CHAT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //chatting
            } else {

                Toast.makeText(this, "Chatting Permission has denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void openCalling() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent_call = new Intent();
            intent_call.setAction(Intent.ACTION_CALL);
            intent_call.setData(Uri.parse("tel: " + selectedItem.getPhoneNumber()));
            startActivity(intent_call);
        } else {
            // Permission has not been granted, request it
            requestCallPermission();
        }
    }

    private void Calling() {
        binding.call.setOnClickListener(view -> {
            openCalling();
        });
    }
    private void openChatting() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + selectedItem.getPhoneNumber()));
            intent.putExtra("sms_body", "Hello " + selectedItem.getName());
            startActivity(intent);
        } else {
            requestChatPermission();
        }
    }
    private void Chatting() {
        binding.chat.setOnClickListener(view -> {
            openChatting();
        });
    }

    private void Delete() {
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
                        }else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Delete successfully" , Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Connect to StaffDB makes error.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

            });
            btnNo.setOnClickListener(view12 -> dialog.dismiss());
        });

    }

    private void Handling_Selection_Events(Spinner spinner, EditText txt) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                txt.setText(adapterView.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void Edit() {
        binding.edit.setOnClickListener(view -> {
            Dialog dialog = new Dialog(StaffInformation.this);
            dialog.setContentView(R.layout.activity_staff_edit_dialog);
            //create spinner position
            ArrayAdapter<CharSequence> adapter_position = ArrayAdapter.createFromResource(StaffInformation.this, R.array.type_position, android.R.layout.simple_spinner_item);
            adapter_position.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            /// create spinner gender
            ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(StaffInformation.this, R.array.type_gender, android.R.layout.simple_spinner_item);
            adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // create spinner status
            ArrayAdapter<CharSequence> adapter_status = ArrayAdapter.createFromResource(StaffInformation.this, R.array.type_status_working, android.R.layout.simple_spinner_item);
            adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Spinner
            Spinner spinner_position = dialog.findViewById(R.id.img_position);
            Spinner spinner_gender = dialog.findViewById(R.id.img_gender);
            Spinner spinner_status_working = dialog.findViewById(R.id.img_status);
            spinner_position.setAdapter(adapter_position);
            spinner_gender.setAdapter(adapter_gender);
            spinner_status_working.setAdapter(adapter_status);
            spinner_position.setSelection(0, false);
            //spinner_position.setSelection(-1);
            //spinner_gender.setSelection(-1);
            spinner_gender.setSelection(0, false);
            //spinner_status_working.setSelection(-1);
            spinner_status_working.setSelection(0, false);
            //
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
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            //Xử lý sự lựa chọn sự kiện
            Handling_Selection_Events(spinner_position, txt_position);
            Handling_Selection_Events(spinner_gender, txt_gender);
            Handling_Selection_Events(spinner_status_working, txt_status);
            btnClose.setOnClickListener(view1 -> dialog.dismiss());
            btnSave.setOnClickListener(view12 -> {
                if (CheckFormValidation(txt_ID.getText().toString().trim(), txt_name.getText().toString().trim(), txt_status.getText().toString().trim(), txt_position.getText().toString().trim(), txt_gender.getText().toString().trim(), txt_phonenumber.getText().toString().trim(), txt_email.getText().toString().trim(), txt_salary.getText().toString().trim(), txt_image.getText().toString().trim())) {
                    if (connection_staff_information != null) {
                        try {
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