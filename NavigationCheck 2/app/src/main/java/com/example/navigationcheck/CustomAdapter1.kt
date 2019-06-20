package com.example.navigationcheck


import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.navigationcheck.DataBase.DataBaseManager
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by delaroy on 9/10/17.
 */

 class CustomAdapter1(private val mContext:Context):RecyclerView.Adapter<CustomAdapter1.ImagesViewHolder>() {

 // Class variables for the Cursor that holds task data and the Context
    private var mCursor:Cursor? = null


 override fun getItemCount():Int {
     if (mCursor == null) {
         return  0
     } else
         return mCursor!!.count

 }

 override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):ImagesViewHolder {
val inflater = LayoutInflater.from(mContext)
val view = inflater.inflate(R.layout.item_layout, parent, false)
return ImagesViewHolder(view)
}

 override fun onBindViewHolder(holder:ImagesViewHolder, position:Int) {

 // Indices for the _id, description, and priority columns
        //int idIndex = mCursor.getColumnIndex(_ID);
        val fragranceName = mCursor!!.getColumnIndex(DataBaseManager.CONTACT_NAME)



mCursor!!.moveToPosition(position) // get to the right location in the cursor

 // Determine the values of the wanted data
       // final int id = mCursor.getInt(idIndex);
        val image = mCursor!!.getBlob(fragranceName)

 //Set values
       // holder.itemView.setTag(id);

        val bmp = BitmapFactory.decodeByteArray(image, 0, image.size)
     holder.mImageView!!.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200,
200, false))
     holder.checkBox
     holder.mHeader


}

 fun swapCursor(c:Cursor?):Cursor? {
 // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor === c)
{
return null // bc nothing has changed
}
val temp = mCursor
this.mCursor = c // new cursor value assigned

 //check if this is a valid cursor, then update the cursor
        if (c != null)
{
this.notifyDataSetChanged()
}
return temp
}

inner class ImagesViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

    var mImageView: ImageView? = null
    var mHeader: TextView? = null
    var checkBox: CheckBox?=null

init{
  mImageView = itemView.findViewById<ImageView>(R.id.profile_pic) as CircleImageView
mHeader = itemView.findViewById<TextView>(R.id.profile_name) as TextView
  checkBox=itemView.findViewById<CheckBox>(R.id.checkBox)



}

}
}

