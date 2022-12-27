package com.net.pvr1.ui.profile.userDetails

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityProfileBinding
import com.net.pvr1.databinding.EditProfileDialogBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr1.ui.profile.userDetails.viewModel.UserProfileViewModel
import com.net.pvr1.utils.Constant.Companion.ProfileResponseConst
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
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
    private var dobClick= false
    private  var dob:TextView?=null
    private  var anniversary:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)
        //name
        binding?.textView212?.text=preferences.getUserName()
        profileResponse=ProfileResponseConst
        movedNext()
        detailsDataSet()
    }

    private fun detailsDataSet() {
        //name
        binding?.textView212?.text= preferences.getUserName()
        //member
        binding?.textView213?.text=profileResponse?.cd
        //phone
        binding?.textView215?.text=profileResponse?.ph
        //email
        binding?.textView217?.text= profileResponse?.em
        //gender
        binding?.textView219?.text= profileResponse?.g
        //dob
        binding?.textView221?.text= profileResponse?.dob
        //martial status
        binding?.textView223?.text= profileResponse?.ms
        //anniversary
        binding?.textView225?.text= profileResponse?.im
    }

    private fun movedNext() {
        binding?.imageView117?.setOnClickListener {
            editProfileDialog()
        }
    }

    private fun editProfileDialog() {
        val dialog = BottomSheetDialog(this, R.style.NoBackgroundDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //profileDialog
        val inflater = LayoutInflater.from(this)
        val bindingProfile = EditProfileDialogBinding.inflate(inflater)
        val behavior: BottomSheetBehavior<*> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(bindingProfile.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.show()
        //name
        bindingProfile.name.setText(preferences.getUserName())
        //phone
        bindingProfile.phone.setText(profileResponse?.ph)
        //email
        bindingProfile.email.setText(profileResponse?.em)
        //gender
        bindingProfile.gender.setText(profileResponse?.g)
        //dob
        dob= bindingProfile.dob
        dob?.text= profileResponse?.dob
        //martial status
        bindingProfile.martialStatus.setText(profileResponse?.ms)
        //anniversary
        anniversary= bindingProfile.anniversary
        anniversary?.text = profileResponse?.im
        //Dob DatePicker
        bindingProfile.constraintLayout68.setOnClickListener {
            dobClick=true
            DatePickerDialog(
                this, dateD, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        //anniversaryClick
        bindingProfile.anniversaryClick.setOnClickListener {
            dobClick=false
            DatePickerDialog(
                this, dateD, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
//            myCalendar.getDatePicker().setMaxDate(System.currentTimeMillis());

        }
        //dismiss Dialog
        bindingProfile.textView247.setOnClickListener {
            dialog.dismiss()
        }
        //save
        bindingProfile.save.setOnClickListener {
            authViewModel.editProfile(preferences.getUserId(),bindingProfile.email.text.toString(), bindingProfile.phone.text.toString(),
                bindingProfile.name.text.toString(),bindingProfile.dob.text.toString(),bindingProfile.gender.text.toString(),
                bindingProfile.martialStatus.text.toString(),bindingProfile.anniversary.text.toString()
            )
        }

    }
//calender
    private  var myCalendar = Calendar.getInstance()

    private  var dateD =
        OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
        }else{
            anniversary?.text = sdf.format(myCalendar.time)
        }
    }
}