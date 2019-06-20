package com.example.navigationcheck.DataBase



import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.navigationcheck.Contacts

import com.example.navigationcheck.Entity.Event
import java.util.*
import kotlin.collections.ArrayList


private val TABLE_USERS = "Users"
private val USER_ID = "User_Id"
private val PASSWORD = "Password"
private val DATABASE_NAME="Calendar"
private val TABLE_EVENTS = "Events"
private val EVENT_ID = "Event_Id"
private val EVENT_TITLE = "Event_Title"
private val EVENT_FROM_DATE = "From_Date"
private val EVENT_TO_DATE = "To_Date"
private val EVENT_DESCRIPTION = "Event_Description"


private val TABLE_EVENTS_GUESTS = "Event_Guests"
private val EVENT_GUESTS = "Event_Guests"
lateinit var eventCommon:String



class DataBaseManager(var context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,1) {
    companion object {
        val CONTACTS = "Contacts"
        val CONTACT_ID = "Id"
        val CONTACT_NAME = "Name"
        val CONTACT_EMAIL="Email"
        val CONTACT_IMAGE = "Photo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable =
            "create table if not exists " + TABLE_USERS + "( " + USER_ID + " int(11) PRIMARY KEY UNIQUE, " + PASSWORD + "  varchar(45) NOT NULL) "
        val createEventTable =
            "CREATE TABLE if not exists " + TABLE_EVENTS + " (" + EVENT_ID + " varchar(45) not null," + EVENT_TITLE + " varchar(45)  not null," + EVENT_FROM_DATE + " int  not null," + EVENT_TO_DATE + " int  not null, " + EVENT_GUESTS + " varchar(45) NOT NULL, " + EVENT_DESCRIPTION + " varchar(45) NOT NULL," + USER_ID + " int(11),PRIMARY KEY(" + EVENT_ID + ", " + USER_ID + " , " + EVENT_FROM_DATE + " )  ON CONFLICT REPLACE ,FOREIGN KEY (" + USER_ID + ") REFERENCES  " + TABLE_USERS + " ( " + USER_ID + " ))"
        val createGuestTable =
            "create table if not exists " + TABLE_EVENTS_GUESTS + "( " + EVENT_ID + " varchar(45) NOT NULL, " + EVENT_GUESTS + "  varchar(45) NOT NULL,FOREIGN KEY (" + EVENT_ID + ") REFERENCES  " + TABLE_EVENTS + " ( " + EVENT_ID + " ) ON DELETE CASCADE ON UPDATE RESTRICT)"

        val createContactsTable =
            "CREATE TABLE if not exists " + CONTACTS + " (" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTACT_NAME + " varchar(45) NOT NULL, " + CONTACT_EMAIL + " varchar(45) NOT NULL, " +
                    CONTACT_IMAGE + " BLOB NOT NULL )"

        db?.execSQL(createContactsTable)

        db?.execSQL(createUserTable)
        db?.execSQL(createEventTable)
        db?.execSQL(createGuestTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addEvent(userId: String, event: Event): Boolean {
        val db = this.writableDatabase
        var cv = ContentValues()
        eventCommon = event.eventId
        cv.put(EVENT_ID, event.eventId)
        cv.put(EVENT_TITLE, event.title)
        cv.put(EVENT_FROM_DATE, event.startDate)
        cv.put(EVENT_TO_DATE, event.endDate)
        cv.put(EVENT_GUESTS, event.guests.joinToString())
        cv.put(EVENT_DESCRIPTION, event.description)
        cv.put(USER_ID, userId)
        var result = db.insert(TABLE_EVENTS, null, cv)
        if (result == -1.toLong())
            return false
        else
            return true
    }

    fun getEvent(eventId: String): Event? {

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_EVENTS + " where " + EVENT_ID + " = \"$eventId\" "
        var event: Event? = null
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var id = result.getString(result.getColumnIndex(EVENT_ID))
                var title = result.getString(result.getColumnIndex(EVENT_TITLE))
                var startDate = result.getString(result.getColumnIndex(EVENT_FROM_DATE)).toLong()
                var endDate = result.getString(result.getColumnIndex(EVENT_TO_DATE)).toLong()
                var description = result.getString(result.getColumnIndex(EVENT_DESCRIPTION))
                var guests = result.getString(result.getColumnIndex(EVENT_GUESTS))
                val parts: List<String> = guests.split(",")
                event = Event(id, title, startDate, endDate, parts, description)


            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return event


    }

    fun getEvents(date: Long, userId: String): ArrayList<Event> {
        var eventList = ArrayList<Event>()
        val db = this.readableDatabase
        /*val query = ("select * from  " + TABLE_EVENTS + " where " + EVENT_FROM_DATE + " <= \"$date\" and " + EVENT_TO_DATE
                + " >= \"$date\" and " + USER_ID + " = \"$userId\" union select * from  " + TABLE_EVENTS + " where " + EVENT_FROM_DATE
                + " between \"$date\" and \"${date+86399000L}\"  and " + USER_ID + " = \"$userId\"")*/
        val query =
            ("select * from  " + TABLE_EVENTS + " where " + EVENT_FROM_DATE + " <= \"$date\" and " + EVENT_TO_DATE
                    + " >= \"$date\" and " + USER_ID + " = \"$userId\" ")
        var event: Event? = null
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var id = result.getString(result.getColumnIndex(EVENT_ID))
                var title = result.getString(result.getColumnIndex(EVENT_TITLE))
                var startDate = result.getString(result.getColumnIndex(EVENT_FROM_DATE)).toLong()
                var endDate = result.getString(result.getColumnIndex(EVENT_TO_DATE)).toLong()
                var description = result.getString(result.getColumnIndex(EVENT_DESCRIPTION))
                var guests = result.getString(result.getColumnIndex(EVENT_GUESTS))
                val parts: List<String> = guests.split(",")
                event = Event(id, title, startDate, endDate, parts, description)
                eventList.add(event)

            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return eventList


    }

    fun getEventId(): String {
        return eventCommon
    }

    fun addContact(name: String, email:String, image: ByteArray) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(CONTACT_NAME, name)
        cv.put(CONTACT_EMAIL,email)
        cv.put(CONTACT_IMAGE, image)
        db.insert(CONTACTS, null, cv)
    }

    fun getContacts(): ArrayList<Contacts> {
        var contactList = ArrayList<Contacts>()
        val db = this.readableDatabase
        val query = "Select * from " + CONTACTS + " ORDER BY ROWID "
        val result = db.rawQuery(query, null)
        var contact: Contacts? = null
        if (result.moveToFirst()) {
            do {

                var id = result.getLong(result.getColumnIndex(CONTACT_ID))
                var name = result.getString(result.getColumnIndex(CONTACT_NAME))
                var email = result.getString(result.getColumnIndex(CONTACT_EMAIL))
                var imageArray = result.getBlob(result.getColumnIndex(CONTACT_IMAGE))

                contact = Contacts(id, name,email, imageArray)
                contactList.add(contact)

            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return contactList

    }

    fun getContactNames(): ArrayList<String> {
        var contactList = ArrayList<String>()
        val db = this.readableDatabase
        val query = "Select " + CONTACT_NAME + " from " + CONTACTS + " "
        val result = db.rawQuery(query, null)
        var contact: String? = null
        if (result.moveToFirst()) {
            do {


                var name = result.getString(result.getColumnIndex(CONTACT_NAME))



                contactList.add(name)

            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return contactList

    }

    fun getSingleContact(get: String): Contacts? {
        val db = this.readableDatabase
        val query = "Select * from " + CONTACTS + " where " + CONTACT_NAME + " = \"$get\" "
        var contact: Contacts? = null
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var id = result.getLong(result.getColumnIndex(CONTACT_ID))
                var name = result.getString(result.getColumnIndex(CONTACT_NAME))
                var email = result.getString(result.getColumnIndex(CONTACT_EMAIL))
                var imageArray = result.getBlob(result.getColumnIndex(CONTACT_IMAGE))

                contact = Contacts(id, name,email, imageArray)


            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return contact


    }

    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TABLE_EVENTS, EVENT_ID + " = ? ", arrayOf(1.toString()))
        //db.delete(TABLE_NAME, COL_ID+"=?", arrayOf(1.toString()))
        db.close()
    }

    fun deleteEvent(eventId: String): Boolean {
        val db = this.writableDatabase
        //db.delete(TABLE_EVENTS, EVENT_ID +" = ? ", arrayOf(1.toString()))
        //db.delete(TABLE_NAME, COL_ID+"=?", arrayOf(1.toString()))
        db.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + EVENT_ID + "= \"$eventId\"");

        db.close()
        return true
    }

    fun update(event:Event,userId: String){
        val db = this.writableDatabase

        var cv = ContentValues()
                cv.put(EVENT_ID, event.eventId)
                cv.put(EVENT_TITLE, event.title)
                cv.put(EVENT_FROM_DATE, event.startDate)
                cv.put(EVENT_TO_DATE, event.endDate)
                cv.put(EVENT_GUESTS, event.guests.joinToString())
                cv.put(EVENT_DESCRIPTION, event.description)
                cv.put(USER_ID, userId)


        val retVal = db.update( TABLE_EVENTS  ,cv,  EVENT_ID + " =  \"${event.eventId}\"" , null)
        if (retVal >= 1) {
            Log.v("@@@WWe", " Record updated")
        } else {
            Log.v("@@@WWe", " Not updated")
        }



    }

    fun getContactEmails(): Any {
        var contactList = ArrayList<String>()
        val db = this.readableDatabase
        val query = "Select " + CONTACT_EMAIL + " from " + CONTACTS + " "
        val result = db.rawQuery(query, null)
        var contact: String? = null
        if (result.moveToFirst()) {
            do {


                var mailId = result.getString(result.getColumnIndex(CONTACT_EMAIL))



                contactList.add(mailId)

            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return contactList

    }
}
