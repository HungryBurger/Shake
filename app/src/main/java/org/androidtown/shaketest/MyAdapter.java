package org.androidtown.shaketest;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    List<ContactInformation> productList = new ArrayList<>();
    View.OnClickListener mListener;
    View.OnLongClickListener mLongListener;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ContactInformation product = productList.get(position);

        holder.name.setText(product.getText1());
        holder.pnum.setText(product.getText2());
        holder.email.setText(product.getText3());
        holder.list_pos.setText(String.valueOf(position + 1));
        holder.mImageView.setImageResource(product.getImage());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Toast.makeText(context, position + 1 + "번 클릭", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mImageView;
        TextView name;
        TextView pnum;
        TextView email;
        TextView list_pos;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (CircleImageView) itemView.findViewById(R.id.user_picture);
            this.name = (TextView) itemView.findViewById(R.id.user_name);
            this.pnum = (TextView) itemView.findViewById(R.id.user_phone);
            this.email = (TextView) itemView.findViewById(R.id.user_email);
            this.list_pos = (TextView) itemView.findViewById(R.id.list_pos);
            if (mListener != null) {
                itemView.setOnClickListener(mListener);
                itemView.setOnLongClickListener(mLongListener);
            }
        }
    }

    //    @Override
//    public boolean onLongClick(View v) {
//        Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();
//        return true;
//
//    }
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