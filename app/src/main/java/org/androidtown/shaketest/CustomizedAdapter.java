package org.androidtown.shaketest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomizedAdapter extends RecyclerView.Adapter<CustomizedAdapter.ViewHolder> {
    List<Item> countries=new ArrayList<>();
    Context mContext;

    public CustomizedAdapter(List<Item> countries, Context context) {
        this.countries = countries;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card1, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Item myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
        viewHolder.Image.setImageDrawable(mContext.getDrawable(myItem.getOmg()));
        viewHolder.email.setText(myItem.getEmail());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name, email;
        public ImageView Image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.Name = (TextView) itemView.findViewById(R.id.text_name);
            this.Image = (ImageView) itemView.findViewById(R.id.Image);
            this.email = (TextView) itemView.findViewById(R.id.text_email);
        }
    }
}