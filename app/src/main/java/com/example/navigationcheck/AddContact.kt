package com.example.navigationcheck

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.afollestad.materialdialogs.MaterialDialog
import com.example.navigationcheck.dataBase.DataBaseManager
import com.example.navigationcheck.adapter.CustomAdapter
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.add_contact.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.regex.Pattern

class AddContact() : AppCompatActivity(), View.OnClickListener {
    var customAdapter: CustomAdapter? = null
    private var profileImageView: ImageView? = null
    private var name: EditText? = null
    private var email: EditText? = null
    private var emailLayout: TextInputLayout? = null
    private var nameLayout: TextInputLayout? = null
    lateinit var dbHelper: DataBaseManager


    constructor(customAdapter: CustomAdapter) : this() {
        this.customAdapter = customAdapter
    }

    override fun onSupportNavigateUp(): Boolean {

        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_warning_black_24dp)
            .setTitle("Cancel")
            .setMessage("Discard your changes?")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
        return true

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_warning_black_24dp)
            .setTitle("Cancel")
            .setMessage("Discard your changes?")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)

        profileImageView = findViewById(R.id.profile_photo) as ImageView
        emailLayout = findViewById(R.id.email_layout)
        nameLayout = findViewById(R.id.name_layout)
        name = findViewById(R.id.name) as EditText
        email = findViewById(R.id.email) as EditText

        profileImageView!!.setOnClickListener(this)
        setSupportActionBar(toolbar2)
        val actionBar = supportActionBar

        actionBar!!.title = "Create new contact"


        toolbar2.setTitleTextColor(Color.WHITE);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close)
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
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
// Results for Image Picker Activity
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {

                // Setting the selected image uri

                profileImageView!!.setImageURI(result.uri)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.i("Dashboard", error.message)
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
                var listOfNames = dbHelper.getContactNames()
                var listOfMails = dbHelper.getContactEmails()
                if (name?.text.toString().equals("") || email?.text.toString().equals("")) {
                    Toast.makeText(this, "contact fields empty", Toast.LENGTH_SHORT).show()
                } else if (listOfNames.contains(name?.text.toString()) && listOfMails.contains(email?.text.toString())) {
                    Toast.makeText(this, "contact already exist", Toast.LENGTH_SHORT).show()
                } else {
                    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                    val matcherObj = Pattern.compile(emailPattern).matcher(email?.text)
                    if (matcherObj.matches()) {
                        emailLayout!!.setError(null)
                        var bitmap: Bitmap? = null
                        if (profileImageView!!.getDrawable().getConstantState().equals
                                (getResources().getDrawable(R.drawable.default_profile).getConstantState())
                        ) {
                            println("You got it!")

                            bitmap = writeOnDrawable(name!!.text.substring(0, 1).toUpperCase()).bitmap
                        } else {
                            bitmap = (profileImageView!!.getDrawable() as (BitmapDrawable)).getBitmap();

                            println("You don't get it!")
                        }
                        val baos = ByteArrayOutputStream()
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()


                        dbHelper.addContact(name?.text.toString(), email?.text.toString(), data)
                        Toast.makeText(this, "contact saved successfully", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        emailLayout!!.setError("Invalid mail")
                    }
                }
                return true

            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    fun writeOnDrawable(text: String): RoundedBitmapDrawable {

        //var bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        var b: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        var paint: Paint = Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40F);
        var d: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(getResources(), b);
        d.setCircular(true);


        var canvas = Canvas(b);

        canvas.drawColor(ContextCompat.getColor(this, R.color.contact_default_image));
        if (text.length == 1) {
            var xPos = (canvas.getWidth() / 2) + (paint.descent() + paint.ascent()) / 2
            var yPos = ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText(text, xPos, yPos, paint);
        } else if (text.length == 2) {
            var xPos = (canvas.getWidth() / 2) + (paint.descent() + paint.ascent())
            var yPos = ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText(text, xPos, yPos, paint);
        }


        return d
    }


}