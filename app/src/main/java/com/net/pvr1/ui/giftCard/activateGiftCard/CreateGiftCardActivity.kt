package com.net.pvr1.ui.giftCard.activateGiftCard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCreateGiftcardBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr1.ui.giftCard.response.GiftCardListResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import com.net.pvr1.utils.toast
import dagger.hilt.android.AndroidEntryPoint
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
        binding?.tvTitle?.text = "Expired Gift Card"

        //Screen Width

        if (intent != null) {
            if (intent.hasExtra("genericList")) {
                giftCardListFilter =
                    intent.getSerializableExtra("genericList") as ArrayList<GiftCardListResponse.Output.GiftCard>
            }
            if (intent.hasExtra("key")) {
                card_type = intent.getStringExtra("key").toString()
            }
            if (intent.hasExtra("limit")) {
                limit = intent.getStringExtra("limit")!!.toInt()
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
            }
            R.id.ll_cancel_gift -> onBackPressed()
        }
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
        if (data != null) {
            selectedImageUri = data.data
            binding?.ivUploadImage?.setImageURI(selectedImageUri)
            binding?.llRemoveImage?.show()
            binding?.llProceedGift?.show()
            binding?.llProceedGiftUnselect?.hide()
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

}