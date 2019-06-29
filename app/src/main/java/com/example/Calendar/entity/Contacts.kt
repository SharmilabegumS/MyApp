package com.example.Calendar.entity

import android.os.Parcel
import android.os.Parcelable

private var image: ByteArray? = null
var size: Int = 0

class Contacts : Parcelable {
    var id: Long = 0
    var name: String = ""
    var email: String = ""
    var picId: ByteArray = ByteArray(10000)

    constructor(id: Long, name: String, email: String, picId: ByteArray) {
        this.id = id
        this.name = name
        this.email = email
        this.picId = picId

    }

    constructor(parcel: Parcel) {
        id = parcel.readLong()
        name = parcel.readString()
        email = parcel.readString()
        picId = parcel.createByteArray()
    }


    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {

        dest!!.writeLong(id)
        dest!!.writeString(name)
        dest!!.writeString(email)
        dest!!.writeByteArray(picId)
    }

    companion object CREATOR : Parcelable.Creator<Contacts> {
        override fun createFromParcel(parcel: Parcel): Contacts {
            return Contacts(parcel)
        }

        override fun newArray(size: Int): Array<Contacts?> {
            return arrayOfNulls(size)
        }
    }
}


