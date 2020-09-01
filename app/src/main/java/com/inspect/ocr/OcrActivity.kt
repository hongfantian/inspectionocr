package com.inspect.ocr

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.gson.Gson
import com.inspect.ocr.common.Common
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class OcrActivity : AppCompatActivity() {

    lateinit var imageView: OcrImageView
    //    lateinit var editText: EditText
    val REQUEST_IMAGE_SELECT = 1
    val REQUEST_IMAGE_CAPTURE = 2
    val CAMERA_PERMISSIONS_RESULT = 102
    var imageUri:Uri? = null
    private lateinit var fBtnContainer:View
    private lateinit var sBtnContainer:View
    lateinit var currentPhotoPath: String
    lateinit var photoURI: Uri
    lateinit var progressBar:ProgressBar
    lateinit var hintTextView:TextView
    lateinit var progressContainer:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ocr )
        InspectResult.initialize()
        imageView = findViewById(R.id.imageView)
        fBtnContainer = findViewById( R.id.btn_container_1 )
        sBtnContainer = findViewById( R.id.btn_container_2 )

        progressContainer = findViewById( R.id.progress_container )
        progressBar = findViewById( R.id.progressBar )
        hintTextView = findViewById( R.id.hint_txt )

        progressContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        hintTextView.visibility = View.GONE

        Common.currentActivity = this
        controlBtn(1)
    }
    fun selectImage(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "画像を選択してください。"), REQUEST_IMAGE_SELECT)
    }

    fun takePicture( v:View ){

        val permissions: ArrayList<String?> = ArrayList<String?>()
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val requirePermissions =
            Common.getInstance().checkPermissions(permissions)

        if (!requirePermissions.isEmpty()) {

            if (ActivityCompat.shouldShowRequestPermissionRationale( this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                val array = arrayOfNulls<String>(permissions.size)
                permissions.toArray(array)
                ActivityCompat.requestPermissions( this,
                    array,
                    CAMERA_PERMISSIONS_RESULT)
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

/*            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this,
                    arrayOfNulls<String>( requirePermissions.size ),
                    CAMERA_PERMISSIONS_RESULT )
            }*/
        } else {
            cameraCall()
        }
    }

    private fun cameraCall(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Global.makeLargeTextToast( applicationContext,"写真撮影時に問題が発生しました。" ).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.inspect.ocr.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE )
                }
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
    fun getUriFromPath(context: Context, destination: String?): Uri? {
        val file = File(destination)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                context.getPackageName().toString() + ".provider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageView.angle = 0
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(data!!.data)
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
            //val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap( imageBitmap )
        }
        imageView.initialize()
//        makeLargeTextToast("指で画面を動かし、画像を調整してください。").show()
        controlBtn(0)
    }

    private fun controlBtn( which:Int ){
        if( which == 1 ){
            fBtnContainer.visibility = View.VISIBLE
            sBtnContainer.visibility = View.GONE
        }else{
            fBtnContainer.visibility = View.GONE
            sBtnContainer.visibility = View.VISIBLE
        }
    }

    fun rotateImage( v:View ){
        imageView.startAnimation()
    }
    fun cancel( v:View ){
        controlBtn(1 )
    }

    private fun recognizeTextCloud(image: FirebaseVisionImage) {
        // [START set_detector_options_cloud]
        val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
            .setLanguageHints(listOf("en", "hi"))
            .build()
        // [END set_detector_options_cloud]

        // [START get_detector_cloud]
        val detector = FirebaseVision.getInstance().cloudTextRecognizer
        // Or, to change the default settings:
        // val detector = FirebaseVision.getInstance().getCloudTextRecognizer(options)
        // [END get_detector_cloud]

        // [START run_detector_cloud]
        val result = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                // Task completed successfully
                // [START_EXCLUDE]
                // [START get_text_cloud]
                for (block in firebaseVisionText.textBlocks) {
                    val boundingBox = block.boundingBox
                    val cornerPoints = block.cornerPoints
                    val text = block.text

                    for (line in block.lines) {
                        // ...
                        for (element in line.elements) {
                            // ...
                        }
                    }
                }
                // [END get_text_cloud]
                // [END_EXCLUDE]
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
Log.i( "Ocr failed", e.message.toString() )
            }
        // [END run_detector_cloud]
    }

    fun startRecognizing(v: View) {

        var bitmap:Bitmap? = imageView.getBitmap()
        if( bitmap != null ){
            val image:FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)
            //recognizeText( image );
            recognizeTextCloud( image );
        }else{
            Global.makeLargeTextToast( applicationContext, "先ず画像を選択してください。" ).show()
//            Toast.makeText(this, "Select an Image First", Toast.LENGTH_LONG).show()
        }
        progressContainer.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        hintTextView.visibility = View.VISIBLE
    }
    private fun recognizeText(image: FirebaseVisionImage) { // [START get_detector_default]
        InspectPaper.initialize()
        CandidateList.initialize()
        val detector = FirebaseVision.getInstance()
            .onDeviceTextRecognizer
        val result =
            detector.processImage(image)
                .addOnSuccessListener { firebaseVisionText ->
                    processTextBlock( firebaseVisionText );
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
    }

    public fun processTextBlock(result: FirebaseVisionText) { // [START mlkit_process_text_block]

        //initialize previous ocr information.
        InspectOcrPaper.initializePaper()
        if (result.textBlocks.size == 0) {
            //editText.setText("No Text Found")
            progressContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
            hintTextView.visibility = View.GONE
            Global.makeLargeTextToast( applicationContext, "検査用紙の認識に失敗しました。" ).show()
            return
        }

        for (block in result.textBlocks) {
            val blockText = block.text
        }
        val gson = Gson()
        var s:String = gson.toJson( result )

        if (result.textBlocks.size == 0) {
        }else{
            for (block in result.textBlocks) {
                val blockText = block.text
                val blockConfidence = block.confidence
                val blockLanguages =
                    block.recognizedLanguages
                val blockCornerPoints = block.cornerPoints
                val blockFrame = block.boundingBox
                for (line in block.lines) {
                    val lineText = line.text
                    val lineConfidence = line.confidence
                    val lineLanguages =
                        line.recognizedLanguages
                    val lineCornerPoints = line.cornerPoints
                    val lineFrame = line.boundingBox
                    //println( " $lineText-$lineFrame---- \n" )
                    for (element in line.elements) {
                        val elementText = element.text
                        val elementConfidence = element.confidence
                        val elementLanguages =
                            element.recognizedLanguages
                        val elementCornerPoints =
                            element.cornerPoints
                        val elementFrame = element.boundingBox
                        val top = elementFrame!!.top
                        val bottom = elementFrame!!.bottom
                        val left = elementFrame!!.left
                        val right = elementFrame!!.right
                        InspectAnalyser.adjustColumn( element )
                    }
                }
            }
        }

        var paper:String = InspectOcrPaper.getPaperToJson()
        print( paper )

        InspectAnalyser.processInspectResult()

//        var resultJSON:String = InspectResult.resultByJSON()
        // [END mlkit_proces-s_text_block]
        paper = InspectOcrPaper.getPaperToJson()
        print( paper )
        progressContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        hintTextView.visibility = View.GONE

        val intent = Intent(this, ResultShowActivity::class.java).apply {
            //            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    private fun processResultText(resultText: FirebaseVisionText) {
        if (resultText.textBlocks.size == 0) {
            //editText.setText("No Text Found")
            return
        }

        for (block in resultText.textBlocks) {
            val blockText = block.text
//            val points :ArrayList<Point> = block.cornerPoints;

            //editText.append(blockText + "\n")
        }
    }

    private fun checkAndRequestPermissions(): Boolean{
        val camera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val wtite = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val read = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val listPermissionsNeeded: MutableList<String> =
            ArrayList()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                CAMERA_PERMISSIONS_RESULT
            )
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        Log.d("in fragment on request", "Permission callback called-------")
        when (requestCode) {
            CAMERA_PERMISSIONS_RESULT -> {
                val perms: MutableMap<String, Int> =
                    HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    // Check for both permissions
                    if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                    ) {
                         cameraCall()
                    } else {
                        Log.d(
                            "in fragment on request",
                            "Some permissions are not granted ask again "
                        )
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
//show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE -> {
                                        }
                                    }
                                })
                        } else {
                            Toast.makeText(
                                this,
                                "Go to settings and enable permissions",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private fun showDialogOK(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }


}
