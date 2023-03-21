package com.net.pvr.ui.filter;


import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.net.pvr.R;
import com.net.pvr.ui.filter.adapter.GenericFilterAdapter;
import com.net.pvr.ui.home.fragment.home.HomeFragment;
import com.net.pvr.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class GenericFilterHome implements GenericFilterAdapter.onFilterItemSelected {
    public String show2 = "ALL";
    public String show1 = "ALL";
    int start_time = 8;
    int end_time = 24;
    CrystalRangeSeekbar seek_progress = null;
    TextView left_text;
    TextView right_text;
    String languagesStr = "";
    String genereStr = "";
    String formatStr = "";
    String specialStr = "";
    String cinemaStr = "";
    String accessabilityStr = "";
    TextView showCount;
    HashMap<String, ArrayList<String>> allFilterList = new HashMap<>();
    String priceStr = "";
    String timeStr = "";
    boolean isSelected = false;
    String filterItemSelected = "";
    static ArrayList<String> type = new ArrayList<>();
    static HashMap<String, String> selectedFilters = new HashMap<>();

    static ArrayList<String> filterValue = new ArrayList<>();

    public static Map<String , ArrayList<String>> filterStrings = new HashMap<>();

    onButtonSelected onSelection;


    public HashMap<String, ArrayList<String>> getFilters() {
//        HashMap<String, ArrayList<String>> filterPoints = new HashMap<>();
//        filterPoints.put(PCConstants.FilterType.LANG_FILTER, new ArrayList<>(Arrays.asList(new String[]{"English", "Kannada", "Hindi", "Punjabi", "Tamil", "Telugu"})));
//        filterPoints.put(PCConstants.FilterType.GENERE_FILTER, new ArrayList<>(Arrays.asList(new String[]{"Drama", "Action", "Comedy", "Thriller", "Romance", "Adventure"})));
//        filterPoints.put(PCConstants.FilterType.FORMAT_FILTER, new ArrayList<>(Arrays.asList(new String[]{"3D", "IMAX", "4DX", "Playhouse", "GOLD", "ONYX"})));
//        filterPoints.put(PCConstants.FilterType.ACCESSABILITY_FILTER, new ArrayList<>(Arrays.asList(new String[]{"Subtitles", "Wheelchair Friendly"})));
//        filterPoints.put(PCConstants.FilterType.PRICE_FILTER, new ArrayList<>(Arrays.asList(new String[]{"Below ₹300", "₹301 - 500", "₹501 - 1000", "₹1001 - 1500", "Above ₹1500"})));
//        filterPoints.put(PCConstants.FilterType.SHOWTIME_FILTER, new ArrayList<>(Arrays.asList(new String[]{"Morning (6:00am-11:59am)", "Afternoon (12:00am-3:59pm)", "Evening (4:00pm-7:59pm)", "Night (8:00pm-11:59pm)"})));


        return allFilterList;
    }

    public void openFilters(Context context, String from, onButtonSelected onClickListener, String filterType, HashMap<String, String> filteredSelectedItem, HashMap<String, ArrayList<String>> allFilterList) {

        selectedFilters = filteredSelectedItem;
        this.allFilterList = allFilterList;

//        type = new ArrayList<>();
        final BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.NoBackgroundDialogTheme);
        BottomSheetBehavior behavior = dialog.getBehavior();
        behavior.setState(STATE_EXPANDED);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_page);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Rect displayRectangle = new Rect();
        dialog.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(displayRectangle);
        ConstraintLayout mainView = dialog.findViewById(R.id.mainView);
        showCount = (TextView) dialog.findViewById(R.id.showCount);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (displayRectangle.width() * 1f),(int) (displayRectangle.height() * 0.8f));
