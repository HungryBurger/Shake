package org.androidtown.shaketest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    List<ContactInformation> productList;
    View.OnClickListener mListener;
    View.OnLongClickListener mLongListener;
    private SharedPrefManager mSharedPrefManager;

    public MyAdapter(Context context, List<ContactInformation> productList, View.OnClickListener mListener) {
        this.context = context;
        this.productList = productList;
        this.mListener = mListener;
        mSharedPrefManager = SharedPrefManager.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_contact_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ContactInformation product = productList.get(position);

        holder.name.setText(product.getText1());
        holder.pnum.setText(product.getText2());
        holder.email.setText(product.getText3());
        if (product.getImage() != null)
            holder.mImageView.setImageBitmap(product.getImage());
        else
            holder.mImageView.setImageResource(R.drawable.user_profile);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteDialog(product.getUid(), position);
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactData cur = new ContactData(
                        product.getText1(),
                        product.getText2(),
                        product.getText3(),
                        product.getTemplateNo(),
                        bitmapToString(product.getImage())
                );
                DialogFragment fragment = DialogFragment.newInstance(
                        10, 5, false, false, product.getTemplateNo(), cur
                );
                fragment.show(((AppCompatActivity) context).getFragmentManager(), "blur_sample");
                Log.d("ListView", "onItemClicked");
            }
        });
    }

    public void deleteDialog(final String uid, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("연락처 삭제");
        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServiceApplication.myContactList.remove(uid);
                ServiceApplication.person.remove(uid);

                productList.remove(pos);
                notifyItemRemoved(pos);
                updateDB();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateDB () {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("contact_list").child(mSharedPrefManager.getUserUid());
        mRef.setValue(ServiceApplication.myContactList);
    }

    public String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) return null;

        Log.d("tag", "bitmapToString: ");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
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

        public ViewHolder(View itemView) {
            super(itemView);
            this.mImageView = (CircleImageView) itemView.findViewById(R.id.user_picture);
            this.name = (TextView) itemView.findViewById(R.id.user_name);
            this.pnum = (TextView) itemView.findViewById(R.id.user_phone);
            this.email = (TextView) itemView.findViewById(R.id.user_email);
            if (mListener != null) {
                itemView.setOnClickListener(mListener);
                itemView.setOnLongClickListener(mLongListener);
            }
        }
    }

    public static class ContactInformation {
        String uid;
        String name, phoneNum, email, image;
        int templateNo;

        public void setUid (String uid) {
            this.uid = uid;
        }

        public String getUid () {
            return this.uid;
        }

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

        public int getTemplateNo() {
            return templateNo;
        }

        public void setTemplateNo(int image) {
            this.templateNo = image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Bitmap getImage() {
            return stringToBitmap(this.image);
        }

        public ContactInformation(String uid, ContactData current) {
            setUid(uid);
            setText1(current.getName());
            setText2(current.getPhoneNum());
            setText3(current.getEmail());
            setTemplateNo(current.getTemplate_no());
            setImage(current.getImage());
        }

        public Bitmap stringToBitmap(String bitmapString) {
            if (bitmapString == null) return null;

            Log.d("tag", "stringToBitmap: ");
            byte[] bytes = Base64.decode(bitmapString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }
}