package com.example.navigationcheck

import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.content.Intent
import android.os.Bundle
import android.util.LongSparseArray
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import com.example.navigationcheck.DataBase.DataBaseManager
import com.example.navigationcheck.Domain.AddEvent
import com.example.navigationcheck.Entity.Event
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_invite_guests.*
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class InviteGuests() : AppCompatActivity(),View.OnClickListener,Parcelable {
    private val IMAGES_LOADER = 0
    lateinit var dbm:DataBaseManager
    lateinit var customAdapter :CustomAdapter


    override fun onClick(v: View?) {
      //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var listView: ListView
    var contactNames = mutableListOf<String>()
    lateinit var searchView:SearchView
    lateinit var list: LongSparseArray<Boolean>
    lateinit var ok : FloatingActionButton
    var selectedContacts=arrayListOf<Contacts>()
    lateinit var no_search:ImageView

    constructor(parcel: Parcel) : this() {


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_guests)
        setSupportActionBar(toolbar_invite_guests)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Event"

        // Set action bar/toolbar sub title


        // Set action bar elevation
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);

        dbm=DataBaseManager(this)
        customAdapter = CustomAdapter(this,getContacts())
       listView=findViewById(R.id.listView)
        searchView=findViewById(R.id.searchView)
        no_search=findViewById(R.id.search_not_found)

        ok=findViewById(R.id.ok)

        listView.adapter = customAdapter
        var boolArray=customAdapter.getCheckedContacts()
        var boolArrayList=ArrayList<Boolean>()
        for(i in 0..boolArray.size()){
            if(boolArray.get(i.toLong())==true){
                boolArrayList.add(true)
            }
        }
        if(boolArrayList.contains(true)){

        } else {

        }

        listView.setOnItemClickListener { adapterView, view, position, l ->

            customAdapter.toggle(l)
            var boolArray=customAdapter.getCheckedContacts()
            var boolArrayList=ArrayList<Boolean>()
            for(i in 0..boolArray.size()){
                if(boolArray.get(i.toLong())==true){
                    boolArrayList.add(true)
                }
            }
            if(boolArrayList.contains(true)){

            } else {

            }
            //Provide the data on Click position in our listview
            var contactSelected: Contacts = customAdapter.getItem(position) as Contacts
            var name: String = contactSelected.name
            var image: ByteArray = contactSelected.picId
            var id: Long = contactSelected.id

            Toast.makeText(this@InviteGuests, "Name : " + name, Toast.LENGTH_LONG).show()


        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val text = newText
                /*Call filter Method Created in Custom Adapter
                    This Method Filter ListView According to Search Keyword
                 */
                customAdapter.filter(text)
                if (customAdapter.filter(text).isEmpty()!=true) {
                    no_search.visibility=View.INVISIBLE
                } else {
                    no_search.visibility=View.VISIBLE
                }

                return false
            }

        })
ok.setOnClickListener{
    var intent=Intent(this, AddContact::class.java)
    startActivity(intent)
}


var dbm=DataBaseManager(this)
    }





    fun getContacts():MutableList<Contacts>{

        var ist=dbm.getContacts()

        return ist
    }
    fun putGuestList(){
        list=customAdapter.getCheckedContacts()
        var contactList=getContacts()
        for(i in 0..list.size()-1){



        }
        var contactPos=ArrayList<Int>()
        for(i in 0..list.size()-1){
            val key = list.keyAt(i)
            if(list.get(key)==true){
                contactPos.add(list.keyAt(i).toInt())
            }
        }

        for (j in 0..contactPos.size-1) {
                selectedContacts.add(contactList.get((contactPos.get(j)-1).toInt()))


        }
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
                var contactList=ArrayList<Contacts>()

                contactList.addAll(
                    selectedContacts.stream()
                        .distinct()
                        .collect(Collectors.toList()) as Collection<Contacts>
                )


                var conp:ContactParcel= ContactParcel(contactList)
                val resultIntent = Intent()
                var bundle=Bundle()
                bundle.putParcelableArrayList("arrayList",conp.getContacts())
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