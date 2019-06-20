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
    private var emailLayout:TextInputLayout?=null
    private var nameLayout:TextInputLayout?=null

    private var progressBar: ProgressDialog? = null
    private var progressBarStatus = 0
    private val progressBarbHandler = Handler()
    private val hasImageChanged = false
    lateinit var dbHelper: DataBaseManager

    lateinit var thumbnail: Bitmap


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
        emailLayout=findViewById(R.id.email_layout)
        nameLayout=findViewById(R.id.name_layout)
        name = findViewById(R.id.name) as EditText
        email = findViewById(R.id.email) as EditText
        //profileImageView!!.setBackground(writeOnDrawable(R.drawable.current_date_click,"AB"))
        profileImageView!!.setOnClickListener(this)
        setSupportActionBar(toolbar2)

        // Now get the support action bar
        val actionBar = supportActionBar
        // Set toolbar title/app title

        actionBar!!.title = "Create new contact"

        // Set action bar/toolbar sub title

        toolbar2.setTitleTextColor(Color.WHITE);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
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
    /* profileImageView.setOnClickListener(View.OnClickListener() {

              // Code to show image in full screen:
         PhotoFullPopupWindow(context, R.layout.popup_photo_full, view, URL, null);
      });*/

    }

    override fun onClick(view: View) {
        /*when (view.id) {

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
        }*/*/
        // Used to Get an Image from Camera or Gallery
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

    fun getRoundedRectBitmap(bitmap: Bitmap, pixels: Int): Bitmap? {
        var result: Bitmap? = null;
        try {
            result = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);

        } catch (e: NullPointerException) {
        } catch (o: OutOfMemoryError) {
        }
        return result;
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
// Results for Image Picker Activity
        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {

                // Setting the selected image uri

                profileImageView!!.setImageURI(result.uri)

                /*// Store the selected and cropped image uri
                val selectedImage: Uri = result.uri
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = contentResolver.query(
                    selectedImage,
                    filePathColumn, null, null, null
                )
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()*/
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.i("Dashboard", error.message)
            }
            /* if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                   /* val imageUri = data!!.data
                    // performCrop(imageUri)
                    val imageStream = getContentResolver().openInputStream(imageUri)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    var croppedBmp: Bitmap = Bitmap.createBitmap(
                        selectedImage, 100, 100,
                        selectedImage.getWidth(), selectedImage.getHeight()
                    );
                    //set Progress Bar
                    setProgressBar()
                    val resized = Bitmap.createScaledBitmap(croppedBmp, 100, 100, true)
                    val conv_bm = getRoundedRectBitmap(resized, 100)
                    //set profile picture form gallery
                    profileImageView!!.setImageBitmap(conv_bm)*/
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
        } else if (requestCode == 5) {
            if (data != null) {
                // get the returned data
                var extras: Bundle = data.getExtras();
                // get the cropped bitmap
                var selectedBitmap: Bitmap = extras.getParcelable("data");

                profileImageView!!.setImageBitmap(selectedBitmap);
            }
        }*/

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
                if (name?.text.toString().equals("")||email?.text.toString().equals("")) {
                        Toast.makeText(this, "contact fields empty", Toast.LENGTH_SHORT).show()
                } else if (listOfNames.contains(name?.text.toString()) && listOfMails.contains(email?.text.toString())) {
                        Toast.makeText(this, "contact already exist", Toast.LENGTH_SHORT).show()
                } else {
                    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                    val matcherObj = Pattern.compile(emailPattern).matcher(email?.text)
                    if(matcherObj.matches()){
                        emailLayout!!.setError(null)
                       if (profileImageView!!.getDrawable().getConstantState().equals
                                (getResources().getDrawable(R.drawable.default_profile).getConstantState())){
                            println("You got it!")

                            profileImageView!!.setImageDrawable(writeOnDrawable(name!!.text.substring(0,1)))

                           //profileImageView!!.setVisibility(View.VISIBLE);
                        }else{
                            //profileImageView!!.setVisibility(View.GONE);
                            println("You don't get it!")
                        }
                        profileImageView!!.isDrawingCacheEnabled = true
                        profileImageView!!.buildDrawingCache()
                        var bitmap = profileImageView!!.drawingCache
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()


                        dbHelper.addContact(name?.text.toString(), email?.text.toString(), data)
                        Toast.makeText(this, "contact saved successfully", Toast.LENGTH_SHORT).show()

                        finish()
                    }
                    else{
                       emailLayout!!.setError("Invalid mail")
                    }
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

    private fun performCrop(picUri: Uri) {
        try {
            var cropIntent: Intent = Intent("com.android.camera.action.CROP");
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
            startActivityForResult(cropIntent, 5);
        }
        // respond to users whose devices do not support the crop action
        catch (anfe: ActivityNotFoundException) {
            // display an error message
            var errorMessage: String = "Whoops - your device doesn't support the crop action!";
            var toast: Toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    fun ImageCropFunction(picUri: Uri) {

        // Image Crop Code
        try {
            var CropIntent = Intent("com.android.camera.action.CROP")

            CropIntent.setDataAndType(picUri, "image/*")

            CropIntent.putExtra("crop", "true")
            CropIntent.putExtra("outputX", 180)
            CropIntent.putExtra("outputY", 180)
            CropIntent.putExtra("aspectX", 3)
            CropIntent.putExtra("aspectY", 4)
            CropIntent.putExtra("scaleUpIfNeeded", true)
            CropIntent.putExtra("return-data", true)

            startActivityForResult(CropIntent, 1)

        } catch (e: ActivityNotFoundException) {

        }

    }
 fun writeOnDrawable(text: String):RoundedBitmapDrawable {

       //var bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);
var b:Bitmap  = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
       var paint: Paint =  Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40F);
     var d: RoundedBitmapDrawable =
         RoundedBitmapDrawableFactory.create(getResources(), b);
     d.setCircular(true);


      var canvas:  Canvas = Canvas(b);
     //canvas.drawColor(Color.BLUE, PorterDuff.Mode.CLEAR);
     //canvas.drawARGB(0, 225, 225, 255);
     canvas.drawColor(ContextCompat.getColor(this, R.color.contact_default_image));
    // var xPos = (canvas.getWidth() / 2)+(paint.descent() + paint.ascent())
     //var yPos = ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
     if(text.length==1) {
         var xPos = (canvas.getWidth() / 2) + (paint.descent() + paint.ascent()) / 2
         var yPos = ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
         canvas.drawText(text, xPos, yPos, paint);
     }
     else if(text.length==2) {
         var xPos = (canvas.getWidth() / 2) + (paint.descent() + paint.ascent())
         var yPos = ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
         canvas.drawText(text, xPos, yPos, paint);
     }


        return d
    }


}