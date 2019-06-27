package com.example.navigationcheck

import android.Manifest
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.navigationcheck.DataBase.DataBaseManager
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.add_contact.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class AddContact() : AppCompatActivity(), View.OnClickListener {
    var customAdapter: CustomAdapter?=null
    private var profileImageView: ImageView? = null
    private var name: EditText? = null
    private var email: EditText? = null

    private var progressBar: ProgressDialog? = null
    private var progressBarStatus = 0
    private val progressBarbHandler = Handler()
    private val hasImageChanged = false
    lateinit var dbHelper: DataBaseManager

    lateinit var thumbnail: Bitmap


constructor(customAdapter: CustomAdapter):this(){
    this.customAdapter=customAdapter
}

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)

        profileImageView = findViewById(R.id.profile_photo) as ImageView
        name = findViewById(R.id.name) as EditText
        email = findViewById(R.id.email) as EditText

        profileImageView!!.setOnClickListener(this)
        setSupportActionBar(toolbar2)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Contact"

        // Set action bar/toolbar sub title


        // Set action bar elevation
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)

        if (ContextCompat.checkSelfPermission(
                this@AddContact,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            profileImageView!!.isEnabled = true
            ActivityCompat.requestPermissions(
                this@AddContact,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        } else {
            profileImageView!!.isEnabled = true
        }

        dbHelper = DataBaseManager(this)

    }
    override fun onClick(view: View) {
        when (view.id) {

            R.id.profile_photo ->

            MaterialDialog.Builder(this)
                .title("set your image")
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(object : MaterialDialog.ListCallback {
                    override fun onSelection(dialog: MaterialDialog, view: View, which: Int, text: CharSequence) {
                        when (which) {
                            0 -> {
                                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                                photoPickerIntent.type = "image/*"
                                startActivityForResult(photoPickerIntent, SELECT_PHOTO)
                            }
                            1 -> {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAPTURE_PHOTO)
                            }
                            2 -> profileImageView!!.setImageResource(R.drawable.default_profile)
                        }
                    }
                })
                .show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                profileImageView!!.isEnabled = true
            }
        }
    }

    fun setProgressBar() {
        progressBar = ProgressDialog(this)
        progressBar!!.setCancelable(true)
        progressBar!!.setMessage("Please wait...")
        progressBar!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressBar!!.progress = 0
        progressBar!!.max = 100
        progressBar!!.show()
        progressBarStatus = 0
        Thread(Runnable {
            while (progressBarStatus < 100) {
                progressBarStatus += 30

                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                progressBarbHandler.post { progressBar!!.progress = progressBarStatus }
            }
            if (progressBarStatus >= 100) {
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                progressBar!!.dismiss()
            }
        }).start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    val imageUri = data!!.data
                    performCrop(imageUri)
                    val imageStream = getContentResolver().openInputStream(imageUri)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    //set Progress Bar
                    setProgressBar()
                    //set profile picture form gallery
                    profileImageView!!.setImageBitmap(selectedImage)


                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            }

        } else if (requestCode == CAPTURE_PHOTO) {
            if (resultCode == RESULT_OK) {
                onCaptureImageResult(data!!)
            }
        }
        else if (requestCode == 5) {
            if (data != null) {
            // get the returned data
         var extras:   Bundle  = data.getExtras();
            // get the cropped bitmap
        var selectedBitmap:    Bitmap  = extras.getParcelable("data");

                profileImageView!!.setImageBitmap(selectedBitmap);
        }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.contacts_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save -> {
                profileImageView!!.isDrawingCacheEnabled = true
                profileImageView!!.buildDrawingCache()
                val bitmap = profileImageView!!.drawingCache
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                var listOfNames=dbHelper.getContactNames()
                var listOfMails=dbHelper.getContactEmails()
                if(name?.text.toString().equals("")){
                    if(email?.text.toString().equals("")){
                        Toast.makeText(this, "contact fields empty", Toast.LENGTH_SHORT).show()
                    }  }
                else if(listOfNames.contains(name?.text.toString())&&listOfNames.contains(email?.text.toString())){
                     if(email?.text.toString().equals("")){
                        Toast.makeText(this, "contact already exist", Toast.LENGTH_SHORT).show()
                    }

                }
                else {
                    dbHelper.addContact(name?.text.toString(), email?.text.toString(),data)
                    Toast.makeText(this, "contact saved to DB successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return true

            }

            else -> return super.onOptionsItemSelected(item)
        }

    }


    private fun onCaptureImageResult(data: Intent) {
        thumbnail = data.extras!!.get("data") as Bitmap

        //set Progress Bar
        setProgressBar()
        //set profile picture form camera
        profileImageView!!.maxWidth = 200
        profileImageView!!.setImageBitmap(thumbnail)

    }


    companion object {

        private val SELECT_PHOTO = 1
        private val CAPTURE_PHOTO = 2
    }
    private fun performCrop(picUri:Uri ) {
    try {
      var cropIntent:  Intent = Intent("com.android.camera.action.CROP");
        // indicate image type and Uri
        cropIntent.setDataAndType(picUri, "image/*");
        // set crop properties here
        cropIntent.putExtra("crop", true);
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 128);
        cropIntent.putExtra("outputY", 128);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent,5);
    }
    // respond to users whose devices do not support the crop action
    catch (anfe:ActivityNotFoundException ) {
        // display an error message
        var errorMessage:String  = "Whoops - your device doesn't support the crop action!";
        var toast:Toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}



}