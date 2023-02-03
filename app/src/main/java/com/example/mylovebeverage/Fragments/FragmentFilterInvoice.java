package com.example.mylovebeverage.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mylovebeverage.R;

public class FragmentFilterInvoice extends Fragment
{
    Button btnFilterPrice1;
    Button btnFilterPrice2;
    Button btnFilterPrice3;
    Button btnFilterDate1;
    Button btnFilterDate2;
    Button btnFilterDate3;

    FragmentFilterInvoiceListener activityCallback;
    public interface FragmentFilterInvoiceListener {
        void onButtonClick(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityCallback = (FragmentFilterInvoiceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " You must implement FragmentFilterInvoiceListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_invoice_filter, container, false);
        btnFilterPrice1 = view.findViewById(R.id.btnFilterPrice1);
        btnFilterPrice2 = view.findViewById(R.id.btnFilterPrice2);
        btnFilterPrice3 = view.findViewById(R.id.btnFilterPrice3);
        btnFilterDate1 = view.findViewById(R.id.btnFilterDate1);
        btnFilterDate2 = view.findViewById(R.id.btnFilterDate2);
        btnFilterDate3 = view.findViewById(R.id.btnFilterDate3);

        btnFilterPrice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCallback.onButtonClick("Price1");
            }
        });

        btnFilterPrice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCallback.onButtonClick("Price2");
            }
        });

        btnFilterPrice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCallback.onButtonClick("Price3");
            }
        });

        btnFilterDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCallback.onButtonClick("Date1");
            }
        });

        btnFilterDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCallback.onButtonClick("Date2");
            }
        });

        btnFilterDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityCallback.onButtonClick("Date3");
            }
        });

        return view;
    }
}
