package com.example.flipimage

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class newImage : AppCompatActivity() {
    var selectedUri: String? = null
    var bitmap: Bitmap? = null
    var scaledBitMap:Bitmap?=null
    var newBitmap: Bitmap? = null
    var contentUri:Uri?=null
    var goc:Float = 0f
    var demXoay:Int=0
    var demD = 0
    var demN = 0
    var angle = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_image)

        pickImageFromGallery()

        val btnLuu: ImageButton = findViewById(R.id.luu)
//        //lat
        //latdoc
        val latDoc: ImageButton = findViewById(R.id.latDoc)
        var bienDoc = -1f
        var bienDoc2 = -1f
        var bienNgang = -1f

        val latNgan: ImageButton = findViewById(R.id.latNgan)

        latNgan.setOnClickListener(View.OnClickListener {
//            var a:Int = demXoay % 2
//            Toast.makeText(this ,"$",Toast.LENGTH_SHORT).show()
            demN ++
            val image_view: ImageView = findViewById(R.id.image_view)
            if ((demXoay % 2) == 0){
                image_view.setScaleX( bienDoc)

                bienDoc *=-1;
            }else{
                image_view.setScaleY(bienDoc)

                bienDoc *=-1;
            }
            Toast.makeText(this ,"$demXoay \n $demN",Toast.LENGTH_SHORT).show()
        })
        latDoc.setOnClickListener(View.OnClickListener {
            demD ++
            val image_view: ImageView = findViewById(R.id.image_view)
                if ((demXoay % 2) == 0) {
                    image_view.setScaleY(bienNgang)

                    bienNgang *= -1
                }else{
                    image_view.setScaleX(bienDoc2)

                    bienDoc2 *=-1
                }
            Toast.makeText(this ,"$demXoay \n $demD",Toast.LENGTH_SHORT).show()
        })


//        latNgan.setOnClickListener(View.OnClickListener {
//            val linearLayoutTrong: LinearLayout = findViewById(R.id.linearLayoutTrong)
//            linearLayoutTrong.setScaleY(-1f)
//        })


        //xoay
        val xoay: ImageButton = findViewById(R.id.xoay)
        val image_view: ImageView = findViewById(R.id.image_view)
        xoay.setOnClickListener(View.OnClickListener {
            demXoay ++
            val filename: String = selectedUri!!.substring(selectedUri!!.lastIndexOf("/") + 1)
            angle += 90
            image_view.setRotation(angle.toFloat())
            Toast.makeText(this ,"$demXoay \n $angle",Toast.LENGTH_SHORT).show()

        })



        btnLuu.setOnClickListener {
            Log.d("a","Luu")
            val matric = Matrix()

            matric.postRotate(angle.toFloat())
            scaledBitMap = Bitmap.createScaledBitmap(bitmap!!,bitmap!!.width,bitmap!!.height,true)
            newBitmap = Bitmap.createBitmap(scaledBitMap!!,0, 0, scaledBitMap!!.width, scaledBitMap!!.height, matric, true)

            var oldFile:File?=null
            oldFile = bitmapToFile(newBitmap!!,Math.random().toString())
            val matric2 = Matrix()

            var x:Float?=null
            var y:Float?= null
            if (demN % 2 == 0){
                x=1f
            }else{
                x = -1f
            }
            if (demD % 2 == 0){
                y=1f
            }else{
                y = -1f
            }
            matric2.setScale(x,y)

            var bitmap2 = BitmapFactory.decodeFile(oldFile.toString() + ".png")
            scaledBitMap = Bitmap.createScaledBitmap(bitmap2,bitmap2.width,bitmap2.height,true)
            newBitmap = Bitmap.createBitmap(scaledBitMap!!,0, 0, scaledBitMap!!.width, scaledBitMap!!.height, matric2, true)

            val filename: String = oldFile.toString().substring(oldFile.toString().lastIndexOf("/") + 1)
            bitmapToFile(newBitmap!!,filename)
            Toast.makeText(this ,"Lưu thành công :v",Toast.LENGTH_SHORT).show()
        }

        //btn click
        val img_pick_btn: ImageButton = findViewById(R.id.img_pick_btn)
        img_pick_btn.setOnClickListener {
            //check runtime permission

            pickImageFromGallery()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                    //permission denied
//                    var permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                    //show popup to request runtime permission
//                    requestPermissions(permissions, PERMISSION_CODE)
//
//                } else {
//                    //permission already grated
//                    pickImageFromGallery();
//                }
//
//            } else {
//                //system OS is <= Marshmallow
//                pickImageFromGallery();
//            }
        }
    }



    private fun pickImageFromGallery() {
        //Intent to pick image
        MediaScannerConnection.scanFile(
            this@newImage,
            arrayOf(Environment.getExternalStorageDirectory().toString()),
            null
        ) { path, uri ->
            Log.i("ExternalStorage", "Scanned $path:")
            Log.i("ExternalStorage", "-> uri=$uri")
        }

        val gallaryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(gallaryIntent, 1)

    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1;

        //Permission code
        private val PERMISSION_CODE = 1001;

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == Activity.RESULT_OK && resultCode == IMAGE_PICK_CODE){
        var inverse = Matrix()
        contentUri = data!!.data
        var image_view: ImageView = findViewById(R.id.image_view)
//        Toast.makeText(this, " ${image_view.equals("sq")}", Toast.LENGTH_SHORT).show()
        selectedUri = getRealPathFromURIAPI19(this, contentUri!!)
        //hủy xoay
        val canvas = Canvas()
        image_view.invalidate()
        angle = 0
        demXoay=0
         demD = 0
         demN = 0

        image_view.setScaleY(1f)
        image_view.setScaleX(1f)
        bitmap = BitmapFactory.decodeFile(selectedUri)
        Log.d("X :", bitmap!!.getScaledWidth(canvas).toString())
        Log.d("Y :", bitmap!!.getScaledHeight(canvas).toString())
//        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
        var angle = 0
        image_view.setRotation(angle.toFloat())

        image_view.setImageURI(contentUri)

    }

    @SuppressLint("NewApi")
    private fun getRealPathFromURIAPI19(context: Context, uri: Uri): String? {
        Log.d("GetPath", uri.toString())
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                var cursor: Cursor? = null
                try {
                    cursor = context.contentResolver.query(
                        uri,
                        arrayOf(MediaStore.MediaColumns.DISPLAY_NAME),
                        null,
                        null,
                        null
                    )
                    cursor!!.moveToNext()
                    val fileName = cursor.getString(0)
                    val path = Environment.getExternalStorageDirectory()
                        .toString() + "/Download/" + fileName
                    if (!TextUtils.isEmpty(path)) {
                        return path
                    }
                } finally {
                    cursor?.close()
                }
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads"),
                    java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor =
                context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        var file2: File? = null
        return try {
            val folder = File(Environment.getExternalStorageDirectory().path + "/DCIM/FlipImage")
            if (!folder!!.exists()){
                folder!!.mkdirs()
            }
            file2 = File(folder.toString(), fileNameToSave)
            file = File(folder.toString(), fileNameToSave + ".png")
//            Toast.makeText(this ,"File: $file",Toast.LENGTH_SHORT).show()
            Log.d("File",file.toString())
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
            return file2
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}