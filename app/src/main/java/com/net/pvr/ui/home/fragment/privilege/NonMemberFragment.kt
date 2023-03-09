package com.net.pvr.ui.home.fragment.privilege

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.net.pvr.R
import com.net.pvr.databinding.FragmentPrivilegeBinding
import com.net.pvr.di.preference.PreferenceManager
import com.net.pvr.ui.home.HomeActivity
import com.net.pvr.ui.home.HomeActivity.Companion.getCurrentItem
import com.net.pvr.ui.home.HomeActivity.Companion.reviewPosition
import com.net.pvr.ui.home.fragment.privilege.adapter.PrivilegeTypeAdapter
import com.net.pvr.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr.ui.login.LoginActivity
import com.net.pvr.ui.webView.WebViewActivity
import com.net.pvr.utils.*
import com.net.pvr.utils.Constant.Companion.onShareClick
import com.net.pvr.utils.ga.GoogleAnalytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NonMemberFragment : Fragment() {
    private var binding: FragmentPrivilegeBinding? = null

    @Inject
    lateinit var preferences: PreferenceManager
    private val authViewModel by activityViewModels<PrivilegeLoginViewModel>()

    companion object {
        var scheme_id = ""
        var scheme_price = "0.0"
        var maxtrycount = 0
        var retrymsg1 = "NO"
        var retrymsg2 = "NO"
        var subPlans = "NO"
        var visits = "0.0"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrivilegeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HomeActivity.backToTop?.hide()
        binding?.shareBtn?.setOnClickListener(View.OnClickListener {
            val newUrl = "https://www.pvrcinemas.com/loyalty/home"
            onShareClick(
                requireContext(),
                """ $newUrl""".trimIndent(),
                "The secret to my movie binge-watching: PVR Passport voucher!\nCheck it out:"
            )
        })

        binding?.passportView?.faqBtn?.setOnClickListener(View.OnClickListener {
            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra("from", "passFaq")
            intent.putExtra("title", "FAQ'S")
            intent.putExtra("getUrl", preferences.getString("FAQ"))
            startActivity(intent)
        })

        val cities: String = preferences.getString(Constant.SharedPreference.pcities)
        val text =
            "<font color=#B3ffffff>Currently Applicable for booking made in </font> <font color=#ffffff><b>" + cities.replaceFirst(
                ",(?=[^,]+$)".toRegex(),
                " & "
            ) + "</b></font>"
        val textone =
            "<font color=#B3ffffff>Use the Passport voucher to pay for<br></br></font> <font color=#ffffff><b>one ticket everyday. </b></font>"
        val text5 =
            "<font color=#B3ffffff>Ticket price </font> <font color=#ffffff><b>limit is INR 400.</b></font><font color=#B3ffffff> If the ticket price is more than INR 400, the difference shall be payable by patron. </font>"
        val text3 =
            "<font color=#B3ffffff>Only </font> <font color=#ffffff><b>one ticket</b> </font><font color=#B3ffffff>is covered under a booking session </font>"
        binding?.passportView?.text1?.text = Html.fromHtml(text)
        binding?.passportView?.text2?.text = Html.fromHtml(textone)
        binding?.passportView?.text4?.text = Html.fromHtml(text5)
        authViewModel.passportPlans(
            preferences.getUserId(),
            preferences.getCityName()
        )
        getPlans()

        binding?.textView378?.setOnClickListener {
            val newUrl = "https://www.pvrcinemas.com/loyalty/home"
            Constant.onShareClick(
                requireActivity(), newUrl, "The secret to my movie binge-watching: PVR Passport voucher!\nCheck it out:"
            )
        }

        binding?.tvEnroll?.setOnClickListener {
            if (binding?.tvTerms1?.isChecked == false) {
                requireContext().toast("Please agree to the terms & conditions.")
            } else {
                val intent4: Intent
                if (!preferences.getIsLogin()) {
                    intent4 = Intent(requireContext(), LoginActivity::class.java)
                    intent4.putExtra("from", Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype)
                    startActivity(intent4)
                } else {
                    if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype == ("P")) {
//
//                            intent4 = Intent(this, EnrollmentActivity::class.java)
//                            intent4.putExtra(
//                                Constant.PCBackStackActivity.OPEN_ACTIVITY_NAME,
//                                Constant.PCBackStackActivity.LOYALITY_NONMEMBER_ACTIVITY
//                            )
//
//                        startActivity(intent4)
                    } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype == ("PP")) {
                        // Hit Event
                        try {
                            val bundle = Bundle()
                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Privilege")

                            GoogleAnalytics.hitEvent(requireActivity(), "passport_join_button", bundle)
                        }catch (e:Exception) {
                            e.printStackTrace()
                        }
                        val intent = Intent(requireContext(), EnrollInPassportActivity::class.java)
                        intent.putExtra("scheme_id", NonMemberActivity.scheme_id)
                        intent.putExtra("scheme_price", NonMemberActivity.scheme_price)
                        startActivity(intent)
                    } else {
                        // Hit Event
                        try {
                            val bundle = Bundle()
                            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Privilege")
                            bundle.putString("var_passport_apply_now", "")

                            GoogleAnalytics.hitEvent(requireActivity(), "kotak_apply_now", bundle)
                        }catch (e:Exception) {
                            e.printStackTrace()
                        }

                        val intent = Intent(requireContext(), WebViewActivity::class.java)
                        intent.putExtra("from", "kotakApply")
                        intent.putExtra("title", "Privilege+")
                        intent.putExtra("getUrl", "https://www.kotak.com/en/personal-banking/cards/debit-cards/pvr-debit-card-redirection-pvr-website.html")
                        startActivity(intent)
                    }
                }
            }
        }

        spannable()

    }

    private fun makePageDataToShow() {
        try {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val cardAdapter = Constant.PrivilegeHomeResponseConst?.pinfo?.let {
                PrivilegeTypeAdapter(
                    it, requireActivity()
                )
            }
            binding?.privilegeCardList?.layoutManager = layoutManager
            binding?.privilegeCardList?.adapter = cardAdapter
            val mSnapHelper = PagerSnapHelper()
            binding?.privilegeCardList?.onFlingListener = null
            mSnapHelper.attachToRecyclerView(binding?.privilegeCardList)
            for (i in Constant.PrivilegeHomeResponseConst?.pinfo?.indices!!) {
                if (arguments?.getString("type").equals("P", ignoreCase = true)) {
                    if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(i)?.ptype.equals(
                            arguments?.getString(
                                "type"
                            )
                        )
                    ) {
                        reviewPosition = i
                        binding?.tvEnroll?.text = "Join for Free"
                        binding?.privilegeView?.salted?.text = "Salted"
                        binding?.privilegeView?.points?.text =
                            "all tickets, food and beverage purchases"
                        binding?.mainView?.setBackgroundResource(R.drawable.gradient_loyalty)
                        break
                    }
                } else if (arguments?.getString("type").equals("PP", ignoreCase = true)) {
                    try {
                        if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(i)?.ptype.equals(arguments?.getString("type"))) {
                            reviewPosition = i
                            binding?.mainView?.setBackgroundResource(R.drawable.gradient_passport)
                            binding?.passportView?.visitCount?.text = visits
                            binding?.passportView?.topText?.text =
                                binding?.passportView?.topText?.text.toString().replace("30".toRegex(), visits)

                            binding?.tvEnroll?.text = "Join for ₹" + scheme_price.toInt() / 100
                            break
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(i)?.ptype.equals(arguments?.getString("type"))) {
                        reviewPosition = i
                        binding?.tvEnroll?.text = "Apply Now"
                        binding?.privilegeView?.salted?.text = "Salted on your first visit after joining"
                        binding?.privilegeView?.points?.text = "on Tickets & Food items"
                        binding?.mainView?.setBackgroundResource(R.drawable.gradient_kotak)
                        break
                    }
                }
            }
            if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype.equals("P")) {
                binding?.passportView?.root?.hide()
                binding?.privilegeView?.root?.show()
            } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype.equals("PP")) {
                binding?.privilegeView?.boxKotak?.hide()
                binding?.passportView?.root?.show()
                binding?.privilegeView?.root?.hide()
            } else {
                binding?.privilegeView?.boxKotak?.show()
                binding?.passportView?.root?.hide()
                binding?.privilegeView?.root?.show()

            }
            binding?.privilegeCardList?.scrollToPosition(reviewPosition)
            binding?.privilegeCardList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        //Dragging
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                        reviewPosition = getCurrentItem(binding?.privilegeCardList!!)
                        println("review_position3--->$reviewPosition")

                        if (reviewPosition == -1) reviewPosition = 0
                        if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype.equals("PP")) {
                            binding?.passportView?.root?.show()
                            binding?.tvTerms1?.show()
                            binding?.tvEnroll?.text = "Join for ₹" + scheme_price.toInt() / 100
                            binding?.privilegeView?.root?.hide()
                            binding?.passportView?.visitCount?.text = visits
                            binding?.passportView?.topText?.text =
                                binding?.passportView?.topText?.text.toString().replace("30".toRegex(), visits)
                            binding?.privilegeView?.boxKotak?.hide()
                            binding?.mainView?.setBackgroundResource(R.drawable.gradient_passport)
                        } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype.equals("PPP")) {
                            binding?.passportView?.root?.hide()
                            binding?.tvTerms1?.show()
                            binding?.privilegeView?.boxKotak?.show()
                            binding?.privilegeView?.root?.show()
                            binding?.tvEnroll?.text = "Apply Now"
                            binding?.privilegeView?.salted?.text = "Salted on your first visit after joining"
                            binding?.privilegeView?.points?.text = "on Tickets & Food items"
                            binding?.mainView?.setBackgroundResource(R.drawable.gradient_kotak)
                        } else {
                            binding?.tvEnroll?.text = "Join for Free"
                            binding?.tvTerms1?.show()
                            binding?.privilegeView?.tvTerms?.hide()
                            binding?.privilegeView?.boxKotak?.hide()
                            binding?.passportView?.root?.hide()
                            binding?.privilegeView?.root?.show()
                            binding?.privilegeView?.salted?.text = "Salted"
                            binding?.privilegeView?.points?.text = "all tickets, food and beverage purchases"
                            binding?.mainView?.setBackgroundResource(R.drawable.gradient_loyalty)
                        }
                        /*
                    Here load the Image to image view with picaso
                 */
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val firstVisibleItem: Int = getCurrentItem(binding?.privilegeCardList!!)
                    /* Log.e ("VisibleItem", String.valueOf(firstVisibleItem));*/
                }
            })
            visits = Constant.PrivilegeHomeResponseConst?.pinfo!![reviewPosition].visits
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getPlans() {
        authViewModel.passportPlanLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        retrieveData(it.data.output)
                        makePageDataToShow()
                    } else {
                        makePageDataToShow()

                    }
                }
                is NetworkResult.Error -> {
                }
                is NetworkResult.Loading -> {
                }
            }
        }

    }

    private fun retrieveData(output: PassportPlanResponse.Output) {
        scheme_id = output.scheme[0].schemeid
        scheme_price = output.scheme[0].price
        subPlans = output.scheme[0].subsplan
        maxtrycount = output.scheme[0].maxtrycount.toInt()
        retrymsg1 = output.scheme[0].retrymsgone
        retrymsg2 = output.scheme[0].retrymsgtwo
        binding?.passportView?.priceNewText?.text = ""

        val text =
            "<font color=#ffffff><b>" + "₹" + scheme_price.toFloat() / 100.0 + "</b> </font><font color=#ffffff>" + " / " + output.scheme[0].duration + "</font>"

        binding?.passportView?.priceNewText?.text = Html.fromHtml(text)

        binding?.passportView?.textBelow?.text = output.scheme[0].text
    }

    private fun spannable() {
        val ss = SpannableString("   I agree to the Terms & Conditions")
        val span2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(requireActivity(), WebViewActivity::class.java)
                intent.putExtra("from", "passFaq")
                intent.putExtra("title", getString(R.string.terms_amp_condition))
                var termsurl = "https://www.pvrcinemas.com/termsandconditions?typeId=3"
                termsurl = when (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype) {
                    ("P") -> {
                        "https://www.pvrcinemas.com/termsandconditions?typeId=3"
                    }
                    ("PP") -> {
                        "https://www.pvrcinemas.com/termsandconditions?typeId=5"
                    }
                    else -> {
                        "https://www.pvrcinemas.com/termsandconditions?typeId=4"
                    }
                }
                intent.putExtra("getUrl", termsurl)
                startActivity(intent)
            }

        }
        ss.setSpan(span2, 18, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.white)),
            18,
            ss.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(UnderlineSpan(), 18, ss.length, 0)
        binding?.tvTerms1?.text = ss
        binding?.tvTerms1?.movementMethod = LinkMovementMethod.getInstance()
    }

}