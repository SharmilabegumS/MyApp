package com.example.Calendar.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList
import android.util.LongSparseArray
import android.widget.*
import android.widget.CompoundButton
import android.graphics.BitmapFactory
import com.example.Calendar.entity.Contacts
import com.example.Calendar.R


class CustomAdapter(var context: Context, var contacts: MutableList<Contacts>) : BaseAdapter(),
    CompoundButton.OnCheckedChangeListener {
    private var tempContactList = ArrayList(contacts)
    private val mCheckStates = LongSparseArray<Boolean>()


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myview = convertView
        val holder: ViewHolder
        if (convertView == null) {
            val mInflater = (context as Activity).layoutInflater
            myview = mInflater.inflate(R.layout.item_layout, parent, false)

            holder = ViewHolder()

            holder.mImageView = myview!!.findViewById<ImageView>(R.id.profile_pic) as CircleImageView
            holder.mHeader = myview.findViewById<TextView>(R.id.profile_name) as TextView
            holder.checkBox = myview.findViewById<CheckBox>(R.id.checkBox)
            myview.setTag(holder)
        } else {
            holder = myview!!.getTag() as ViewHolder
        }
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeByteArray(
            contacts[position].picId,
            0,
            contacts[position].picId.size,
            options
        )

        holder.mImageView!!.setImageBitmap(bitmap)
        holder.mHeader!!.text = contacts[position].name
        holder.checkBox!!.tag = contacts[position].id
        holder.checkBox!!.isChecked = mCheckStates.get(contacts[position].id, false)
        holder.checkBox!!.setOnCheckedChangeListener(this)
        return myview

    }

    private fun isChecked(id: Long): Boolean {
        return mCheckStates.get(id, false)
    }

    private fun setChecked(id: Long, isChecked: Boolean) {
        mCheckStates.put(id, isChecked)
        notifyDataSetChanged()
    }

    fun toggle(id: Long) {
        setChecked(id, !isChecked(id))
    }

    override fun onCheckedChanged(
        buttonView: CompoundButton,
        isChecked: Boolean
    ) {

        mCheckStates.put(buttonView.tag as Long, isChecked)
    }

    override fun getItem(p0: Int): Any {
        return contacts[p0]

    }

    fun getCheckedContacts(): LongSparseArray<Boolean> {
        return mCheckStates
    }

    override fun getItemId(p0: Int): Long {
        return contacts.get(p0).id.toLong()

    }

    override fun getCount(): Int {
        return contacts.size
    }

    private class ViewHolder {

        var mImageView: ImageView? = null
        var mHeader: TextView? = null
        var checkBox: CheckBox? = null
    }

    fun filter(text: String?): ArrayList<Contacts> {
        val text = text!!.toLowerCase(Locale.getDefault())
        contacts.clear()


        if (text.length == 0) {
            contacts.addAll(tempContactList)


        } else {


            for (i in 0..tempContactList.size - 1) {

                if (tempContactList.get(i).name.toLowerCase(Locale.getDefault()).contains(text)) {
                    contacts.add(tempContactList.get(i))


                }

            }
        }
        notifyDataSetChanged()
        return ArrayList(contacts)


    }

    fun updateList(contacts: ArrayList<Contacts>) {
        this.contacts.clear()
        this.contacts.addAll(contacts)
    }

}

