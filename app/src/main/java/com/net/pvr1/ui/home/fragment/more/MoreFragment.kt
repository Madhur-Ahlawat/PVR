package com.net.pvr1.ui.home.fragment.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentMoreBinding
import com.net.pvr1.ui.contactUs.ContactUsActivity
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.giftCard.GiftCardActivity
import com.net.pvr1.ui.home.fragment.more.bookingRetrieval.BookingRetrievalActivity
import com.net.pvr1.ui.login.LoginActivity
import com.net.pvr1.ui.myBookings.MyBookingsActivity
import com.net.pvr1.ui.offer.OfferActivity
import com.net.pvr1.ui.privateScreenings.PrivateScreeningsActivity
import com.net.pvr1.ui.profile.userDetails.ProfileActivity
import com.net.pvr1.ui.scanner.ScannerActivity
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.PreferenceManager
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment() {
    private var binding: FragmentMoreBinding? = null

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
        (requireActivity().findViewById(R.id.include) as ConstraintLayout).hide()
        preferences = PreferenceManager(requireActivity())
        //SetName
        binding?.profileDetails?.textView205?.text = preferences.getUserName()
        movedNext()
    }

    //Ui ClickAction
    private fun movedNext() {

        //Account
        binding?.profileDetails?.textView206?.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

//        //MyBookings
        binding?.login?.constraintLayout70?.setOnClickListener {
            val intent = Intent(requireContext(), MyBookingsActivity::class.java)
            startActivity(intent)
        }
//        //Merchandise
        binding?.logout?.constraintLayout79?.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra("title", "Return to App")
            intent.putExtra("from", "more")
            intent.putExtra("getUrl", Constant.merchandise)
            startActivity(intent)
        }

//        //Offers
        binding?.logout?.constraintLayout78?.setOnClickListener {
            val intent = Intent(requireContext(), OfferActivity::class.java)
            startActivity(intent)
        }

//        //GiftCard
        binding?.logout?.constraintLayout77?.setOnClickListener {
            val intent = Intent(requireContext(), GiftCardActivity::class.java)
            startActivity(intent)
        }
//
//        //Private Screen
        binding?.logout?.constraintLayout79?.setOnClickListener {
            val intent = Intent(requireContext(), PrivateScreeningsActivity::class.java)
            startActivity(intent)
        }
//        //ScanQr
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

        //Manage ui
        binding?.tvLoginButton?.textView5?.text= getString(R.string.login)

        binding?.tvLoginButton?.textView5?.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
        println("loginStatus--->${preferences.getIsLogin()}")
        if (preferences.getIsLogin()) {
            binding?.tvSignOut?.show()
            binding?.profileLinear?.show()
            binding?.termsContainer?.show()
            binding?.loginBt?.hide()
        } else {
            binding?.profileLinear?.hide()
            binding?.termsContainer?.hide()
            binding?.loginUi?.hide()
            binding?.tvSignOut?.hide()
            binding?.loginBt?.show()
            binding?.tvLoginButton?.textView5?.text= getString(R.string.login)
            binding?.tvLoginButton?.textView5?.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun logOut() {
        preferences.clearData()
        val dialog = OptionDialog(requireActivity(),
            R.mipmap.ic_launcher_foreground,
            R.string.app_name,
            getString(R.string.logout),
            positiveBtnText = R.string.yes,
            negativeBtnText = R.string.no,
            positiveClick = {
                preferences.clearData()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            },
            negativeClick = {})
        dialog.show()
    }

}