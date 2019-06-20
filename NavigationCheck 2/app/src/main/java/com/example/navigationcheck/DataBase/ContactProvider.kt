package com.example.navigationcheck.DataBase


import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log

import android.provider.BaseColumns._ID
import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 * Created by delaroy on 9/10/17.
 */

class ContactProvider : ContentProvider() {

    /** Database helper object  */
    private var dbHelper: DataBaseManager? = null

    override fun onCreate(): Boolean {
        dbHelper = DataBaseManager(context)
        return true
    }

    @Nullable
    override fun query(@NonNull uri: Uri, @Nullable projection: Array<String>?, @Nullable selection: String?, @Nullable selectionArgs: Array<String>?, @Nullable sortOrder: String?): Cursor? {
        var selection = selection
        var selectionArgs = selectionArgs
        // Get readable database
        val database = dbHelper!!.getReadableDatabase()

        // This cursor will hold the result of the query
        val cursor: Cursor

        // Figure out if the URI matcher can match the URI to a specific code
        val match = sUriMatcher.match(uri)
        when (match) {
            PICTURES ->

                cursor =
                    database.query(DataBaseManager.CONTACTS, projection, selection, selectionArgs, null, null, sortOrder)
            PICTURES_ID -> {

                selection = "$_ID=?"
                selectionArgs = arrayOf(ContentUris.parseId(uri).toString())

                cursor =
                    database.query(DataBaseManager.CONTACTS, projection, selection, selectionArgs, null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(context!!.contentResolver, uri)

        // Return the cursor
        return cursor
    }


    @Nullable
    override fun getType(@NonNull uri: Uri): String? {
        return null
    }

    @Nullable
    override fun insert(@NonNull uri: Uri, @Nullable contentValues: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        when (match) {
            PICTURES -> return insertCart(uri, contentValues)
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }
    }

    private fun insertCart(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(@NonNull uri: Uri, @Nullable s: String?, @Nullable strings: Array<String>?): Int {

        return 0
    }

    override fun update(@NonNull uri: Uri, @Nullable contentValues: ContentValues?, @Nullable s: String?, @Nullable strings: Array<String>?): Int {
        return 0
    }

    companion object {

        val LOG_TAG = ContactProvider::class.java.simpleName

        /** URI matcher code for the content URI for the pictures table  */
        private val PICTURES = 100

        /** URI matcher code for the content URI for a single picture in the movies table  */
        private val PICTURES_ID = 101

        /**
         * The "Content authority" is a name for the entire content provider, similar to the
         * relationship between a domain name and its website.  A convenient string to use for the
         * content authority is the package name for the app, which is guaranteed to be unique on the
         * device.
         */
        val CONTENT_AUTHORITY = "com.delaroystudios.imagecamerabitmap"

        /**
         * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
         * the content provider.
         */
        val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")


        val PATH_IMAGES = "image-path"

        val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_IMAGES)


        /**
         * UriMatcher object to match a content URI to a corresponding code.
         * The input passed into the constructor represents the code to return for the root URI.
         * It's common to use NO_MATCH as the input for this case.
         */
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        // Static initializer. This is run the first time anything is called from this class.
        init {
            // The calls to addURI() go here, for all of the content URI patterns that the provider
            // should recognize. All paths added to the UriMatcher have a corresponding code to return
            // when a match is found.

            sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_IMAGES, PICTURES)

            sUriMatcher.addURI(CONTENT_AUTHORITY, "$PATH_IMAGES/#", PICTURES_ID)

        }
    }

}

