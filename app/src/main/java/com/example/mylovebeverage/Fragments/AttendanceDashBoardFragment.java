package com.example.mylovebeverage.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylovebeverage.Adapters.AttendanceDashBoardAdapter;
import com.example.mylovebeverage.Data.Connecting_MSSQL;
import com.example.mylovebeverage.Models.Detail_Attendance;
import com.example.mylovebeverage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendanceDashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceDashBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Connection connection;
    private FirebaseFirestore firestore;
    private ListView listView;
    private ImageView imageView;
    private TextView txt, txt1, txt2;
    private Button btn_settimer, btn_sendinvoice, btn_exportinvoice;
    private ArrayList<Detail_Attendance> attendanceArrayList = new ArrayList<>();

    public AttendanceDashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceDashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceDashBoardFragment newInstance(String param1, String param2) {
        AttendanceDashBoardFragment fragment = new AttendanceDashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = new Connecting_MSSQL(connection).Connecting();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private String setDate()
    {
        return new SimpleDateFormat("dd/MM/Y").format(new Date());
    }
    private void RetriveDataFromCloudFireStore()
    {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("Attendance").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> map = document.getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            attendanceArrayList.add(new Detail_Attendance(entry.getKey().trim(), entry.getValue().toString(), R.drawable.green_tick));
                        }
                    }
                    AttendanceDashBoardAdapter attendanceDashBoardAdapter = new AttendanceDashBoardAdapter(attendanceArrayList);
                    listView.setAdapter(attendanceDashBoardAdapter);
                    try {
                        Check_status_amount_of_staff();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void Check_status_amount_of_staff() throws SQLException {
        if (connection != null) {
            int total_staff = 0;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(Position) from [dbo].[ACCOUNT] where Position != 'BOSS'");
            while (resultSet.next()) {
                total_staff = resultSet.getInt(1);
            }
            txt1.setText(attendanceArrayList.size() + "/" + total_staff);
            if (attendanceArrayList.size() < total_staff) {
                txt2.setText("Absent");
            } else {
                txt2.setText("Full");
            }


        }
    }

//    private void Set_timer() {
//
//    }

    private int check_day_of_month(String month) {
        int last_day = 0;
        if (month.equals("01") || month.equals("03") || month.equals("05") || month.equals("07") || month.equals("08") || month.equals("10") || month.equals("12"))
            last_day = 31;
        else if (month.equals("04") || month.equals("06") || month.equals("09") || month.equals("11")) {
            last_day = 30;
        } else if (month.equals("02")) {
            last_day = 28; // năm không nhuận
        }
        return last_day;
    }

    private void Send_Invoice() {
        String date = setDate();
        int day = Integer.valueOf(date.toString().split("/")[0]);
        String month = date.toString().split("/")[1];
        int last_day_month = check_day_of_month(month);
        if (day < last_day_month) {
            Toast.makeText(getContext(), "Sending invoice is available on the last day of month !!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void Export_Invoice() {
        String date = setDate();
        int day = Integer.valueOf(date.toString().split("/")[0]);
        String month = date.toString().split("/")[1];
        int last_day_month = check_day_of_month(month);
        if (day < last_day_month) {
            Toast.makeText(getContext(), "Exporting invoice is available on the last day of month !!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance_dash_board, container, false);
        EditText editText = view.findViewById(R.id.editText2);
        listView = view.findViewById(R.id.listviewattendance);
        imageView = view.findViewById(R.id.view_warn);
        txt = view.findViewById(R.id.text_warn);
        txt1 = view.findViewById(R.id.amount_of_staff);
        txt2 = view.findViewById(R.id.status);
        //set các nút
//        btn_settimer =view.findViewById(R.id.button_timer);
        btn_sendinvoice = view.findViewById(R.id.button_sendvoice);
        btn_exportinvoice = view.findViewById(R.id.button_exportvoice);
        //set các hàm
//        Set_timer();
        btn_sendinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send_Invoice();
            }
        });
        btn_exportinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Export_Invoice();
            }
        });
        editText.setText(setDate());
        attendanceArrayList.clear();
        RetriveDataFromCloudFireStore();
        return view;
    }
}