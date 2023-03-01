package com.net.pvr.ui.dailogs

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.net.pvr.R
import com.net.pvr.databinding.DialogLoaderBinding

class LoaderDialog(private val message: Int) : DialogFragment() {
    private var binding: DialogLoaderBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setStyle(STYLE_NORMAL, R.style.full_screen_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.color.translucent1);
        val displayRectangle = Rect()
        dialog!!.window!!.decorView
            .getWindowVisibleDisplayFrame(displayRectangle)
        dialog?.window?.setLayout(displayRectangle.width(), displayRectangle.height());

        isCancelable = false
        return DialogLoaderBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}