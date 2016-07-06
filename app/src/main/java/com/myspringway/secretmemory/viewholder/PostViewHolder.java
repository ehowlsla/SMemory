package com.myspringway.secretmemory.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.model.Post;
import com.squareup.picasso.Picasso;

/**
 * Created by legab on 2016-06-25.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {

//    public ImageView offerView;
    public TextView bodyView;
    public ImageView likeView;
    public TextView likeNumView;
    public ImageView comView;
    public TextView comNumView;


    public PostViewHolder(View itemView) {
        super(itemView);

//        offerView = (ImageView) itemView.findViewById(R.id.offer_img);
        bodyView = (TextView) itemView.findViewById(R.id.body_text);
        likeView = (ImageView) itemView.findViewById(R.id.like_btn);
        likeNumView = (TextView) itemView.findViewById(R.id.like_num);
//        comView = (ImageView) itemView.findViewById(R.id.com_ic); // 코멘트 개수 출력용
//        comNumView =
    }

    public void bindToPost(Post post, View.OnClickListener likeClickListener) {
        bodyView.setText(post.pos_content);
        likeNumView.setText(post.numOfLike);

        likeView.setOnClickListener(likeClickListener);
    }
}
