package com.example.mylovebeverage.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylovebeverage.Models.Detail_Human_Resource;
import com.example.mylovebeverage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HumanResourceAdapter extends BaseAdapter {
    final ArrayList<Detail_Human_Resource> arrayList;

    public HumanResourceAdapter(ArrayList<Detail_Human_Resource> arrayList) {
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

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private  class ViewHolder
    {
        TextView txt1 , txt2;
        ImageView img;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        View viewproduct ;//View hien thi san pham tu arrayList
        ViewHolder viewHolder ;//lưu thông tin ánh xạ
        if(view == null)
        {
            //viewproduct= View.inflate(viewGroup.getContext() , R.layout.actionn jj njn , null);
            viewproduct = View.inflate(viewGroup.getContext(), R.layout.detail_human_resource, null);
            viewHolder = new HumanResourceAdapter.ViewHolder();
            viewHolder.txt1 = viewproduct.findViewById(R.id.text_ID);
            viewHolder.txt2 = viewproduct.findViewById(R.id.text_position);
            viewHolder.img = viewproduct.findViewById(R.id.imageview_human);
            viewproduct.setTag(viewHolder); // Tạo tag để nắm viewholder mà lưu trữ các thông tin ánh xạ để dùng cho lần sau.

        }else
        {
            viewproduct = view;
            viewHolder = (HumanResourceAdapter.ViewHolder) viewproduct.getTag();
        }
        Detail_Human_Resource product = (Detail_Human_Resource) getItem(position);
        viewHolder.txt1.setText(product.getID());
        viewHolder.txt2.setText(product.getPosition());
        Picasso.get().load(product.getImageview()).fit().into(viewHolder.img);
        return viewproduct;
    }
}
