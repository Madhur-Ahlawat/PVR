package com.net.pvr1.ui.giftCard.activateGiftCard

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPlaceGcOrderBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.GiftCardSummaryActivity
import com.net.pvr1.ui.giftCard.activateGiftCard.viewModel.ActivateGiftCardViewModel
import com.net.pvr1.ui.giftCard.response.SaveGiftCardCount
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.NumberFormat
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class GiftCardPlaceOrderActivity : AppCompatActivity(){
    private var binding: ActivityPlaceGcOrderBinding? = null
    private var loader: LoaderDialog? = null
    private var imageUrl = ""
    private var card_type = ""
    private var isSelf = ""
    private var gtype = ""
    private var customImage = ""
    private var customName = ""
    private var quantit = ""
    private var amount = ""
    private var sum = 0f
    private var imageValueUri:Uri? = null
    private var saveGiftCardCount: SaveGiftCardCount? = null
    private val authViewModel: ActivateGiftCardViewModel by viewModels()

    @Inject
    lateinit var preferences: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceGcOrderBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        binding?.tvTitle?.text = "Expired Gift Card"
        binding?.ivBack?.setOnClickListener {
            onBackPressed()
        }
        //Screen Width
        if (intent.getSerializableExtra(Constant.SharedPreference.GIFT_CARD_DETAILS) != null) {
            saveGiftCardCount = intent.getSerializableExtra(Constant.SharedPreference.GIFT_CARD_DETAILS) as SaveGiftCardCount
        }

        if (intent.getStringExtra("imageValueUriUrl") != null) {
            imageUrl = intent.getStringExtra("imageValueUriUrl").toString()
        }

        if (intent.extras?.getString("key") != null)
            card_type = intent.extras?.getString("key").toString()

        initData()
        personalMessage()
        saveGiftCard()
    }

    private fun initData() {
        if (card_type.equals("PCARD", ignoreCase = true) || card_type.equals("SCARD", ignoreCase = true)) {
            binding?.eCartView?.hide()
            binding?.pCartView?.show()
        } else {
            binding?.eCartView?.show()
            binding?.pCartView?.hide()
        }

        binding?.personalMessage?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        binding?.switchButton?.isChecked = true

        binding?.switchButton?.setOnClickListener(View.OnClickListener {
            if (binding?.switchButton?.isChecked == true) {
                binding?.recipientName?.setText("")
                binding?.emailAddressEditText?.setText("")
                binding?.mobileEditText?.setText("")
                binding?.personalMessage?.setText("")
                binding?.recipientName?.requestFocus()
                if (preferences.getIsLogin()) {
                    binding?.recipientName?.isEnabled = true
                    binding?.personalMessage?.isEnabled = true
                    binding?.emailAddressEditText?.isEnabled = true
                    binding?.mobileEditText?.isEnabled = true
                }
            } else {
                val userName = preferences.getUserName()
                val userEmail = preferences.getEmail()
                val userMobile = preferences.getString(Constant.SharedPreference.USER_NUMBER)
                if (preferences.getIsLogin()) {
                    binding?.mobileEditText?.setText(userMobile)
                    binding?.emailAddressEditText?.setText(userEmail)
                    binding?.recipientName?.setText(userName)
                    binding?.recipientName?.isEnabled = false
                    binding?.personalMessage?.isEnabled = true
                    binding?.mobileEditText?.isEnabled = false
                    binding?.emailAddressEditText?.isEnabled = false
                }
            }
            reset()
        })
//        switch_button.performClick();

        //        switch_button.performClick();
        binding?.isSelfSelectorTab?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            if (checkedId == R.id.radioYes) {
                binding?.recipientName?.setText("")
                binding?.emailAddressEditText?.setText("")
                binding?.mobileEditText?.setText("")
                binding?.personalMessage?.setText("")
                binding?.recipientName?.requestFocus()
                if (preferences.getIsLogin()) {
                    binding?.recipientName?.isEnabled = true
                    binding?.personalMessage?.isEnabled = true
                    binding?.emailAddressEditText?.isEnabled = true
                    binding?.mobileEditText?.isEnabled = true
                }
            } else {
                val userName = preferences.getUserName()
                val userEmail = preferences.getEmail()
                val userMobile = preferences.getString(Constant.SharedPreference.USER_NUMBER)
                if (preferences.getIsLogin()) {
                    binding?.mobileEditText?.setText(userMobile)
                    binding?.emailAddressEditText?.setText(userEmail)
                    binding?.recipientName?.setText(userName)
                    binding?.recipientName?.isEnabled = false
                    binding?.personalMessage?.isEnabled = true
                    binding?.mobileEditText?.isEnabled = false
                    binding?.emailAddressEditText?.isEnabled = false
                }
            }
            reset()
        })
        binding?.llCancelGift?.setOnClickListener{
            onBackPressed()
        }

        binding?.llProceedGift?.setOnClickListener(View.OnClickListener {
            if (validateInputFields())
                setData()
        })

        binding?.btncontinue?.setOnClickListener(View.OnClickListener { //                Toast.makeText(context, String.valueOf(validateInputFields()), Toast.LENGTH_SHORT).show();
            if (validateInputFields())
                setData()
        })

        if (intent != null) {
            if (intent.getStringExtra("imageValueUri") != null) {
                val imageValue = intent.getStringExtra("imageValueUri")
                imageValueUri = Uri.parse(imageValue)
            }
        }

    }

    private fun reset() {
        binding?.recipientName?.error = null
        binding?.personalMessage?.error = null
        binding?.delAddress?.error = null
        binding?.city?.error = null
        binding?.pincode?.error = null
        binding?.mobileEditText?.error = null
        binding?.emailAddressEditText?.error = null
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    private fun personalMessage() {
        binding?.personalMessage?.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            changeColor(hasFocus)
        }
        binding?.personalMessage?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val Textlen = s.toString().length
                if (100 - Textlen >= 0) binding?.TvCounter?.text = "" + (100 - Textlen)
            }
        })
    }


    private fun changeColor(hasFocus: Boolean) {
        if (hasFocus) {
            binding?.TVPersonal?.setTextColor(resources.getColor(R.color.pvr_yellow))
            binding?.personalMessageView?.setBackgroundColor(resources.getColor(R.color.pvr_yellow))
            binding?.personalMessageView?.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Constant().convertDpToPixel(2f, this)
            )
        } else {
            binding?.TVPersonal?.setTextColor(resources.getColor(R.color.gray_))
            binding?.personalMessageView?.setBackgroundColor(resources.getColor(R.color.gray_new))
            binding?.personalMessageView?.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Constant().convertDpToPixel(1f, this)
            )
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun validateInputFields(): Boolean {
        return if (card_type.equals("PCARD", ignoreCase = true) || card_type.equals("SCARD", ignoreCase = true)) {
            if (!InputTextValidator.hasText(binding?.recipientName!!)) {
                binding?.recipientName?.error = getString(R.string.gift_card_name_msg_required)
                binding?.recipientName?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.hasText(binding?.emailAddressEditText!!)) {
                binding?.emailAddressEditText?.error = getString(R.string.email_msg_required)
                binding?.emailAddressEditText?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.validateEmail(binding?.emailAddressEditText!!)) {
                if (binding?.emailAddressEditText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                    binding?.emailAddressEditText?.error = getString(R.string.email_msg_required)
                } else binding?.emailAddressEditText?.error = getString(R.string.email_msg_invalid)
                binding?.emailAddressEditText?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.hasText(binding?.mobileEditText!!)) {
                binding?.mobileEditText?.error = getString(R.string.mobile_msg_required)
                binding?.mobileEditText?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (binding?.mobileEditText?.text.toString().length < 10) {
                if (binding?.mobileEditText?.text.toString().isEmpty()) {
                    binding?.mobileEditText?.error = getString(R.string.mobile_msg_required)
                } else binding?.mobileEditText?.error = getString(R.string.mobile_msg_invalid)
                binding?.mobileEditText?.requestFocus()
                hideSoftKeyboard()
            }
            if (!InputTextValidator.hasText(binding?.delAddress!!)) {
                binding?.delAddress?.error = getString(R.string.gift_card_address_msg_required)
                binding?.delAddress?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.hasText(binding?.city!!)) {
                binding?.city?.error = getString(R.string.gift_card_city_msg_required)
                binding?.city?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.hasText(binding?.pincode!!)) {
                binding?.pincode?.error = getString(R.string.gift_card_pincode_msg_required)
                binding?.pincode?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.validatePin(binding?.pincode!!)) {
                if (binding?.pincode?.text.toString().trim { it <= ' ' }.isEmpty()) {
                    binding?.pincode?.error = getString(R.string.gift_card_pincode_msg_required)
                } else binding?.pincode?.error = getString(R.string.gift_card_pincode_msg_invalid)
                binding?.pincode?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            true
        } else {
            if (!InputTextValidator.hasText(binding?.recipientName!!)) {
                binding?.recipientName?.error = getString(R.string.gift_card_name_msg_required)
                binding?.recipientName?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.hasText(binding?.emailAddressEditText!!)) {
                binding?.emailAddressEditText?.error = getString(R.string.email_msg_required)
                binding?.emailAddressEditText?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.validateEmail(binding?.emailAddressEditText!!)) {
                //  emailAddressEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_invalid_input_selector));
                if (binding?.emailAddressEditText?.text.toString().trim { it <= ' ' }.isEmpty()) {
                    binding?.emailAddressEditText?.error = getString(R.string.email_msg_required)
                    /*emailValidationMessage.setText(getString(R.string.email_msg_required));*/
                } else binding?.emailAddressEditText?.error = getString(R.string.email_msg_invalid)
                binding?.emailAddressEditText?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (!InputTextValidator.hasText(binding?.mobileEditText!!)) {
                binding?.mobileEditText?.error = getString(R.string.mobile_msg_required)
                binding?.mobileEditText?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            if (binding?.mobileEditText?.text.toString().length < 10) {
                if (binding?.mobileEditText?.text.toString().isEmpty()) {
                    binding?.mobileEditText?.error = getString(R.string.mobile_msg_required)
                } else binding?.mobileEditText?.error = getString(R.string.mobile_msg_invalid)
                binding?.mobileEditText?.requestFocus()
                hideSoftKeyboard()
                return false
            }
            true

        }
    }

    private fun setData() {
        val id = binding?.isSelfSelectorTab?.checkedRadioButtonId
        isSelf = if (id == R.id.radioYes) {
            "0"
        } else {
            "1"
        }
        saveGiftCardCount =
            if (card_type.equals("PCARD", ignoreCase = true) || card_type.equals("SCARD", ignoreCase = true)) {
                SaveGiftCardCount(binding?.emailAddressEditText?.text.toString(),binding?.mobileEditText?.text.toString(),binding?.recipientName?.text.toString(),binding?.personalMessage?.text.toString(),binding?.delAddress?.text.toString(),isSelf,binding?.city?.text.toString(),binding?.pincode?.text.toString(),saveGiftCardCount?.gift_cards!!)
            } else {
                SaveGiftCardCount(binding?.emailAddressEditText?.text.toString(),binding?.mobileEditText?.text.toString(),binding?.recipientName?.text.toString(),binding?.personalMessage?.text.toString(),"",isSelf,"","",saveGiftCardCount?.gift_cards!!)
            }



        gtype = if (saveGiftCardCount?.gift_cards!![0].type == "0")
            "GENERIC"
        else saveGiftCardCount?.gift_cards!![0].type
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            customImage = imageUrl
            customName = preferences.getUserName().toString()
        }

        for (i in 0 until saveGiftCardCount?.gift_cards?.size!!) {
            if (saveGiftCardCount?.gift_cards!![i].c != "0") {
                if (i == 0) {
                    quantit = saveGiftCardCount?.gift_cards!![i].c
                    amount = (saveGiftCardCount?.gift_cards!![i].d.replace("xCUSTOMISED".toRegex(), "").toFloat() * 100).toString()
                    amount = if (saveGiftCardCount?.gift_cards!![i].d.contains("xCUSTOMISED"))
                        amount.substring(0, amount.indexOf(".")) + "xCUSTOMISED"
                    else amount.substring(0, amount.indexOf("."))
                } else {
                    quantit = quantit + "," + saveGiftCardCount?.gift_cards!![i].c
                    var aa: String = (saveGiftCardCount?.gift_cards!![i].d.replace("xCUSTOMISED".toRegex(), "").toFloat() * 100).toString()
                    aa = aa.substring(0, aa.indexOf("."))
                    amount = if (saveGiftCardCount?.gift_cards!![i].d.contains("xCUSTOMISED"))
                        amount + "," + aa + "xCUSTOMISED" else "$amount,$aa"
                }
            }
        }
        val total = amount.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val quan = quantit.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (i in total.indices) {
            sum += total[i].replace("xCUSTOMISED".toRegex(), "")
                .toFloat() * quan[i].replace("xCUSTOMISED".toRegex(), "").toFloat()
        }
        authViewModel.saveGiftCard(
            saveGiftCardCount?.recipient_name!!,
            saveGiftCardCount?.recipient_email!!,
            saveGiftCardCount?.recipient_mobile!!,
            card_type,gtype,
            intent.getStringExtra("pkGiftId") ?: "",
            saveGiftCardCount?.pincode!!,
            saveGiftCardCount?.personal_message!!,saveGiftCardCount?.del_address!!,amount,quantit,preferences.getEmail(),isSelf,
            sum.toString().substring(0, sum.toString().indexOf(".")),customImage,customName
        )

    }

    private fun saveGiftCard() {
        authViewModel.saveGCResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        Constant.BOOKING_ID = it.data.output.id
                        val intent = Intent(this, GiftCardSummaryActivity::class.java)
                        if (imageValueUri != null) intent.putExtra(
                            "imageValueUri",
                            imageValueUri.toString()
                        )
//                        intent.putExtra(PCConstants.IntentKey.GIFT_PURCHASE_SUMMARY, response)

                        val nf: NumberFormat = DecimalFormat("#.########")
                        val s1: String = nf.format(sum.toDouble())

                        intent.putExtra("totalAmount", s1)
                        intent.putExtra("quantity", quantit)
                        intent.putExtra("ifSelf", java.lang.Boolean.getBoolean(isSelf))
                        intent.putExtra("rName", saveGiftCardCount?.recipient_name)
                        intent.putExtra("personalMessage", saveGiftCardCount?.personal_message)
                        intent.putExtra("delAddress", saveGiftCardCount?.del_address)
                        intent.putExtra("pincode", saveGiftCardCount?.pincode)
                        intent.putExtra("denomination", amount)
                        startActivity(intent)
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