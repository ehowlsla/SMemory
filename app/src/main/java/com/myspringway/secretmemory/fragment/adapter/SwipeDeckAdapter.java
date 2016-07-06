package com.myspringway.secretmemory.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.activity.CommentActivity;
import com.myspringway.secretmemory.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by legab on 2016-06-30.
 */
public class SwipeDeckAdapter extends BaseAdapter {

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
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.view_card, parent, false);
            ((CardView) v).setRadius(0);
            v.setPadding(0, 0, 0, 0);
        }

        ImageView imageView = (ImageView) v.findViewById(R.id.offer_img);
        TextView textView = (TextView) v.findViewById(R.id.body_text);

        Picasso.with(context).load(data.get(position).pos_imgData)
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
        return v;
    }
}
