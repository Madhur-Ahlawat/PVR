package com.net.pvr1.ui.home.fragment.more.profile.userDetails

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityProfileBinding
import com.net.pvr1.databinding.EditProfileDialogBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.more.profile.userDetails.viewModel.UserProfileViewModel
import com.net.pvr1.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.Constant.Companion.ProfileResponseConst
import com.net.pvr1.utils.NetworkResult
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

    //gender
    private var selectedGender=""
    var gender = arrayOf("Select","Male", "Female")

    //marital Status
    private var selectedMarital=""
    var maritalStatus = arrayOf("Select","Married", "Unmarried")

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
        binding?.textView225?.text = profileResponse?.doa
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
        dialog = BottomSheetDialog(this, R.style.CustomAlertDialog)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        //profileDialog
        val inflater = LayoutInflater.from(this)
        val bindingProfile = EditProfileDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<FrameLayout>? = dialog?.behavior
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.setContentView(bindingProfile.root)

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)
        dialog?.show()


        //Gender

        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, gender)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        bindingProfile.gender.adapter = aa


        bindingProfile.gender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedGender = gender[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


        //marital Status
        val aaa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, maritalStatus)
        aaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        bindingProfile.martialStatus.adapter = aaa

        bindingProfile.martialStatus.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedMarital = maritalStatus[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        //name
        bindingProfile.name.setText(preferences.getUserName())

        //phone
        bindingProfile.phone.setText(profileResponse?.ph)

        //email
        bindingProfile.email.setText(profileResponse?.em)

        if (profileResponse?.gd!=null && profileResponse?.gd != ""){
            if (profileResponse?.gd == "Male"){
                bindingProfile.gender.setSelection(1)
            }else{
                bindingProfile.gender.setSelection(2)
            }
        }

        if (profileResponse?.ms!=null && profileResponse?.ms != ""){
            if (profileResponse?.ms == "Married"){
                anniversary?.text = profileResponse?.doa
                bindingProfile.martialStatus.setSelection(1)
            }else{
                bindingProfile.martialStatus.setSelection(2)
            }
        }

        //gender
//        bindingProfile.gender.setText(profileResponse?.g)

        //dob
        dob = bindingProfile.dob
        dob?.text = profileResponse?.dob
        //martial status
//        bindingProfile.martialStatus.setText(profileResponse?.ms)

        //anniversary
        anniversary = bindingProfile.anniversary

        //Dob DatePicker
        bindingProfile.constraintLayout68.setOnClickListener {
            dobClick = true
            val year=myCalendar.get(Calendar.YEAR)
            val newYear= year-13
            val mDatePickerDialog = DatePickerDialog(    this,R.style.dateDialog,
                dateD,
                newYear,
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))



            mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis()  + 0 * 24 * 60 * 60 * 1000
            mDatePickerDialog.show()
            mDatePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            mDatePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)

        }

        //anniversaryClick
        bindingProfile.anniversary.setOnClickListener {
            if (selectedMarital=="Married"){
                    dobClick = false
                val mDatePickerDialog = DatePickerDialog(    this,R.style.dateDialog,
                        dateD,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))

                    mDatePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 0 * 24 * 60 * 60 * 1000
                    mDatePickerDialog.show()
                mDatePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                mDatePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            }
        }


        //dismiss Dialog
        bindingProfile.textView247.setOnClickListener {
            dialog?.dismiss()
        }

        //save
        bindingProfile.save.setOnClickListener {
            val dob = Constant().changeDateFormat(bindingProfile.dob.text.toString())
            val anniversary = Constant().changeDateFormat(bindingProfile.anniversary.text.toString())

            println("anniversary--->$dob---$anniversary")

            if (selectedMarital=="Married"){
                if (bindingProfile.name.text.toString()==""){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Enter Name",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }else if (bindingProfile.phone.text.toString()==""){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Enter Phone Number",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }
                else if (bindingProfile.email.text.toString()==""){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Enter Email",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }else if (selectedGender==""){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Select Gender",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }else if (dob.toString()==""){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Enter Date Of Birth",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
<<<<<<< HEAD
                }else if (selectedGender=="" || selectedGender=="Select"){
=======
                }else if ( Data(dob.toString()) < 13){
>>>>>>> 0564f926 (fixes)
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Minimum 13 years of age required for enrolling this functionality.",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }else if (selectedMarital=="" || selectedMarital=="Select"){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Select Martial Status",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }else if (anniversary.toString()==""){
                    val dialog = OptionDialog(this,
                        R.mipmap.ic_launcher,
                        R.string.app_name,
                        "Select Anniversary",
                        positiveBtnText = R.string.ok,
                        negativeBtnText = R.string.no,
                        positiveClick = {},
                        negativeClick = {})
                    dialog.show()
                }else{
                    authViewModel.editProfile(
                        preferences.getUserId(),
                        bindingProfile.email.text.toString(),
                        bindingProfile.phone.text.toString(),
                        bindingProfile.name.text.toString(),
                        dob.toString(),
                        selectedGender,
                        selectedMarital,
                        anniversary.toString()
                    )
                }

            }else{
                authViewModel.editProfile(
                    preferences.getUserId(),
                    bindingProfile.email.text.toString(),
                    bindingProfile.phone.text.toString(),
                    bindingProfile.name.text.toString(),
                    dob.toString(),
                    selectedGender,
                    selectedMarital,
                    anniversary.toString()
                )
            }


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

    fun Data(date: String?): Int {
        if (!TextUtils.isEmpty(date)) {
            val cal = Calendar.getInstance()
            val dateInString = SimpleDateFormat("dd MMM, yyyy")
                .format(cal.time)
            val formatter = SimpleDateFormat("dd MMM, yyyy")
            var parsedDate: Date? = null
            var Date: Date? = null
            try {
                parsedDate = formatter.parse(dateInString)
                Date = formatter.parse(date)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            if (parsedDate != null && Date != null) return getDiffYears(Date, parsedDate)
        }
        return 0
    }

    private fun getDiffYears(first: Date?, last: Date?): Int {
        val a: Calendar = getCalendar(first)
        val b: Calendar = getCalendar(last)
        var diff = b[Calendar.YEAR] - a[Calendar.YEAR]
        if ((a[Calendar.MONTH] > b[Calendar.MONTH] || a[Calendar.MONTH] == b[Calendar.MONTH]) && a[Calendar.DATE] > b[Calendar.DATE]) {
            diff--
        }
        return diff
    }

    private fun getCalendar(date: Date?): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
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
        binding?.textView219?.text = output.gd
        //dob
        binding?.textView221?.text = output.dob
        //martial status
        binding?.textView223?.text = output.ms
        //anniversary
        binding?.textView225?.text = output.doa

    }

    private fun profileUpdate() {
        authViewModel.userResponseLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    loader?.dismiss()
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        dialog?.dismiss()
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