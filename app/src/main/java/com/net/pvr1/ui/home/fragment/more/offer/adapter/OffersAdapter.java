package com.net.pvr1.ui.home.fragment.more.offer.adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.net.pvr1.R;
import com.net.pvr1.ui.home.fragment.more.offer.OfferList;
import com.net.pvr1.ui.home.fragment.more.offer.response.MOfferResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> implements View.OnClickListener, OfferFilterAdapter.RecycleViewItemClickListener, PHAdapter.OpenRedirection {
    public static List<MOfferResponse.Output.Offer> allData = new ArrayList<>();
    final Handler handler = new Handler();
    public ArrayList<MOfferResponse.Output.Pu> phList = new ArrayList<>();
    int speedScroll = 3000;
    int rowIndex = -1;
    Runnable runnable = null;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private List<OfferList> offerList;
    private final List<OfferList> offerListAll;
    private final Activity context;
    private final RecycleViewItemClickListener listener;
    private final String type;

    public OffersAdapter(Activity context, List<OfferList> offerList, List<OfferList> offerListAll, RecycleViewItemClickListener listener, String type) {
        this.offerList = offerList;
        this.offerListAll = offerListAll;
        this.context = context;
        this.listener = listener;
        this.type = type;
        notifyDataSetChanged();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    }


    @Override
    public void onClick(View view) {
//        Datum datum = (Datum) view.getTag();
//        if (datum != null &&  listener!=null)
//            listener.onItemClick(datum);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_offer_item, null);

        return (new ViewHolder(itemLayoutView));
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OfferList obj = offerList.get(position);
        System.out.println("printCategory---->"+obj.getList().size()+"---->"+obj.getCat());
        holder.titlePcTextView.setText(obj.getCat());

        if (position == offerList.size() - 1) {
            holder.view.setVisibility(View.GONE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }
        if (position == 0) {
            if (obj.getCat().equalsIgnoreCase("TRENDING")) {
                holder.mainView.setBackgroundColor(Color.parseColor("#EDE8E9"));
            } else {
                holder.mainView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        } else {
            holder.mainView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayMetrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.mainView.setLayoutParams(layoutParams);
        SnapHelper snapHelper1 = new PagerSnapHelper();
        holder.offerRecList.setOnFlingListener(null);
        snapHelper1.attachToRecyclerView(holder.offerRecList);
        holder.offerRecList.setLayoutManager(new LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        if (obj.getCat().equalsIgnoreCase("")) {
            List<String> list = Arrays.asList(offerList.get(0).getList().get(0).getCatp().replaceAll("Trending,", "").replaceAll("TREND,", "").split("\\s*,\\s*"));
            OfferFilterAdapter adapter1 = new OfferFilterAdapter(list, context, this, rowIndex, holder.offerRecList);
            holder.offerRecList.setAdapter(adapter1);
            if (rowIndex >= 0 && rowIndex < list.size())
                holder.offerRecList.smoothScrollToPosition(rowIndex + 1);
            holder.titlePcTextView.setVisibility(View.GONE);
            holder.headerView.setVisibility(View.GONE);
            holder.seeAll.setVisibility(View.GONE);
            if (phList != null && phList.size() > 0) {
                holder.upperView.setVisibility(View.VISIBLE);
                phList = phList;
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                SnapHelper snapHelper = new PagerSnapHelper();
                holder.demoSlider.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(holder.demoSlider);
                holder.demoSlider.setLayoutManager(layoutManager);
                PHAdapter recyclerAdapter = new PHAdapter(context, phList, this, 1);
                holder.demoSlider.setAdapter(recyclerAdapter);

                if (phList.size() > 1) {
                    addTimer(recyclerAdapter, holder.demoSlider);
                }
            } else {
                holder.upperView.setVisibility(View.GONE);
            }
        } else {

            holder.upperView.setVisibility(View.GONE);
            if (obj.getList().size() > 1) {
                if (position == 0) {
                    holder.seeAll.setVisibility(View.GONE);
                } else {
                    holder.seeAll.setVisibility(View.VISIBLE);
                }
            } else {
                holder.seeAll.setVisibility(View.GONE);
            }
            OffersFAdapter adapter1 = new OffersFAdapter(context, obj.getList(), "F");
            holder.offerRecList.setAdapter(adapter1);
            holder.titlePcTextView.setVisibility(View.VISIBLE);
            holder.headerView.setVisibility(View.VISIBLE);
        }

        holder.seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, PCOffersAllActivity.class);
//                intent.putExtra("TITLE",obj.getCat());
//                allData = obj.getList();
//                context.startActivity(intent);
            }
        });

    }

    private void addTimer(PHAdapter recyclerAdapter, RecyclerView demoSlider) {
        runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                System.out.println("position--->" + count + flag + recyclerAdapter.getItemCount());

                if (count < recyclerAdapter.getItemCount()) {
                    if (count == recyclerAdapter.getItemCount() - 1) {
                        flag = false;
                    } else if (count == 0) {
                        flag = true;
                    }
                    if (flag) count++;
                    else count--;
                    demoSlider.smoothScrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    @Override
    public void onFilterClick(String offer, int type, int rowIndex, RecyclerView recyclerView) {
        try {
            if (runnable != null)
                handler.removeCallbacksAndMessages(null);
            this.rowIndex = rowIndex;
            List<OfferList> offers = new ArrayList<>();
            for (OfferList data : offerListAll) {
                if (data.getCat().equalsIgnoreCase(offer)) {
                    if (listHas(offerListAll)) {
                        offers.add(offerListAll.get(0));
                        offers.add(offerListAll.get(1));
                    } else {
                        offers.add(offerListAll.get(0));
                    }
                    if (offerListAll.size() > 2)
                        offers.add(data);
                }
            }
            if (type == 0) {
                offerList = offerListAll;
            } else {
                if (offers.size() > 0) {
                    offerList = offers;
                } else {
                    Toast.makeText(context, "No offer available for selected category!", Toast.LENGTH_SHORT).show();
                }
            }
            //if (handler!=null && runnable!=null){

            // }
            notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    private boolean listHas(List<OfferList> offerListAll) {
        for (OfferList offerData : offerListAll) {
            if (offerData.getCat().equalsIgnoreCase("TRENDING")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onOpen(@Nullable MOfferResponse.Output.Pu exmovieVO) {

    }

    @Override
    public int getItemCount() {
        if (offerList != null)
            return offerList.size();
        return 0;
    }

    public interface RecycleViewItemClickListener {
        void onItemClick(MOfferResponse.Output.Offer offer);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titlePcTextView, seeAll;
        RecyclerView offerRecList, demoSlider;
        CardView mainView;
        RelativeLayout headerView;
        LinearLayout view, upperView;

        public ViewHolder(View itemView) {
            super(itemView);
            headerView = itemView.findViewById(R.id.headerView);
            view = itemView.findViewById(R.id.view);
            upperView = itemView.findViewById(R.id.upperView);
            mainView = itemView.findViewById(R.id.mainView);
            offerRecList = itemView.findViewById(R.id.offerList);
            demoSlider = itemView.findViewById(R.id.slider);
            titlePcTextView = itemView.findViewById(R.id.text);
            seeAll = itemView.findViewById(R.id.seeAll);
        }
    }
}