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
import com.example.mylovebeverage.Adapters.OtherExpenseAdapter;
import com.example.mylovebeverage.Models.Other_Expense_Invoice;
import com.example.mylovebeverage.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OthersExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OthersExpenseFragment extends Fragment {

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
    private String type ="";
    private Connection connection_others_expense;

    public OthersExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OthersExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OthersExpenseFragment newInstance(String param1, String param2) {
        OthersExpenseFragment fragment = new OthersExpenseFragment();
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
    private void ShowInformation(ArrayList<Other_Expense_Invoice> _otherExpenseInvoiceArrayList, int icon)
    {
        try {
            Statement statement1 = connection_others_expense.createStatement();
            Statement statement = connection_others_expense.createStatement();
            if (fyear == tyear) {
                ResultSet resultSet = statement.executeQuery("SELECT *\n" +
                        "FROM OTHEREXPENSE\n" +
                        "WHERE Kind_of_Invoice = "+"'"+type+"'"+ " AND Month_Invoice BETWEEN " + fmonth + " AND " + tmonth + "\n"+
                        "ORDER BY Month_Invoice");
                while (resultSet.next()) {
                    _otherExpenseInvoiceArrayList.add(new Other_Expense_Invoice(resultSet.getString(1).trim(), resultSet.getInt(3), resultSet.getString(4).trim(),resultSet.getString(5).trim(), resultSet.getString(6).trim(),resultSet.getString(9).trim() , resultSet.getString(2).trim(), String.valueOf(resultSet.getInt(7)), String.valueOf(resultSet.getInt(8)), icon));
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
                    _otherExpenseInvoiceArrayList.add(new Other_Expense_Invoice(resultSet1.getString(1).trim(), resultSet1.getInt(3), resultSet1.getString(4).trim(),resultSet1.getString(5).trim(), resultSet1.getString(6).trim(),resultSet1.getString(9).trim() , resultSet1.getString(2).trim(), String.valueOf(resultSet1.getInt(7)), String.valueOf(resultSet1.getInt(8)), icon));
                }
                while (resultSet2.next()) {
                    _otherExpenseInvoiceArrayList.add(new Other_Expense_Invoice(resultSet2.getString(1).trim(), resultSet2.getInt(3), resultSet2.getString(4).trim(),resultSet2.getString(5).trim(), resultSet2.getString(6).trim(),resultSet2.getString(9).trim() , resultSet2.getString(2).trim(), String.valueOf(resultSet2.getInt(7)), String.valueOf(resultSet2.getInt(8)), icon));
                }
                resultSet1.close();
                resultSet2.close();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater , container , savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_others_expense , container , false);
        TextView txt_notfication = view.findViewById(R.id.Notice);
        if(getArguments() == null)
        {
            txt_notfication.setVisibility(View.INVISIBLE);
        }else
        {
            fmonth = Integer.valueOf(getArguments().getString("FromMonth"));
            fyear = Integer.valueOf(getArguments().getString("FromYear"));
            tmonth = Integer.valueOf(getArguments().getString("ToMonth"));
            tyear = Integer.valueOf(getArguments().getString("ToYear"));
            type  = getArguments().getString("Type");
            connection_others_expense = new Connecting_MSSQL(connection_others_expense).Connecting();
            ListView listView = view.findViewById(R.id.listview_other_expense);
            ArrayList<Other_Expense_Invoice> _otherExpenseInvoiceArrayList = new ArrayList<>();
            if (connection_others_expense != null)
            {
                OtherExpenseAdapter otherExpenseAdapter = null;
                switch (type)
                {
                    case "Electricity":
                         ShowInformation(_otherExpenseInvoiceArrayList, R.drawable.electricity);
                         break;
                    case "Water":
                         ShowInformation(_otherExpenseInvoiceArrayList, R.drawable.water);
                         break;
                    case "Wifi":
                         ShowInformation(_otherExpenseInvoiceArrayList, R.drawable.wifi);
                         break;
                }
                otherExpenseAdapter = new OtherExpenseAdapter(_otherExpenseInvoiceArrayList);
                otherExpenseAdapter.notifyDataSetChanged();
                if(otherExpenseAdapter.getCount() ==0)
                {
                    txt_notfication.setVisibility(View.VISIBLE);
                }else
                {
                    listView.setAdapter(otherExpenseAdapter);
                    listView.setVisibility(View.VISIBLE);
                }
            }else
            {
                Toast.makeText(getContext(), "Connect makes error.Please hotline with the administrator!", Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }
}