package com.example.Calendar.entity

import android.os.Parcel
import android.os.Parcelable


class ContactParcel() : Parcelable {

    private var contacts: ArrayList<Contacts>? = null

    constructor(contacts: ArrayList<Contacts>) : this() {
        this.contacts = contacts
    }

    constructor(parcel: Parcel) : this() {
        parcel.readTypedList(contacts, Contacts.CREATOR);
    }

    fun getContacts(): ArrayList<Contacts>? {
        return contacts
    }
    fun getContactIds():ArrayList<Long>{
        var list1=ArrayList<Long>()
        for(i in 0.. contacts!!.size-1){
            list1.add(contacts!!.get(i).id)
        }
return list1
    }

    //getters, other fields, Builder class
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(contacts)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactParcel> {
        override fun createFromParcel(parcel: Parcel): ContactParcel {
            return ContactParcel(parcel)
        }

        override fun newArray(size: Int): Array<ContactParcel?> {
            return arrayOfNulls(size)
        }
    }
}