package com.example.navigationcheck

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList
import android.R.attr.data
import android.database.Cursor
import android.util.LongSparseArray
import android.widget.*
import android.util.SparseBooleanArray
import android.widget.CompoundButton
import android.graphics.BitmapFactory
import android.R.attr.bitmap
import android.graphics.Bitmap


/*We Have Created Constructor of Custom Adapter and Pass
                context
                ArrayList<Int> which Contain images
                ArrayList<HashMap<String,String>> which contain name and version*/

//Here We extend over Adapter With BaseAdapter()

class CustomAdapter(context: Context,contacts: MutableList<Contacts>) : BaseAdapter(),CompoundButton.OnCheckedChangeListener {

    //Passing Values to Local Variables
 var mCursor:Cursor?=null
    var contacts=contacts
    var context = context
    var tempContactList = ArrayList(contacts)
    private val mCheckStates= LongSparseArray<Boolean>()
    var checkedcontacts= mutableListOf<Contacts>()


    //Auto Generated Method

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myview = convertView
        val holder: ViewHolder



        if (convertView == null) {

            //If Over View is Null than we Inflater view using Layout Inflater
            val mInflater = (context as Activity).layoutInflater

            //Inflating our list_row.
            myview = mInflater!!.inflate(R.layout.item_layout, parent, false)

            //Create Object of ViewHolder Class and set our View to it
            holder = ViewHolder()

            //Find view By Id For all our Widget taken in list_row.

            /*Here !! are use for non-null asserted two prevent From null.
             you can also use Only Safe (?.)

            */
            holder.mImageView = myview!!.findViewById<ImageView>(R.id.profile_pic) as CircleImageView
            holder.mHeader = myview!!.findViewById<TextView>(R.id.profile_name) as TextView
            holder.checkBox=myview!!.findViewById<CheckBox>(R.id.checkBox)

            //Set A Tag to Identify our view.
            myview.setTag(holder)
        } else {

            //If Ouer View in not Null than Just get View using Tag and pass to holder Object.
            holder = myview!!.getTag() as ViewHolder
        }

        //Getting HasaMap At Perticular Position


        //Setting Image to ImageView by position
        val options = BitmapFactory.Options()
        var bitmap = BitmapFactory.decodeByteArray(contacts[position].picId, 0, contacts[position].picId.size, options) //Convert bytearray to bitmap
        holder.mImageView!!.setImageBitmap(bitmap);

        //Setting name to TextView it's Key from HashMap At Position
        holder.mHeader!!.setText(contacts[position].name)
        holder.checkBox!!.setTag(contacts[position].id);
        println("mCheckStates     "+mCheckStates)
       holder.checkBox!!.setChecked(mCheckStates!!.get(contacts[position].id, false));
        holder.checkBox!!.setOnCheckedChangeListener(this);

        //Setting version to TextView it's Key from HashMap At Position


        return myview

    }

    fun isChecked(id: Long): Boolean {
        return mCheckStates!!.get(id, false)
    }

    fun setChecked(id: Long, isChecked: Boolean) {
        mCheckStates!!.put(id, isChecked)
        notifyDataSetChanged()
    }

    fun toggle(id: Long) {
        setChecked(id, !isChecked(id))
    }

    override fun onCheckedChanged(
        buttonView: CompoundButton,
        isChecked: Boolean
    ) {

        mCheckStates!!.put(buttonView.tag as Long, isChecked)
    }


    //Auto Generated Method
    override fun getItem(p0: Int): Any {

        //Return the Current Position of ArrayList.
        return contacts.get(p0)

    }
    fun getCheckedContacts():LongSparseArray<Boolean>{
        return mCheckStates
    }

    //Auto Generated Method
    override fun getItemId(p0: Int): Long {
        return contacts.get(p0).id.toLong()

    }

    //Auto Generated Method

    override fun getCount(): Int {

        //Return Size Of ArrayList
        return contacts.size
    }



    //Create A class To hold over View like we taken in list_row.xml
    class ViewHolder {

        var mImageView: ImageView? = null
        var mHeader: TextView? = null
        var checkBox:CheckBox?=null
    }


    //Function to set data according to Search Keyword in ListView
    fun filter(text: String?):ArrayList<Contacts> {


        //Our Search text
        val text = text!!.toLowerCase(Locale.getDefault())


        //Here We Clear Both ArrayList because We update according to Search query.
   contacts.clear()


        if (text.length == 0) {

            /*If Search query is Empty than we add all temp data into our main ArrayList

            We store Value in temp in Starting of Program.

            */
contacts.addAll(tempContactList)


        } else {


            for (i in 0..tempContactList.size - 1) {

                /*
                If our Search query is not empty than we Check Our search keyword in Temp ArrayList.
                if our Search Keyword in Temp ArrayList than we add to our Main ArrayList
                */

                if (tempContactList.get(i).name!!.toLowerCase(Locale.getDefault()).contains(text)) {
contacts.add(tempContactList.get(i))


                }

            }
        }

        //This is to notify that data change in Adapter and Reflect the changes.
        notifyDataSetChanged()
return ArrayList(contacts)



    }
fun updateList( contacts:ArrayList<Contacts>) {
        this.contacts.clear();
        this.contacts.addAll(contacts)
    }
    fun swapCursor(c: Cursor?): Cursor? {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor === c) {
            return null // bc nothing has changed
        }
        val temp = mCursor
        this.mCursor = c // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged()
        }
        return temp
    }



}

