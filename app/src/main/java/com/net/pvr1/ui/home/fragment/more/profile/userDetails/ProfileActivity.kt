package com.net.pvr1.ui.home.fragment.more.profile.userDetails

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityProfileBinding
import com.net.pvr1.databinding.EditProfileDialogBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.profile.userDetails.viewModel.UserProfileViewModel
import com.net.pvr1.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.Constant.Companion.ProfileResponseConst
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.di.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.and
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityProfileBinding? = null
    private val authViewModel: UserProfileViewModel by viewModels()
    private var loader: LoaderDialog? = null
    private var profileResponse: ProfileResponse.Output? = null
    private var dobClick = false
    private var dob: TextView? = null
    private var anniversary: TextView? = null
    private var dialog: BottomSheetDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        //name
        binding?.textView212?.text = preferences.getUserName()
        profileResponse = ProfileResponseConst
        movedNext()

        //profile
        if (intent.getStringExtra("from") == "home") {
            val timeStamp = (System.currentTimeMillis() / 1000).toString()
            authViewModel.userProfile(
                preferences.getCityName(), preferences.getUserId(), timeStamp, getHashProfile(
                    preferences.getUserId() + "|" + timeStamp
                )
            )

        } else {
            detailsDataSet()
        }

        profileUpdate()
        profileLoad()
    }

    private fun detailsDataSet() {
        //name
        binding?.textView212?.text = preferences.getUserName()
        //member
        binding?.textView213?.text = profileResponse?.cd
        //phone
        binding?.textView215?.text = profileResponse?.ph
        //email
        binding?.textView217?.text = profileResponse?.em
        //gender
        binding?.textView219?.text = profileResponse?.g
        //dob
        binding?.textView221?.text = profileResponse?.dob
        //martial status
        binding?.textView223?.text = profileResponse?.ms
        //anniversary
        binding?.textView225?.text = profileResponse?.doa.toString()
    }

    private fun movedNext() {
        binding?.imageView117?.setOnClickListener {
            editProfileDialog()
        }
        binding?.imageView116?.setOnClickListener {
            finish()
        }
    }

    private fun editProfileDialog() {
        dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //profileDialog
        val inflater = LayoutInflater.from(this)
        val bindingProfile = EditProfileDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout>? = dialog?.behavior
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.setContentView(bindingProfile.root)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.show()

        //name
        bindingProfile.name.setText(preferences.getUserName())
        //phone
        bindingProfile.phone.setText(profileResponse?.ph)
        //email
        bindingProfile.email.setText(profileResponse?.em)
        //gender
        bindingProfile.gender.setText(profileResponse?.g)
        //dob
        dob = bindingProfile.dob
        dob?.text = profileResponse?.dob
        //martial status
        bindingProfile.martialStatus.setText(profileResponse?.ms)
        //anniversary
        anniversary = bindingProfile.anniversary
        anniversary?.text = profileResponse?.doa.toString()

        //Dob DatePicker
        bindingProfile.constraintLayout68.setOnClickListener {
            dobClick = true
            DatePickerDialog(
                this,
                dateD,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        //anniversaryClick
        bindingProfile.anniversaryClick.setOnClickListener {
            dobClick = false
            DatePickerDialog(
                this,
                dateD,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        //dismiss Dialog
        bindingProfile.textView247.setOnClickListener {
            dialog?.dismiss()
        }
        //save
        bindingProfile.save.setOnClickListener {
            val dob = Constant().changeDateFormat(bindingProfile.dob.text.toString())
            val anniversary =
                Constant().changeDateFormat(bindingProfile.anniversary.text.toString())
            authViewModel.editProfile(
                preferences.getUserId(),
                bindingProfile.email.text.toString(),
                bindingProfile.phone.text.toString(),
                bindingProfile.name.text.toString(),
                dob.toString(),
                bindingProfile.gender.text.toString(),
                bindingProfile.martialStatus.text.toString(),
                anniversary.toString()
            )
        }

    }

    //calender
    private var myCalendar = Calendar.getInstance()

    private var dateD = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar[Calendar.YEAR] = year
        myCalendar[Calendar.MONTH] = monthOfYear
        myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        updateLabel()
    }

    private fun updateLabel() {
        val myFormat = "dd MMM, yyyy " //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        if (dobClick) {
            dob?.text = sdf.format(myCalendar.time)
        } else {
            anniversary?.text = sdf.format(myCalendar.time)
        }
    }

    private fun profileLoad() {
        authViewModel.userProfileLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveDataProfile(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveDataProfile(output: ProfileResponse.Output) {
        profileResponse = output
        ProfileResponseConst = output
        binding?.textView212?.text = preferences.getUserName()

        //member
        binding?.textView213?.text = output.cd
        //phone
        binding?.textView215?.text = output.ph
        //email
        binding?.textView217?.text = output.em
        //gender
        binding?.textView219?.text = output.g
        //dob
        binding?.textView221?.text = output.dob
        //martial status
        binding?.textView223?.text = output.ms
        //anniversary
        binding?.textView225?.text = output.doa.toString()

//        //Update
//        //name
//        bindingProfile.name.setText(preferences.getUserName())
//        //phone
//        bindingProfile.phone.setText(profileResponse?.ph)
//        //email
//        bindingProfile.email.setText(profileResponse?.em)
//        //gender
//        bindingProfile.gender.setText(profileResponse?.g)
//        //dob
//        dob = bindingProfile.dob
//        dob?.text = profileResponse?.dob
//        //martial status
//        bindingProfile.martialStatus.setText(profileResponse?.ms)
//        //anniversary
//        anniversary = bindingProfile.anniversary
//        anniversary?.text = profileResponse?.doa.toString()
    }

    private fun profileUpdate() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data.msg,
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
                        dialog.show()
                        retrieveData(it.data.output)
                    } else {
                        val dialog = OptionDialog(this,
                            R.mipmap.ic_launcher,
                            R.string.app_name,
                            it.data?.msg.toString(),
                            positiveBtnText = R.string.ok,
                            negativeBtnText = R.string.no,
                            positiveClick = {},
                            negativeClick = {})
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
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                is NetworkResult.Loading -> {
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun retrieveData(output: ProfileResponse.Output) {
        ProfileResponseConst = output
        dialog?.dismiss()
    }

    @Throws(Exception::class)
    fun getHashProfile(text: String): String {
        val mdText = MessageDigest.getInstance("SHA-512")
        val byteData = mdText.digest(text.toByteArray())
        val sb = StringBuffer()
        for (i in byteData.indices) {
            sb.append(((byteData[i] and 0xff) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
    }
}