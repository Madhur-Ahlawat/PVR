package com.net.pvr1.ui.home.fragment.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.net.pvr1.R
import com.net.pvr1.databinding.FragmentMoreBinding
import com.net.pvr1.ui.scanner.ScannerActivity
import com.net.pvr1.utils.hide


class MoreFragment : Fragment() {
    private var binding: FragmentMoreBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().findViewById(R.id.notify) as ImageView).hide()
        (requireActivity().findViewById(R.id.locationBtn) as ImageView).hide()
        (requireActivity().findViewById(R.id.textView2) as TextView).hide()
        (requireActivity().findViewById(R.id.subTitle) as TextView).hide()
        (requireActivity().findViewById(R.id.txtCity) as TextView).hide()
        (requireActivity().findViewById(R.id.include) as ConstraintLayout).hide()
        (requireActivity().findViewById(R.id.constraintLayout55) as ConstraintLayout).hide()
        movedNext()
    }

//Ui ClickAction
    private fun movedNext() {
//
//        //MyBookings
//        binding?.myBookings?.setOnClickListener {
//            val intent = Intent(requireContext(), MyBookingsActivity::class.java)
//            startActivity(intent)
//        }
//        //Merchandise
//        binding?.merchandise?.setOnClickListener {
//            val intent = Intent(requireContext(), WebViewActivity::class.java)
//            intent.putExtra("title", "Return to App")
//            intent.putExtra("from", "more")
//            intent.putExtra("getUrl", Constant.merchandise)
//            startActivity(intent)
//        }
//
//        //Terms And Condition
//        binding?.merchandise?.setOnClickListener {
//            val intent = Intent(requireContext(), WebViewActivity::class.java)
//            intent.putExtra("title", "Return to App")
//            intent.putExtra("from", "more")
//            intent.putExtra("getUrl", Constant.merchandise)
//            startActivity(intent)
//        }
//
//        //Offers
//        binding?.Offer?.setOnClickListener {
//            val intent = Intent(requireContext(), OfferActivity::class.java)
//            startActivity(intent)
//        }
//
//        //GiftCard
//        binding?.giftCardClick?.setOnClickListener {
//            val intent = Intent(requireContext(), GiftCardActivity::class.java)
//            startActivity(intent)
//        }
//
//        //Private Screen
//        binding?.privateScreen?.setOnClickListener {
//            val intent = Intent(requireContext(), PrivateScreeningsActivity::class.java)
//            startActivity(intent)
//        }
//
//        //ScanQr
        binding?.imageView101?.setOnClickListener {
            val intent = Intent(requireContext(), ScannerActivity::class.java)
            startActivity(intent)
        }
    }

}