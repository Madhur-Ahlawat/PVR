package com.net.pvr.ui.giftCard.activateGiftCard

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.lyrebirdstudio.aspectratiorecyclerviewlib.aspectratio.model.AspectRatio
import com.lyrebirdstudio.croppylib.Croppy
import com.lyrebirdstudio.croppylib.main.CropRequest
import com.lyrebirdstudio.croppylib.main.CroppyTheme
import com.lyrebirdstudio.croppylib.util.file.FileCreator
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest
import com.net.pvr.R
import com.net.pvr.databinding.ActivityCreateGiftcardBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr.ui.giftCard.response.GiftCardListResponse
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URISyntaxException
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class CreateGiftCardActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityCreateGiftcardBinding? = null
    private var loader: LoaderDialog? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()
    private var permsRequestCode = 202
    private val REQUEST_SELECT_FILE = 2222
    private var selectedImageUri: Uri? = null
    private var card_type = ""
    private var limit = 0
    private var giftCardListFilter = ArrayList<GiftCardListResponse.Output.GiftCard>()


    @Inject
    lateinit var preferences: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGiftcardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.llTop?.titleCommonToolbar?.text = "Create Gift Card"
        binding?.llTop?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        //Screen Width

        if (intent != null) {
            if (intent.hasExtra("genericList")) {
                giftCardListFilter = intent.getSerializableExtra("genericList") as ArrayList<GiftCardListResponse.Output.GiftCard>
            }
            if (intent.hasExtra("key")) {
                card_type = intent.getStringExtra("key").toString()
            }
            if (intent.hasExtra("limit")) {
                limit = intent.getStringExtra("limit")?.toInt() ?: 0
            }
        }

        binding?.llUploadImage?.setOnClickListener{
            if (!checkPermission()) {
                requestPermission()
            } else {
                uploadPhoto()
            }
        }
        binding?.llRemoveImage?.setOnClickListener(this)
        binding?.llCancelGift?.setOnClickListener(this)
        binding?.llProceedGift?.setOnClickListener(this)
        binding?.llProceedGift?.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.ll_remove_image -> {
                binding?.ivUploadImage?.setImageDrawable(resources.getDrawable(R.color.gray))
                binding?.llRemoveImage?.hide()
                binding?.llProceedGift?.hide()
                binding?.llProceedGiftUnselect?.show()
            }
            R.id.ll_proceed_gift_unselect -> {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "Please upload image to continue",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
            R.id.ll_proceed_gift -> {
                if (intent.getStringExtra("from") == "upload") {
                    val intent = Intent(this, AddGiftCardActivity::class.java)
                    if (selectedImageUri != null) intent.putExtra(
                        "imageValueUri",
                        selectedImageUri.toString()
                    )
                    intent.putExtra("genericList", giftCardListFilter)
                    intent.putExtra("key", giftCardListFilter[0].channel)
                    intent.putExtra("limit", limit)
                    intent.putExtra("custom", "true")
                    startActivity(intent)
                }else{
                    val file = File(getPath(this, selectedImageUri!!)!!)
                    val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

                    val body = MultipartBody.Part.createFormData("fileImage", file.name, requestFile)

                    val fullName: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), preferences.getUserName().toString())
                    val time = SystemClock.uptimeMillis()
                    val userID: String = preferences.getUserId()
                    val userNum: String = preferences.getString(Constant.SharedPreference.USER_TOKEN)
                    try {
                        val token = Constant.getHash("$userID|$userNum|$time")
                        authViewModel.uploadGiftCard(body,fullName,RequestBody.create("multipart/form-data".toMediaTypeOrNull(), time.toString()),RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userID),RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userNum),token)
                        uploadGiftCard()
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }
            }
            R.id.ll_cancel_gift -> onBackPressed()
        }
    }

    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.applicationContext, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                when (split[0]) {
                    "image" -> {
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor =
                    context.contentResolver.query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index)
                }
            } catch (e: Exception) {
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val result2 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && (result2 == PackageManager.PERMISSION_GRANTED)
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), permsRequestCode
        )
    }

    private fun uploadPhoto() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, "Select File"),
            REQUEST_SELECT_FILE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101){
            selectedImageUri = data?.data
            val file = selectedImageUri?.path?.let { File(it) }

            binding?.ivUploadImage?.setImageURI(file?.let { saveBitmapToFile(it)?.toUri() })
            binding?.llRemoveImage?.show()
            binding?.llProceedGift?.show()
            binding?.llProceedGiftUnselect?.hide()
        }else {
            if (data != null) {
                selectedImageUri = data.data
                binding?.ivUploadImage?.setImageURI(selectedImageUri)
                CropImage()
                binding?.llRemoveImage?.show()
                binding?.llProceedGift?.show()
                binding?.llProceedGiftUnselect?.hide()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            202 -> if (grantResults.isNotEmpty()) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val writeAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && readAccepted && writeAccepted) {
                    uploadPhoto()
                } else {
                }
            }
        }
    }

    private fun CropImage() {
        try {
//            val CropIntent = Intent("com.android.camera.action.CROP")
//            CropIntent.setDataAndType(selectedImageUri, "image/*")
//            CropIntent.putExtra("crop", "true")
//            CropIntent.putExtra("outputX", 920)
//            CropIntent.putExtra("outputY", 420)
//            CropIntent.putExtra("scaleUpIfNeeded", true)
//            CropIntent.putExtra("return-data", true)
//            startActivityForResult(CropIntent, 1)
            val destinationUri =
                FileCreator
                    .createFile(FileOperationRequest.createRandom(), application.applicationContext)
                    .toUri()
            val cropRequest = selectedImageUri?.let { CropRequest.Auto(sourceUri = it, requestCode = 101) }
            val excludeAspectRatiosCropRequest = selectedImageUri?.let {
                CropRequest.Manual(
                    sourceUri = it,
                    destinationUri,
                    requestCode = 101,
                    excludedAspectRatios = arrayListOf(AspectRatio.ASPECT_FREE),
                            croppyTheme = CroppyTheme(R.color.yellow)
                )
            }
            println("cropRequest--->"+cropRequest?.croppyTheme)
            println("cropRequest123--->"+cropRequest?.excludedAspectRatios)
            excludeAspectRatiosCropRequest

            //            CropIntent.putExtra("outputX", 920)
//            CropIntent.putExtra("outputY", 420)
            excludeAspectRatiosCropRequest?.let { Croppy.start(this, it) }
        } catch (ex: ActivityNotFoundException) {
        }
    }

    private fun uploadGiftCard() {
        authViewModel.uploadGCResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        authViewModel.reUpload(preferences.getUserId(),it.data.output.url,
                            preferences.getUserName().toString(),it.data.output.id)
                        reUploadGiftCard()
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }
    private fun reUploadGiftCard() {
        authViewModel.reUploadGCResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val intent = Intent(
                            this@CreateGiftCardActivity,
                            ActivateGiftCardActivity::class.java
                        )
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        it.message.toString(),
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun saveBitmapToFile(file: File): File? {
        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(file)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val REQUIRED_SIZE = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                o.outHeight / scale / 2 >= REQUIRED_SIZE
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(file)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            file.createNewFile()
            val outputStream = FileOutputStream(file)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            file
        } catch (e: java.lang.Exception) {
            null
        }
    }


}