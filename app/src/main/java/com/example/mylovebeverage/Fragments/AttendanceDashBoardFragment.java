package com.example.mylovebeverage.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mylovebeverage.Adapters.AttendanceDashBoardAdapter;
import com.example.mylovebeverage.Models.Detail_Attendance;
import com.example.mylovebeverage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseFirestore firestore;
    private ListView listView;

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
                    ArrayList<Detail_Attendance> attendanceArrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        Map<String , Object> map = document.getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                             attendanceArrayList.add(new Detail_Attendance(entry.getKey().trim(), entry.getValue().toString() ,R.drawable.green_tick));
                        }

                    }
                    AttendanceDashBoardAdapter attendanceDashBoardAdapter = new AttendanceDashBoardAdapter(attendanceArrayList);
                    listView.setAdapter(attendanceDashBoardAdapter);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_attendance_dash_board, container, false);
        EditText editText = view.findViewById(R.id.editText2);
        listView = view.findViewById(R.id.listviewattendance);
        editText.setText(setDate());
        RetriveDataFromCloudFireStore();
        return view;
    }
}