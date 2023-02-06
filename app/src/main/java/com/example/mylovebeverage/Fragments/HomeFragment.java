package com.example.mylovebeverage.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mylovebeverage.ManageInvoice;
import com.example.mylovebeverage.ManageProduct;
import com.example.mylovebeverage.ManageSupplier;
import com.example.mylovebeverage.ManageWareHouse;
import com.example.mylovebeverage.R;
import com.example.mylovebeverage.ManageExpense;
import com.example.mylovebeverage.ManageStaff;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton img_staff;
    private ImageButton img_expense;
    private ImageButton img_product;
    private ImageButton img_invoice;
    private ImageButton img_supplier;
    private ImageButton img_warehouse;

    //private String staff_id = "";
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private void Show() {

        Manage_Staff();
        Manage_Expense();
        Manage_Product();
        Manage_Invoice();
        Manage_Supplier();
        Manage_Warehouse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        img_staff = view.findViewById(R.id.button_staff); // anh xa đặc tính
        img_expense = view.findViewById(R.id.button_expense);
        img_product = view.findViewById(R.id.button_product);
        img_invoice = view.findViewById(R.id.button_invoice);
        img_supplier = view.findViewById(R.id.button_supplier);
        img_warehouse = view.findViewById(R.id.button_warehouse);
        //staff_id = getArguments().getString("AccountName").trim();
        Show();
        return view;
    }
    protected void Manage_Warehouse()
    {
        img_warehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(getContext() , ManageWareHouse.class));
            }
        });
    }
    protected  void Manage_Staff()
    {
        img_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext() , ManageStaff.class));
            }
        });
    }
    protected void Manage_Product()
    {
        img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(getContext() , ManageProduct.class));
            }
        });
    }
    protected void Manage_Invoice()
    {
        img_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   startActivity(new Intent(getContext(), ManageInvoice.class));
            }
        });
    }
    protected  void Manage_Supplier()
    {
        img_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   startActivity(new Intent(getContext() , ManageSupplier.class));
            }
        });
    }
    protected void Manage_Expense()
    {
        img_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , ManageExpense.class);
                //intent.putExtra("Staff_ID", staff_id);
                startActivity(intent);
            }
        });
    }
}