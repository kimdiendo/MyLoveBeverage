package com.example.mylovebeverage.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinner;
    EditText edt_from_time;
    EditText edt_to_time;
    ImageButton img_calendar_from_time;
    ImageButton img_calendar_to_time;
    Button btnCheck , btnClear ;
    private String type="";
    Connection connection_statistic;
    private int fmonth , fyear , tmonth  , tyear;
    BarChart barChart;
    TextView txt_status ;
    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void SetupSpinner()
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.type_statistic ,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void SetupToDate()
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);
        img_calendar_to_time.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                month1 = month1 + 1;
                String date = month1 +"/"+ year1;
                edt_to_time.setText(date);
            },year , month, day);
            datePickerDialog.show();
        });
    }
    private void SetupFromDate()
    {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);
        img_calendar_from_time.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, day1) -> {
                month1 = month1 + 1;
                String date = month1 +"/"+ year1;
                edt_from_time.setText(date);
            },year , month, day);
            datePickerDialog.show();
        });
    }
    private boolean Check_date(String from_date , String to_date)
    {
        if(from_date.isEmpty())
        {
            Toast.makeText(getContext(), "Please enter the from date", Toast.LENGTH_SHORT).show();
            return false;
        }else if (to_date.isEmpty())
        {
            Toast.makeText(getContext(), "Please enter the end date", Toast.LENGTH_SHORT).show();
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
    private void CatchItemSpinner()
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(20);
                type = adapterView.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void ShowInformationOtherExpense(ArrayList<Pair<Integer,Integer>> arrayListStatistic)
    {
        try {
            Statement statement1 = connection_statistic.createStatement();
            Statement statement = connection_statistic.createStatement();
            if (fyear == tyear) {
                ResultSet resultSet = statement.executeQuery("SELECT *\n" +
                        "FROM OTHEREXPENSE\n" +
                        "WHERE Kind_of_Invoice = "+"'"+type+"'"+ " AND Month_Invoice BETWEEN " + fmonth + " AND " + tmonth + "\n"+
                        "ORDER BY Month_Invoice");
                while (resultSet.next()) {
                       arrayListStatistic.add(new Pair<Integer, Integer>(resultSet.getInt(3) , resultSet.getInt(7)));
                }
                resultSet.close();
            } else
            {
                ResultSet resultSet1 = statement1.executeQuery("SELECT *\n" +
                        "FROM OTHEREXPENSE\n" +
                        "WHERE  Kind_of_Invoice = "+ "'"+type+"'" +" and (Year_Invoice BETWEEN " + fyear + " AND " + tyear + ") and (Month_Invoice BETWEEN " + fmonth + " AND 12)\n"+
                        "ORDER BY Year_Invoice , Month_Invoice");
                ResultSet resultSet2 = statement.executeQuery("SELECT *\n" +
                        "FROM OTHEREXPENSE\n" +
                        "WHERE  Kind_of_Invoice = "+ "'"+type+"'" +" and (Month_Invoice BETWEEN 1 AND "+tmonth+ ") AND (Year_Invoice BETWEEN " + fyear + " AND " + tyear + ")\n" +
                        "ORDER BY Year_Invoice , Month_Invoice");
                while (resultSet1.next()) {
                    arrayListStatistic.add(new Pair<Integer ,Integer>(resultSet1.getInt(3) , resultSet1.getInt(7)));
                }
                while (resultSet2.next()) {
                    arrayListStatistic.add(new Pair<Integer , Integer>(resultSet2.getInt(3) , resultSet2.getInt(7)));
                }
                resultSet1.close();
                resultSet2.close();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    private void ShowInformationStaffSalary(ArrayList<Pair<Integer,Integer>> arrayListStatistic)
    {
        try {
            Statement statement1 = connection_statistic.createStatement();
            Statement statement = connection_statistic.createStatement();
            if (fyear == tyear) {
                ResultSet resultSet = statement.executeQuery("\n" +
                        "SELECT *\n" +
                        "FROM EXPENSESTAFFSALARY \n" +
                        "WHERE Month_Invoice BETWEEN " + fmonth + " AND " + tmonth + "\n" +
                        "ORDER BY Month_Invoice");
                while (resultSet.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet.getInt(2), resultSet.getInt(8)));
                }
                resultSet.close();
            } else {
                ResultSet resultSet1 = statement1.executeQuery("SELECT *\n" +
                        "FROM EXPENSESTAFFSALARY\n" +
                        "WHERE  (Year_Invoice BETWEEN " + fyear + " AND " + tyear + ") and (Month_Invoice BETWEEN " + fmonth + " AND 12)\n" +
                        "ORDER BY Year_Invoice , Month_Invoice");
                ResultSet resultSet2 = statement.executeQuery("SELECT *\n" +
                        "FROM EXPENSESTAFFSALARY\n" +
                        "WHERE (Month_Invoice BETWEEN 1 AND " + tmonth + ") AND (Year_Invoice BETWEEN " + fyear + " AND " + tyear + ") \n" +
                        "ORDER BY Year_Invoice , Month_Invoice\n");
                while (resultSet1.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet1.getInt(2), resultSet1.getInt(8)));
                }
                while (resultSet2.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet2.getInt(2), resultSet2.getInt(8)));
                }
                resultSet1.close();
                resultSet2.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void ShowInformationWarehouseExpense(ArrayList<Pair<Integer,Integer>> arrayListStatistic)
    {
        try {
            Statement statement1 = connection_statistic.createStatement();
            Statement statement = connection_statistic.createStatement();
            if (fyear == tyear) {
                ResultSet resultSet = statement.executeQuery("SELECT *\n" +
                        "FROM\n" +
                        "(SELECT MONTH(DateTime_of_WarehouseInvoice) as Month, YEAR(DateTime_of_WarehouseInvoice) as Year, SUM(Price_of_WarehouseInvoice) as Sum\n" +
                        "FROM WAREHOUSEINVOICE\n" +
                        "GROUP BY YEAR(DateTime_of_WarehouseInvoice) , MONTH(DateTime_of_WarehouseInvoice) ) AS NEW_TABLE_INVOICE_WAREHOUSE\n" +
                        "WHERE  Year = " +"'"+fyear+"'"+" and (Month BETWEEN " + fmonth +" AND "+ tmonth+")");
                while (resultSet.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet.getInt(3), resultSet.getInt(1)));
                }
                resultSet.close();
            } else {
                ResultSet resultSet1 = statement1.executeQuery("SELECT *\n" +
                        "FROM\n" +
                        "(SELECT MONTH(DateTime_of_WarehouseInvoice) as Month, YEAR(DateTime_of_WarehouseInvoice) as Year, SUM(Price_of_WarehouseInvoice) as Sum\n" +
                        "FROM WAREHOUSEINVOICE\n" +
                        "GROUP BY YEAR(DateTime_of_WarehouseInvoice) , MONTH(DateTime_of_WarehouseInvoice) ) AS NEW_TABLE_INVOICE_WAREHOUSE\n" +
                        "WHERE  (Year BETWEEN " + fyear + " AND " + tyear + ") and (Month BETWEEN " + fmonth + " AND 12)");
                ResultSet resultSet2 = statement.executeQuery("SELECT *\n" +
                        "FROM\n" +
                        "(SELECT MONTH(DateTime_of_WarehouseInvoice) as Month, YEAR(DateTime_of_WarehouseInvoice) as Year, SUM(Price_of_WarehouseInvoice) as Sum\n" +
                        "FROM WAREHOUSEINVOICE\n" +
                        "GROUP BY YEAR(DateTime_of_WarehouseInvoice) , MONTH(DateTime_of_WarehouseInvoice) ) AS NEW_TABLE_INVOICE_WAREHOUSE\n" +
                        "WHERE (Month BETWEEN 1 AND " + tmonth + ") AND (Year BETWEEN " + fyear + " AND " + tyear +")");
                while (resultSet1.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet1.getInt(3), resultSet1.getInt(1)));
                }
                while (resultSet2.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet2.getInt(3), resultSet2.getInt(1)));
                }
                resultSet1.close();
                resultSet2.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void ShowInformationInvoiceExpense(ArrayList<Pair<Integer , Integer>>arrayListStatistic)
    {
        try {
            Statement statement1 = connection_statistic.createStatement();
            Statement statement = connection_statistic.createStatement();
            if (fyear == tyear) {
                ResultSet resultSet = statement.executeQuery("SELECT *\n" +
                        "FROM\n" +
                        "(SELECT MONTH(Datetime_Invoice) as Month, YEAR(Datetime_Invoice) as Year, SUM(Money_Received) as Sum\n" +
                        "FROM INVOICE\n" +
                        "GROUP BY YEAR(Datetime_Invoice) , MONTH(Datetime_Invoice) ) AS NEW_TABLE_INVOICE_STATISTIC\n" +
                        "WHERE  Year = " +"'"+fyear+"'"+" and (Month BETWEEN " + fmonth +" AND "+ tmonth+")");
                while (resultSet.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet.getInt(3), resultSet.getInt(1)));
                }
                resultSet.close();
            } else {
                ResultSet resultSet1 = statement1.executeQuery("SELECT *\n" +
                        "FROM\n" +
                        "(SELECT MONTH(Datetime_Invoice) as Month, YEAR(Datetime_Invoice) as Year, SUM(Money_Received) as Sum\n" +
                        "FROM INVOICE\n" +
                        "GROUP BY YEAR(Datetime_Invoice) , MONTH(Datetime_Invoice) ) AS NEW_TABLE_INVOICE_STATISTIC\n" +
                        "WHERE  (Year BETWEEN " + fyear + " AND " + tyear + ") and (Month BETWEEN " + fmonth + " AND 12)");
                ResultSet resultSet2 = statement.executeQuery("SELECT *\n" +
                        "FROM\n" +
                        "(SELECT MONTH(Datetime_Invoice) as Month, YEAR(Datetime_Invoice) as Year, SUM(Money_Received) as Sum\n" +
                        "FROM INVOICE\n" +
                        "GROUP BY YEAR(Datetime_Invoice) , MONTH(Datetime_Invoice) ) AS NEW_TABLE_INVOICE_STATISTIC\n" +
                        "WHERE (Month BETWEEN 1 AND " + tmonth + ") AND (Year BETWEEN " + fyear + " AND " + tyear +")");
                while (resultSet1.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet1.getInt(3), resultSet1.getInt(1)));
                }
                while (resultSet2.next())
                {
                    arrayListStatistic.add(new Pair<Integer, Integer>(resultSet2.getInt(3), resultSet2.getInt(1)));
                }
                resultSet1.close();
                resultSet2.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void ShowInformationProfit(ArrayList<Pair<Integer , Integer>>arrayListStatistic)
    {
        try {
            Statement statement_IN = connection_statistic.createStatement();
            Statement statement_OUT = connection_statistic.createStatement();
            if (fyear == tyear) {
                ResultSet resultSet_OUT = statement_OUT.executeQuery("SELECT * \n" +
                        "FROM\n" +
                        "(SELECT [OUT12].[Month],\n" +
                        "[OUT12].[Year],\n" +
                        "[OUT12].[Sum_OUT_1],\n" +
                        "[OUT12].[Sum_OUT_2] ,OUT3.[Sum_OUT_3]\n" +
                        "FROM\n" +
                        "(SELECT [OUT1].[Month],\n" +
                        "[OUT1].[Year],\n" +
                        "[OUT1].[Sum_OUT_1] , OUT2.Sum_OUT_2\n" +
                        "FROM\n" +
                        "(select Month_Invoice as Month , Year_Invoice as Year , SUM(Price_of_Invoice) as Sum_OUT_1\n" +
                        "from OTHEREXPENSE\n" +
                        "GROUP BY Month_invoice , Year_invoice) AS OUT1 \n" +
                        "INNER JOIN\n" +
                        "(select Month_Invoice as Month , Year_Invoice as Year,Price_of_invoice as Sum_OUT_2 from EXPENSESTAFFSALARY) AS OUT2 ON OUT1.[Month] = OUT2.[Month]) AS OUT12\n" +
                        "INNER JOIN\n" +
                        "(SELECT MONTH(DateTime_of_WarehouseInvoice) as Month, YEAR(DateTime_of_WarehouseInvoice) as Year, SUM(Price_of_WarehouseInvoice) as Sum_OUT_3\n" +
                        "FROM WAREHOUSEINVOICE\n" +
                        "GROUP BY YEAR(DateTime_of_WarehouseInvoice) , MONTH(DateTime_of_WarehouseInvoice)) AS OUT3 ON OUT12.[Month] = OUT3.[Month]) AS NEW_1\n" +
                        "WHERE Year= "+fyear+" and (Month BETWEEN "+fmonth+" and "+tmonth+")\n" +
                        "ORDER BY Year , Month");
                ResultSet resultSet_IN = statement_IN.executeQuery("SELECT *\n" +
                        "FROM\n" +
                        "(\n" +
                        "SELECT MONTH(Datetime_Invoice) as Month, YEAR(Datetime_Invoice) as Year, SUM(Money_Received) as Sum\n" +
                        "FROM INVOICE\n" +
                        "GROUP BY YEAR(Datetime_Invoice) , MONTH(Datetime_Invoice)) AS NEW\n" +
                        "WHERE  Year = "+fyear+" and (Month BETWEEN "+fmonth+" and "+tmonth+")\n" +
                        "ORDER BY Year , Month");
                while (resultSet_OUT.next() && resultSet_IN.next())
                {
                      arrayListStatistic.add(new Pair<Integer , Integer>((resultSet_IN.getInt(3)-(resultSet_OUT.getInt(3)+resultSet_OUT.getInt(4)+resultSet_OUT.getInt(5))),resultSet_IN.getInt(1)));
                }
                resultSet_IN.close();
                resultSet_OUT.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void ShowdataOnBarChart()
    {
        connection_statistic = new Connecting_MSSQL(connection_statistic).Connecting();
        ArrayList<Pair<Integer,Integer>> arrayListStatistic = new ArrayList<Pair<Integer, Integer>>();
        if (connection_statistic!= null)
        {
            if (type.equals("Salary"))
            {
                ShowInformationStaffSalary(arrayListStatistic);
            }else if (type.equals("Revenue"))
            {
                ShowInformationInvoiceExpense(arrayListStatistic);

            }else if (type.equals("Profit"))
            {
                ShowInformationProfit(arrayListStatistic);

            }else if(type.equals("Warehouse"))
            {
                ShowInformationWarehouseExpense(arrayListStatistic);
            }else
            {
                ShowInformationOtherExpense(arrayListStatistic);
            }
            //kiểm tra danh sách
            if (arrayListStatistic.size()==0)
            {
                txt_status.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.INVISIBLE);
            }else
            {
                ArrayList<BarEntry> arrayListdata = new ArrayList<>();
                for (int i =0 ; i <arrayListStatistic.size() ; i++)
                {
                    arrayListdata.add(new BarEntry(arrayListStatistic.get(i).second , arrayListStatistic.get(i).first/1000));
                }
                BarDataSet barDataSet = new BarDataSet(arrayListdata , type);
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                BarData barData = new BarData(barDataSet);
                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.getDescription().setText("Bar Chart: "+type);
                barChart.animateY(2000);
                barChart.setVisibility(View.VISIBLE);
                txt_status.setVisibility(View.INVISIBLE);
            }

        }else
        {
            Toast.makeText(getContext(), "Connect makes error.Please hotline with the administrator!", Toast.LENGTH_SHORT).show();
        }
    }
    private void SetupCheck()
    {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   if (Check_date(edt_from_time.getText().toString().trim() , edt_to_time.getText().toString().trim()))
                   {
                       ArrayList<String> array_from_time;
                       array_from_time = Processingdate(edt_from_time.getText().toString().trim());
                       ArrayList<String> array_to_time;
                       array_to_time = Processingdate(edt_to_time.getText().toString().trim());
                       String from_month = array_from_time.get(0);
                       String from_year = array_from_time.get(1);
                       String to_month = array_to_time.get(0);
                       String to_year = array_to_time.get(1);
                       if(Check_valid_date(from_month , from_year , to_month , to_year))
                       {
                           //kết nối csdl để check dữ liệu show lên biểu đồ
                           fmonth = Integer.parseInt(from_month);
                           fyear = Integer.parseInt(from_year);
                           tmonth = Integer.parseInt(to_month);
                           tyear = Integer.parseInt(to_year);
                           ShowdataOnBarChart();

                       }else
                       {
                           Toast.makeText(getContext() , "You enter wrong date", Toast.LENGTH_SHORT).show();
                       }
                   }
            }
        });
    }
    private void Clear()
    {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   barChart.setData(null);
                   barChart.setVisibility(View.INVISIBLE);
                   txt_status.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater , container , savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        //set txt status
        txt_status = view.findViewById(R.id.textview_status);
        //set up spinner type : wifi , electricty , water
        spinner = view.findViewById(R.id.spinner);
        SetupSpinner();
        CatchItemSpinner();
        //set up from date
        img_calendar_from_time = view.findViewById(R.id.calendar_fromtime);
        edt_from_time = view.findViewById(R.id.from_time);
        SetupFromDate();
        //set up to date
        img_calendar_to_time = view.findViewById(R.id.calendar_totime);
        edt_to_time = view.findViewById(R.id.to_time);
        SetupToDate();
        //set up check
        btnCheck = view.findViewById(R.id.See);
        SetupCheck();
        //gọi bar chart
        barChart = view.findViewById(R.id.bar_chart);
        //set up clear
        btnClear = view.findViewById(R.id.Delete);
        Clear();
        return view;
    }
}