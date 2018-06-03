package org.androidtown.shaketest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    List<ContactInformation>productList = new ArrayList<>();
    View.OnClickListener mListener;

    public MyAdapter(Context context, List<ContactInformation> productList, View.OnClickListener mListener) {
        this.context = context;
        this.productList = productList;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_contact_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactInformation product  = productList.get(position);

        holder.name.setText(product.getText1());
        holder.pnum.setText(product.getText2());
        holder.email.setText(product.getText3());
        holder.mImageView.setImageResource(product.getImage());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView name;
        TextView pnum;
        TextView email;
        public ViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.name = (TextView) itemView.findViewById(R.id.textView);
            this.pnum = (TextView) itemView.findViewById(R.id.textView2);
            this.email = (TextView)itemView.findViewById(R.id.textView3);
            if(mListener != null)
                itemView.setOnClickListener(mListener);
        }
    }
    public static class ContactInformation {
        String name, phoneNum, email;
        int image;

        public String getText1() {
            return name;
        }

        public void setText1(String name) {
            this.name = name;
        }

        public String getText2() {
            return phoneNum;
        }

        public void setText2(String phonNum) {
            this.phoneNum = phonNum;
        }

        public String getText3() {
            return email;
        }

        public void setText3(String email) {
            this.email = email;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public ContactInformation(String name, String phonNum, String email, int image) {
            this.name = name;
            this.phoneNum = phonNum;
            this.email = email;
            this.image = image;
        }
    }

}