package com.example.navigationcheck


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.Px


/**
 * Neat animation that captures a layer bitmap and "flies" it to the corner of the screen, used to create
 * an "added to playlist" effect or something like that. The method delegate_draw() must be called from the
 * parent view to update the animation
 *
 * @author rupps'2014
 * license public domain, attribution appreciated
 */

@SuppressWarnings("unused")
public class FlyOverView {

   val DEFAULT_DURATION:Int = 1000;
   val TAG:String  = "FlyOverView";
val LOG_ON:Boolean  = false;

    lateinit var mFlyoverAnimation:ObjectAnimator;

 var mCurrentX:Float=-724f
 var mCurrentY:Float=-1453f
var mMatrix:Matrix = Matrix();

   var  mBitmap:Bitmap?=null
     var mPaint:Paint  = Paint();

    var mParentView: View? = null;
  lateinit var mOnFlyOverFinishedListener:OnFlyOverFinishedListener

   interface OnFlyOverFinishedListener {
     fun onFlyOverFinishedListener()
    }

    /**
     * Creates the FlyOver effect
     *
     * @param parent    Container View to invalidate. That view has to call this class' delegate_draw() in its dispatchDraw().
     * @param viewToFly Target View to animate
     * @param finalX    Final X coordinate
     * @param finalY    Final Y coordinate
     */

   constructor(parent:View, viewToFly:View, @Px finalX:  Int, @Px finalY:Int,  listener:OnFlyOverFinishedListener) {
        setupFlyOver(parent, viewToFly, finalX, finalY, DEFAULT_DURATION, listener);
    }

    /**
     * Creates the FlyOver effect
     *
     * @param parent    Container View to invalidate. That view has to call this class' delegate_draw() in its dispatchDraw().
     * @param viewToFly Target View to animate
     * @param finalX    Final X coordinate
     * @param finalY    Final Y coordinate
     * @param duration  Animation duration
     */

 constructor(parent:View , viewToFly:View, @Px finalX:Int, @Px  finalY:Int,  duration:Int, listener:OnFlyOverFinishedListener ) {
        setupFlyOver(parent, viewToFly, finalX, finalY, duration, listener);
    }

    /**
     * cancels current animation from the outside
     */
fun cancel()  {
        if (mFlyoverAnimation != null) {
           mFlyoverAnimation.cancel();
        }

    }

fun setupFlyOver(parentContainer:View , viewToFly:View , @Px  finalX:Int, @Px  finalY:Int,  duration:Int,listener: OnFlyOverFinishedListener ) {


     var location = IntArray(2)

        mParentView = parentContainer;
        mOnFlyOverFinishedListener = listener;
        viewToFly.getLocationInWindow(location);


            var sourceX = location[0]
           var  sourceY = location[1]

        if (LOG_ON) Log.v(TAG, "FlyOverView, item " + viewToFly+", finals "+finalX+", "+finalY+", sources "+sourceX+", "+sourceY+ " duration "+duration);

         /* Animation definition table */

        mFlyoverAnimation = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat("translationX", sourceX.toFloat(), finalX.toFloat()),
            PropertyValuesHolder.ofFloat("translationY", sourceY.toFloat(), finalY.toFloat()),
            PropertyValuesHolder.ofFloat("scaleAlpha", 1f, 1f) // not to 0 so we see the end of the effect in other properties
        );

        mFlyoverAnimation.setDuration(100);
        mFlyoverAnimation.setRepeatCount(0);
        mFlyoverAnimation.setInterpolator(AccelerateDecelerateInterpolator());
    mFlyoverAnimation.addListener(object : SimpleAnimationListener() {
        override fun onAnimationEnd(animation: Animator) {
            if (LOG_ON) Log.v(TAG, "FlyOver: End")
            val mParentView1=mParentView
            mParentView1!!.invalidate()
            if (mBitmap != null) mBitmap!!.recycle() // just for safety
            mBitmap = null
            mOnFlyOverFinishedListener.onFlyOverFinishedListener()
        }
    })
        // take snapshot of viewToFly
        viewToFly.setDrawingCacheEnabled(true);
        mBitmap = Bitmap.createBitmap(viewToFly.getDrawingCache());
        viewToFly.setDrawingCacheEnabled(false);

        mFlyoverAnimation.start();

    }

    // ANIMATOR setter
 fun setTranslationX( position:Float) {
        mCurrentX = position;
    }

    // ANIMATOR setter
   fun setTranslationY(position:Float ) {
        mCurrentY = position;
    }

    // ANIMATOR setter
    // as this will be called in every iteration, we set here all parameters at once then call invalidate,
    // rather than separately
  fun setScaleAlpha( position:Float) {

        mPaint.setAlpha((100 * position).toInt());
        mMatrix.setScale(position, position);
        mMatrix.postRotate(360 * position); // asemos de to'
        mMatrix.postTranslate(mCurrentX, mCurrentY);

        mParentView!!.invalidate();
    }

    /**
     * This has to be called from the root container's dispatchDraw()
     * in order to update the animation.
     */

   fun delegate_draw( c:Canvas) {
        if (LOG_ON) Log.v(TAG, "CX " + mCurrentX + ", CY " + mCurrentY);
        c.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    private abstract class SimpleAnimationListener:Animator.AnimatorListener {
  override fun onAnimationStart( animation:Animator) {}
   override fun  onAnimationRepeat( animation:Animator) {}
       override fun onAnimationCancel(animation:Animator) {}

    }
}
