package com.example.Calendar

import android.Manifest
import android.app.Activity
import android.app.AlertDialog

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.Calendar.dataBase.DataBaseManager
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.add_contact.*
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

class AddContact : AppCompatActivity(), View.OnClickListener {
    private var profileImageView: ImageView? = null
    private var name: EditText? = null
    private var email: EditText? = null
    private var emailLayout: TextInputLayout? = null
    private var nameLayout: TextInputLayout? = null
    private lateinit var dbHelper: DataBaseManager

    var keyBoardState:Boolean=false

    override fun onSupportNavigateUp(): Boolean {
        if(name!!.text.toString().equals("")&&email!!.text.toString().equals("")){
finish()
        }
        else {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_warning_black_24dp)
            .setTitle("Cancel")
            .setMessage("Discard your changes?")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
        }
        return true

    }

    override fun onBackPressed() {
        if(name!!.text.toString().equals("")&&email!!.text.toString().equals("")){
            finish()
        }
        else {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setTitle("Cancel")
                .setMessage("Discard your changes?")
                .setPositiveButton("Yes") { dialog, which -> finish() }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact)
var status=true
var status1=true
        setupUI(findViewById(R.id.add_contact_layout))
        var layout=findViewById<RelativeLayout>(R.id.add_contact_layout)
        layout.getViewTreeObserver().addOnGlobalLayoutListener(object :ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                var r = Rect()
                layout.getWindowVisibleDisplayFrame(r);
                var screenHeight = layout.getRootView().getHeight();
                var keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    keyBoardState=true
                } else {
                    keyBoardState=false
                }
            }
        });
        profileImageView = findViewById(R.id.profile_photo)
        emailLayout = findViewById(R.id.email_layout)
        nameLayout = findViewById(R.id.name_layout)
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)

        profileImageView!!.setOnClickListener(this)
        setSupportActionBar(toolbar2)
        val actionBar = supportActionBar

        actionBar!!.title = "Create new contact"


        toolbar2.setTitleTextColor(Color.WHITE)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close)
        actionBar.elevation = 4.0F
        actionBar.setDisplayHomeAsUpEnabled(true)

        if (ContextCompat.checkSelfPermission(
                this@AddContact,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
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
       /*name!!.setOnClickListener{
           status = if(status){
               showKeyBoard()
               false
           } else {
               hideSoftKeyboard(this)
               true
           }
        }
        email!!.setOnClickListener{
            status1 = if(status1){
                showKeyBoard()
                false
            } else {
                hideSoftKeyboard(this)
                true
            }
        }*/


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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                // Setting the selected image uri

                profileImageView!!.setImageURI(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.i("Dashboard", error.message)
            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.contacts_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.save -> {
                val listOfNames = dbHelper.getContactNames()
                val listOfMails = dbHelper.getContactEmails()
                if (name?.text.toString().equals("") || email?.text.toString().equals("")) {
                    Toast.makeText(this, "contact fields empty", Toast.LENGTH_SHORT).show()
                } else if (listOfNames.contains(name?.text.toString()) && listOfMails.contains(email?.text.toString())) {
                    Toast.makeText(this, "contact already exist", Toast.LENGTH_SHORT).show()
                } else {
                    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                    val matcherObj = Pattern.compile(emailPattern).matcher(email?.text)
                    if (matcherObj.matches()) {
                        emailLayout!!.error = null
                        var bitmap: Bitmap?
                        if (profileImageView!!.drawable.constantState.equals
                                (ContextCompat.getDrawable(this,R.drawable.default_profile)?.constantState)
                        ) {

                            bitmap = writeOnDrawable(name!!.text.substring(0, 1).toUpperCase()).bitmap
                        } else {
                            bitmap = (this.profileImageView!!.drawable as (BitmapDrawable)).bitmap
                        }
                        val baos = ByteArrayOutputStream()
                        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()


                        dbHelper.addContact(name?.text.toString(), email?.text.toString(), data)
                        Toast.makeText(this, "contact saved successfully", Toast.LENGTH_SHORT).show()

                        finish()
                    } else {
                        emailLayout!!.error = "Invalid mail"
                    }
                }
                return true

            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun writeOnDrawable(text: String): RoundedBitmapDrawable {

        //var bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
        val b: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val paint= Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.textSize = 40F
        val d: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(resources, b)
        d.isCircular = true


        val canvas = Canvas(b)

        canvas.drawColor(ContextCompat.getColor(this, R.color.contact_default_image))
        if (text.length == 1) {
            val xPos: Float = (canvas.width / 2) + (paint.descent() + paint.ascent()) / 2
            val yPos = ((canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2))
            canvas.drawText(text, xPos, yPos, paint)
        } else if (text.length == 2) {
            val xPos = (canvas.width / 2) + (paint.descent() + paint.ascent())
            val yPos = ((canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2))
            canvas.drawText(text, xPos, yPos, paint)
        }


        return d
    }
    private fun showKeyBoard(){
        val view:View?=this.currentFocus
        if(view!=null){
            val imm: InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view,0)
        }
    }
    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
    }

    fun setupUI( view:View) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view is EditText)) {
            view.setOnTouchListener(object :View.OnTouchListener {
                override fun onTouch(v:View , event: MotionEvent):Boolean {
                    hideSoftKeyboard(this@AddContact);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for ( i in 0..(view as ViewGroup).getChildCount()-1) {
                var innerView = ( view as ViewGroup).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}