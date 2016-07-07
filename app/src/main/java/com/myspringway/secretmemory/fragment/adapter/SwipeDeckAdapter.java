package com.myspringway.secretmemory.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.activity.CommentActivity;
import com.myspringway.secretmemory.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by legab on 2016-06-30.
 */
public class SwipeDeckAdapter extends BaseAdapter {

    private static final String TAG = "SwipeDeckAdapter";

    private List<Post> data;
    private Context context;
    private LayoutInflater inflater;

    public SwipeDeckAdapter(List<Post> data, Context context, LayoutInflater inflater) {
        this.data = data;
        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.view_card, parent, false);
            ((CardView) v).setRadius(0);
            v.setPadding(0, 0, 0, 0);
        }

        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG, ""+data.size());

        ImageView imageView = (ImageView) v.findViewById(R.id.offer_img);
        final TextView textView = (TextView) v.findViewById(R.id.body_text);
        ImageView likeBtn = (ImageView) v.findViewById(R.id.like_btn);

        String imgData = data.get(position).pos_imgData;

        Log.d(TAG, "pos_imgData: " + imgData);

        // TODO: 이미지 데이터가 NULL일 경우 ERROR -> 해결 필요

            Picasso.with(context)
                    .load(imgData)
                    .error(R.drawable.bg0)
                    .fit()
                    .centerCrop()
                    .into(imageView);

        textView.setText(data.get(position).pos_content);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CommentActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                v.getContext().startActivity(i);
            }
        });

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
                dataRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Post p = mutableData.getValue(Post.class);
                        if (p == null) {
                            return Transaction.success(mutableData);
                        }

                        if (p.likes.containsKey(getUid())) {
                            p.numOfLike = --p.numOfLike;
                            p.likes.remove(getUid());
                        } else {
                            p.numOfLike = ++p.numOfLike;
                            p.likes.put(getUid(), true);
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });


            }
        });
        return v;
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
