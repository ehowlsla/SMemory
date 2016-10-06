package com.myspringway.secretmemory.library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.myspringway.secretmemory.R;
import com.myspringway.secretmemory.valid.ValidHelper;


/**
 * Created by yoontaesup on 2015. 5. 4..
 */
public class ClearableEditText extends EditText implements OnTouchListener,
        OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

    public interface Listener {
        void didClearText();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private Listener listener;
    private boolean isEmailType = false;
    private boolean isCheck = false;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setEmailType(boolean isEmailType) { this.isEmailType = isEmailType; }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(Strings.isNotEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            if(ValidHelper.checkEmail(text.trim()) && isCheck == false && isEmailType) {
                isCheck = true;
                xD = getResources().getDrawable(R.drawable.check);
                xD.setBounds(0, 0, xD.getIntrinsicWidth() / 2, xD.getIntrinsicHeight() / 2);
                setClearIconVisible(true, true);
            } else if(!ValidHelper.checkEmail(text.trim()) && isCheck == true && isEmailType) {
                isCheck = false;
                xD = getResources().getDrawable(R.drawable.close);
                xD.setBounds(0, 0, xD.getIntrinsicWidth() / 2, xD.getIntrinsicHeight() / 2);
                setClearIconVisible(true, true);
            } else {
                setClearIconVisible(Strings.isNotEmpty(text), false);
            }
        }
    }

    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources().getDrawable(R.drawable.close);
        }
        isCheck = false;
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false, false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    protected void setClearIconVisible(boolean visible) {
        boolean wasVisible = (getCompoundDrawables()[2] != null); // if(null) return false;
        if (visible != wasVisible) {
            Drawable x = visible ? xD : null;
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }

    protected void setClearIconVisible(boolean visible, boolean isForce) {
        boolean wasVisible = (getCompoundDrawables()[2] != null); // if(null) return false;
        if (visible != wasVisible || isForce) {
            Drawable x = visible ? xD : null;
            setCompoundDrawables(getCompoundDrawables()[0],
                    getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }
}
