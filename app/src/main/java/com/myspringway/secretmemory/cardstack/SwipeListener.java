package com.myspringway.secretmemory.cardstack;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by aaron on 4/12/2015.
 */
public class SwipeListener implements View.OnTouchListener {

    private float ROTATION_DEGREES = 15f;
    private float OPACITY_END = 0.33f;
    private float initialX;
    private float initialY;

    private int mActivePointerId;
    private float initialXPress;
    private float initialYPress;
    private ViewGroup parent;
    private float parentWidth;
    private int paddingLeft;

    private View card;
    SwipeCallback callback;
    private boolean deactivated;
    private View rightView;
    private View leftView;


    public SwipeListener(View card, final SwipeCallback callback, float initialX, float initialY, float rotation, float opacityEnd) {
        this.card = card;
        this.initialX = initialX;
        this.initialY = initialY;
        this.callback = callback;
        this.parent = (ViewGroup) card.getParent();
        this.parentWidth = parent.getWidth();
        this.ROTATION_DEGREES = rotation;
        this.OPACITY_END = opacityEnd;
        this.paddingLeft = ((ViewGroup) card.getParent()).getPaddingLeft();
    }

    public SwipeListener(View card, final SwipeCallback callback, float initialX, float initialY, float rotation, float opacityEnd, int screenWidth) {
        this.card = card;
        this.initialX = initialX;
        this.initialY = initialY;
        this.callback = callback;
        this.parent = (ViewGroup) card.getParent();
        this.parentWidth = screenWidth;
        this.ROTATION_DEGREES = rotation;
        this.OPACITY_END = opacityEnd;
        this.paddingLeft = ((ViewGroup) card.getParent()).getPaddingLeft();
    }


    private boolean click = true;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (deactivated) return false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                click = true;
                //gesture has begun
                float x;
                float y;
                //cancel any current animations
                v.clearAnimation();

                mActivePointerId = event.getPointerId(0);

                x = event.getX();
                y = event.getY();

                if(event.findPointerIndex(mActivePointerId) == 0) {
                    callback.cardActionDown();
                }

                initialXPress = x;
                initialYPress = y;
                break;

            case MotionEvent.ACTION_MOVE:
                //gesture is in progress

                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                //Log.i("pointer index: " , Integer.toString(pointerIndex));
                if(pointerIndex < 0 || pointerIndex > 0 ){
                    break;
                }

                final float xMove = event.getX(pointerIndex);
                final float yMove = event.getY(pointerIndex);

                //calculate distance moved
                final float dx = xMove - initialXPress;
                final float dy = yMove - initialYPress;

                //throw away the move in this case as it seems to be wrong
                //TODO: figure out why this is the case
                if((int)initialXPress == 0 && (int) initialYPress == 0){
                    //makes sure the pointer is valid
                    break;
                }
                //calc rotation here
                float posX = card.getX() + dx;
                float posY = card.getY() + dy;

                //in this circumstance consider the motion a click
                if (Math.abs(dx + dy) > 5) click = false;

                card.setX(posX);
                card.setY(posY);

                //card.setRotation
                float distObjectX = posX - initialX;
                float rotation = ROTATION_DEGREES * 2.f * distObjectX / parentWidth;
                card.setRotation(rotation);

                float alpha = (((posX - paddingLeft) / (parentWidth * OPACITY_END)));
                card.setAlpha(1 - Math.abs(alpha/2));

                break;

            case MotionEvent.ACTION_UP:
                //gesture has finished
                //check to see if card has moved beyond the left or right bounds or reset
                //card position
                checkCardForEvent();

                if(event.findPointerIndex(mActivePointerId) == 0) {
                    callback.cardActionUp();
                }
                //check if this is a click event and then perform a click
                //this is a workaround, android doesn't play well with multiple listeners

                if (click) v.performClick();
                //if(click) return false;

                break;

            default:
                return false;
        }
        return true;
    }

    public void checkCardForEvent() {

        if (cardBeyondLeftBorder()) {
            animateOffScreenLeft(700)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                            callback.cardOffScreen();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
            callback.cardSwipedLeft();
            this.deactivated = true;
        } else if (cardBeyondRightBorder()) {
            animateOffScreenRight(700)
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            callback.cardOffScreen();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            callback.cardSwipedRight();
            this.deactivated = true;
        } else {
            resetCardPosition();
        }
    }

    private boolean cardBeyondLeftBorder() {
        //check if cards middle is beyond the left quarter of the screen
        return (card.getX() + (card.getWidth() / 2) < (parentWidth / 4.f));
    }

    private boolean cardBeyondRightBorder() {
        //check if card middle is beyond the right quarter of the screen
        return (card.getX() + (card.getWidth() / 2) > ((parentWidth / 4.f) * 3));
    }

    public ViewPropertyAnimator resetCardPosition() {
        if(rightView!=null)rightView.setAlpha(0);
        if(leftView!=null)leftView.setAlpha(0);
        return card.animate()
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator(1.5f))
                .x(initialX)
                .y(initialY)
                .alpha(1)
                .rotation(0);
    }

    public ViewPropertyAnimator animateOffScreenLeft(int duration) {
        float y = card.getY();
        if(card.getX() != 0) y = card.getY() * (1 + (parentWidth - Math.abs(card.getX())) / parentWidth);

        return card.animate()
                .setDuration(duration)
                .x(-(parentWidth) * (float) 1.4)
                .y(y)
                .rotation(-30);
    }


    public ViewPropertyAnimator animateOffScreenRight(int duration) {
        float y = card.getY();
        if(card.getX() != 0) y = card.getY() * (1 + (parentWidth - Math.abs(card.getX())) / parentWidth);

        return card.animate()
                .setDuration(duration)
                .x(parentWidth * 2)
                .y(y)
                .rotation(30);
    }

    public interface SwipeCallback {
        void cardSwipedLeft();
        void cardSwipedRight();
        void cardOffScreen();
        void cardActionDown();
        void cardActionUp();
    }
}
