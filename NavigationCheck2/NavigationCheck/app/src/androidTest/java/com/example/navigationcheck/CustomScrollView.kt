package com.example.navigationcheck

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView

class CustomScrollView : ScrollView {
    var gestureDetector: GestureDetector? = null
    var gestureListener: View.OnTouchListener? = null;

  constructor(context: Context, attrs: AttributeSet) : super(context,attrs){
        gestureDetector = GestureDetector(YScrollDetector())
        setFadingEdgeLength(0)
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(ev);
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //Call super first because it does some hidden motion event handling
        var result: Boolean = super.onInterceptTouchEvent(ev);
        //Now see if we are scrolling vertically with the custom gesture detector
        if (gestureDetector!!.onTouchEvent(ev)) {
            return result;
        }
        //If not scrolling vertically (more y than x), don't hijack the event.
        else {
            return false;
        }
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            try {
                if (Math.abs(distanceY) > Math.abs(distanceX)) {
                    return true;
                } else {
                    return false;
                }
            } catch (e: Exception) {
                // nothing
            }
            return false;
        }
    }
}