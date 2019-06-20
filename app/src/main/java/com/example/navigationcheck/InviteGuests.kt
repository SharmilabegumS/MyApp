package com.example.navigationcheck

import android.content.Intent
import android.os.Bundle
import android.util.LongSparseArray
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.entity.ContactParcel
import com.example.navigationcheck.entity.Contacts
import com.example.navigationcheck.adapter.CustomAdapter
import kotlinx.android.synthetic.main.activity_invite_guests.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class InviteGuests() : AppCompatActivity(), View.OnClickListener, Parcelable {
    private val IMAGES_LOADER = 0
    lateinit var dbm: DataBaseManager
    lateinit var customAdapter: CustomAdapter


    override fun onClick(v: View?) {
        //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var listView: ListView
    var contactNames = mutableListOf<String>()
    lateinit var list: LongSparseArray<Boolean>
    lateinit var ok: FloatingActionButton
    var selectedContacts = arrayListOf<Contacts>()
    lateinit var no_search: ImageView
    lateinit var no_search_text: TextView

    constructor(parcel: Parcel) : this() {


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_guests)
        setSupportActionBar(toolbar_invite_guests)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Contacts"

        // Set action bar/toolbar sub title

        toolbar_invite_guests.setTitleTextColor(Color.WHITE);
        // Set action bar elevation
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        toolbar_invite_guests.setTitleTextColor(Color.WHITE);

        dbm = DataBaseManager(this)
        var bundle = intent.extras
        var value = bundle!!.getStringArrayList("nameList")
        println("Received nameList: "+value)
        customAdapter = CustomAdapter(this, getContacts())
        listView = findViewById(R.id.listView)
     listView.setDivider(null);
        no_search = findViewById(R.id.search_not_found)
        no_search_text = findViewById(R.id.search_not_found_msg)
        ok = findViewById(R.id.ok)

        listView.adapter = customAdapter
        var boolArray = customAdapter.getCheckedContacts()
        var boolArrayList = ArrayList<Boolean>()
        for (i in 0..boolArray.size()) {
            if (boolArray.get(i.toLong()) == true) {
                boolArrayList.add(true)
            }
        }
        if (boolArrayList.contains(true)) {

        } else {

        }
        for(i in 0..value.size-1) {

            var id=dbm.getContactId(value.get(i))
            println("Its id: "+id)
            customAdapter.toggle(id)
        }

        listView.setOnItemClickListener { adapterView, view, position, l ->
            customAdapter.toggle(l)
            var boolArray = customAdapter.getCheckedContacts()
            var boolArrayList = ArrayList<Boolean>()
            for (i in 0..boolArray.size()) {
                if (boolArray.get(i.toLong()) == true) {
                    boolArrayList.add(true)
                }
            }
            if (boolArrayList.contains(true)) {

            } else {

            }
            //Provide the data on Click position in our listview
           /* var contactSelected: Contacts = customAdapter.getItem(position) as Contacts
            var name: String = contactSelected.name
            var image: ByteArray = contactSelected.picId
            var id: Long = contactSelected.id*/

        }


        ok.setOnClickListener {
            var intent = Intent(this, AddContact::class.java)
            startActivity(intent)
        }


        //var dbm = DataBaseManager(this)
    }


    fun getContacts(): MutableList<Contacts> {

        var ist = dbm.getContacts()

        return ist
    }

    fun putGuestList() {
        list = customAdapter.getCheckedContacts()
        var contactList = getContacts()
        for (i in 0..list.size() - 1) {


        }
        var contactPos = ArrayList<Int>()
        for (i in 0..list.size() - 1) {
            val key = list.keyAt(i)
            if (list.get(key) == true) {
                contactPos.add(list.keyAt(i).toInt())
            }
        }
        println("Contact pos: "+contactPos)

        for (j in 0..contactList.size - 1) {
            for(i in 0..contactPos.size-1){

                if(contactList.get(j).id==contactPos.get(i).toLong()){
                    selectedContacts.add(contactList.get(j) )
                }
            }


        }
        println("Selected contacts: "+selectedContacts)
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<InviteGuests> {
        override fun createFromParcel(parcel: Parcel): InviteGuests {
            return InviteGuests(parcel)
        }

        override fun newArray(size: Int): Array<InviteGuests?> {
            return arrayOfNulls(size)
        }

    }

    public override fun onResume() {
        super.onResume()
        customAdapter.updateList(getContacts() as ArrayList<Contacts>)
        customAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.invite_guests_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as androidx.appcompat.widget.SearchView

        searchView?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val text = newText
                /*Call filter Method Created in Custom Adapter
                    This Method Filter ListView According to Search Keyword
                 */
                customAdapter.filter(text)
                if (customAdapter.filter(text).isEmpty() != true) {
                    no_search.visibility = View.INVISIBLE
                    no_search_text.visibility = View.INVISIBLE
                } else if (text.equals("")) {
                    no_search.visibility = View.INVISIBLE
                    no_search_text.visibility = View.INVISIBLE
                } else {
                    no_search_text.text="Search $text not found!"
                    no_search.visibility = View.VISIBLE
                    no_search_text.visibility = View.VISIBLE
                }

                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("NewApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.save -> {
                putGuestList()
                var contactList = ArrayList<Contacts>()

                contactList.addAll(
                    selectedContacts.stream()
                        .distinct()
                        .collect(Collectors.toList()) as Collection<Contacts>
                )


                var conp: ContactParcel =
                    ContactParcel(contactList)
                val resultIntent = Intent()
                var bundle = Bundle()
                bundle.putParcelableArrayList("arrayList", conp.getContacts())
                resultIntent.putExtra("bundle", bundle)
                setResult(1, resultIntent)
                finish()
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }

    }

}