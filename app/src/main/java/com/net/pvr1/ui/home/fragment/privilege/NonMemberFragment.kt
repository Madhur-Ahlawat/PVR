package com.net.pvr1.ui.home.fragment.privilege

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentPrivilegeBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity.Companion.getCurrentItem
import com.net.pvr1.ui.home.HomeActivity.Companion.reviewPosition
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeTypeAdapter
import com.net.pvr1.ui.home.fragment.privilege.response.PassportPlanResponse
import com.net.pvr1.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.Constant
import com.net.pvr1.utils.Constant.Companion.onShareClick
import com.net.pvr1.utils.NetworkResult
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
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

        makePageDataToShow()
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
                        binding?.tvEnroll?.text = "Join Now"
                        binding?.privilegeView?.salted?.text = "Salted"
                        binding?.privilegeView?.points?.text =
                            "all tickets, food and beverage purchases"
                        binding?.parrentView?.setBackgroundResource(R.drawable.gradient_loyalty)
                        break
                    }
                } else if (arguments?.getString("type").equals("PP", ignoreCase = true)) {
                    try {
                        if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(i)?.ptype.equals(arguments?.getString("type"))) {
                            reviewPosition = i
                            binding?.parrentView?.setBackgroundResource(R.drawable.gradient_passport)
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
                        binding?.parrentView?.setBackgroundResource(R.drawable.gradient_kotak)
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
                            binding?.parrentView?.setBackgroundResource(R.drawable.gradient_passport)
                        } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.get(reviewPosition)?.ptype.equals("PPP")) {
                            binding?.passportView?.root?.hide()
                            binding?.tvTerms1?.show()
                            binding?.privilegeView?.boxKotak?.show()
                            binding?.privilegeView?.root?.show()
                            binding?.tvEnroll?.text = "Apply Now"
                            binding?.privilegeView?.salted?.text = "Salted on your first visit after joining"
                            binding?.privilegeView?.points?.text = "on Tickets & Food items"
                            binding?.parrentView?.setBackgroundResource(R.drawable.gradient_kotak)
                        } else {
                            binding?.tvEnroll?.text = "Join Now"
                            binding?.tvTerms1?.show()
                            binding?.privilegeView?.tvTerms?.hide()
                            binding?.privilegeView?.boxKotak?.hide()
                            binding?.passportView?.root?.hide()
                            binding?.privilegeView?.root?.show()
                            binding?.privilegeView?.salted?.text = "Salted"
                            binding?.privilegeView?.points?.text = "all tickets, food and beverage purchases"
                            binding?.parrentView?.setBackgroundResource(R.drawable.gradient_loyalty)
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
                    } else {
                        val dialog = OptionDialog(requireActivity(),
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

    private fun retrieveData(output: PassportPlanResponse.Output) {
        scheme_id = output.scheme[0].schemeid
        scheme_price = output.scheme[0].price
        binding?.passportView?.priceNewText?.text = ""

        val text =
            "<font color=#ffffff><b>" + "₹" + scheme_price.toFloat() / 100.0 + "</b> </font><font color=#ffffff>" + " / " + output.scheme[0].duration + "</font>"

        binding?.passportView?.priceNewText?.text = Html.fromHtml(text)

        binding?.passportView?.textBelow?.setText(output.scheme[0].text)
    }


}