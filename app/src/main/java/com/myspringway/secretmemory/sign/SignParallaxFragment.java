package com.myspringway.secretmemory.sign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.activity.SignActivity;
import com.myspringway.secretmemory.library.SecretTextView;


public class SignParallaxFragment extends Fragment {

    private SignParallaxAdapter mCatsAdapter;
    private int position;
    private Object parent;

    //toggle animation
    SecretTextView title;
    Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_sign_view, container, false);
        final ImageView image = (ImageView) v.findViewById(R.id.image);
        final ImageView image_bottom = (ImageView) v.findViewById(R.id.image_bottom);
        final LinearLayout join_layout = (LinearLayout) v.findViewById(R.id.join_layout);
        title = ((SecretTextView) v.findViewById(R.id.title));
        title.setDuration(2000);
        title.setIsVisible(false);

        position = getArguments().getInt("position");

        image.setImageResource(getImage(position));
        image_bottom.setImageResource(getImageBottom(position));
        image.post(new Runnable() {
            @Override
            public void run() {
                Matrix matrix = new Matrix();
                matrix.reset();

                float wv = image.getWidth();
                float hv = image.getHeight();

                float wi = image.getDrawable().getIntrinsicWidth();
                float hi = image.getDrawable().getIntrinsicHeight();

                float width = wv;
                float height = hv;

                if (wi / wv > hi / hv) {
                    matrix.setScale(hv / hi, hv / hi);
                    width = wi * hv / hi;
                } else {
                    matrix.setScale(wv / wi, wv / wi);
                    height= hi * wv / wi;
                }

                matrix.preTranslate((wv - width) / 2, (hv - height) / 2);
                image.setScaleType(ImageView.ScaleType.MATRIX);
                image.setImageMatrix(matrix);
            }
        });

        join_layout.setAlpha(0f);

        if(position == 0) {
            image_bottom.setVisibility(View.GONE);
            v.findViewById(R.id.login).setVisibility(View.VISIBLE);
            v.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (parent != null) ((SignActivity) parent).goLoginActivity();
                }
            });
//            title.toggle();
            startToggle();

            v.findViewById(R.id.intro_is_already_singup).setVisibility(View.VISIBLE);

        } else {
            title.hide();
            title.setIsVisible(false);
            title.setVisibility(View.GONE);

            v.findViewById(R.id.login).setVisibility(View.GONE);
            v.findViewById(R.id.intro_is_already_singup).setVisibility(View.GONE);

            if(image_bottom.getVisibility() == View.GONE) image_bottom.setVisibility(View.VISIBLE);


            if(position % 2 == 0) {
                final TextView content = (TextView) v.findViewById(R.id.content_left);
                content.setText(getResources().getStringArray(R.array.bg_text)[position - 1]);
            } else {
                final TextView content = (TextView) v.findViewById(R.id.content_right);
                content.setText(getResources().getStringArray(R.array.bg_text)[position - 1]);
            }

            if(position == 5) {
                final TextView content = (TextView) v.findViewById(R.id.content_right);
                final TextView join_text = (TextView) v.findViewById(R.id.join_text);
                final TextView login2 = (TextView) v.findViewById(R.id.login2);

                startToggle();

                content.animate().setDuration(5000)
                        .setStartDelay(3000)
                        .translationY(-150)
                        .alpha(0)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                join_layout.animate().setDuration(300).alpha(1);
                            }
                        });

                join_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (parent != null) ((SignActivity) parent).goJoinActivity();
                    }
                });

                login2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (parent != null) ((SignActivity) parent).goLoginActivity();
                    }
                });
            }
        }
        return v;
    }

    boolean isStartTimer = false;
    void startToggle() {
        title.setVisibility(View.VISIBLE);
        title.toggle();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(isStartTimer == false) return;
                title.toggle();
                handler.sendEmptyMessageDelayed(0, 5000);
            }
        };
        handler.sendEmptyMessageDelayed(0, 5000);
        isStartTimer = true;
    }



    public void setParent(Object parent) { this.parent = parent; }



    private int getImage(int position) {
        if(position == 0) return R.drawable.bg0;
        else if(position == 1) return R.drawable.bg1;
        else if(position == 2) return R.drawable.bg2;
        else if(position == 3) return R.drawable.bg3;
        else if(position == 4) return R.drawable.bg4;
        else if(position == 5) return R.drawable.bg5;
        else if(position == 6) return R.drawable.bg6;
        else return R.drawable.bg_join;
    }

    private int getImageBottom(int position) {
        if(position % 2 == 0) return R.drawable.bottom_left;
        else return R.drawable.bottom_right;
    }

    public void setAdapter(SignParallaxAdapter catsAdapter) {
        mCatsAdapter = catsAdapter;
    }
}
