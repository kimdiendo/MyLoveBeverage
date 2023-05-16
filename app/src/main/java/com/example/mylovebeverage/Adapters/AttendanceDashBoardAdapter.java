package com.example.mylovebeverage.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovebeverage.Models.Detail_Attendance;
import com.example.mylovebeverage.Models.Detail_Profile;
import com.example.mylovebeverage.R;

import java.util.ArrayList;

public class AttendanceDashBoardAdapter extends BaseAdapter {
    final ArrayList<Detail_Attendance> arrayList;

    public AttendanceDashBoardAdapter(ArrayList<Detail_Attendance> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    private class ViewHolder {
        TextView txt1, txt2;
        ImageView img;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewproduct;//View hien thi san pham tu arrayList
        ViewHolder viewHolder;//lưu thông tin ánh xạ
        if (view == null) {
            //viewproduct= View.inflate(viewGroup.getContext() , R.layout.actionn jj njn , null);
            viewproduct = View.inflate(viewGroup.getContext(), R.layout.detail_attendance, null);
            viewHolder = new ViewHolder();
            viewHolder.txt1 = viewproduct.findViewById(R.id.textStaffID);
            viewHolder.txt2 = viewproduct.findViewById(R.id.textCheckIn);
            viewHolder.img = viewproduct.findViewById(R.id.checkview);
            viewproduct.setTag(viewHolder); // Tạo tag để nắm viewholder mà lưu trữ các thông tin ánh xạ để dùng cho lần sau.

        } else {
            viewproduct = view;
            viewHolder = (ViewHolder) viewproduct.getTag();
        }
        Detail_Attendance product = (Detail_Attendance) getItem(position);
        viewHolder.txt1.setText(product.getStaff_ID());
        viewHolder.txt2.setText(product.getCheck_In_time());
        viewHolder.img.setImageResource(product.getImageView());
        return viewproduct;
    }
}
