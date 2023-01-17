package com.example.mylovebeverage.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Salary_Staff_Expense_Invoice;
import com.example.mylovebeverage.Adapters.StaffSalaryAdapter;
import com.example.mylovebeverage.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalaryStaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalaryStaffFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int fmonth;
    private int fyear;
    private int tmonth;
    private int tyear;
    private Connection connection_staff_salary;
    ListView listView ;
    public SalaryStaffFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SalaryStaffFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SalaryStaffFragment newInstance(String param1, String param2) {
        SalaryStaffFragment fragment = new SalaryStaffFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_salary_staff, container, false);
        TextView txt_notfication = view.findViewById(R.id.Notice);
        if (getArguments() == null) {
            txt_notfication.setVisibility(View.INVISIBLE);
        } else {

            fmonth = Integer.valueOf(getArguments().getString("FromMonth"));
            fyear = Integer.valueOf(getArguments().getString("FromYear"));
            tmonth = Integer.valueOf(getArguments().getString("ToMonth"));
            tyear = Integer.valueOf(getArguments().getString("ToYear"));
            listView = view.findViewById(R.id.listviewsalary);
            connection_staff_salary = new Connecting_MSSQL(connection_staff_salary).Connecting();
            //truy xuất thông tin từ nếu năm bằng nhau từ csdl\
            ArrayList<Salary_Staff_Expense_Invoice> invoiceSalaryStaffArrayList = new ArrayList<>();//khởi tạo list luu thong tin doi tuong
            if (connection_staff_salary != null) {
                StaffSalaryAdapter expenseAdapter = null;
                try {
                    Statement statement1 = connection_staff_salary.createStatement();
                    Statement statement = connection_staff_salary.createStatement();
                    if (fyear == tyear) {
                        ResultSet resultSet = statement.executeQuery("\n" +
                                "SELECT *\n" +
                                "FROM EXPENSESTAFFSALARY \n" +
                                "WHERE Month_Invoice BETWEEN " + fmonth + " AND " + tmonth + "\n" +
                                "ORDER BY Month_Invoice");
                        while (resultSet.next()) {
                            invoiceSalaryStaffArrayList.add(new Salary_Staff_Expense_Invoice(resultSet.getString(1).trim(), resultSet.getInt(2), resultSet.getString(3).trim(), resultSet.getString(6).trim(), resultSet.getString(7).trim(), resultSet.getString(4).trim(), resultSet.getString(5).trim(), String.valueOf(resultSet.getInt(8)), String.valueOf(resultSet.getInt(9))));
                        }
                        resultSet.close();
                        expenseAdapter = new StaffSalaryAdapter(invoiceSalaryStaffArrayList);
                        expenseAdapter.notifyDataSetChanged();
                    } else {
                        ResultSet resultSet1 = statement1.executeQuery("SELECT *\n" +
                                "FROM EXPENSESTAFFSALARY\n" +
                                "WHERE  (Year_Invoice BETWEEN " + fyear + " AND " + tyear + ") and (Month_Invoice BETWEEN " + fmonth + " AND 12)\n" +
                                "ORDER BY Year_Invoice , Month_Invoice");
                        ResultSet resultSet2 = statement.executeQuery("SELECT *\n" +
                                "FROM EXPENSESTAFFSALARY\n" +
                                "WHERE (Month_Invoice BETWEEN 1 AND " + tmonth + ") AND (Year_Invoice BETWEEN " + fyear + " AND " + tyear + ") \n" +
                                "ORDER BY Year_Invoice , Month_Invoice\n");
                        while (resultSet1.next()) {
                            invoiceSalaryStaffArrayList.add(new Salary_Staff_Expense_Invoice(resultSet1.getString(1).trim(), resultSet1.getInt(2), resultSet1.getString(3).trim(), resultSet1.getString(6).trim(), resultSet1.getString(7).trim(), resultSet1.getString(4).trim(), resultSet1.getString(5).trim(), String.valueOf(resultSet1.getInt(8)), String.valueOf(resultSet1.getInt(9))));
                        }
                        while (resultSet2.next()) {
                            invoiceSalaryStaffArrayList.add(new Salary_Staff_Expense_Invoice(resultSet2.getString(1).trim(), resultSet2.getInt(2), resultSet2.getString(3).trim(), resultSet2.getString(6).trim(), resultSet2.getString(7).trim(), resultSet2.getString(4).trim(), resultSet2.getString(5).trim(), String.valueOf(resultSet2.getInt(8)), String.valueOf(resultSet2.getInt(9))));
                        }
                        resultSet1.close();
                        resultSet2.close();
                        expenseAdapter = new StaffSalaryAdapter(invoiceSalaryStaffArrayList);
                        expenseAdapter.notifyDataSetChanged();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (expenseAdapter.getCount() == 0) {
                    txt_notfication.setVisibility(View.VISIBLE);
                } else {
                    listView.setAdapter(expenseAdapter);
                    listView.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(getContext(), "Connect makes error.Please hotline with the administrator!", Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }

}

