package com.example.Calendar

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class SimpleDividerItemDecoration : RecyclerView.ItemDecoration {
    private var mDivider: Drawable?=null

 constructor( context:Context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

   override fun onDrawOver(c:Canvas ,parent: RecyclerView , state:RecyclerView.State ) {
     var left = parent.getPaddingLeft();
        var right = parent.getWidth() - parent.getPaddingRight();

        var childCount = parent.getChildCount();
        for (i in 0..childCount-1) {
           var child = parent.getChildAt(i);

          var  params:  RecyclerView.LayoutParams = child.getLayoutParams() as RecyclerView.LayoutParams

            var top = child.getBottom() + params.bottomMargin;
            var bottom = top + mDivider!!.getIntrinsicHeight();

            mDivider!!.setBounds(left, top, right, bottom);
            mDivider!!.draw(c);
        }
    }
}