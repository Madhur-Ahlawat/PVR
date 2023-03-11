package com.net.pvr.ui.home.fragment.more

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.base.Splitter
import com.net.pvr.R
import com.net.pvr.databinding.FragmentMoreBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.dailogs.LoaderDialog
import com.net.pvr.ui.dailogs.OptionDialog
import com.net.pvr.ui.giftCard.GiftCardActivity
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.fragment.more.adapter.ProfileCompleteAdapter
import com.net.pvr.ui.home.fragment.more.bookingRetrieval.BookingRetrievalActivity
import com.net.pvr.ui.home.fragment.more.contactUs.ContactUsActivity
import com.net.pvr.ui.home.fragment.more.eVoucher.EVoucherActivity
import com.net.pvr.ui.home.fragment.more.experience.ExperienceActivity
import com.net.pvr.ui.home.fragment.more.model.ProfileModel
import com.net.pvr.ui.home.fragment.more.offer.OfferActivity
import com.net.pvr.ui.home.fragment.more.prefrence.PreferenceActivity
import com.net.pvr.ui.home.fragment.more.privacy.TermsPrivacyActivity
import com.net.pvr.ui.home.fragment.more.profile.userDetails.ProfileActivity
import com.net.pvr.ui.home.fragment.more.response.ProfileResponse
import com.net.pvr.ui.home.fragment.more.viewModel.MoreViewModel
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.ui.myBookings.MyBookingsActivity
import com.net.pvr.ui.privateScreenings.PrivateScreeningsActivity
import com.net.pvr.ui.scanner.ScannerActivity
import com.net.pvr.ui.watchList.WatchListActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.ProfileResponseConst
import com.net.pvr.utils.Constant.Companion.newTag
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.and
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment() {
    private var timeStamp = ""
    private var qrCode = ""
    private var binding: FragmentMoreBinding? = null
    private val authViewModel by activityViewModels<MoreViewModel>()
    private var loader: LoaderDialog? = null
    private val profileList: ArrayList<ProfileModel> = ArrayList()

    @Inject
    lateinit var preferences: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HomeActivity.backToTop?.hide()
        preferences = PreferenceManager(requireActivity())
        timeStamp = (System.currentTimeMillis() / 1000).toString()

        //add List
        profileList.add(ProfileModel("Provide your contact number", "MOBILE"))
        profileList.add(ProfileModel("Your email", "EMAIL"))
        profileList.add(ProfileModel("Gender information", "GENDER"))
        profileList.add(ProfileModel("Date of birth", "DOB"))
        profileList.add(ProfileModel("Anniversary", "DOA"))
        profileList.add(ProfileModel("Save movie preferences", "MOVIE"))

        //SetName
        binding?.profileDetails?.textView205?.text = preferences.getUserName()
        manageFunctions()


    }

    private fun manageFunctions() {
        //Manage ui
        binding?.tvLoginButton?.textView5?.text = getString(R.string.login)

        movedNext()
        //whatsapp Status
        whatsappOptStatus()
        //profileResponse
        profileResponse()
        newTagShow()
        //mange privilege show hide
        if (preferences.getIsLogin()) {
            binding?.whatsappUi?.show()
            authViewModel.whatsappOpt(
                preferences.getUserId(),
                preferences.getToken().toString(),
                timeStamp,
                getHash(preferences.getUserId() + "|" + preferences.getToken() + "|" + "optin-info" + "|" + timeStamp)
            )

//profile
            authViewModel.userProfile(
                preferences.getCityName(), preferences.getUserId(), timeStamp, getHashProfile(
                    preferences.getUserId() + "|" + timeStamp
                )
            )

            val ls = preferences.getString(Constant.SharedPreference.LOYALITY_STATUS)
            val isHl: String = preferences.getString(Constant.SharedPreference.IS_HL)
            val isLy: String = preferences.getString(Constant.SharedPreference.IS_LY)
            if (isLy.equals("true", ignoreCase = true)) {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    if (isHl.equals("true", ignoreCase = true)) {
                        binding?.privilegeLoginUi?.show()
                        binding?.privilegeLogOutUi?.hide()
                        if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == Constant.SharedPreference.ACTIVE && preferences.getString(Constant.SharedPreference.SUBS_OPEN) == "true"){
                            binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                        }else{
                            binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                        }
                    } else {
                        if (ls != null && !ls.equals("", ignoreCase = true)) {
                            binding?.privilegeLogOutUi?.hide()
                            binding?.privilegeLoginUi?.show()
                            if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == Constant.SharedPreference.ACTIVE && preferences.getString(
                                    Constant.SharedPreference.SUBS_OPEN
                                ) == "true"){
                                binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                            }else{
                                binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                            }
                        }else{
                            binding?.privilegeLogOutUi?.show()
                            binding?.privilegeLoginUi?.hide()
                        }
                    }
                } else {
                    binding?.privilegeLogOutUi?.show()
                    binding?.privilegeLoginUi?.hide()
                }
            } else {
                if (ls != null && !ls.equals("", ignoreCase = true)) {
                    binding?.privilegeLogOutUi?.hide()
                    binding?.privilegeLoginUi?.show()
                    if (preferences.getString(Constant.SharedPreference.SUBSCRIPTION_STATUS) == Constant.SharedPreference.ACTIVE && preferences.getString(
                            Constant.SharedPreference.SUBS_OPEN
                        ) == "true"){
                        binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.subs_back_b)
                    }else{
                        binding?.privilegeLogin?.paidMemberBack?.setBackgroundResource(R.drawable.privilege_home_back)
                    }
                }else{
                    binding?.privilegeLogOutUi?.show()
                    binding?.privilegeLoginUi?.hide()
                }
            }
            binding?.llBookingSection?.show()
            binding?.profileLinear?.show()
            binding?.tvSignOut?.show()
            binding?.loginUi?.show()
            binding?.loginBt?.hide()
            binding?.privilegeLogin?.pt?.text = Constant.PRIVILEGEPOINT
            binding?.privilegeLogin?.numVou?.text = Constant.PRIVILEGEVOUCHER
            binding?.privilegeLogin?.qrImgMainPage?.setOnClickListener {
                oPenDialogQR()
            }

            createQr()

        } else {
            binding?.whatsappUi?.hide()
            binding?.llBookingSection?.show()
            binding?.tvSignOut?.hide()
            binding?.profileLinear?.hide()
            binding?.loginBt?.show()
            binding?.loginUi?.hide()
            binding?.privilegeLoginUi?.hide()
            binding?.privilegeLogOutUi?.show()
        }

        binding?.privilege?.setOnClickListener {
            val navigationView = requireActivity().findViewById(R.id.bottomNavigationView) as BottomNavigationView
            val bottomMenuView = navigationView.getChildAt(0) as BottomNavigationMenuView
            val newView = bottomMenuView.getChildAt(2)
            val itemView = newView as BottomNavigationItemView
            itemView.performClick()
        }
    }

    private fun newTagShow() {
        val str = newTag
        val list = Splitter.on(",").splitToList(str)
        for (i in 0 until list.size) {
            if (list[i].contains("Experience")) {
                binding?.logout?.includeExperience?.cardView19?.show()

            } else if (list[i].contains("PVR Cares")) {
                binding?.logout?.includePvrCare?.cardView19?.show()

            } else if (list[i].contains("Merchandise")) {
                binding?.logout?.includeMerchandise?.cardView19?.show()

            } else if (list[i].contains("Gift Cards")) {
                binding?.logout?.includeGiftCard?.cardView19?.show()

            } else if (list[i].contains("Offers")) {
                binding?.logout?.includeOffers?.cardView19?.show()

            } else if (list[i].contains("My Bookings")) {
                binding?.login?.includeMyBooking?.cardView19?.show()

            } else if (list[i].contains("Booking Retrieval")) {
                binding?.login?.includeBookingRetrieval?.cardView19?.show()

            } else if (list[i].contains("Movie Alerts")) {
                binding?.login?.includeMovieAlert?.cardView19?.show()

            } else if (list[i].contains("My Preferences")) {
                binding?.login?.includeMyPreference?.cardView19?.show()

            } else if (list[i].contains("Macmerise")) {
                binding?.logout?.includeMacmerise?.cardView19?.show()

            }
        }
    }

    //   Ui ClickAction
    private fun movedNext() {
        //Account
        binding?.profileDetails?.nameView?.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        //MyBookings
        binding?.login?.constraintLayout70?.setOnClickListener {
            val intent = Intent(requireContext(), MyBookingsActivity::class.java)
            startActivity(intent)
        }

        //Experience
        binding?.logout?.experience?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Experiences Page")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(requireActivity(), "experiences", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }
            val intent = Intent(requireContext(), ExperienceActivity::class.java)
            startActivity(intent)
        }

        //Merchandise
        binding?.logout?.constraintLayout79?.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra("title", "Return to App")
            intent.putExtra("from", "more")
            intent.putExtra("getUrl", Constant.merchandise)
            startActivity(intent)
        }

        //Offers
        binding?.logout?.constraintLayout78?.setOnClickListener {
            // Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Offer")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(requireActivity(), "offers_button", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            val intent = Intent(requireContext(), OfferActivity::class.java)
            startActivity(intent)
        }

        // Merchandise
        binding?.logout?.constraintLayout76?.setOnClickListener {
            val intent1 = Intent(requireContext(), WebViewActivity::class.java)
            intent1.putExtra("from", "more")
            intent1.putExtra("title", "Merchandise")
            intent1.putExtra("getUrl", Constant.merchandise)
            startActivity(intent1)
        }

        // Macmerise
        binding?.logout?.constraintLayout167?.setOnClickListener {
            val intent1 = Intent(requireContext(), WebViewActivity::class.java)
            intent1.putExtra("from", "more")
            intent1.putExtra("title", "Macmerise")
            intent1.putExtra("getUrl", Constant.macmerise)
            startActivity(intent1)
        }

        // PVR care
        binding?.logout?.constraintLayout75?.setOnClickListener {
            val intent1 = Intent(requireContext(), WebViewActivity::class.java)
            intent1.putExtra("from", "more")
            intent1.putExtra("title", "PVR Care")
            intent1.putExtra("getUrl", Constant.pvrCare)
            startActivity(intent1)
        }

        val text = "<font color=#000000>${"T&C "}</font>" + "<font color=#EDE8E9>  |  </font>" + "<font color=#000000>${"Privacy"}</font>"
        binding?.tvTerm?.text = Html.fromHtml(text)

        // Terms Condition
        binding?.tvTerm?.setOnClickListener {
            val intent1 = Intent(requireContext(), TermsPrivacyActivity::class.java)
            startActivity(intent1)
        }


        //GiftCard
        binding?.logout?.constraintLayout77?.setOnClickListener {
            val intent = Intent(requireContext(), GiftCardActivity::class.java)
            startActivity(intent)
        }

        //Private Screen
        binding?.logout?.constraintLayout79?.setOnClickListener {
            val intent = Intent(requireContext(), PrivateScreeningsActivity::class.java)
            startActivity(intent)
        }

        //ScanQr
        binding?.imageView101?.setOnClickListener {
            val intent = Intent(requireContext(), ScannerActivity::class.java)
            startActivity(intent)
        }

        //Booking Retrieval
        binding?.login?.constraintLayout71?.setOnClickListener {
            val intent = Intent(requireContext(), BookingRetrievalActivity::class.java)
            startActivity(intent)
        }

        //LogOut
        binding?.tvSignOut?.setOnClickListener {
            logOut()
        }

        //Contact Us
        binding?.tvContact?.setOnClickListener {
            val intent = Intent(requireContext(), ContactUsActivity::class.java)
            startActivity(intent)
        }

        //e Voucher
        binding?.logout?.constraintLayout172?.setOnClickListener {
            val intent = Intent(requireContext(), EVoucherActivity::class.java)
            startActivity(intent)
        }


        //Login
        binding?.tvLoginButton?.textView5?.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

        //MovieAlert
        binding?.login?.constraintLayout72?.setOnClickListener {
            val intent = Intent(requireContext(), WatchListActivity::class.java)
            startActivity(intent)
        }

        //preference
        binding?.login?.constraintLayout73?.setOnClickListener {
// Hit Event
            try {
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "My Preferences")
//                bundle.putString("var_experiences_banner", comingSoonItem.name)
                GoogleAnalytics.hitEvent(requireActivity(), "my_pvr", bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

            val intent = Intent(requireContext(), PreferenceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createQr() {
        qrCode = Constant().getLoyaltyQr(preferences.geMobileNumber(), "180x180")
    }

    //Privilege Details
    private fun oPenDialogQR() {
        val dialogQR = Dialog(requireActivity())
        dialogQR.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogQR.setCancelable(false)
        dialogQR.setContentView(R.layout.activity_privilege_details)
        dialogQR.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialogQR.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialogQR.window?.setGravity(Gravity.CENTER)
        dialogQR.setTitle("")
        val pointsPcTextView = dialogQR.findViewById<TextView>(R.id.points_txt)
        val voucher = dialogQR.findViewById<TextView>(R.id.vouchers_txt_)
        val tvUserName: TextView = dialogQR.findViewById<View>(R.id.TVusername) as TextView
        val ivImage = dialogQR.findViewById<View>(R.id.ivImage) as ImageView
        val tvCross = dialogQR.findViewById<View>(R.id.tvCross) as ImageView

        Glide.with(requireActivity()).load(qrCode).into(ivImage)

        tvUserName.text = preferences.getUserName()

        pointsPcTextView.text = Constant.PRIVILEGEPOINT

        voucher.text = Constant.PRIVILEGEVOUCHER

        tvCross.setOnClickListener {
            dialogQR.dismiss()
        }

        dialogQR.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialogQR.dismiss()
            }
            true
        }

        dialogQR.show()
    }

    private fun logOut() {
        val dialog = OptionDialog(requireActivity(),
            R.mipmap.ic_launcher_foreground,
            R.string.app_name,
            getString(R.string.logout),
            positiveBtnText = R.string.yes,
            negativeBtnText = R.string.no,
            positiveClick = {
                preferences.clearData(requireActivity())

            },
            negativeClick = {})
        dialog.show()
    }


    @Throws(Exception::class)
    fun getHash(text: String): String {
        val mdText = MessageDigest.getInstance("SHA-512")
        val byteData = mdText.digest(text.toByteArray())
        val sb = StringBuffer()
        for (i in byteData.indices) {
            sb.append(((byteData[i] and 0xff) + 0x100).toString(16).substring(1))
        }
        return sb.toString()
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

    private fun whatsappOptStatus() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                    }
                }
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
            }
        }

    }

    private fun retrieveData(data: Boolean) {
        binding?.checkState?.isChecked = data
        binding?.checkState?.setOnCheckedChangeListener { _, isChecked ->
            timeStamp = (System.currentTimeMillis() / 1000).toString()
            if (isChecked) {
                authViewModel.whatsappOptIn(
                    preferences.getUserId(),
                    preferences.getToken().toString(),
                    timeStamp,
                    getHash(preferences.getUserId() + "|" + preferences.getToken() + "|" + "opt-in" + "|" + timeStamp)
                )
            } else {
                authViewModel.whatsappOptOut(
                    preferences.getUserId(),
                    preferences.getToken().toString(),
                    timeStamp,
                    getHash(preferences.getUserId() + "|" + preferences.getToken() + "|" + "opt-out" + "|" + timeStamp)
                )
            }
        }
    }


    //Profile Response
    private fun profileResponse() {
        authViewModel.userProfileLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        loader?.dismiss()
                        retrieveProfileData(it.data.output)
                    } else {

                    }
                }
                is NetworkResult.Error -> {
                    loader?.dismiss()

                }
                is NetworkResult.Loading -> {
                    println("loadingHome--->")
                    loader = LoaderDialog(R.string.pleaseWait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun retrieveProfileData(output: ProfileResponse.Output) {
        ProfileResponseConst = output
        binding?.profileDetails?.textView206?.text = output.cd
        binding?.profileDetails?.textView208?.text = output.percentage.toString() + "%"

        binding?.profileDetails?.textView209?.text = output.msg
        binding?.profileDetails?.progressBar?.progress = output.percentage

        //profile Complete
        binding?.profileDetails?.completeProfile?.setOnClickListener {
            openProfilePopup(output)
        }
    }


    private fun openProfilePopup(output: ProfileResponse.Output) {
        val dialog = BottomSheetDialog(requireActivity(), R.style.NoBackgroundDialogTheme)
        val behavior: BottomSheetBehavior<*> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.profile_popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayRectangle = Rect()
        dialog.window?.decorView?.getWindowVisibleDisplayFrame(displayRectangle)
        behavior.peekHeight = (displayRectangle.height() * 0.9f).toInt()
        behavior.maxHeight = (displayRectangle.height() * 0.9f).toInt()
        val mainView = dialog.findViewById<View>(R.id.mainView) as ConstraintLayout?
        mainView?.maxHeight = (displayRectangle.height() * 0.6f).toInt()
        val progressBar2 = dialog.findViewById<ProgressBar>(R.id.progressBar2)
        val profileRecycler = dialog.findViewById<RecyclerView>(R.id.profileList)

        val title = dialog.findViewById<TextView>(R.id.voucherText)
        //title
        title?.text=output.msg
        progressBar2?.progress = output.percentage
        val layoutManager = LinearLayoutManager(context)
        profileRecycler?.layoutManager = layoutManager
        val recyclerAdapter = ProfileCompleteAdapter(requireActivity(), profileList, output.params)
        profileRecycler?.adapter = recyclerAdapter
        dialog.show()
    }
}