//        mainView.setLayoutParams(params);
        GenericFilterAdapter adapterGeners, adapterLanguage, adapterFormat, adapterShowTime, adapterAccessability, adapterPriceRange;
        RecyclerView recyclerGeners, recyclerLanguage, recyclerFormat, recyclerShowTIme, recyclerAccessibility, recyclerPriceRange, cinemaRecyclerFormat, specialRecyclerFormat;

        cinemaRecyclerFormat = dialog.findViewById(R.id.cinemaRecyclerFormat);
        specialRecyclerFormat = dialog.findViewById(R.id.specialRecyclerFormat);
        recyclerLanguage = dialog.findViewById(R.id.recyclerLanguage);
        recyclerGeners = dialog.findViewById(R.id.recyclerGenere);
        recyclerFormat = dialog.findViewById(R.id.recyclerFormat);
        recyclerPriceRange = dialog.findViewById(R.id.recyclerPrice);
        recyclerShowTIme = dialog.findViewById(R.id.recyclerTime);
        recyclerAccessibility = dialog.findViewById(R.id.recyclerAccessibility);

        TextView formatT = dialog.findViewById(R.id.formatT);
        ImageView langDropImage = dialog.findViewById(R.id.langDropImage);
        ImageView genereDropImage = dialog.findViewById(R.id.genereDropImage);
        ImageView cinemaFormatDropImage = dialog.findViewById(R.id.cinemaFormatDropImage);
        ImageView specialFormatDropImage = dialog.findViewById(R.id.specialFormatDropImage);
        ImageView formatDropImage = dialog.findViewById(R.id.formatDropImage);
        ImageView priceDropImage = dialog.findViewById(R.id.priceDropImage);
        ImageView timeDropImage = dialog.findViewById(R.id.timeDropImage);
        ImageView accessabilityDropImage = dialog.findViewById(R.id.accessabilityDropImage);

        LinearLayout language_filter_tabs = dialog.findViewById(R.id.language_filter_tabs);
        LinearLayout cinemaFormat_filter_tabs = dialog.findViewById(R.id.cinemaFormat_filter_tabs);
        LinearLayout specialFormat_filter_tabs = dialog.findViewById(R.id.specialFormat_filter_tabs);
        LinearLayout genre_filter_tabs = dialog.findViewById(R.id.genere_filter_tabs);
        LinearLayout format_filter_tabs = dialog.findViewById(R.id.format_filter_tabs);
        LinearLayout price_filter_tabs = dialog.findViewById(R.id.price_filter_tabs);
        LinearLayout time_filter_tabs = dialog.findViewById(R.id.time_filter_tabs);
        LinearLayout accessability_filter_tabs = dialog.findViewById(R.id.accessibility_filter_tabs);

        LinearLayout languageFilterLay = dialog.findViewById(R.id.languageFilterLay);
        LinearLayout genereFilterLay = dialog.findViewById(R.id.genereFilterLay);
        LinearLayout cinemaFormatFilterLay = dialog.findViewById(R.id.cinemaFormatFilterLay);
        LinearLayout specialFormatFilterLay = dialog.findViewById(R.id.specialFormatFilterLay);
        LinearLayout formatFilterLay = dialog.findViewById(R.id.formatFilterLay);
        LinearLayout priceFilterLay = dialog.findViewById(R.id.priceFilterLay);
        LinearLayout timeFilterLay = dialog.findViewById(R.id.timeFilterLay);
        LinearLayout accessibilityFilterLay = dialog.findViewById(R.id.accessibilityFilterLay);

        seek_progress = dialog.findViewById(R.id.seek_progress1);
        left_text = dialog.findViewById(R.id.left_text);
        right_text = dialog.findViewById(R.id.right_text);
        if (start_time < 10) {
            left_text.setText("0" + start_time + ":00 hrs");
        } else {
            left_text.setText(start_time + ":00 hrs");

        }

        if (end_time < 10) {
            right_text.setText("0" + end_time + ":00 hrs");
        } else {
            right_text.setText(end_time + ":00 hrs");
        }

        seek_progress.setGap(1);
        seek_progress.setSteps(1);
