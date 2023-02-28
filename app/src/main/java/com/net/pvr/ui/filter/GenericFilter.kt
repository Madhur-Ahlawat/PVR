package com.net.pvr.ui.filter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.net.pvr.R
import com.net.pvr.ui.filter.adapter.GenericFilterAdapter
import com.net.pvr.utils.Constant
import java.util.*


class GenericFilter : GenericFilterAdapter.onFilterItemSelected {
    var show2 = "ALL"
    var show1 = "ALL"
    var start_time = 8
    var end_time = 24
    var seek_progress: CrystalRangeSeekbar? = null
    var left_text: TextView? = null
    var right_text: TextView? = null
    var languagesStr = ""
    var genereStr = ""
    var formatStr = ""
    var specialStr = ""
    var cinemaStr = ""
    var accessabilityStr = ""

    var filters = HashMap<String, ArrayList<String>>()
    var priceStr = ""
    var timeStr = ""
    var isSelected = false
    var filterItemSelected = ""
    var onSelection: onButtonSelected? = null
    var openFlag = false
    fun openFilters(
        context: Context,
        from: String,
        onClickListener: onButtonSelected?,
        filterType: String?,
        filteredSelectedItem: HashMap<String, String>,
        allFilterList: HashMap<String, ArrayList<String>>
    ) {
        selectedFilters = filteredSelectedItem
        filters = allFilterList

        val dialog = BottomSheetDialog(context, R.style.NoBackgroundDialogTheme)
        val behavior: BottomSheetBehavior<*> = dialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.filter_page)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val displayRectangle = Rect()
        dialog.window!!.decorView
            .getWindowVisibleDisplayFrame(displayRectangle)
        val mainView = dialog.findViewById<View>(R.id.mainView) as ConstraintLayout?
        val params = FrameLayout.LayoutParams(
            (displayRectangle.width() * 1f).toInt(),
            (displayRectangle.height() * 0.8f).toInt()
        )
        mainView!!.layoutParams = params
        val adapterGeners: GenericFilterAdapter
        val adapterLanguage: GenericFilterAdapter
        val adapterFormat: GenericFilterAdapter
        var adapterShowTime: GenericFilterAdapter
        var adapterAccessability: GenericFilterAdapter?
        val adapterPriceRange: GenericFilterAdapter
        val cinemaRecyclerFormat: RecyclerView? = dialog.findViewById<View>(R.id.cinemaRecyclerFormat) as RecyclerView?
        val specialRecyclerFormat: RecyclerView? = dialog.findViewById<View>(R.id.specialRecyclerFormat) as RecyclerView?
        val recyclerLanguage: RecyclerView? = dialog.findViewById<View>(R.id.recyclerLanguage) as RecyclerView?
        val recyclerGeners: RecyclerView? = dialog.findViewById<View>(R.id.recyclerGenere) as RecyclerView?
        val recyclerFormat: RecyclerView? = dialog.findViewById<View>(R.id.recyclerFormat) as RecyclerView?
        val recyclerPriceRange: RecyclerView? = dialog.findViewById<View>(R.id.recyclerPrice) as RecyclerView?
        val recyclerShowTIme: RecyclerView? = dialog.findViewById<View>(R.id.recyclerTime) as RecyclerView?
        val recyclerAccessibility: RecyclerView? = dialog.findViewById<View>(R.id.recyclerAccessibility) as RecyclerView?
        val formatT = dialog.findViewById<View>(R.id.formatT) as TextView?
        val langDropImage = dialog.findViewById<View>(R.id.langDropImage) as ImageView?
        val genereDropImage = dialog.findViewById<View>(R.id.genereDropImage) as ImageView?
        val cinemaFormatDropImage =
            dialog.findViewById<View>(R.id.cinemaFormatDropImage) as ImageView?
        val specialFormatDropImage =
            dialog.findViewById<View>(R.id.specialFormatDropImage) as ImageView?
        val formatDropImage = dialog.findViewById<View>(R.id.formatDropImage) as ImageView?
        val priceDropImage = dialog.findViewById<View>(R.id.priceDropImage) as ImageView?
        val timeDropImage = dialog.findViewById<View>(R.id.timeDropImage) as ImageView?
        val accessabilityDropImage =
            dialog.findViewById<View>(R.id.accessabilityDropImage) as ImageView?
        val language_filter_tabs =
            dialog.findViewById<View>(R.id.language_filter_tabs) as LinearLayout?
        val cinemaFormat_filter_tabs =
            dialog.findViewById<View>(R.id.cinemaFormat_filter_tabs) as LinearLayout?
        val specialFormat_filter_tabs =
            dialog.findViewById<View>(R.id.specialFormat_filter_tabs) as LinearLayout?
        val genre_filter_tabs = dialog.findViewById<View>(R.id.genere_filter_tabs) as LinearLayout?
        val format_filter_tabs = dialog.findViewById<View>(R.id.format_filter_tabs) as LinearLayout?
        val price_filter_tabs = dialog.findViewById<View>(R.id.price_filter_tabs) as LinearLayout?
        val time_filter_tabs = dialog.findViewById<View>(R.id.time_filter_tabs) as LinearLayout?
        val accessability_filter_tabs =
            dialog.findViewById<View>(R.id.accessibility_filter_tabs) as LinearLayout?
        val languageFilterLay = dialog.findViewById<View>(R.id.languageFilterLay) as LinearLayout?
        val genereFilterLay = dialog.findViewById<View>(R.id.genereFilterLay) as LinearLayout?
        val cinemaFormatFilterLay =
            dialog.findViewById<View>(R.id.cinemaFormatFilterLay) as LinearLayout?
        val specialFormatFilterLay =
            dialog.findViewById<View>(R.id.specialFormatFilterLay) as LinearLayout?
        val formatFilterLay = dialog.findViewById<View>(R.id.formatFilterLay) as LinearLayout?
        val priceFilterLay = dialog.findViewById<View>(R.id.priceFilterLay) as LinearLayout?
        val timeFilterLay = dialog.findViewById<View>(R.id.timeFilterLay) as LinearLayout?
        val accessibilityFilterLay =
            dialog.findViewById<View>(R.id.accessibilityFilterLay) as LinearLayout?
        timeFilterLay!!.visibility = View.VISIBLE
        seek_progress = dialog.findViewById<View>(R.id.seek_progress1) as CrystalRangeSeekbar?
        left_text = dialog.findViewById<View>(R.id.left_text) as TextView?
        right_text = dialog.findViewById<View>(R.id.right_text) as TextView?
        if (start_time < 10) {
            left_text!!.text = "0$start_time:00 hrs"
        } else {
            left_text!!.text = "$start_time:00 hrs"
        }
        if (end_time < 10) {
            right_text!!.text = "0$end_time:00 hrs"
        } else {
            right_text!!.text = "$end_time:00 hrs"
        }
        seek_progress!!.setGap(1f)
        seek_progress!!.setSteps(1f)
        // set listener
        seek_progress!!.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            start_time = minValue.toString().toInt()
            end_time = maxValue.toString().toInt()
            if (start_time > 8 || end_time < 24) {
                if (minValue.toString().toInt() < 10) {
                    left_text!!.text = "0$minValue:00 hrs"
                    show1 = "$minValue:00"
                } else {
                    left_text!!.text = "$minValue:00 hrs"
                    show1 = "$minValue:00"
                }
                if (maxValue.toString().toInt() < 10) {
                    right_text!!.text = "0$maxValue:00 hrs"
                    show2 = "$maxValue:00"
                } else {
                    right_text!!.text = "$maxValue:00 hrs"
                    show2 = "$maxValue:00"
                }
            }
        }
        setAlreadySelected()
        when (from) {
            "Home" -> {
                priceFilterLay!!.visibility = View.GONE
                timeFilterLay.visibility = View.GONE
                formatFilterLay!!.visibility = View.GONE
                accessibilityFilterLay!!.visibility = View.GONE
                specialFormatFilterLay!!.visibility = View.GONE
                cinemaFormatFilterLay!!.visibility = View.GONE
            }
            "ShowTime" -> {
                formatT!!.text = "Format"
                genereFilterLay!!.visibility = View.GONE
            }
            "ShowTimeT" -> {
                formatT!!.text = "Movie Format"
                genereFilterLay!!.visibility = View.GONE
            }
            "ComingSoon" -> {
                formatFilterLay!!.visibility = View.GONE
                priceFilterLay!!.visibility = View.GONE
                timeFilterLay.visibility = View.GONE
                accessibilityFilterLay!!.visibility = View.GONE
                specialFormatFilterLay!!.visibility = View.GONE
                cinemaFormatFilterLay!!.visibility = View.GONE
            }
            else -> {
                languageFilterLay!!.visibility = View.VISIBLE
                genereFilterLay!!.visibility = View.VISIBLE
                formatFilterLay!!.visibility = View.VISIBLE
                priceFilterLay!!.visibility = View.VISIBLE
                timeFilterLay.visibility = View.VISIBLE
                accessibilityFilterLay!!.visibility = View.VISIBLE
            }
        }
        if (selectedFilters.size > 0) {
            if (type.contains("language")) {
                language_filter_tabs!!.visibility = View.VISIBLE
                langDropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
            if (type.contains("geners")) {
                genre_filter_tabs!!.visibility = View.VISIBLE
                genereDropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
            if (type.contains("format")) {
                format_filter_tabs!!.visibility = View.VISIBLE
                formatDropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
            if (type.contains("time")) {
                time_filter_tabs!!.visibility = View.VISIBLE
                timeDropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
            if (type.contains("accessability")) {
                accessability_filter_tabs!!.visibility = View.VISIBLE
                accessabilityDropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
            if (type.contains("cinemaformat")) {
                cinemaFormat_filter_tabs!!.visibility = View.VISIBLE
                cinemaFormatDropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
            if (type.contains("specialshow")) {
                specialFormat_filter_tabs!!.visibility = View.VISIBLE
                specialFormatDropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
        }
        if (from.equals("Home", ignoreCase = true) || from.equals(
                "ComingSoon",
                ignoreCase = true
            )
        ) {
            language_filter_tabs!!.visibility = View.GONE
            updateUI(language_filter_tabs, langDropImage, context)
        } else if (selectedFilters.containsKey("language")) {
            language_filter_tabs!!.visibility = View.GONE
            updateUI(language_filter_tabs, langDropImage, context)
        } else if (selectedFilters.containsKey("geners")) {
            genre_filter_tabs!!.visibility = View.GONE
            updateUI(genre_filter_tabs, genereDropImage, context)
        } else if (selectedFilters.containsKey("accessability")) {
            accessability_filter_tabs!!.visibility = View.GONE
            updateUI(accessability_filter_tabs, accessabilityDropImage, context)
        } else if (selectedFilters.containsKey("format")) {
            format_filter_tabs!!.visibility = View.GONE
            updateUI(format_filter_tabs, accessabilityDropImage, context)
        } else if (selectedFilters.containsKey("time")) {
            time_filter_tabs!!.visibility = View.GONE
            updateUI(time_filter_tabs, accessabilityDropImage, context)
        } else if (selectedFilters.containsKey("cinemaformat")) {
            cinemaFormat_filter_tabs!!.visibility = View.GONE
            updateUI(cinemaFormat_filter_tabs, accessabilityDropImage, context)
        } else if (selectedFilters.containsKey("specialshow")) {
            specialFormat_filter_tabs!!.visibility = View.GONE
            updateUI(specialFormat_filter_tabs, accessabilityDropImage, context)
        }
        languageFilterLay!!.setOnClickListener { view: View? ->
            updateUI(
                language_filter_tabs,
                langDropImage,
                context
            )
        }
        genereFilterLay!!.setOnClickListener { view: View? ->
            updateUI(
                genre_filter_tabs,
                genereDropImage,
                context
            )
        }
        formatFilterLay!!.setOnClickListener { view: View? ->
            updateUI(
                format_filter_tabs,
                formatDropImage,
                context
            )
        }

        timeFilterLay.setOnClickListener { view: View? ->
            updateUI(
                time_filter_tabs,
                timeDropImage,
                context
            )
        }
        priceFilterLay!!.setOnClickListener { view: View? ->
            updateUI(
                price_filter_tabs,
                priceDropImage,
                context
            )
        }
        accessibilityFilterLay!!.setOnClickListener { view: View? ->
            updateUI(
                accessability_filter_tabs,
                accessabilityDropImage,
                context
            )
        }
        cinemaFormatFilterLay!!.setOnClickListener { view: View? ->
            updateUI(
                cinemaFormat_filter_tabs,
                cinemaFormatDropImage,
                context
            )
        }
        specialFormatFilterLay!!.setOnClickListener { view: View? ->
            updateUI(
                specialFormat_filter_tabs,
                specialFormatDropImage,
                context
            )
        }

        // Setting the layout as Staggered Grid for vertical orientation
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        val languageList = filters[Constant.FilterType.LANG_FILTER]!!
        val generList = filters[Constant.FilterType.GENERE_FILTER]!!
        val formatList = filters[Constant.FilterType.FORMAT_FILTER]!!
        val priceList = filters[Constant.FilterType.PRICE_FILTER]!!
        val showTimeList = filters[Constant.FilterType.SHOWTIME_FILTER]!!
        val accessabilityList = filters[Constant.FilterType.ACCESSABILITY_FILTER]!!
        val cinemaFormatList = filters[Constant.FilterType.CINEMA_FORMAT]!!
        val specialShowList = filters[Constant.FilterType.SPECIAL_SHOW]!!

        if (languageList.size > 0) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            languageFilterLay.visibility = View.VISIBLE
            adapterLanguage =
                GenericFilterAdapter(languageList, context, "language", this, selectedFilters, type)
            recyclerLanguage!!.layoutManager = layoutManager
            recyclerLanguage.adapter = adapterLanguage
        } else {
            languageFilterLay.visibility = View.GONE
        }
        if (generList.size > 0) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            genereFilterLay.visibility = View.VISIBLE
            adapterGeners =
                GenericFilterAdapter(generList, context, "geners", this, selectedFilters, type)
            recyclerGeners!!.layoutManager = layoutManager
            recyclerGeners.adapter = adapterGeners
        } else {
            genereFilterLay.visibility = View.GONE
        }
        if (formatList.size > 0) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            formatFilterLay.visibility = View.VISIBLE
            adapterFormat =
                GenericFilterAdapter(formatList, context, "format", this, selectedFilters, type)
            recyclerFormat!!.layoutManager = layoutManager
            recyclerFormat.adapter = adapterFormat
        } else {
            formatFilterLay.visibility = View.GONE
        }
        if (priceList.size > 0) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            priceFilterLay.visibility = View.VISIBLE
            adapterPriceRange =
                GenericFilterAdapter(priceList, context, "price", this, selectedFilters, type)
            recyclerPriceRange!!.layoutManager = layoutManager
            recyclerPriceRange.adapter = adapterPriceRange
        } else {
            priceFilterLay.visibility = View.GONE
        }

        if (accessabilityList.size > 0) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            accessibilityFilterLay.visibility = View.VISIBLE
            adapterAccessability = GenericFilterAdapter(
                accessabilityList,
                context,
                "accessability",
                this,
                selectedFilters,
                type
            )
            recyclerAccessibility!!.layoutManager = layoutManager
            recyclerAccessibility.adapter = adapterAccessability
        } else {
            accessibilityFilterLay.visibility = View.GONE
        }
        if (cinemaFormatList.size > 0) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            cinemaFormatFilterLay.visibility = View.VISIBLE
            adapterAccessability = GenericFilterAdapter(
                cinemaFormatList,
                context,
                "cinema",
                this,
                selectedFilters,
                type
            )
            cinemaRecyclerFormat!!.layoutManager = layoutManager
            cinemaRecyclerFormat.adapter = adapterAccessability
        } else {
            cinemaFormatFilterLay.visibility = View.GONE
        }
        if (specialShowList.size > 0) {
            val layoutManager = FlexboxLayoutManager(context)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            layoutManager.alignItems = AlignItems.FLEX_START
            specialFormatFilterLay.visibility = View.VISIBLE
            adapterAccessability = GenericFilterAdapter(
                specialShowList,
                context,
                "special",
                this,
                selectedFilters,
                type
            )
            specialRecyclerFormat!!.layoutManager = layoutManager
            specialRecyclerFormat.adapter = adapterAccessability
        } else {
            specialFormatFilterLay.visibility = View.GONE
        }
        val btnApplyFilter = dialog.findViewById<View>(R.id.btnApplyFilter) as TextView?
        val btnReset = dialog.findViewById<View>(R.id.btnReset) as TextView?
        onSelection = onClickListener
        btnApplyFilter!!.setOnClickListener { v: View? ->
            dialog.dismiss()
            if (!type.contains("time")) type.add("time")
            selectedFilters["time"] = "$show1-$show2"
            onSelection?.onApply(
                type,
                selectedFilters,
                isSelected,
                filterItemSelected
            )
        }
        btnReset!!.setOnClickListener { v: View? ->
            dialog.dismiss()
            onSelection!!.onReset()
        }
        if (languageFilterLay.visibility == View.VISIBLE) {
            openFlag = true
            language_filter_tabs!!.visibility = View.GONE
            updateUI(language_filter_tabs, langDropImage, context)
        } else if (cinemaFormatFilterLay.visibility == View.VISIBLE) {
            if (!openFlag) {
                openFlag = true
                cinemaFormat_filter_tabs!!.visibility = View.GONE
                updateUI(cinemaFormat_filter_tabs, cinemaFormatDropImage, context)
            }
        } else if (specialFormatFilterLay.visibility == View.VISIBLE) {
            if (!openFlag) {
                openFlag = true
                specialFormat_filter_tabs!!.visibility = View.GONE
                updateUI(specialFormat_filter_tabs, specialFormatDropImage, context)
            }
        } else if (formatFilterLay.visibility == View.VISIBLE) {
            if (!openFlag) {
                openFlag = true
                format_filter_tabs!!.visibility = View.GONE
                updateUI(format_filter_tabs, formatDropImage, context)
            }
        } else if (priceFilterLay.visibility == View.VISIBLE) {
            if (!openFlag) {
                openFlag = true
                price_filter_tabs!!.visibility = View.GONE
                updateUI(price_filter_tabs, priceDropImage, context)
            }
        } else if (timeFilterLay.visibility == View.VISIBLE) {
            if (!openFlag) {
                openFlag = true
                time_filter_tabs!!.visibility = View.GONE
                updateUI(time_filter_tabs, timeDropImage, context)
            }
        } else if (accessibilityFilterLay.visibility == View.VISIBLE) {
            if (!openFlag) {
                openFlag = true
                accessability_filter_tabs!!.visibility = View.GONE
                updateUI(accessability_filter_tabs, accessabilityDropImage, context)
            }
        }
        dialog.show()
    }


    private fun removeItem(itemSelected: String, languagesStr: String): String {
        return if (languagesStr.contains(", $itemSelected")) {
            languagesStr.replace(", $itemSelected".toRegex(), "")
        } else if (languagesStr.equals(itemSelected, ignoreCase = true)) {
            languagesStr.replace(itemSelected.toRegex(), "")
        } else if (languagesStr.contains(",")) {
            languagesStr.replace("$itemSelected, ".toRegex(), "")
        } else {
            ""
        }
    }

    interface onButtonSelected {
        fun onApply(
            type: ArrayList<String>?,
            name: HashMap<String, String>?,
            isSelected: Boolean,
            filterItemSelected: String?
        )

        fun onReset()
    }

    @SuppressLint("SetTextI18n")
    private fun setAlreadySelected() {
        if (type.size > 0) {
            val containLanguage = type.contains("language")
            if (containLanguage) {
                val index = type.indexOf("language")
                val value = selectedFilters[type[index]]
                if (value != null) {
                    languagesStr = value
                }
            }
            val containGeners = type.contains("geners")
            if (containGeners) {
                val index = type.indexOf("geners")
                val value = selectedFilters[type[index]]
                if (value != null) {
                    genereStr = value
                }
            }
            val containTime = type.contains("time")
            if (containTime) {
                val index = type.indexOf("time")
                val value = selectedFilters[type[index]]
                if (value != null && !value.equals(
                        "",
                        ignoreCase = true
                    ) && !value.contains("ALL")
                ) {
                    timeStr = value
                    println("timeStr--->$timeStr")
                    timeStr.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    left_text!!.text =
                        timeStr.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[0] + " hrs"
                    right_text!!.text = timeStr.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1] + " hrs"
                    //                    seek_progress.setMaxStartValue(Float.parseFloat(timeStr.split("-")[1].replaceAll(":00",""))).apply();
                    seek_progress!!.setMinStartValue(
                        timeStr.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[0].replace(":00".toRegex(), "").toFloat())
                        .setMaxStartValue(
                            timeStr.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()[1].replace(":00".toRegex(), "").toFloat()).apply()
                }
            }
            val containFormat = type.contains("format")
            if (containFormat) {
                val index = type.indexOf("format")
                val value = selectedFilters[type[index]]
                if (value != null) {
                    formatStr = value
                }
            }
            val containCinemaFormat = type.contains("cinema")
            if (containCinemaFormat) {
                val index = type.indexOf("cinema")
                val value = selectedFilters[type[index]]
                if (value != null) {
                    cinemaStr = value
                }
            }
            val containSpecialFormat = type.contains("special")
            if (containSpecialFormat) {
                val index = type.indexOf("special")
                val value = selectedFilters[type[index]]
                if (value != null) {
                    specialStr = value
                }
            }
            val containPrice = type.contains("price")
            if (containPrice) {
                val index = type.indexOf("price")
                val value = selectedFilters[type[index]]
                if (value != null) {
                    priceStr = value
                }
            }
            val containAccessability = type.contains("accessability")
            if (containAccessability) {
                val index = type.indexOf("accessability")
                val value = selectedFilters[type[index]]
                if (value != null) {
                    accessabilityStr = value
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun updateFilters(type: String, data: String) {
        if (Objects.requireNonNull(selectedFilters[type])?.contains(
                data.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0] + ",")!!
        ) {
            selectedFilters.replace(type, selectedFilters[type]!!
                .uppercase().replace((data.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].uppercase() + ",").toRegex(), "")
            )
        } else {
            selectedFilters.replace(type, selectedFilters[type]!!
                .uppercase().replace(data.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].uppercase().toRegex(), "")
            )
            //System.out.println("languagesStrNewc--->"+selectedFilters+"----"+data.split("-")[0]+"---"+type);
        }
    }

    companion object {
        var type = ArrayList<String>()
        var selectedFilters = HashMap<String, String>()
        private fun updateUI(
            layout_filter_tabs: LinearLayout?,
            dropImage: ImageView?,
            context: Context
        ) {
            if (layout_filter_tabs!!.visibility == View.VISIBLE) {
                layout_filter_tabs.visibility = View.GONE
                dropImage!!.background =
                    ContextCompat.getDrawable(context, R.drawable.arrow_down)
            } else {
                layout_filter_tabs.visibility = View.VISIBLE
                dropImage!!.background = ContextCompat.getDrawable(context, R.drawable.arrow_up)
            }
        }
    }

    override fun onFilterItemClick(
        position: Int,
        typeStr: String?,
        itemSelected: String?,
        selectedValue: Boolean
    ) {
        isSelected = selectedValue
        when (typeStr) {
            "language" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                languagesStr = if (languagesStr.equals("", ignoreCase = true)) ({
                    itemSelected
                }).toString() else {
                    if (languagesStr.contains(itemSelected.toString())) {
                        removeItem(itemSelected.toString(), languagesStr)
                    } else {
                        "$languagesStr, $itemSelected"
                    }
                }
                selectedFilters[typeStr] = languagesStr
            }
            "geners" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                genereStr = if (genereStr.equals("", ignoreCase = true)) ({
                    itemSelected
                }).toString() else {
                    if (genereStr.contains(itemSelected.toString())) {
                        removeItem(itemSelected.toString(), genereStr)
                    } else {
                        "$genereStr, $itemSelected"
                    }
                }
                selectedFilters[typeStr] = genereStr
            }
            "format" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                formatStr = if (formatStr.equals("", ignoreCase = true)) ({
                    itemSelected
                }).toString() else {
                    if (formatStr.contains(itemSelected.toString())) {
                        removeItem(itemSelected.toString(), formatStr)
                    } else {
                        "$formatStr, $itemSelected"
                    }
                }
                selectedFilters[typeStr] = formatStr
            }
            "cinema" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                println("typeStr--->$typeStr----$cinemaStr")
                cinemaStr = if (cinemaStr.equals("", ignoreCase = true)) ({
                    itemSelected
                }).toString() else {
                    if (cinemaStr.contains(itemSelected.toString())) {
                        removeItem(itemSelected.toString(), cinemaStr)
                    } else {
                        "$cinemaStr, $itemSelected"
                    }
                }
                selectedFilters[typeStr] = cinemaStr
            }
            "special" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                specialStr = if (specialStr.equals("", ignoreCase = true)) ({
                    itemSelected
                }).toString() else {
                    if (specialStr.contains(itemSelected.toString())) {
                        removeItem(itemSelected.toString(), specialStr)
                    } else {
                        "$specialStr, $itemSelected"
                    }
                }
                selectedFilters[typeStr] = specialStr
            }
            "price" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                priceStr = if (priceStr === itemSelected) {
                    ""
                } else ({
                    itemSelected
                }).toString()
                selectedFilters[typeStr] = priceStr
            }
            "time" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                timeStr = itemSelected.toString()
                selectedFilters[typeStr] = timeStr
            }
            "accessability" -> {
                if (!type.contains(typeStr)) type.add(typeStr)
                accessabilityStr = if (accessabilityStr.equals("", ignoreCase = true)) ({
                    itemSelected
                }).toString() else {
                    if (accessabilityStr.contains(itemSelected.toString())) {
                        removeItem(itemSelected.toString(), accessabilityStr)
                    } else {
                        "$accessabilityStr, $itemSelected"
                    }
                }
                selectedFilters[typeStr] = accessabilityStr
            }
        }
    }
}
