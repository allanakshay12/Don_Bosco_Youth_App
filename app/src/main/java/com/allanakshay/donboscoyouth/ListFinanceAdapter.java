package com.allanakshay.donboscoyouth;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Allan Akshay on 05-06-2017.
 */

public class ListFinanceAdapter extends BaseAdapter {
    private int lastPosition = -1;
    private Context mContext;
    private List<FinanceProduct> mProductList;

    public ListFinanceAdapter(Context mContext, List<FinanceProduct> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProductList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.finance_listview, null);
        TextView tvmonth = (TextView)v.findViewById(R.id.finance_month);
        TextView tvamount = (TextView)v.findViewById(R.id.finance_amount);
        TextView tvtype = (TextView)v.findViewById(R.id.finance_type);
        TextView description = (TextView)v.findViewById(R.id.finance_desc);
        tvmonth.setText(mProductList.get(position).getName() + "\t\t" + mProductList.get(position).getPrice());
        tvamount.setText(tvamount.getText().toString()+mProductList.get(position).getDescription());
        tvtype.setText(tvtype.getText().toString()+mProductList.get(position).getType());
        description.setText(description.getText().toString()+ mProductList.get(position).getDesc());
        if(tvtype.getText().toString().equals("Type : Expense"))
            tvamount.setTextColor(Color.parseColor("#e00000"));
        else
            tvamount.setTextColor(Color.parseColor("#179100"));

        v.setTag( mProductList.get(position).getId());

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        v.startAnimation(animation);
        lastPosition = position;

        return v;
    }

}