// set listener
        seek_progress.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                start_time = Integer.parseInt(String.valueOf(minValue));
                end_time = Integer.parseInt(String.valueOf(maxValue));

                if (start_time > 8 || end_time < 24) {
                    if (Integer.parseInt(String.valueOf(minValue)) < 10) {
                        left_text.setText("0" + minValue + ":00 hrs");
                        show1 = minValue + ":00";
                    } else {
                        left_text.setText(minValue + ":00 hrs");
                        show1 = minValue + ":00";
                    }
                    if (Integer.parseInt(String.valueOf(maxValue)) < 10) {
                        right_text.setText("0" + maxValue + ":00 hrs");
                        show2 = maxValue + ":00";
                    } else {
                        right_text.setText(maxValue + ":00 hrs");
                        show2 = maxValue + ":00";
                    }
                }

            }
        });
        setAlreadySelected();
        switch (from) {
            case "Home":
                priceFilterLay.setVisibility(View.GONE);
                timeFilterLay.setVisibility(View.GONE);
                formatFilterLay.setVisibility(View.GONE);
                accessibilityFilterLay.setVisibility(View.GONE);
                specialFormatFilterLay.setVisibility(View.GONE);
                cinemaFormatFilterLay.setVisibility(View.GONE);
                break;

            case "ShowTime":
                formatT.setText("Format");
                genereFilterLay.setVisibility(View.GONE);
                break;
            case "ShowTimeT":
                formatT.setText("Movie Format");
                genereFilterLay.setVisibility(View.GONE);
                break;

            case "ComingSoon":
                formatFilterLay.setVisibility(View.GONE);
                priceFilterLay.setVisibility(View.GONE);
                timeFilterLay.setVisibility(View.GONE);
                accessibilityFilterLay.setVisibility(View.GONE);
                specialFormatFilterLay.setVisibility(View.GONE);
                cinemaFormatFilterLay.setVisibility(View.GONE);
                break;

            default:
                languageFilterLay.setVisibility(View.VISIBLE);
                genereFilterLay.setVisibility(View.VISIBLE);
                formatFilterLay.setVisibility(View.VISIBLE);
                priceFilterLay.setVisibility(View.VISIBLE);
                timeFilterLay.setVisibility(View.VISIBLE);
                accessibilityFilterLay.setVisibility(View.VISIBLE);
        }
        if (selectedFilters.size() > 0) {
            if (type.contains("language")) {
                language_filter_tabs.setVisibility(View.VISIBLE);
                langDropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            }
            if (type.contains("geners")) {
                genre_filter_tabs.setVisibility(View.VISIBLE);
                genereDropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            }
            if (type.contains("format")) {
                format_filter_tabs.setVisibility(View.VISIBLE);
                formatDropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            }
            if (type.contains("time")) {
                time_filter_tabs.setVisibility(View.VISIBLE);
                timeDropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            }
            if (type.contains("accessability")) {
                accessability_filter_tabs.setVisibility(View.VISIBLE);
                accessabilityDropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            }
            if (type.contains("cinemaformat")) {
                cinemaFormat_filter_tabs.setVisibility(View.VISIBLE);
                cinemaFormatDropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            }
            if (type.contains("specialshow")) {
                specialFormat_filter_tabs.setVisibility(View.VISIBLE);
                specialFormatDropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
            }
        }
        if (from.equalsIgnoreCase("Home") || from.equalsIgnoreCase("ComingSoon")){
            language_filter_tabs.setVisibility(View.GONE);
            updateUI(language_filter_tabs, langDropImage, context);
        }else if (selectedFilters.containsKey("language")){
            language_filter_tabs.setVisibility(View.GONE);
            updateUI(language_filter_tabs, langDropImage, context);
        }else if (selectedFilters.containsKey("geners")){
            genre_filter_tabs.setVisibility(View.GONE);
            updateUI(genre_filter_tabs, genereDropImage, context);
        }else if (selectedFilters.containsKey("accessability")){
            accessability_filter_tabs.setVisibility(View.GONE);
            updateUI(accessability_filter_tabs, accessabilityDropImage, context);
        }

        languageFilterLay.setOnClickListener(view -> {
            updateUI(language_filter_tabs, langDropImage, context);
        });
        genereFilterLay.setOnClickListener(view -> {
            updateUI(genre_filter_tabs, genereDropImage, context);
        });
        formatFilterLay.setOnClickListener(view -> {
            updateUI(format_filter_tabs, formatDropImage, context);
        });
        timeFilterLay.setOnClickListener(view -> {
            updateUI(time_filter_tabs, timeDropImage, context);
        });
        priceFilterLay.setOnClickListener(view -> {
            updateUI(price_filter_tabs, priceDropImage, context);
        });
        accessibilityFilterLay.setOnClickListener(view -> {
            updateUI(accessability_filter_tabs, accessabilityDropImage, context);
        });
        cinemaFormatFilterLay.setOnClickListener(view -> {
            updateUI(cinemaFormat_filter_tabs, cinemaFormatDropImage, context);
        });
        specialFormatFilterLay.setOnClickListener(view -> {
            updateUI(specialFormat_filter_tabs, specialFormatDropImage, context);
        });

        // Setting the layout as Staggered Grid for vertical orientation
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        ArrayList<String> languageList = getFilters().get(Constant.FilterType.LANG_FILTER);
        ArrayList<String> generList = getFilters().get(Constant.FilterType.GENERE_FILTER);
        ArrayList<String> formatList = getFilters().get(Constant.FilterType.FORMAT_FILTER);
        ArrayList<String> priceList = getFilters().get(Constant.FilterType.PRICE_FILTER);
        ArrayList<String> showTimeList = getFilters().get(Constant.FilterType.SHOWTIME_FILTER);
        ArrayList<String> accessabilityList = getFilters().get(Constant.FilterType.ACCESSABILITY_FILTER);
        ArrayList<String> cinemaFormatList = getFilters().get(Constant.FilterType.CINEMA_FORMAT);
        ArrayList<String> specialShowList = getFilters().get(Constant.FilterType.SPECIAL_SHOW);
