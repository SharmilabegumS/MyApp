package com.example.navigationcheck.adapter


import android.content.Context
import android.view.View
import android.view.ViewGroup
import de.hdodenhof.circleimageview.CircleImageView
import android.widget.*
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import com.example.navigationcheck.entity.Contacts
import com.example.navigationcheck.R
import java.util.HashMap


class CustomAdapter2(
    context: Context,
    contacts: MutableList<Contacts>,
    private val titleList: List<String>,
    private val dataList: HashMap<String, List<Contacts>>
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        //val expandedListText = getChild(groupPosition,childPosition) as Contacts
        var convertView = convertView

        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.guests_layout, null)
        }

        val expandedListTextView = convertView!!.findViewById<ImageView>(R.id.profile_pic) as CircleImageView
        val mail = convertView!!.findViewById<TextView>(R.id.profile_name) as TextView


        val options = BitmapFactory.Options()
        var bitmap =
            BitmapFactory.decodeByteArray(contacts[childPosition].picId, 0, contacts[childPosition].picId.size, options)
        expandedListTextView.setImageBitmap(bitmap);

        mail.setText(contacts[childPosition].email)


        return convertView


        return convertView
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return 0
        //return this.dataList[this.titleList[groupPosition]]!![childPosition]
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.dataList[this.titleList[groupPosition]]!!.size
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val listTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listTitle)
        listTitleTextView.setTextSize(18f)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.titleList[groupPosition]
    }

    var contacts = contacts
    var context = context


}


