package com.myspringway.secretmemory.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.myspringway.secretmemory.R;


/**
 * Created by yoontaesup on 15. 10. 8..
 */
public class DynamicTag {
    Context context;
    public View view;
    public boolean isHiddenTag = false;
    public String value;
    Toast toast;

    public DynamicTag(Context context) {
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.text_tag, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHiddenChange(true);
            }
        });

        toast = Toast.makeText(context, context.getString(R.string.tag_show), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    public void setValue(String value, boolean isHidden) {
        ((TextView) view).setText(value);
        this.isHiddenTag = isHidden;
        this.value = value;
        setHiddenChange(false);
    }

    void setHiddenChange(boolean isClick) {
        if(isClick == true) {
            this.isHiddenTag = !this.isHiddenTag;
            toast.setText(this.isHiddenTag == true ? context.getResources().getString(R.string.tag_hidden) : context.getString(R.string.tag_show));
            toast.show();
        }
        view.setBackground(this.isHiddenTag == true ? context.getResources().getDrawable(R.drawable.round_gray) : context.getResources().getDrawable(R.drawable.round_green));
    }
}
