package com.net.pvr.ui.giftCard.activateGiftCard

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr.R
import com.net.pvr.databinding.ActivityAddGiftcardBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.activateGiftCard.adapter.CustomGiftCardAdapter
import com.net.pvr.ui.giftCard.activateGiftCard.adapter.GiftCardAddAmtAdapter
import com.net.pvr.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr.ui.giftCard.response.GiftCardListResponse
import com.net.pvr.ui.giftCard.response.GiftCards
import com.net.pvr.ui.giftCard.response.SaveGiftCardCount
import com.net.pvr.utils.*
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URISyntaxException
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class AddGiftCardActivity : AppCompatActivity(), View.OnClickListener{
    private var binding: ActivityAddGiftcardBinding? = null
    private var loader: LoaderDialog? = null
    private var customGiftCardAdapter: CustomGiftCardAdapter? = null
    private var giftAddAmountAdapter: GiftCardAddAmtAdapter? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()
    private var permsRequestCode = 202
    private val REQUEST_SELECT_FILE = 2222
    private var custom_amount = 0
    private var limit = 0
    private var total_amount = 0
    private var isCustom = ""
    private var card_type = ""
    private var imageValue = ""
    private var imageValueUri: Uri? = null
    private var giftCardListFilter = ArrayList<GiftCardListResponse.Output.GiftCard>()
    var customGiftCard: GiftCards? = null
    var customizedGiftList: ArrayList<GiftCards> = ArrayList<GiftCards>()
    var customizedAmountList = java.util.ArrayList<String>()
    @Inject
    lateinit var preferences: PreferenceManager
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGiftcardBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        //Screen Width

        binding?.llTop?.btnBack?.setOnClickListener(this)
        binding?.llTop?.titleCommonToolbar?.text = "Add Amount"
        binding?.llCancelGift?.setOnClickListener(this)
        binding?.llProceedGiftUnselect?.setOnClickListener(this)
        binding?.llProceedGift?.setOnClickListener(this)
        binding?.tvAddAmountCustom?.setOnClickListener(this)

        binding?.tvTotal?.text = resources.getString(R.string.currency) + " " + total_amount
        giftCardListFilter = ArrayList<GiftCardListResponse.Output.GiftCard>()
        if (intent != null) {
            if (intent.hasExtra("genericList")) {
                giftCardListFilter = intent.getSerializableExtra("genericList") as ArrayList<GiftCardListResponse.Output.GiftCard>

            }
            if (intent.hasExtra("key")) {
                card_type = intent.getStringExtra("key").toString()
            }
            if (intent.hasExtra("imageValue")) {
                imageValue = intent.getStringExtra("imageValue").toString()
                if (imageValue != null) {
                    Picasso.get()
                            .load(imageValue)
                            .placeholder(resources.getDrawable(R.drawable.gift_card_placeholder))
                            .into(binding?.ivUploadedImage);
                }
            }
            if (intent.hasExtra("imageValueUri")) {
                imageValue = intent.getStringExtra("imageValueUri").toString()
                if (imageValue != null) {
                    imageValueUri = Uri.parse(imageValue)
                    binding?.ivUploadedImage?.setImageURI(imageValueUri)
                }
            }
            if (intent.hasExtra("limit")) {
                limit = intent.getIntExtra("limit", 0)
            }
            binding?.llCustom?.hide()
            if (intent.hasExtra("custom")) {
                isCustom = intent.getStringExtra("custom").toString()
                if (isCustom.equals("true", ignoreCase = true)) {
                    binding?.llCustom?.show()
                } else {
                    binding?.llCustom?.hide()
                }
            }
            binding?.llPvrLogo?.hide()
        }
        if (giftCardListFilter.size > 0) {
            if (isCustom.equals("true", ignoreCase = true)) {
                setAmountAdapterCustom()
            } else {
                setAmountAdapter()
            }
        }

        uploadGiftCard()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_cancel_gift, R.id.btnBack -> onBackPressed()
            R.id.ll_proceed_gift -> if (imageValueUri != null) {
                val file = File(getPath(this, imageValueUri!!))
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
            } else {
                sendData("")
            }
            R.id.ll_proceed_gift_unselect -> {
                val dialog = OptionDialog(this,
                    R.mipmap.ic_launcher,
                    R.string.app_name,
                    "Please choose gift value to continue",
                    positiveBtnText = R.string.ok,
                    negativeBtnText = R.string.no,
                    positiveClick = {
                    },
                    negativeClick = {
                    })
                dialog.show()
            }
            R.id.tv_add_amount_custom ->
                if (binding?.etCustomAmount?.text.toString().isNotEmpty()) {
                if ((binding?.etCustomAmount?.text?.toString()?.toInt()
                        ?: 0) >= 150 && binding?.etCustomAmount?.text.toString()
                        .toInt() <= 5000 && binding?.etCustomAmount?.text.toString()
                        .toInt() % 50 == 0
                ) {
                    custom_amount = binding?.etCustomAmount?.text.toString().toInt()
                    var customAmountTotal = 0
                    var i = 0
                    while (i < customizedGiftList.size) {
                        customAmountTotal += customizedGiftList[i].d.replace("xCUSTOMISED", "").toInt() * customizedGiftList[i].c.toInt()
                        i++
                    }
                    if (customAmountTotal + custom_amount <= 5000) {
                        total_amount += custom_amount
                        binding?.tvTotal?.text = resources.getString(R.string.currency) + " " + total_amount
                        binding?.llProceedGift?.show()
                        binding?.llProceedGiftUnselect?.hide()
                        customGiftCard = GiftCards(
                            custom_amount.toString() + "x" + "CUSTOMISED",
                            "1",
                            "GENERIC",
                            "GENERIC"
                        )
                        if (customizedAmountList.size > 0) {
                            if (customizedAmountList.contains(custom_amount.toString())) {
                                var i = 0
                                while (i < customizedGiftList.size) {
                                    if (customizedGiftList[i].d.replace("xCUSTOMISED", "") == (custom_amount.toString())) {
                                        customizedGiftList[i].c = (customizedGiftList[i].c.toInt() + 1).toString()
                                        customGiftCardAdapter?.notifyDataSetChanged()
                                        break
                                    }
                                    i++
                                }
                            } else {
                                customizedAmountList.add(custom_amount.toString())
                                customizedGiftList.add(customGiftCard!!)
                                customGiftCardAdapter?.notifyDataSetChanged()
                            }
                        } else {
                            customizedAmountList.add(custom_amount.toString())
                            customizedGiftList.add(customGiftCard!!)
                            setCustomAmountAdapter()
                        }
                        binding?.etCustomAmount?.setText("")
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            "Total Amount should be in between 150 - 5000 and multiple of 50",
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                            },
                            negativeClick = {
                            })
                        dialog.show()
                    }
                } else {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Amount should be in between 150 - 5000 and multiple of 50",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
                }
            } else {
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Please enter amount to proceed",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {
                        },
                        negativeClick = {
                        })
                    dialog.show()
            }
        }
    }


    private fun setCustomAmountAdapter() {
        binding?.rvCustomAmountList?.layoutManager = LinearLayoutManager(this)
        customGiftCardAdapter = CustomGiftCardAdapter( customizedGiftList, this,Uri.parse(imageValue))
        binding?.rvCustomAmountList?.adapter = customGiftCardAdapter
    }

    fun customCloseClick(pos: Int, amount: String) {
        total_amount -= amount.toInt() * customizedGiftList[pos].c.toInt()
        binding?.tvTotal?.text = total_amount.toString()
        customizedGiftList.removeAt(pos)
        customizedAmountList.removeAt(customizedAmountList.indexOf(amount))
        customGiftCardAdapter?.notifyDataSetChanged()
    }

   private fun setAmountAdapter() {
        binding?.rvAmountList?.layoutManager = LinearLayoutManager(this)
        giftAddAmountAdapter = GiftCardAddAmtAdapter(giftCardListFilter,this,Uri.parse(imageValue))
        binding?.rvAmountList?.adapter = giftAddAmountAdapter
    }

   private fun setAmountAdapterCustom() {
        binding?.rvAmountList?.layoutManager = LinearLayoutManager(this)
        giftAddAmountAdapter = GiftCardAddAmtAdapter(giftCardListFilter,this,Uri.parse(imageValue))
        binding?.rvAmountList?.adapter = giftAddAmountAdapter
    }

    @SuppressLint("SetTextI18n")
    fun plusClick(pos: Int, amount: Int) {
        if (giftCardListFilter[pos].count < giftCardListFilter[pos].allowedCount) {
            giftCardListFilter[pos].count = (giftCardListFilter[pos].count + 1)
            total_amount += amount
            binding?.tvTotal?.text = resources.getString(R.string.currency) + " " + total_amount
            binding?.llProceedGift?.show()
            binding?.llProceedGiftUnselect?.hide()
        }else{
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                "You can only order " + giftCardListFilter[pos].allowedCount + " items at a time",
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()
        }
        giftAddAmountAdapter?.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    fun minusClick(pos: Int, amount: Int) {
        if (giftCardListFilter[pos].count > 0) {
            giftCardListFilter[pos].count = (giftCardListFilter[pos].count - 1)
            total_amount -= amount
            binding?.tvTotal?.text = (resources.getString(R.string.currency) + " " + total_amount)
            for (i in giftCardListFilter.indices) {
                if (giftCardListFilter[i].count > 0) {
                    binding?.llProceedGift?.show()
                    binding?.llProceedGiftUnselect?.hide()
                    break
                }
            }
        }
        giftAddAmountAdapter?.notifyDataSetChanged()
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

    private fun sendData(url: String) {
        if (limit >= total_amount) {
            val newList = java.util.ArrayList<GiftCards>()
            if (customizedGiftList.size > 0) {
                newList.addAll(customizedGiftList)
                for (i in giftCardListFilter.indices) {
                    if (giftCardListFilter[i].count > 0) {
                        newList.add(
                            GiftCards(java.lang.String.valueOf(giftCardListFilter[i].giftValue/100) + "x" + "CUSTOMISED", giftCardListFilter[i].count.toString(), giftCardListFilter[i].type, giftCardListFilter[i].alias)
                        )
                    }
                }
            } else {
                for (i in giftCardListFilter.indices) {
                    if (giftCardListFilter[i].count > 0) {
                        newList.add(
                            GiftCards(
                                java.lang.String.valueOf(giftCardListFilter[i].giftValue/100),
                                giftCardListFilter[i].count.toString(),
                                giftCardListFilter[i].type,
                                giftCardListFilter[i].alias
                            )
                        )
                    }
                }
            }
            val saveGiftCardCount = SaveGiftCardCount("","","","","","","","",newList)
            if (saveGiftCardCount.gift_cards.size > 0) {
                val intent = Intent(this, GiftCardPlaceOrderActivity::class.java)
                try {
                    if (imageValueUri != null) {
                        intent.putExtra("imageValueUri", imageValueUri.toString())
                        intent.putExtra(
                            "imageValueUriUrl",
                            url
                        )
                    }
                } catch (e: java.lang.Exception) {
                }
                intent.putExtra("key", card_type)
                println("saveGiftCardCount---$saveGiftCardCount")
                intent.putExtra(Constant.SharedPreference.GIFT_CARD_DETAILS, saveGiftCardCount)
                startActivity(intent)
            }
        } else {
            val dialog = OptionDialog(this,
                R.mipmap.ic_launcher,
                R.string.app_name,
                "Total gift limit is : $limit",
                positiveBtnText = R.string.ok,
                negativeBtnText = R.string.no,
                positiveClick = {
                },
                negativeClick = {
                })
            dialog.show()

        }
    }

    private fun uploadGiftCard() {
        authViewModel.uploadGCResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        sendData(it.data.output.url)
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


}