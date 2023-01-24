package com.net.pvr1.ui.home.fragment.more.privacy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.net.pvr1.R
import com.net.pvr1.databinding.ActivityTermsPrivacyBinding
import com.net.pvr1.di.preference.PreferenceManager
import com.net.pvr1.ui.dailogs.LoaderDialog
import com.net.pvr1.ui.home.fragment.more.adapter.TermsConditionAdapter
import com.net.pvr1.ui.webView.WebViewActivity
import com.net.pvr1.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TermsPrivacyActivity : AppCompatActivity(),
    TermsConditionAdapter.RecycleViewItemClickListener {
    @Inject
    lateinit var preferences: PreferenceManager
    private var binding: ActivityTermsPrivacyBinding? = null
    private var loader: LoaderDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsPrivacyBinding.inflate(layoutInflater, null, false)
        val view = binding?.root
        setContentView(view)

        manageFunction()
    }

    private fun manageFunction() {
        //title
        binding?.toolbar?.textView108?.text = getString(R.string.terms_condition_text)
        movedNext()
        loadData()
    }

    private fun movedNext() {
        binding?.toolbar?.imageView58?.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        val layoutManager = LinearLayoutManager(this)
        binding?.recyclerView54?.layoutManager = layoutManager
        val list: Array<String> = resources.getStringArray(R.array.terms_condition_items)
        val termsAdapter = TermsConditionAdapter(this, list, this)
        binding?.recyclerView54?.adapter = termsAdapter
        binding?.textView44?.text =
            resources.getString(R.string.version) + Constant().getAppVersion(this)

    }

    override fun alsoPlaying(comingSoonItem: String, position: Int) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("from", "more")
        when (position) {
            0 -> {
                intent.putExtra("title", comingSoonItem)
                intent.putExtra("getUrl", Constant.privacy)
            }
            1 -> {
                intent.putExtra("title", comingSoonItem)
                intent.putExtra("getUrl", Constant.termsUse)
            }
            2 -> {
                intent.putExtra("title", comingSoonItem)
                intent.putExtra("getUrl", Constant.termsCondition)
            }
        }
        startActivity(intent)

    }
}