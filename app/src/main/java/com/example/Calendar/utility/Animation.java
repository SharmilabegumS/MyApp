package com.example.Calendar.utility;


import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;

class Animations {
    public Animation fromAtoB(float fromX, float fromY, float toX, float toY, Animation.AnimationListener l, int speed) {


        Animation fromAtoB = new TranslateAnimation(
                Animation.ABSOLUTE, //from xType
                fromX,
                Animation.ABSOLUTE, //to xType
                toX,
                Animation.ABSOLUTE, //from yType
                fromY,
                Animation.ABSOLUTE, //to yType
                toY
        );

        fromAtoB.setDuration(speed);
        fromAtoB.setInterpolator(new AnticipateOvershootInterpolator(1.0f));


        if (l != null)
            fromAtoB.setAnimationListener(l);
        return fromAtoB;
    }

}