package com.example.mylovebeverage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Fragments.OthersExpenseFragment;
import com.example.mylovebeverage.Fragments.SalaryStaffFragment;
import com.example.mylovebeverage.Singleton.MySingleton;
import com.example.mylovebeverage.databinding.ActivityManageExpenseBinding;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class ManageExpense extends AppCompatActivity {
    private ActivityManageExpenseBinding binding;
    private String type_expense_invoice = "";
    private Connection connection_manage_expense = null;
    private static String Staff_ID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityManageExpenseBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.other_expense, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        Staff_ID = MySingleton.getInstance().getVariable();


    }
    @Override
    protected void onStart() {
        super.onStart();
        Back_previous_activity();
        CatchItemSpinner();
        StartUpFragment();
        SetupFromDate();
        SetupToDate();
        SetupCheck();
        Adding();
        DeleteAll();
    }
    private void CatchItemSpinner()
    {
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(20);
                type_expense_invoice = adapterView.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void StartUpFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = null;
        if(binding.tablayout.getSelectedTabPosition()==0)
        {
            SalaryStaffFragment salaryStaffFragment = new SalaryStaffFragment();
            salaryStaffFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frame , salaryStaffFragment).commit();
        }else
        {
            OthersExpenseFragment othersExpenseFragment = new OthersExpenseFragment();
            othersExpenseFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frame , othersExpenseFragment).commit();
        }
    }
    private void SetUpDate(EditText edt_date , ImageButton img_calendar)
    {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day  = calendar.get(Calendar.DATE);
        img_calendar.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ManageExpense.this, (datePicker, year1, month1, day1) -> {
                month1 = month1 + 1;
                String date = day1 +"/"+ month1 +"/"+ year1;
                edt_date.setText(date);
            },year , month , day);
            datePickerDialog.show();
        });
    }
    private void ProcessSpinner(EditText editText , Spinner spinner)
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l)
            {
                        editText.setText(adapterView.getAdapter().getItem(position).toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private boolean CheckFormValidation(String str1, String str2 , String str3 , String str4 ,String str5 ,String str6 , String str7 , String str8)
    {
        if (str1.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(str2.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(str3.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(str4.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(str5.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(str6.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(str7.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        if(str8.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Fill the blank." , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private String Processing_ID_invoice(String icon , String type )
    {
        String Quantity ="";
        String ID ="";
        try {
            Statement statement1 = connection_manage_expense.createStatement();
            ResultSet resultSet1 = statement1.executeQuery("\n" +
                    "SELECT COUNT(Invoice_ID) as Quantity\n" +
                    "FROM OTHEREXPENSE\n" +
                    "WHERE Kind_of_Invoice ="+"'"+type+"'");
            while (resultSet1.next())
            {
                Quantity = resultSet1.getString(1).trim();
            }
            resultSet1.close();
            if(Integer.parseInt(Quantity) + 1 >= 100)
            {
                ID = icon + (Integer.parseInt(Quantity) + 1);
            }else if (Integer.parseInt(Quantity) + 1 >= 10)
            {
                ID = icon + "0" + (Integer.parseInt(Quantity) + 1);
            }else
            {
                ID = icon + "00" + (Integer.parseInt(Quantity) + 1);
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ID;
    }
    private boolean AddingInformation(ArrayList<String> arrayListInformationInvoice , String str)
    {
        connection_manage_expense = new Connecting_MSSQL(connection_manage_expense).Connecting();
        if (connection_manage_expense != null)
        {
            String Quantity ="";
            String ID ="";
            if(str.equals("Salary"))
            {
                try {
                    Statement statement1 = connection_manage_expense.createStatement();
                    ResultSet resultSet1 = statement1.executeQuery("SELECT COUNT(StaffSalaryInvoice_ID) as Quantity\n" +
                            "FROM EXPENSESTAFFSALARY;");
                    while (resultSet1.next())
                    {
                        Quantity = resultSet1.getString(1).trim();
                    }
                    resultSet1.close();
                    if(Integer.parseInt(Quantity) + 1 >= 10)
                    {
                        ID = "SA" + (Integer.parseInt(Quantity) + 1);
                    }else
                    {
                        ID = "SA0" + (Integer.parseInt(Quantity) + 1);
                    }
                    Statement statement2 = connection_manage_expense.createStatement();
                    statement2.execute("INSERT INTO EXPENSESTAFFSALARY\n" +
                            "VALUES("+"'"+ID+"'"+ ","+ Integer.parseInt(arrayListInformationInvoice.get(1).trim())+","+ "'"+arrayListInformationInvoice.get(0).trim()+"'"+","+"'"+arrayListInformationInvoice.get(6).trim()+"'"+","+"'"+arrayListInformationInvoice.get(5).trim()+"'"+","+"'"+arrayListInformationInvoice.get(7).trim()+"'"+","+"'"+arrayListInformationInvoice.get(2).trim()+"'" +","+Integer.parseInt(arrayListInformationInvoice.get(3).trim())+","+ Integer.parseInt(arrayListInformationInvoice.get(4).trim())+");");
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }else
            {
                String ID_other_invoice ="";
                switch (arrayListInformationInvoice.get(5).trim())
                {
                    case "Electricity":
                         ID_other_invoice = Processing_ID_invoice("E" , "Electricity");
                         break;
                    case "Water":
                         ID_other_invoice=Processing_ID_invoice("WA" , "Water");
                         break;
                    case "Wifi":
                         ID_other_invoice =Processing_ID_invoice("WFI" , "Wifi");
                         break;
                }
                try {
                    Statement statement = connection_manage_expense.createStatement();
                    statement.execute("INSERT INTO OTHEREXPENSE\n" +
                            "VALUES("+"'"+ID_other_invoice+"'"+ ","+"'"+arrayListInformationInvoice.get(5).trim()+"'"+","+Integer.parseInt(arrayListInformationInvoice.get(1).trim())+","+"'"+arrayListInformationInvoice.get(0).trim()+"'"+","+"'"+arrayListInformationInvoice.get(7).trim()+"'"+","+"'"+arrayListInformationInvoice.get(2).trim()+"'"+","+Integer.parseInt(arrayListInformationInvoice.get(3).trim())+","+Integer.parseInt(arrayListInformationInvoice.get(4).trim())+","+"'"+arrayListInformationInvoice.get(6).trim()+"'"+");");
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }

            }
        }else
        {
            Toast.makeText(ManageExpense.this, "Connect makes error.Please hotline with the administrator!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void AddingStaffSalaryExpense()
    {
        Dialog dialog = new Dialog(ManageExpense.this);
        dialog.setContentView(R.layout.staff_salary_invoice_adding);
        dialog.setCanceledOnTouchOutside(false);
        ImageView btnClose = dialog.findViewById(R.id.close);//nút close
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });//đóng màn hình thêm lương nhân viên
        EditText edt_staff_id = dialog.findViewById(R.id.staff_id);//staff ID
        edt_staff_id.setText(Staff_ID);// set staff ID
        EditText edt_sum_salary = dialog.findViewById(R.id.salary_sum);//lương nhân viên
        EditText edt_date = dialog.findViewById(R.id.date);//ngày nhập hoá đơn
        ImageButton img_calendar = dialog.findViewById(R.id.calendar);//calendar , img
        SetUpDate(edt_date, img_calendar);//hàm giúp nhập calendar
        EditText edt_month = dialog.findViewById(R.id.month);//nhập month
        EditText edt_year = dialog.findViewById(R.id.year);//nhập year
        EditText edt_link_excel = dialog.findViewById(R.id.link_excel);//nhập link excel
        //Khởi tạo spinner
        Spinner spinner_status = dialog.findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> adapter_status = ArrayAdapter.createFromResource(ManageExpense.this, R.array.status, android.R.layout.simple_spinner_item);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(adapter_status);
        EditText edt_status = dialog.findViewById(R.id.status_invoice);
        ProcessSpinner(edt_status, spinner_status);
        //khởi tạo spinner
        Spinner spinner_payment_method = dialog.findViewById(R.id.spinner_payment);
        ArrayAdapter<CharSequence> adapter_payment = ArrayAdapter.createFromResource(ManageExpense.this, R.array.paymentmethod, android.R.layout.simple_spinner_item);
        adapter_payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_payment_method.setAdapter(adapter_payment);
        EditText edt_payment = dialog.findViewById(R.id.payment_method);
        ProcessSpinner(edt_payment, spinner_payment_method);
        Button btnSave = dialog.findViewById(R.id.save);//tạo button save
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (true) {
                    btnSave.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    if (CheckFormValidation(edt_staff_id.getText().toString().trim(), edt_sum_salary.getText().toString().trim(), edt_date.getText().toString().trim(), edt_month.getText().toString().trim(), edt_year.getText().toString().trim(), edt_link_excel.getText().toString().trim(), edt_status.getText().toString().trim(), edt_payment.getText().toString().trim())) {
                        ArrayList<String> arrayListStaffSalaryInvoice = new ArrayList<>();
                         arrayListStaffSalaryInvoice.add(edt_staff_id.getText().toString().trim()); //0
                         arrayListStaffSalaryInvoice.add(edt_sum_salary.getText().toString().trim());//1
                         arrayListStaffSalaryInvoice.add(edt_date.getText().toString().trim());//2
                         arrayListStaffSalaryInvoice.add( edt_month.getText().toString().trim());//3
                         arrayListStaffSalaryInvoice.add(edt_year.getText().toString().trim());//4
                         arrayListStaffSalaryInvoice.add(edt_link_excel.getText().toString().trim());//5
                         arrayListStaffSalaryInvoice.add(edt_status.getText().toString().trim());//6
                         arrayListStaffSalaryInvoice.add( edt_payment.getText().toString().trim());//7
                         if(AddingInformation(arrayListStaffSalaryInvoice , "Salary"))
                         {
                             Toast.makeText(ManageExpense.this, "Adding Successfully!", Toast.LENGTH_SHORT).show();
                             break;
                         }
                         else
                         {
                             break;
                         }
                     }else
                     {
                         break;
                     }
                 }
                btnSave.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
        dialog.show();
    }
    private void AddingOtherExpense()
    {
        Dialog dialog = new Dialog(ManageExpense.this);
        dialog.setContentView(R.layout.others_invoice_adding);
        dialog.setCanceledOnTouchOutside(false);
        ImageView btnClose = dialog.findViewById(R.id.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }); //xử lý đóng khối lệnh
        EditText edt_staff_id = dialog.findViewById(R.id.staff_id);//staff ID
        edt_staff_id.setText(Staff_ID); //set staff ID
        EditText edt_month = dialog.findViewById(R.id.month);//nhập month
        EditText edt_year = dialog.findViewById(R.id.year);//nhập year
        EditText edt_price = dialog.findViewById(R.id.salary_sum);//tổng tiền hoá đơn
        EditText edt_date = dialog.findViewById(R.id.date);//ngày nhập hoá đơn
        ImageButton img_calendar = dialog.findViewById(R.id.calendar);//calendar , img
        SetUpDate(edt_date, img_calendar);//hàm giúp nhập calendar
        //khởi tạo spinner
        Spinner spinner_status = dialog.findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> adapter_status = ArrayAdapter.createFromResource(ManageExpense.this, R.array.status, android.R.layout.simple_spinner_item);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(adapter_status);
        EditText edt_status = dialog.findViewById(R.id.status_invoice);
        ProcessSpinner(edt_status, spinner_status);
        //khởi tạo spinner
        Spinner spinner_payment_method = dialog.findViewById(R.id.spinner_payment);
        ArrayAdapter<CharSequence> adapter_payment = ArrayAdapter.createFromResource(ManageExpense.this, R.array.paymentmethod, android.R.layout.simple_spinner_item);
        adapter_payment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_payment_method.setAdapter(adapter_payment);
        EditText edt_payment = dialog.findViewById(R.id.payment_method);
        ProcessSpinner(edt_payment, spinner_payment_method);
        //Khởi tạo spinner
        Spinner spinner_type_invoice = dialog.findViewById(R.id.spinner_type_invoice);
        ArrayAdapter<CharSequence> adapter_type_invoice = ArrayAdapter.createFromResource(ManageExpense.this, R.array.other_expense, android.R.layout.simple_spinner_item);
        adapter_type_invoice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type_invoice.setAdapter(adapter_type_invoice);
        EditText edt_type_invoice = dialog.findViewById(R.id.type_invoice);
        ProcessSpinner(edt_type_invoice, spinner_type_invoice);
        //Save
        Button btnSave = dialog.findViewById(R.id.save);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (true) {
                    btnSave.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    if (CheckFormValidation(edt_staff_id.getText().toString().trim(), edt_price.getText().toString().trim(), edt_date.getText().toString().trim(), edt_month.getText().toString().trim(), edt_year.getText().toString().trim(), edt_type_invoice.getText().toString().trim(), edt_status.getText().toString().trim(), edt_payment.getText().toString().trim())) {
                        ArrayList<String> arrayListOtherInvoice = new ArrayList<>();
                        arrayListOtherInvoice.add(edt_staff_id.getText().toString().trim()); //0
                        arrayListOtherInvoice.add(edt_price.getText().toString().trim());//1
                        arrayListOtherInvoice.add(edt_date.getText().toString().trim());//2
                        arrayListOtherInvoice.add( edt_month.getText().toString().trim());//3
                        arrayListOtherInvoice.add(edt_year.getText().toString().trim());//4
                        arrayListOtherInvoice.add(edt_type_invoice.getText().toString().trim());//5
                        arrayListOtherInvoice.add(edt_status.getText().toString().trim());//6
                        arrayListOtherInvoice.add( edt_payment.getText().toString().trim());//7
                        if(AddingInformation(arrayListOtherInvoice , "Other"))
                        {
                            Toast.makeText(ManageExpense.this, "Adding Successfully!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else
                        {
                            break;
                        }
                    }else
                    {
                        break;
                    }
                }
                btnSave.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        dialog.show();
    }
    private void Adding()
    {
        binding.floatingaddingactionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(binding.tablayout.getSelectedTabPosition()==0)
                {
                    AddingStaffSalaryExpense();
                }else
                {
                    AddingOtherExpense();
                }
            }
        });
    }
    private void DeleteAll()
    {
        binding.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   StartUpFragment();
                   binding.fromTime.setText("");
                   binding.toTime.setText("");
            }
        });
    }

    private boolean Check_date(String from_date , String to_date)
    {
        if(from_date.isEmpty())
        {
            Toast.makeText(getApplicationContext() , "Please enter the from date", Toast.LENGTH_SHORT).show();
            return false;
        }else if (to_date.isEmpty())
        {
            Toast.makeText(getApplicationContext() , "Please enter the end date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private ArrayList<String> Processingdate(String date)
    {
        ArrayList<String> array = new ArrayList<>();
        String temp_number ="" ;
        for (int i =0 ;i<date.length();i++)
        {
            if (date.charAt(i) =='/')
            {
                array.add(temp_number);
                temp_number = "";

            }else
            {
                temp_number += date.charAt(i);
            }
        }
        array.add(temp_number);
        return array;
    }
    private boolean Check_valid_date(String fmonth , String fyear , String tmonth , String tyear)
    {
        if(Integer.parseInt(fyear) > Integer.parseInt(tyear))
        {
            return false;

        }else if (Integer.valueOf(fyear).equals(Integer.valueOf(tyear)))
        {
            return Integer.parseInt(fmonth) <= Integer.parseInt(tmonth);
        }else
        {
            return true;
        }
    }
    private void SetupCheck()
    {
        binding.See.setOnClickListener(view -> {
            if (Check_date(binding.fromTime.getText().toString().trim() , binding.toTime.getText().toString().trim()))
            {
                ArrayList<String> array_from_time;
                array_from_time = Processingdate(binding.fromTime.getText().toString().trim());
                ArrayList<String> array_to_time;
                array_to_time = Processingdate(binding.toTime.getText().toString().trim());
                String from_month = array_from_time.get(0);
                String from_year = array_from_time.get(1);
                String to_month = array_to_time.get(0);
                String to_year = array_to_time.get(1);
                if(Check_valid_date(from_month , from_year , to_month , to_year))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("FromMonth" , from_month);
                    bundle.putString("FromYear" , from_year);
                    bundle.putString("ToMonth" , to_month);
                    bundle.putString("ToYear" , to_year);
                    if(binding.tablayout.getSelectedTabPosition() == 0)
                    {
                        SalaryStaffFragment salaryStaffFragment = new SalaryStaffFragment();
                        salaryStaffFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.frame , salaryStaffFragment).commit();
                    }else
                    {
                        bundle.putString("Type" , type_expense_invoice);
                        OthersExpenseFragment othersExpenseFragment = new OthersExpenseFragment();
                        othersExpenseFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.frame , othersExpenseFragment).commit();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext() , "You enter wrong date", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void SetupToDate()
    {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day  = calendar.get(Calendar.DATE);
        binding.calendarTotime.setOnClickListener(view -> {
               DatePickerDialog datePickerDialog = new DatePickerDialog(ManageExpense.this, (datePicker, year1, month1, day1) -> {
                   month1 = month1 + 1;
                   String date = month1 +"/"+ year1;
                   binding.toTime.setText(date);
               },year , month , day);
               datePickerDialog.show();
        });
    }
    private void SetupFromDate()
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);
        binding.calendarFromtime.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ManageExpense.this, (datePicker, year1, month1, day1) -> {
                   month1 = month1 + 1;
                   String date = month1 +"/"+ year1;
                   binding.fromTime.setText(date);
            },year , month, day);
            datePickerDialog.show();
        });
    }
    private void Back_previous_activity()
    {
        binding.arrowBack.setOnClickListener(view -> finish());
    }

}