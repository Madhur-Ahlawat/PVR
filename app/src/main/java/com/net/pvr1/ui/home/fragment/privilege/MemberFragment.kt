package com.net.pvr1.ui.home.fragment.privilege

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.text.Html
import android.text.TextUtils
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPrivilegeLogInBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.HomeActivity.Companion.getCurrentItem
import com.net.pvr1.ui.home.HomeActivity.Companion.review_position
import com.net.pvr1.ui.home.fragment.privilege.adapter.PrivilegeCardAdapter
import com.net.pvr1.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeCardData
import com.net.pvr1.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr1.utils.*
import com.net.pvr1.utils.Constant.SharedPreference.Companion.ACTIVE
import com.net.pvr1.utils.Constant.SharedPreference.Companion.LOYALITY_STATUS
import com.net.pvr1.utils.Constant.SharedPreference.Companion.SUBSCRIPTION_STATUS
import com.net.pvr1.utils.Constant.SharedPreference.Companion.SUBS_OPEN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MemberFragment : Fragment(), PrivilegeCardAdapter.RecycleViewItemClickListener {
    private var binding: ActivityPrivilegeLogInBinding? = null
    private var loader: LoaderDialog? = null

    @Inject
    lateinit var preferences: PreferenceManager
    private val authViewModel by activityViewModels<PrivilegeLoginViewModel>()
    private var qrCode:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ActivityPrivilegeLogInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val time = SystemClock.uptimeMillis()

        authViewModel.loyaltyData(
            preferences.getUserId(),
            preferences.getCityName(),
            preferences.getToken().toString(),
            time.toString(),
            Constant.getHash(preferences.getUserId() + "|" + preferences.getToken().toString() + "|" + time.toString())

        )
        createQr()
        getProfileData()
    }

    private fun getProfileData() {
        authViewModel.loyaltyDataLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    if (Constant.status == it.data?.result && Constant.SUCCESS_CODE == it.data.code) {
                        loader?.dismiss()
                        retrieveData(it.data.output)
                    } else {
                        loader?.dismiss()
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
                    loader?.dismiss()
                    val dialog = OptionDialog(requireActivity(),
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
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: LoyaltyDataResponse.Output) {
        try {
            addLoyalty(output)
            updateHeaderView(output)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun addLoyalty(output: LoyaltyDataResponse.Output) {
        if (!TextUtils.isEmpty(output.pt)) {
            val points: Float = output.pt.toFloat()
            var pointsValue = ""
            var percentage = 0f
            var pointsValue1 = 0f
            val ls: String = preferences.getString(LOYALITY_STATUS)
            if (ls.equals("GOLD", ignoreCase = true)) {
                pointsValue = String.format("%.2f", 50 - points)
                pointsValue1 = 50 - points
                percentage = points * 100 / 50
            } else {
                pointsValue = String.format("%.2f", 100 - points)
                pointsValue1 = 100 - points
                percentage = points
                println("percentage===========$percentage")
            }
            if (pointsValue1 < 0) {
                pointsValue = String.format("%.2f", 0f)
                percentage = 0f
            }
            println("percentage===========$percentage")
            binding?.ProgressBAR?.progress = percentage.toInt()
            if (preferences.getString(SUBSCRIPTION_STATUS) == ACTIVE && preferences.getString(
                    SUBS_OPEN
                ) == "true"
            ) {
                binding?.tvPoints?.text = Html.fromHtml("<b>$pointsValue Points Needed</b>")
                binding?.tvPointsMsg?.text = Html.fromHtml("To Unlock Next Voucher")
                binding?.tvPoints?.show()
            } else {
                binding?.tvPoints?.show()
                binding?.tvPoints?.text = Html.fromHtml("<b>$pointsValue Points Needed</b>")
                binding?.tvPointsMsg?.text = Html.fromHtml("To Unlock Next Voucher")
            }
        }
    }

    private fun updateHeaderView(output: LoyaltyDataResponse.Output) {

        val points_data = Constant.PRIVILEGEPOINT
        val cardDataList: ArrayList<PrivilegeCardData> = ArrayList()
        var count = 3
        count =
            if (preferences.getString(SUBS_OPEN) != "true" && preferences.getString(
                    SUBSCRIPTION_STATUS
                ) != ACTIVE
            ) {
                2
            } else {
                3
            }
        for (i in 0 until count) {
            if (i == 0) {
                cardDataList.add(PrivilegeCardData("", "P", false, "", "", points_data, output))
            } else if (i == 1) {
                if (preferences.getString(SUBSCRIPTION_STATUS) == ACTIVE) {
                    cardDataList.add(
                        PrivilegeCardData(
                            java.lang.String.valueOf(output.subscription.can_redeem - output.subscription.redeemed),
                            "PP",
                            false,
                            "",
                            "",
                            points_data,
                            output
                        )
                    )
                } else {
                    if (preferences.getString(SUBS_OPEN) == "true") {
                        cardDataList.add(
                            PrivilegeCardData(
                                Constant.PrivilegeHomeResponseConst?.pinfo?.get(
                                    0
                                )?.pi.toString(), "PP", true, "", "", points_data, output
                            )
                        )
                    } else {
                        if (preferences.getString(LOYALITY_STATUS) == "PPP") {
                            cardDataList.add(PrivilegeCardData(
                                "",
                                "PPP",
                                false,
                                "",
                                "",
                                points_data.toString(),
                                output
                            ))
                        } else {
                            cardDataList.add(PrivilegeCardData(
                                Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(),
                                "PPP",
                                true,
                                "",
                                "",
                                points_data.toString(),
                                output
                            ))
                        }
                    }
                }
            } else {
                if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 2) {
                    if (preferences.getString(LOYALITY_STATUS) == "PPP") {
                        cardDataList.add(PrivilegeCardData("", "PPP", false, "", "", points_data.toString(), output))
                    } else {
                        cardDataList.add(PrivilegeCardData(
                            Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(),
                            "PPP",
                            true,
                            "",
                            "",
                            points_data.toString(),
                            output
                        ))
                    }
                } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 1) {
                    if (preferences.getString(LOYALITY_STATUS) == "PPP") {
                        cardDataList.add(PrivilegeCardData("", "PPP", false, "", "", points_data.toString(), output))
                    } else {
                        cardDataList.add(PrivilegeCardData(
                            Constant.PrivilegeHomeResponseConst?.pinfo?.get(0)?.pi.toString(),
                            "PPP",
                            true,
                            "",
                            "",
                            points_data.toString(),
                            output
                        ))
                    }
                } else if (Constant.PrivilegeHomeResponseConst?.pinfo?.size == 0) {
                    cardDataList.add(PrivilegeCardData("", "PPP", false, "", "", points_data.toString(), output))
                }
            }
        }
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val mSnapHelper = PagerSnapHelper()
            binding?.privilegeCardList?.layoutManager = layoutManager
            val cardAdapter =
                PrivilegeCardAdapter(cardDataList, requireActivity(), preferences, this)
            binding?.privilegeCardList?.adapter = cardAdapter
            mSnapHelper.attachToRecyclerView(binding?.privilegeCardList!!)
            binding?.privilegeCardList?.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        //Dragging
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        review_position = getCurrentItem(binding?.privilegeCardList!!)
                        if (review_position == 0) {
                            if (cardDataList[review_position].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.passportView?.hide()
                                binding?.titleUnlock?.text = "PVR Privilege"
                            } else {
                                binding?.unLockView?.hide()
                                binding?.passportView?.hide()
                                binding?.cardBelowView?.show()
                            }
                        } else if (review_position == 1) {
                            if (cardDataList[review_position].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.passportView?.hide()
                                if (preferences.getString(SUBS_OPEN) == "true") {
                                    binding?.titleUnlock?.text = "PVR Passport"
                                } else {
                                    binding?.titleUnlock?.text = "Kotak Card"
                                }
                            } else {
                                binding?.cardBelowView?.hide()
                                if (cardDataList[review_position].type.equals("PPP")) {
                                    binding?.passportView?.hide()
                                    binding?.unLockView?.hide()
                                    binding?.cardBelowView?.show()
                                } else {
                                    if (preferences.getString(SUBS_OPEN) == "true") {
                                        binding?.passportView?.show()
                                        binding?.unLockView?.hide()
                                        binding?.cardBelowView?.hide()
                                        binding?.bookBtn?.isEnabled = true
                                        binding?.bookBtn?.isClickable = true
                                    } else {
                                        binding?.passportView?.show()
                                        binding?.unLockView?.hide()
                                        binding?.cardBelowView?.hide()
                                        binding?.bookBtn?.isEnabled = false
                                        binding?.bookBtn?.isClickable = false
                                    }
                                }
                            }
                        } else if (review_position == 2) {
                            if (cardDataList[review_position].lock == true) {
                                binding?.unLockView?.show()
                                binding?.cardBelowView?.hide()
                                binding?.passportView?.hide()
                                binding?.titleUnlock?.text = "Kotak Card"
                            } else {
                                binding?.passportView?.hide()
                                binding?.cardBelowView?.show()
                                binding?.unLockView?.hide()
                            }
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
            if (requireArguments().getString("type") != null && !requireArguments().getString("type").equals("", ignoreCase = true)) {
                for (i in cardDataList.indices) {
                    if (cardDataList[i].type.equals(requireArguments().getString("type"))) {
                        if (requireArguments().getString("type").equals("T", ignoreCase = true)) {
                            review_position = 1
                            binding?.passportView?.show()
                            binding?.unLockView?.hide()
                            binding?.cardBelowView?.hide()
                            if (preferences.getString(SUBS_OPEN) != "true") {
                                binding?.bookBtn?.isEnabled = false
                                binding?.bookBtn?.isClickable = false
                            }
                            break
                        } else {
                            review_position = i
                            if (cardDataList[i].type.equals("PP")) {
                                if (cardDataList[i].lock == true) {
                                    binding?.unLockView?.show()
                                    binding?.cardBelowView?.hide()
                                    binding?.passportView?.hide()
                                    binding?.titleUnlock?.text = "PVR Passport"
                                }
                            } else if (requireArguments().getString("type").equals("T", ignoreCase = true)) {
                                review_position = 1
                                binding?.passportView?.show()
                                binding?.unLockView?.hide()
                                binding?.cardBelowView?.hide()
                                if (preferences.getString(SUBS_OPEN) != "true") {
                                    binding?.bookBtn?.isEnabled = false
                                    binding?.bookBtn?.isClickable = false
                                }
                            } else {
                                if (cardDataList[i].lock == true) {
                                    binding?.unLockView?.show()
                                    binding?.cardBelowView?.hide()
                                    binding?.titleUnlock?.text = "Kotak Card"
                                }
                            }
                        }
                        break
                    } else {
                        if (requireArguments().getString("type").equals("T", ignoreCase = true)) {
                            review_position = 1
                            binding?.passportView?.show()
                            binding?.unLockView?.hide()
                            binding?.cardBelowView?.hide()
                            if (preferences.getString(SUBS_OPEN) != "true") {
                                binding?.bookBtn?.isEnabled = false
                                binding?.bookBtn?.isClickable = false
                            }
                        } else {
                            if (requireArguments().getString("type").equals("C", ignoreCase = true)) {
                                binding?.unLockView?.hide()
                                binding?.cardBelowView?.show()
                                binding?.passportView?.hide()
                                review_position = 0
                            } else {
                                review_position = i
                                if (cardDataList[i].type.equals("PP")) {
                                    if (cardDataList[i].lock == true) {
                                        binding?.unLockView?.show()
                                        binding?.cardBelowView?.hide()
                                        binding?.passportView?.hide()
                                        binding?.titleUnlock?.text = "PVR Passport"
                                    }
                                } else {
                                    if (cardDataList[i].lock == true) {
                                        binding?.unLockView?.show()
                                        binding?.cardBelowView?.hide()
                                        binding?.passportView?.hide()
                                        binding?.titleUnlock?.text = "Kotak Card"
                                    }
                                }
                            }
                        }
                    }
                }
                if (preferences.getString(SUBSCRIPTION_STATUS) == ACTIVE && requireArguments().getString("type").equals("C", ignoreCase = true)) {
                    review_position = 1
                    binding?.unLockView?.hide()
                    binding?.cardBelowView?.hide()
                    binding?.passportView?.show()

                    if (preferences.getString(SUBS_OPEN) != "true") {
                        binding?.bookBtn?.isClickable = false
                        binding?.bookBtn?.isEnabled = false
                    }
                }
                binding?.privilegeCardList?.smoothScrollToPosition(review_position)
            }
        }

    override fun onQrClick() {
        oPenDialogQR()
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
        val vochersPcTextView = dialogQR.findViewById<TextView>(R.id.vouchers_txt_)
        val TVusername: TextView = dialogQR.findViewById<View>(R.id.TVusername) as TextView
        val ivImage = dialogQR.findViewById<View>(R.id.ivImage) as ImageView
        val tvCross = dialogQR.findViewById<View>(R.id.tvCross) as ImageView
        Glide.with(requireActivity()).load(qrCode).into(ivImage)
        TVusername.text = preferences.getUserName()
        pointsPcTextView.text = Constant.PRIVILEGEPOINT
        vochersPcTextView.text = Constant.PRIVILEGEVOUCHER
        tvCross.setOnClickListener { dialogQR.dismiss() }
        dialogQR.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialogQR.dismiss()
            }
            true
        }
        dialogQR.show()
    }
}