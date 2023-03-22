package com.net.pvr.ui.home.fragment.privilege

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr.R
import com.net.pvr.databinding.ActivityEnrollPassportBinding
import com.net.pvr.databinding.FragmentPrivilegeBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr.ui.payment.PaymentActivity
import com.net.pvr.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class EnrollInPrivilegeActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityEnrollPassportBinding? = null
    private val authViewModel: PrivilegeLoginViewModel by viewModels()
    private var loader: LoaderDialog? = null
    companion object {
        var scheme_id = ""
        var scheme_price = "0.0"
        var visits = "0.0"

    }

    private var mYear:Int = 0
    private var mMonth:Int = 0
    private var mDay:Int = 0
    var current = ""
    var ddmmyyyy = "DDMMYYYY"
    var cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollPassportBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        binding?.tvSendRequest?.setOnClickListener(View.OnClickListener {
            if (validate()) {
                //
                val dob: String =
                    Constant.getDate("dd/MM/yyyy", "dd-MM-yyyy", binding?.dobEt?.text.toString()).toString()
                preferences.saveString(Constant.SharedPreference.DOB,binding?.dobEt?.text.toString())

                authViewModel.enrollPrivilege(
                    preferences.getUserId(),
                    preferences.getCityName(),
                    binding?.etFname?.text.toString().split(" ").toTypedArray()[0],binding?.etFname?.text.toString().split(" ").toTypedArray()[1],
                    dob,"",
                    preferences.getString(Constant.SharedPreference.USER_EMAIL)
                )
                passportSave()

            }
        })

        binding?.btnBack?.setOnClickListener(View.OnClickListener { finish() })

        spannable()

        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]


        val splited: String = preferences.getString(Constant.SharedPreference.USER_NAME)
        binding?.etFname?.setText(splited)

        val email: String =
            preferences.getString(Constant.SharedPreference.USER_EMAIL)
        binding?.etEmail?.setText(email)


        val city: String =
            preferences.getString(Constant.SharedPreference.SELECTED_CITY_ID)
        binding?.etCity?.setText(city)

        binding?.etCity?.isEnabled = city.isEmpty()


        val mobile: String =
            preferences.getString(Constant.SharedPreference.USER_NUMBER)
        binding?.etMobile?.setText(mobile)

        val DOB: String = preferences.getString(Constant.SharedPreference.DOB)

        val dob: String = Constant.getDate("dd MMM, yyyy", "dd/MM/yyyy", DOB).toString()
        binding?.dobEt?.setText(dob)

        binding?.dobEt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @SuppressLint("DefaultLocale")
            override fun onTextChanged(charSequence: CharSequence, i4: Int, i1: Int, i2: Int) {
                if (charSequence.toString() != current) {
                    var clean = charSequence.toString().replace("[^\\d.]|\\.".toRegex(), "")
                    val cleanC: String = current.replace("[^\\d.]|\\.".toRegex(), "")
                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--
                    if (clean.length < 8) {
                        clean += ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var day = clean.substring(0, 2).toInt()
                        var mon = clean.substring(2, 4).toInt()
                        var year = clean.substring(4, 8).toInt()
                        mon = if (mon < 1) 1 else Math.min(mon, 12)
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else Math.min(year, 2100)
                        cal.set(Calendar.YEAR, year)
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE))
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }
                    clean = String.format(
                        "%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8)
                    )
                    sel = max(sel, 0)
                    current = clean
                    println("current--->$current")
                    binding?.dobEt?.setText(current)
                    binding?.dobEt?.setSelection(min(sel, current.length))
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

    }


    private fun validate(): Boolean {
        if (TextUtils.isEmpty(binding?.etFname?.text.toString().trim { it <= ' ' })) {
            binding?.etFname?.isFocusable = true
            binding?.etFname?.error = "Your name is required"
            return false
        } else if (!InputTextValidator.checkFullName(binding?.etFname!!)) {
            binding?.etFname?.setBackgroundResource(R.drawable.select_red_white)
            binding?.etFname?.error = "Your last name is required"
            return false
        } else if (TextUtils.isEmpty(binding?.etEmail?.text.toString().trim { it <= ' ' })) {
            binding?.etEmail?.isFocusable = true
            binding?.etEmail?.error = "Please enter valid email"
            return false
        } else if (TextUtils.isEmpty(binding?.dobEt?.text.toString().trim { it <= ' ' })) {
            binding?.dobEt?.isFocusable = true
            binding?.dobEt?.error = "Please enter valid date of birth"
            return false
        } else if (Data(binding?.dobEt?.text.toString()) < 13) {

            toast("Minimum 13 years of age required for enrolling this functionality.")
            return false
        }

        return true
    }


    private fun passportSave() {
        authViewModel.enrollPrivilegeLiveData.observe(this) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        Constant.BOOKING_ID = it.data.output.id
                        Constant.BOOK_TYPE = it.data.output.booktype
                        launchPrivilegeActivity(
                            HomeActivity::class.java,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK,"","",
                            "","P")
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
                }
                is NetworkResult.Loading -> {
                }
            }
        }

    }


    fun Data(date: String?): Int {
        if (!TextUtils.isEmpty(date)) {
            val cal = Calendar.getInstance()
            val dateInString = SimpleDateFormat("dd/MM/yyyy")
                .format(cal.time)
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            var parsedDate: Date? = null
            var Date: Date? = null
            try {
                parsedDate = formatter.parse(dateInString)
                Date = formatter.parse(date)
            } catch (e: Exception) {
                val dateInString1 = SimpleDateFormat("dd/MM/yyyy")
                    .format(cal.time)
                val formatter1 = SimpleDateFormat("dd/MM/yyyy")
                e.printStackTrace()
                try {
                    parsedDate = formatter1.parse(dateInString1)
                    Date = formatter1.parse(date)
                } catch (ex: ParseException) {
                    ex.printStackTrace()
                }
            }
            if (parsedDate != null && Date != null) return getDiffYears(Date, parsedDate)
        }
        return 0
    }

    fun getDiffYears(first: Date?, last: Date?): Int {
        val a = getCalendar(first)
        val b = getCalendar(last)
        var diff = b[Calendar.YEAR] - a[Calendar.YEAR]
        if (a[Calendar.MONTH] > b[Calendar.MONTH] ||
            a[Calendar.MONTH] == b[Calendar.MONTH] && a[Calendar.DATE] > b[Calendar.DATE]
        ) {
            diff--
        }
        return diff
    }

    fun getCalendar(date: Date?): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }

    private fun spannable() {
        val ss =
            SpannableString("You will be auto-enrolled into PVR Privilege Program and get its benefits.")
        val span2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
//                Intent intent2 = new Intent(Subscription_Form.this, TermsAndConditionActivity.class);
//                intent2.putExtra("TYPE","PP");
//                startActivity(intent2);
            }

            override fun updateDrawState(ds: TextPaint) { // override updateDrawState
                ds.isUnderlineText = false // set to false to remove underline
            }
        }
        ss.setSpan(span2, 2, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        //        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 2, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new UnderlineSpan(), 33, ss.length(), 0);
        binding?.cbCC?.text = ss
        binding?.cbCC?.movementMethod = LinkMovementMethod.getInstance()
    }
    
}