package com.allanakshay.donboscoyouth;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allanakshay.donboscoyouth.Product;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class ListProductAdapter extends BaseAdapter {
    private int lastPosition = -1;
    private Context mContext;
    private List<Product> mProductList;
    private ArrayList<String> image = new ArrayList<String>();

    public ListProductAdapter(Context mContext, List<Product> mProductList, ArrayList<String> image) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.image = image;
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

        View v = View.inflate(mContext, R.layout.name_list, null);
        TextView tvName = (TextView)v.findViewById(R.id.tv_name);
        TextView tvPhno = (TextView)v.findViewById(R.id.tv_ph_no);
        TextView tvOccu = (TextView)v.findViewById(R.id.tv_occu);
        CardView card = (CardView) v.findViewById(R.id.month_card);
        RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.namelayoutrelative);
        tvName.setText(mProductList.get(position).getName());
        final CircleImageView imageView = (CircleImageView) v.findViewById(R.id.imgProfilePicture);

if(ImageSet.setimage.equals("Upcoming Events")) {
    imageView.setVisibility(View.GONE);
    if (mProductList.get(position).getName().equals("January") || mProductList.get(position).getName().equals("February") || mProductList.get(position).getName().equals("March") || mProductList.get(position).getName().equals("April") || mProductList.get(position).getName().equals("May") || mProductList.get(position).getName().equals("June") || mProductList.get(position).getName().equals("July") || mProductList.get(position).getName().equals("August") || mProductList.get(position).getName().equals("September") || mProductList.get(position).getName().equals("October") || mProductList.get(position).getName().equals("November") || mProductList.get(position).getName().equals("December")) {
        tvPhno.setVisibility(View.GONE);
        tvOccu.setVisibility(View.GONE);
        tvName.setTextSize(40);
        card.setRadius(0);
        relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
    }
}
if(ImageSet.setimage.equals(""))
{
    imageView.setVisibility(View.GONE);
}

        if(ImageSet.setimage.equals("Image")) {
            if(!image.get(position).equals("")) {


                StorageReference mountainsRef = FirebaseStorage.getInstance().getReference().child(image.get(position));
                Glide.with(mContext)
                        .using(new FirebaseImageLoader())
                        .load(mountainsRef)
                        .into(imageView);
            }
            else
                imageView.setBorderColor(Color.parseColor("#ffffff"));


        }
        if(mProductList.get(position).getName().equals("Monday") || mProductList.get(position).getName().equals("Tuesday") || mProductList.get(position).getName().equals("Wednesday") || mProductList.get(position).getName().equals("Thursday") || mProductList.get(position).getName().equals("Friday"))
        {
            tvName.setTextColor(Color.parseColor("#0171fa"));
            card.setRadius(0);
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else if(mProductList.get(position).getName().equals("Saturday"))
        {
            tvName.setTextColor(Color.parseColor("#bf00ff"));
            card.setRadius(0);
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else if(mProductList.get(position).getName().equals("Sunday"))
        {
            tvName.setTextColor(Color.parseColor("#FFFF8800"));
            card.setRadius(0);
            relativeLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        tvPhno.setText(String.valueOf(mProductList.get(position).getPrice()));
        tvOccu.setText(mProductList.get(position).getDescription());
        v.setTag( mProductList.get(position).getId());

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        v.startAnimation(animation);
        lastPosition = position;



        return v;
    }

}
