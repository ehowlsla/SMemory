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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.activity.ActivityComment;
import com.myspringway.secretmemory.constants.AppConstant;
import com.myspringway.secretmemory.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by legab on 2016-06-30.
 */
public class SwipeDeckAdapter extends BaseAdapter {

    private static final String TAG = SwipeDeckAdapter.class.getSimpleName();

    private List<Post> data;
    private Context context;
    private LayoutInflater inflater;

    private ImageView bgImgView;
    private TextView contentText;
    private ImageView img_like;
    private TextView tv_like;

    /* Constructor */
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
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.view_card, parent, false);
            ((CardView) view).setRadius(0);
            view.setPadding(0, 0, 0, 0);
        }

        initContent(view);

        bindToPost(data.get(position), view);

        return view;
    }

    private void initContent(View view) {
        bgImgView = (ImageView) view.findViewById(R.id.offer_img);
        contentText = (TextView) view.findViewById(R.id.body_text);
        img_like = (ImageView) view.findViewById(R.id.img_like);
        tv_like = (TextView) view.findViewById(R.id.tv_like);
    }

    private void bindToPost(final Post post, View view) {

        if (post == null) {
            Log.e(TAG, "Post is null");
            return;
        }

        final String imgData = post.pos_imgData;
        final String contentData = post.pos_content;
        final String contentKey = post.pos_key;
        int likeData = post.numOfLike;

        // TODO: 이미지 데이터가 NULL일 경우 ERROR -> 해결 필요
        Picasso.with(context)
                .load(imgData)
                .error(R.drawable.bg0)
                .fit()
                .into(bgImgView);

        contentText.setText(contentData);

        view.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), ActivityComment.class);
            i.putExtra(AppConstant.IMG_URL, imgData);
            i.putExtra(AppConstant.TXT_CONTENT, contentData);
            i.putExtra(AppConstant.USER_CONTENT_ID, contentKey);
            v.getContext().startActivity(i);
        });

        tv_like.setText("" + likeData);

        // TODO: Click 시 실시간 데이터 변화를 어떻게 처리할 것인지 고민해봐야 함
        tv_like.setOnClickListener(v -> {
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
        });
    }

    private String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
