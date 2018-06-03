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
    View.OnClickListener clickListener;
    View.OnLongClickListener longClickListener;

    public MyAdapter(Context context, List<ContactInformation> productList, View.OnClickListener mListener) {
        this.context = context;
        this.productList = productList;
        this.clickListener = mListener;
    }

    public MyAdapter(Context context, List<ContactInformation> productList, View.OnLongClickListener mListener) {
        this.context = context;
        this.productList = productList;
        this.longClickListener = mListener;
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
            this.email = itemView.findViewById(R.id.textView3);
            if(clickListener != null)
                itemView.setOnClickListener(clickListener);
            if(longClickListener != null)
                itemView.setOnLongClickListener(longClickListener);
        }
    }
    public static class ContactInformation {
        String text1, text2, text3;
        int image;

        public String getText1() {
            return text1;
        }

        public void setText1(String text1) {
            this.text1 = text1;
        }

        public String getText2() {
            return text2;
        }

        public void setText2(String text2) {
            this.text2 = text2;
        }
        public String getText3() {
            return text3;
        }

        public void setText3(String text3) {
            this.text3 = text3;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public ContactInformation(String text1, String text2, int image) {
            this.text1 = text1;
            this.text2 = text2;
            this.image = image;
        }
    }

}