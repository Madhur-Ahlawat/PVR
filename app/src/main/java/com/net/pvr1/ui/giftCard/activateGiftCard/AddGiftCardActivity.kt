package com.net.pvr1.ui.giftCard.activateGiftCard

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityAddGiftcardBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.activateGiftCard.adapter.CustomGiftCardAdapter
import com.net.pvr1.ui.giftCard.activateGiftCard.adapter.GiftCardAddAmtAdapter
import com.net.pvr1.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr1.ui.giftCard.response.GiftCardListResponse
import com.net.pvr1.ui.giftCard.response.GiftCards
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import com.net.pvr1.utils.toast
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
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

        binding?.ivBack?.setOnClickListener(this)
        binding?.llCancelGift?.setOnClickListener(this)
        binding?.llProceedGiftUnselect?.setOnClickListener(this)
        binding?.llProceedGift?.setOnClickListener(this)
        binding?.tvAddAmountCustom?.setOnClickListener(this)

        binding?.tvTotal?.text = resources.getString(R.string.currency) + " " + total_amount
        giftCardListFilter = ArrayList<GiftCardListResponse.Output.GiftCard>()
        if (intent != null) {
            if (intent.hasExtra("genericList")) {
                giftCardListFilter = intent.getSerializableExtra("genericList") as ArrayList<GiftCardListResponse.Output.GiftCard>
                toast("called this one....."+giftCardListFilter.size)

            }
            if (intent.hasExtra("key")) {
                card_type = intent.getStringExtra("key").toString()
            }
            if (intent.hasExtra("imageValue")) {
                imageValue = intent.getStringExtra("imageValue").toString()
                if (imageValue != null) {
                    Picasso.get()
                            .load(imageValue)
                            .placeholder(resources.getDrawable(R.drawable.gift_card_default))
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
            binding?.llPvrLogo?.show()
        }
        if (giftCardListFilter.size > 0) {
            if (isCustom.equals("true", ignoreCase = true)) {
                setAmountAdapterCustom()
            } else {
                setAmountAdapter()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_cancel_gift, R.id.iv_back -> onBackPressed()
            R.id.ll_proceed_gift -> if (imageValueUri != null) {
//                com.net.pvr.ui.giftcard.activities.AddAmountGiftCardActivity.UploadProfileImage()
//                    .execute()
            } else {
//                sendData()
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
            giftCardListFilter[pos].count = (giftCardListFilter[pos].allowedCount + 1)
            total_amount += amount
            binding?.tvTotal?.text = resources.getString(R.string.currency) + " " + total_amount
            binding?.llProceedGift?.show()
            binding?.llProceedGiftUnselect?.hide()
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

}