//       for (String lang : languageList) {
//           FilterData data = new FilterData(lang, false);
//           tabList.add(data);
//       }
//        ArrayList<FilterData> languageList = new ArrayList<>();


        if (languageList.size() > 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            languageFilterLay.setVisibility(View.VISIBLE);
            adapterLanguage = new GenericFilterAdapter(languageList, context, "language", this, selectedFilters, type);
            recyclerLanguage.setLayoutManager(layoutManager);
            recyclerLanguage.setAdapter(adapterLanguage);
        } else {
            languageFilterLay.setVisibility(View.GONE);
        }

        if (generList.size() > 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            genereFilterLay.setVisibility(View.VISIBLE);
            adapterGeners = new GenericFilterAdapter(generList, context, "geners", this, selectedFilters, type);
            recyclerGeners.setLayoutManager(layoutManager);
            recyclerGeners.setAdapter(adapterGeners);
        } else {
            genereFilterLay.setVisibility(View.GONE);
        }


        if (formatList.size() > 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            formatFilterLay.setVisibility(View.VISIBLE);
            adapterFormat = new GenericFilterAdapter(formatList, context, "format", this, selectedFilters, type);
            recyclerFormat.setLayoutManager(layoutManager);
            recyclerFormat.setAdapter(adapterFormat);
        } else {
            formatFilterLay.setVisibility(View.GONE);
        }

        if (priceList.size() > 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            priceFilterLay.setVisibility(View.VISIBLE);
            adapterPriceRange = new GenericFilterAdapter(priceList, context, "price", this, selectedFilters, type);
            recyclerPriceRange.setLayoutManager(layoutManager);
            recyclerPriceRange.setAdapter(adapterPriceRange);
        } else {
            priceFilterLay.setVisibility(View.GONE);
        }


