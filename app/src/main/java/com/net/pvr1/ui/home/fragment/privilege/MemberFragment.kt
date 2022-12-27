package com.net.pvr1.ui.home.fragment.privilege

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityPrivilegeLogInBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.privilege.response.LoyaltyDataResponse
import com.net.pvr1.ui.home.fragment.privilege.viewModel.PrivilegeLoginViewModel
import com.net.pvr1.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MemberFragment : Fragment() {
    private var binding: ActivityPrivilegeLogInBinding? = null
    private var loader: LoaderDialog? = null
    @Inject
    lateinit var preferences: PreferenceManager
    private val authViewModel by activityViewModels<PrivilegeLoginViewModel>()


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
            Constant.HYATT

        )
        getPlans()
    }

    private fun getPlans() {
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
                    loader = LoaderDialog(R.string.pleasewait)
                    loader?.show(requireActivity().supportFragmentManager, null)
                }
            }
        }

    }

    private fun retrieveData(output: LoyaltyDataResponse.Output) {
        try{
            if (output != null) {
               var voucherNewCombineLists = ArrayList<VoucherNewCombineList>()
                var usedVoucherNewCombineLists = ArrayList<VoucherNewCombineList>()
                var unUsedVoucherNewCombineLists = ArrayList<VoucherNewCombineList>()
                if (output.vou != null) {
                    context.runOnUiThread(Runnable {
                        getAllData()
                        Addloyalty()
                    })
                    updateHeaderView(model.output)
                    if (PCApplication.getPreference()
                            .getString(PCConstants.SharedPreference.SUBSCRIPTION_STATUS)
                            .equalsIgnoreCase(ACTIVE)
                        && PCApplication.getPreference().getString(SUBS_OPEN)
                            .equalsIgnoreCase("true")
                    ) {
                        //tvPoints.setText("PVR Passport");
                        // tvCheckUsage.setVisibility(View.VISIBLE);
                        //tvLastHistory.setVisibility(View.GONE);
                        //tvExpiry.setText(tvLastHistory.getText().toString());
                        //llTop.setBackground(context.getResources().getDrawable(R.drawable.priv_user_back));
                        //rlMid.setBackground(context.getResources().getDrawable(R.drawable.redback));
                        //rlMid.setOnClickListener(LoyalityMemberFragmentNew.this);
//                              ProgressBAR.setProgressDrawable(context.getResources().getDrawable(R.drawable.curved_progress_bar_yellow));
                        //tvExpiry.setTextColor(context.getResources().getColor(R.color.white));
//                                tvPoints_data.setTextColor(context.getResources().getColor(R.color.pvr_yellow));
                        ProgressBAR.setMax(model.output.subscription.can_redeem * 100)
                        tv_visit_seekbar.setMax(model.output.subscription.can_redeem * 100)
                        if (PCApplication.getPreference()
                                .getBoolean(PCConstants.SharedPreference.REDEEMED_SET) && model.output.subscription.redeemed !== 0 && PCApplication.getPreference()
                                .getInt(PCConstants.SharedPreference.REDEEMED_COUNT) + 1 === model.output.subscription.redeemed
                        ) {
                            animateProgression(model.output.subscription.redeemed)
                        } else {
                            tv_visit_seekbar.setProgress(model.output.subscription.redeemed * 100)
                            ProgressBAR.setProgress(model.output.subscription.redeemed * 100)
                        }
                        PCApplication.getPreference().putInt(
                            PCConstants.SharedPreference.REDEEMED_COUNT,
                            model.output.subscription.redeemed
                        )
                        PCApplication.getPreference()
                            .putBoolean(PCConstants.SharedPreference.REDEEMED_SET, true)
                        System.out.println("subscription=====================" + model.output.subscription.can_redeem)

                        /*  if (model.output.subscription.redeemed<1)
                                    tvvoucher.setText(model.output.subscription.can_redeem+" vouchers available");
                                else
                                    tvvoucher.setText((model.output.subscription.can_redeem-model.output.subscription.redeemed)+" vouchers available and " +model.output.subscription.redeemed +" used" );
*/

//                                if (model.output.subscription.redeemed < 1) {
//                                    if (model.output.subscription.can_redeem > 1)
//                                        tv_visit.setText(model.output.subscription.can_redeem + " visits available");
//                                    else
//                                        tv_visit.setText(model.output.subscription.can_redeem + " visit available");
//                                } else {
//                                    if (model.output.subscription.can_redeem - model.output.subscription.redeemed > 1)
//                                        tv_visit.setText((model.output.subscription.can_redeem - model.output.subscription.redeemed) + " visits available and " + model.output.subscription.redeemed + " used");
//                                    else
//                                        tv_visit.setText((model.output.subscription.can_redeem - model.output.subscription.redeemed) + " visit available and " + model.output.subscription.redeemed + " used");
//                                }

//                                tv_visit.setVisibility(View.VISIBLE);
//                                tv_visit_seekbar.setVisibility(View.VISIBLE);
                        val new_data: Int = totalVoc + 1
                        totalVoc = totalVoc + 1
                        //
                        totalVocTextView.setText("" + new_data)
                        total_voc_box_in.setText("" + new_data)
                        //
//                                if (totalVoc > 1) {
//                                    tvvoucher.setText(new_data + " vouchers available to redeem");
//                                } else {
//                                    tvvoucher.setText(new_data + " voucher available to redeem");
//                                }
                    } else {
                        tv_visit.setVisibility(View.GONE)
                        tv_visit_seekbar.setVisibility(View.INVISIBLE)
                    }
                    setupViewPager(
                        viewPager,
                        voucherNewCombineLists,
                        unUsedVoucherNewCombineLists,
                        model.output.pt,
                        model.output.ct,
                        model.output.stf,
                        llShimmer,
                        model.output.subscription
                    )
                }
            }

        }catch (e:Exception){

        }
    }


}