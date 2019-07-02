package com.example.Calendar

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
import com.example.Calendar.dataBase.DataBaseManager
import com.example.Calendar.entity.ContactParcel
import com.example.Calendar.entity.Contacts
import com.example.Calendar.adapter.CustomAdapter
import kotlinx.android.synthetic.main.activity_invite_guests.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class InviteGuests() : AppCompatActivity() {
    lateinit var dbm: DataBaseManager
    lateinit var customAdapter: CustomAdapter

    lateinit var listView: ListView
    lateinit var list: LongSparseArray<Boolean>
    lateinit var ok: FloatingActionButton
    var selectedContacts = arrayListOf<Contacts>()
    lateinit var no_search: ImageView
    lateinit var no_search_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_guests)
        setSupportActionBar(toolbar_invite_guests)
        val actionBar = supportActionBar
        actionBar!!.title = "Contacts"
        toolbar_invite_guests.setTitleTextColor(Color.WHITE);
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        toolbar_invite_guests.setTitleTextColor(Color.WHITE);

        dbm = DataBaseManager(this)
        val bundle = intent.extras
        val value = bundle!!.getStringArrayList("nameList")
        customAdapter = CustomAdapter(this, getContacts())
        listView = findViewById(R.id.listView)
        listView.setDivider(null);
        no_search = findViewById(R.id.search_not_found)
        no_search_text = findViewById(R.id.search_not_found_msg)
        ok = findViewById(R.id.ok)

        listView.adapter = customAdapter
        val boolArray = customAdapter.getCheckedContacts()
        val boolArrayList = ArrayList<Boolean>()
        for (i in 0..boolArray.size()) {
            if (boolArray.get(i.toLong()) == true) {
                boolArrayList.add(true)
            }
        }
        if (boolArrayList.contains(true)) {

        } else {

        }
        for (i in 0..value.size - 1) {

            val id = dbm.getContactId(value.get(i))
            customAdapter.toggle(id)
        }

        listView.setOnItemClickListener { adapterView, view, position, l ->
            customAdapter.toggle(l)
            val boolArray = customAdapter.getCheckedContacts()
            val boolArrayList = ArrayList<Boolean>()
            for (i in 0..boolArray.size()) {
                if (boolArray.get(i.toLong()) == true) {
                    boolArrayList.add(true)
                }
            }
            if (boolArrayList.contains(true)) {

            } else {

            }


        }


        ok.setOnClickListener {
            val intent = Intent(this, AddContact::class.java)
            startActivity(intent)
        }

    }

    fun getContacts(): MutableList<Contacts> {

        val ist = dbm.getContacts()

        return ist
    }

    fun putGuestList() {
        list = customAdapter.getCheckedContacts()
        val contactList = getContacts()
        for (i in 0..list.size() - 1) {


        }
        val contactPos = ArrayList<Int>()
        for (i in 0..list.size() - 1) {
            val key = list.keyAt(i)
            if (list.get(key) == true) {
                contactPos.add(list.keyAt(i).toInt())
            }
        }

        for (j in 0..contactList.size - 1) {
            for (i in 0..contactPos.size - 1) {

                if (contactList.get(j).id == contactPos.get(i).toLong()) {
                    selectedContacts.add(contactList.get(j))
                }
            }


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

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val text = newText
                customAdapter.filter(text)
                if (customAdapter.filter(text).isEmpty() != true) {
                    no_search.visibility = View.INVISIBLE
                    no_search_text.visibility = View.INVISIBLE
                } else if (text.equals("")) {
                    no_search.visibility = View.INVISIBLE
                    no_search_text.visibility = View.INVISIBLE
                } else {
                    no_search_text.text = "Search $text not found!"
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
                val contactList = ArrayList<Contacts>()

                contactList.addAll(
                    selectedContacts.stream()
                        .distinct()
                        .collect(Collectors.toList()) as Collection<Contacts>
                )

                val conp: ContactParcel =
                    ContactParcel(contactList)

                val resultIntent = Intent()
                val bundle = Bundle()
                bundle.putLongArray("arrayList", conp.getContactIds().toLongArray())
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