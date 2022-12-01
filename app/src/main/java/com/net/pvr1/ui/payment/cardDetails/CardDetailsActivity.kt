package com.net.pvr1.ui.payment.cardDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityCardDetailsBinding
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.payment.viewModel.PaymentViewModel
import com.net.pvr1.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CardDetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityCardDetailsBinding? = null
    private val authViewModel: PaymentViewModel by viewModels()
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        movedNext()
    }

    private fun movedNext() {
        //Back
        binding?.include27?.imageView58?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        //Title
        binding?.include27?.textView108?.text = getString(R.string.pay_via_card)
        //mm//year
        var lastInput = ""

        binding?.monthYear?.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(
                p0: CharSequence?,
                start: Int,
                removed: Int,
                added: Int
            ) {
                val input = p0.toString()
                val formatter = SimpleDateFormat("MM/YY", Locale.GERMANY)
                val expiryDateDate = Calendar.getInstance()
                try {
                    expiryDateDate.time = formatter.parse(input)
                } catch (e: ParseException) {
                    if (p0?.length == 2 && !lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            binding?.monthYear?.setText(
                                binding?.monthYear?.text.toString() + "/"
                            )
                            binding?.monthYear?.setSelection(
                                3
                            )
                        }
                    } else if (p0?.length == 2 && lastInput.endsWith("/")) {
                        val month = Integer.parseInt(input)
                        if (month <= 12) {
                            binding?.monthYear?.setText(
                                binding?.monthYear?.text.toString()
                                    .substring(0, 1)
                            )
                        }
                    }
                    lastInput =
                        binding?.monthYear?.text.toString()
                    //because not valid so code exits here
                    return
                }
            }
        })
    }
}