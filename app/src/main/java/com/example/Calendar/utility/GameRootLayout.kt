package com.example.Calendar.utility

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Px
import android.widget.FrameLayout


class GameRootLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    // the currently-animating effect, if any
    private var mFlyOverView: FlyOverView? = null

    override fun dispatchDraw(canvas: Canvas) {

        // draw the children
        super.dispatchDraw(canvas)

        // draw our stuff
        if (mFlyOverView != null) {
            mFlyOverView!!.delegate_draw(canvas)
        }
    }


    /**
     * Starts a flyover animation for the specified view.
     * It will "fly" to the desired position with an alpha / translation / rotation effect
     *
     * @param viewToFly The view to fly
     * @param targetX The target X coordinate
     * @param targetY The target Y coordinate
     */

    fun addFlyOver(viewToFly: View, @Px targetX: Int, @Px targetY: Int): FlyOverView? {
        if (mFlyOverView != null) mFlyOverView!!.cancel()
        mFlyOverView = FlyOverView(
            this,
            viewToFly,
            targetX,
            targetY,
            object : FlyOverView.OnFlyOverFinishedListener {
                override fun onFlyOverFinishedListener() {
                    mFlyOverView = null
                }
            })
        return mFlyOverView
    }
}