//        if (showTimeList.size() > 0) {
//            timeFilterLay.setVisibility(View.VISIBLE);
//            adapterShowTime = new GenericFilterAdapter(showTimeList, context, "time", this, selectedFilters, type);
//            recyclerShowTIme.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL));
//            recyclerShowTIme.setAdapter(adapterShowTime);
//        }else {
//            timeFilterLay.setVisibility(View.GONE);
//        }

        if (accessabilityList.size() > 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            accessibilityFilterLay.setVisibility(View.VISIBLE);
            adapterAccessability = new GenericFilterAdapter(accessabilityList, context, "accessability", this, selectedFilters, type);
            recyclerAccessibility.setLayoutManager(layoutManager);
            recyclerAccessibility.setAdapter(adapterAccessability);
        } else {
            accessibilityFilterLay.setVisibility(View.GONE);
        }

        if (cinemaFormatList.size() > 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            cinemaFormatFilterLay.setVisibility(View.VISIBLE);
            adapterAccessability = new GenericFilterAdapter(cinemaFormatList, context, "cinema", this, selectedFilters, type);
            cinemaRecyclerFormat.setLayoutManager(layoutManager);
            cinemaRecyclerFormat.setAdapter(adapterAccessability);
        } else {
            cinemaFormatFilterLay.setVisibility(View.GONE);
        }


        if (specialShowList.size() > 0) {
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            specialFormatFilterLay.setVisibility(View.VISIBLE);
            adapterAccessability = new GenericFilterAdapter(specialShowList, context, "special", this, selectedFilters, type);
            specialRecyclerFormat.setLayoutManager(layoutManager);
            specialRecyclerFormat.setAdapter(adapterAccessability);
        } else {
            specialFormatFilterLay.setVisibility(View.GONE);
        }


        TextView btnApplyFilter = dialog.findViewById(R.id.btnApplyFilter);
        TextView btnReset = dialog.findViewById(R.id.btnReset);
        onSelection = onClickListener;

        btnApplyFilter.setOnClickListener(v -> {
            dialog.dismiss();
            if (!type.contains("time"))
                type.add("time");
            selectedFilters.put("time", show1 + "-" + show2);
            onSelection.onApply(type, selectedFilters, isSelected, filterItemSelected);
        });

        btnReset.setOnClickListener(v -> {
            dialog.dismiss();
            selectedFilters = new HashMap<>();
            filterStrings = new HashMap<>();
            filterValue = new ArrayList<>();
            onSelection.onReset();
        });

        int count = new HomeFragment().getShowCountHome(filterStrings);
        if (count>0){
            showCount.setVisibility(View.VISIBLE);
            showCount.setText("Movie Count:  "+count);
        }else {
            showCount.setVisibility(View.GONE);
        }

        dialog.show();
    }

    private static void updateUI(LinearLayout layout_filter_tabs, ImageView dropImage, Context context) {

        if (layout_filter_tabs.getVisibility() == View.VISIBLE) {
            layout_filter_tabs.setVisibility(View.GONE);
            dropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_down));
        } else {
            layout_filter_tabs.setVisibility(View.VISIBLE);
            dropImage.setBackground(ContextCompat.getDrawable(context, R.drawable.arrow_up));
        }
    }


    @Override
    public void onFilterItemClick(int position, String typeStr, String itemSelected, boolean selectedValue) {
        isSelected = selectedValue;

        switch (typeStr) {
            case "language":
                if (!type.contains(typeStr))
                    type.add(typeStr);
                if (languagesStr.equalsIgnoreCase("")) {
                    languagesStr = itemSelected;
                } else {
                    if (languagesStr.contains(itemSelected)) {
                        languagesStr = removeItem(itemSelected, languagesStr);
                    } else {
                        languagesStr = languagesStr + ", " + itemSelected;
                    }

                }

                selectedFilters.put(typeStr, languagesStr);


//                if(isSelected){
//                    if (languagesStr.equalsIgnoreCase(""))
//                        languagesStr = itemSelected;
//                    else languagesStr = languagesStr + "," + itemSelected;
//                    type.add(typeStr);
//                    selectedFilters.put(typeStr, languagesStr);
//                }else{
//                    if(!languagesStr.contains(",")) {
//                        languagesStr = "";
//                        type.remove(typeStr);
//                        selectedFilters.remove(typeStr);
//                    } else {
//                        languagesStr = languagesStr.replace("," + itemSelected, "");
//                        type.add(typeStr);
//                        selectedFilters.put(typeStr, languagesStr);
//                    }
//
//                }
//                Log.d("GenericFilter", "languagesStr: " + languagesStr);
//                onSelection.onApply(type, selectedFilters, isSelected, itemSelected);

                break;
            case "geners":
                if (!type.contains(typeStr))
                    type.add(typeStr);

                if (genereStr.equalsIgnoreCase("")) {
                    genereStr = itemSelected;
                } else {
                    if (genereStr.contains(itemSelected)) {
                        genereStr = removeItem(itemSelected, genereStr);
                    } else {
                        genereStr = genereStr + ", " + itemSelected;
                    }

                }

                selectedFilters.put(typeStr, genereStr);


                break;
            case "format":
                if (!type.contains(typeStr))
                    type.add(typeStr);

                if (formatStr.equalsIgnoreCase("")) {
                    formatStr = itemSelected;
                } else {
                    if (formatStr.contains(itemSelected)) {
                        formatStr = removeItem(itemSelected, formatStr);
                    } else {
                        formatStr = formatStr + ", " + itemSelected;
                    }

                }
                selectedFilters.put(typeStr, formatStr);


                break;
            case "cinema":
                if (!type.contains(typeStr))
                    type.add(typeStr);
                System.out.println("typeStr--->"+typeStr+"----"+cinemaStr);

                if (cinemaStr.equalsIgnoreCase("")) {
                    cinemaStr = itemSelected;
                } else {
                    if (cinemaStr.contains(itemSelected)) {
                        cinemaStr = removeItem(itemSelected, cinemaStr);
                    } else {
                        cinemaStr = cinemaStr + ", " + itemSelected;
                    }

                }
                selectedFilters.put(typeStr, cinemaStr);


                break;

            case "special":
                if (!type.contains(typeStr))
                    type.add(typeStr);

                if (specialStr.equalsIgnoreCase("")) {
                    specialStr = itemSelected;
                } else {
                    if (specialStr.contains(itemSelected)) {
                        specialStr = removeItem(itemSelected, specialStr);
                    } else {
                        specialStr = specialStr + ", " + itemSelected;
                    }

                }
                selectedFilters.put(typeStr, specialStr);


                break;
            case "price":
                if (!type.contains(typeStr))
                    type.add(typeStr);
                if (priceStr==itemSelected){
                    priceStr = "";
                }else {
                    priceStr = itemSelected;
                }
                selectedFilters.put(typeStr, priceStr);

                break;
            case "time":
                if (!type.contains(typeStr))
                    type.add(typeStr);
                timeStr = itemSelected;
                selectedFilters.put(typeStr, timeStr);

                break;
            case "accessability":
                if (!type.contains(typeStr))
                    type.add(typeStr);

                if (accessabilityStr.equalsIgnoreCase("")) {
                    accessabilityStr = itemSelected;
                } else {
                    if (accessabilityStr.contains(itemSelected)) {
                        accessabilityStr = removeItem(itemSelected, accessabilityStr);
                    } else {
                        accessabilityStr = accessabilityStr + ", " + itemSelected;
                    }

                }

                selectedFilters.put(typeStr, accessabilityStr);

                break;

        }

        if (isSelected) {
            if (typeStr.equalsIgnoreCase("accessability")){
                filterValue = new ArrayList<>();
                filterValue.add(itemSelected.toUpperCase());
                filterStrings.put(typeStr, filterValue);
            }else {
                filterValue.add(itemSelected.toUpperCase());
                filterStrings.put(typeStr, filterValue);
            }
        }else {
            if (typeStr.equalsIgnoreCase("accessability")){
                if (filterValue.contains(itemSelected.toUpperCase(Locale.ROOT))) {
                    filterValue= new ArrayList<>();
                    filterStrings.put(typeStr, filterValue);
                }
            }else {
                if (filterValue.contains(itemSelected.toUpperCase(Locale.ROOT))) {
                    filterValue.remove(itemSelected.toUpperCase());
                    filterStrings.put(typeStr, filterValue);
                }
            }
        }

        System.out.println("filterStrings---"+filterStrings);

        int count = new HomeFragment().getShowCountHome(filterStrings);
        if (count>0){
            showCount.setVisibility(View.VISIBLE);
            showCount.setText("Movie Count:  "+count);
        }else {
            showCount.setVisibility(View.GONE);
        }
    }

    private String removeItem(String itemSelected, String languagesStr) {

        if (languagesStr.contains(", " + itemSelected)) {
            return languagesStr.replaceAll(", " + itemSelected, "");
        } else if (languagesStr.equalsIgnoreCase(itemSelected)) {
            return languagesStr.replaceAll(itemSelected , "");
        }else if (languagesStr.contains(",")) {
            return languagesStr.replaceAll(itemSelected + ", ", "");
        } else {
            return "";
        }
    }

    public interface onButtonSelected {

        void onApply(ArrayList<String> type, HashMap<String, String> name, boolean isSelected, String filterItemSelected);

        void onReset();

    }

    private void setAlreadySelected() {
        if (type.size() > 0) {
            boolean containLanguage = type.contains("language");
            if (containLanguage) {
                int index = type.indexOf("language");
                String value = selectedFilters.get(type.get(index));
                if (value != null) {
                    languagesStr = value;
                }

            }

            boolean containGeners = type.contains("geners");
            if (containGeners) {
                int index = type.indexOf("geners");
                String value = selectedFilters.get(type.get(index));
                if (value != null) {
                    genereStr = value;
                }
            }

            boolean containTime = type.contains("time");
            if (containTime) {
                int index = type.indexOf("time");
                String value = selectedFilters.get(type.get(index));
                if (value != null && !value.equalsIgnoreCase("")&& !value.contains("ALL")) {
                    timeStr = value;
                    System.out.println("timeStr--->"+timeStr);
                    timeStr.split("-");
                    left_text.setText( timeStr.split("-")[0]+" hrs");
                    right_text.setText( timeStr.split("-")[1]+ " hrs");
//                    seek_progress.setMaxStartValue(Float.parseFloat(timeStr.split("-")[1].replaceAll(":00",""))).apply();
                    seek_progress.setMinStartValue(Float.parseFloat(timeStr.split("-")[0].replaceAll(":00",""))).setMaxStartValue(Float.parseFloat(timeStr.split("-")[1].replaceAll(":00",""))).apply();
                }
            }

            boolean containFormat = type.contains("format");
            if (containFormat) {
                int index = type.indexOf("format");
                String value = selectedFilters.get(type.get(index));
                if (value != null) {
                    formatStr = value;
                }
            }

            boolean containCinemaFormat = type.contains("cinema");
            if (containCinemaFormat) {
                int index = type.indexOf("cinema");
                String value = selectedFilters.get(type.get(index));
                if (value != null) {
                    cinemaStr = value;
                }
            }

            boolean containSpecialFormat = type.contains("special");
            if (containSpecialFormat) {
                int index = type.indexOf("special");
                String value = selectedFilters.get(type.get(index));
                if (value != null) {
                    specialStr = value;
                }
            }

            boolean containPrice = type.contains("price");
            if (containPrice) {
                int index = type.indexOf("price");
                String value = selectedFilters.get(type.get(index));
                if (value != null) {
                    priceStr = value;
                }
            }

            boolean containAccessability = type.contains("accessability");
            if (containAccessability) {
                int index = type.indexOf("accessability");
                String value = selectedFilters.get(type.get(index));
                if (value != null) {
                    accessabilityStr = value;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateFilters(String type, String data){
        System.out.println("languagesStrNewcoming--->"+data+ Objects.requireNonNull(selectedFilters.get(type).toUpperCase(Locale.ROOT)).contains(","+data.split("-")[0].trim().toUpperCase(Locale.ROOT))+"---, "+Objects.requireNonNull(selectedFilters.get(type).toUpperCase(Locale.ROOT)).contains(data.split("-")[0].toUpperCase(Locale.ROOT)+",")+"---"+selectedFilters);

        if (Objects.requireNonNull(selectedFilters.get(type).toUpperCase(Locale.ROOT)).contains(data.split("-")[0].toUpperCase(Locale.ROOT)+",")) {
            selectedFilters.replace(type,selectedFilters.get(type).toUpperCase(Locale.ROOT).replaceAll(data.split("-")[0].toUpperCase(Locale.ROOT)+",",""));
        }else if (Objects.requireNonNull(selectedFilters.get(type)).toUpperCase(Locale.ROOT).contains(", "+data.split("-")[0].toUpperCase(Locale.ROOT))){
            selectedFilters.replace(type,selectedFilters.get(type).toUpperCase(Locale.ROOT).replaceAll(", "+data.split("-")[0].toUpperCase(Locale.ROOT),""));
        }else if (Objects.requireNonNull(selectedFilters.get(type)).toUpperCase(Locale.ROOT).contains(","+data.split("-")[0].trim().toUpperCase(Locale.ROOT))){
            selectedFilters.replace(type,selectedFilters.get(type).toUpperCase(Locale.ROOT).replaceAll(","+data.split("-")[0].toUpperCase(Locale.ROOT),""));
        }else {
            selectedFilters.replace(type,selectedFilters.get(type).toUpperCase(Locale.ROOT).replaceAll(data.split("-")[0].toUpperCase(Locale.ROOT),""));

        }
        System.out.println("languagesStrNewcoming123--->"+Objects.requireNonNull(selectedFilters.get(type).toUpperCase(Locale.ROOT)).contains(", "+data.split("-")[0].toUpperCase(Locale.ROOT))+"---, "+Objects.requireNonNull(selectedFilters.get(type).toUpperCase(Locale.ROOT)).contains(data.split("-")[0].toUpperCase(Locale.ROOT)+",")+"---"+selectedFilters);



    }

}
