package com.net.pvr.ui.home.fragment.more.offer.adapter;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.net.pvr.R;
import com.net.pvr.ui.home.fragment.more.offer.response.MOfferResponse;
import com.net.pvr.utils.Constant;

import java.util.ArrayList;

public class PHAdapter extends RecyclerView.Adapter<PHAdapter.ViewHolder> {
    Activity context;
    ArrayList<MOfferResponse.Output.Pu> exmovies;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    private int screenWidth = 0;
    private int type = 0;
    private final OpenRedirection listener;


    public PHAdapter(Activity context, ArrayList<MOfferResponse.Output.Pu> exmovies, OpenRedirection listener, int type) {

        this.exmovies = exmovies;
        this.type = type;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = null;
        if (type == 1) {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ph_slider_item, null);
        } else {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ph_slider_item_new, null);
        }
        // create ViewHolder
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println("datasize-->" + exmovies.size());
        final MOfferResponse.Output.Pu exmovieVO = exmovies.get(position);
        if (exmovieVO.getType().equalsIgnoreCase("VIDEO") && (exmovieVO.getTrailerUrl() != null && !exmovieVO.getTrailerUrl().equalsIgnoreCase(""))) {
            holder.tv_play.setVisibility(View.VISIBLE);
        } else {
            holder.tv_play.setVisibility(View.GONE);
        }
        if (type == 0) {
            if (exmovies.size() > 1) {
                if (exmovies.size() == 2) {
                    int itemWidth = (int) ((screenWidth - 0) / (1.25f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    if (position == 0) {
                        layoutParams.leftMargin = new Constant().convertDpToPixel(13F, context);
                        layoutParams.rightMargin = new Constant().convertDpToPixel(1F, context);
                    } else {
                        layoutParams.leftMargin = new Constant().convertDpToPixel(1F, context);
                        layoutParams.rightMargin = new Constant().convertDpToPixel(13F, context);
                    }
                    holder.itemView.setLayoutParams(layoutParams);
                } else if (position == 0) {
                    int itemWidth = (int) ((screenWidth - 0) / (1.25f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = new Constant().convertDpToPixel(13F, context);
                    holder.itemView.setLayoutParams(layoutParams);
                } else if (position == exmovies.size() - 1) {
                    int itemWidth = (int) ((screenWidth - 0) / (1.25f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = new Constant().convertDpToPixel(13F, context);
                    holder.itemView.setLayoutParams(layoutParams);
                } else {
                    int itemWidth = (int) ((screenWidth - 0) / (1.25f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = new Constant().convertDpToPixel(1F, context);
                    layoutParams.leftMargin = new Constant().convertDpToPixel(1F, context);
                    holder.itemView.setLayoutParams(layoutParams);
                }

            } else {
//            int itemWidth = (int) ((screenWidth - 0)/(1.10f));
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = new Constant().convertDpToPixel(13F, context);
                layoutParams.rightMargin = new Constant().convertDpToPixel(13F, context);
                holder.itemView.setLayoutParams(layoutParams);
            }
        } else {
            if (exmovies.size() > 1) {
                if (exmovies.size() == 2) {
                    int itemWidth = (int) ((screenWidth - 0) / (1.1f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    if (position == 0) {
                        layoutParams.leftMargin = new Constant().convertDpToPixel(13F, context);
                        layoutParams.rightMargin = new Constant().convertDpToPixel(1F, context);
                    } else {
                        layoutParams.leftMargin = new Constant().convertDpToPixel(1F, context);
                        layoutParams.rightMargin = new Constant().convertDpToPixel(13F, context);
                    }
                    holder.itemView.setLayoutParams(layoutParams);
                } else if (position == 0) {
                    int itemWidth = (int) ((screenWidth - 0) / (1.1f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = new Constant().convertDpToPixel(13F, context);
                    holder.itemView.setLayoutParams(layoutParams);
                } else if (position == exmovies.size() - 1) {
                    int itemWidth = (int) ((screenWidth - 0) / (1.1f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = new Constant().convertDpToPixel(13F, context);
                    holder.itemView.setLayoutParams(layoutParams);
                } else {
                    int itemWidth = (int) ((screenWidth - 0) / (1.1f));
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(itemWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.rightMargin = new Constant().convertDpToPixel(1F, context);
                    layoutParams.leftMargin = new Constant().convertDpToPixel(1F, context);
                    holder.itemView.setLayoutParams(layoutParams);
                }

            } else {
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = new Constant().convertDpToPixel(13F, context);
                layoutParams.rightMargin = new Constant().convertDpToPixel(13F, context);
                holder.itemView.setLayoutParams(layoutParams);
            }
        }

        holder.itemView.setOnClickListener(view -> listener.onOpen(exmovieVO));
        Glide.with(context).load(exmovieVO.getI()).into(holder.sliderImg);
    }

    @Override
    public int getItemCount() {

        return exmovies.size();

    }

    public interface OpenRedirection {
        void onOpen(MOfferResponse.Output.Pu exmovieVO);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sliderImg, tv_play;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            sliderImg = itemView.findViewById(R.id.sliderImg);
            tv_play = itemView.findViewById(R.id.tv_play);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }


}
