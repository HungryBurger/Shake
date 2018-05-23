package org.androidtown.shaketest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        final Item myItem = countries.get(i);
        viewHolder.Name.setText(myItem.getName());
        viewHolder.Image.setImageDrawable(mContext.getDrawable(myItem.getOmg()));
        viewHolder.email.setText(myItem.getEmail());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(mContext,myItem.getName(),Toast.LENGTH_SHORT).show();
            }
        });
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
    public static class Item {
        public String name, email;
        public int omg;

        public Item(String name, String email, int omg) {
            this.name = name;
            this.email = email;
            this.omg = omg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getOmg() {
            return omg;
        }

        public void setOmg(int omg) {
            this.omg = omg;
        }

    }
}