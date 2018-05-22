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

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    List<ContactInformation>productList = new ArrayList<>();

    public MyAdapter(Context context, List<ContactInformation> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_contact_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactInformation product  = productList.get(position);

        holder.mTextView.setText(product.getText1());
        holder.mTextView2.setText(product.getText2());
        holder.mImageView.setImageResource(product.getImage());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mImageView;
        TextView mTextView;
        TextView mTextView2;
        public ViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (CircleImageView) itemView.findViewById(R.id.imageView);
            this.mTextView = (TextView) itemView.findViewById(R.id.textView);
            this.mTextView2 = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}