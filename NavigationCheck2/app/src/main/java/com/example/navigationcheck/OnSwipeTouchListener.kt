package com.example.navigationcheck

import android.util.Log
import android.view.GestureDetector
import android.view.View
import android.widget.Toast
import android.view.MotionEvent as MotionEvent1


open class OnSwipeTouchListener : View.OnTouchListener {
    override fun onTouch(v: View?, event: MotionEvent1?): Boolean {
        return gestureDetector.onTouchEvent(event)    }

    private val SWIPE_MIN_DISTANCE = 120
    private val SWIPE_MAX_OFF_PATH = 250
    private val SWIPE_THRESHOLD_VELOCITY = 200
    private val gestureDetector = GestureDetector(GestureListener())

    fun onTouch(event: MotionEvent1): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener(), GestureDetector.OnDoubleTapListener {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent1): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent1): Boolean {
            onTouch(e)
            return true
        }
        override fun onLongPress(e: MotionEvent1) {
            Log.e("", "Longpress detected")
        }



        override fun onFling(e1: MotionEvent1, e2: MotionEvent1, velocityX: Float, velocityY: Float): Boolean {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
                    return false;
                }
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwipeLeft();
                }
                // left to right swipe
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onSwipeRight();
                }
            } catch ( e:Exception) {

            }
            return false;
        }
        }












    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}
}
