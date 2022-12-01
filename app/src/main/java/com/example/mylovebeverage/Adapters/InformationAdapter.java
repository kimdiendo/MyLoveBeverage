package com.example.mylovebeverage.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovebeverage.Models.detail_profile;
import com.example.mylovebeverage.R;
import java.util.ArrayList;

public class InformationAdapter extends BaseAdapter {
    final ArrayList<detail_profile> arrayList ;
    public InformationAdapter(ArrayList<detail_profile> arrayList) {
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
    private  class ViewHolder
    {
        TextView txt1 , txt2;
        ImageView img;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewproduct ;//View hien thi san pham tu arrayList
        ViewHolder viewHolder ;//lưu thông tin ánh xạ
        if(view == null)
        {
            //viewproduct= View.inflate(viewGroup.getContext() , R.layout.actionn jj njn , null);
            viewproduct = View.inflate(viewGroup.getContext(), R.layout.detail_profile , null );
            viewHolder = new ViewHolder();
            viewHolder.txt1 = (TextView) viewproduct.findViewById(R.id.text1);
            viewHolder.txt2 = (TextView) viewproduct.findViewById(R.id.text2);
            viewHolder.img =(ImageView) viewproduct.findViewById(R.id.image);
            viewproduct.setTag(viewHolder); // Tạo tag để nắm viewholder mà lưu trữ các thông tin ánh xạ để dùng cho lần sau.

        }else
        {
            viewproduct = view;
            viewHolder = (ViewHolder) viewproduct.getTag();
        }
        detail_profile product = (detail_profile) getItem(position);
        viewHolder.txt1.setText(product.getName1());
        viewHolder.txt2.setText(product.getName2());
        viewHolder.img.setImageResource(product.getImageView());
        return viewproduct;
    }
}
