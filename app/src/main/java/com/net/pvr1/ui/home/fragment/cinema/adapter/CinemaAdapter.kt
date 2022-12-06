package com.net.pvr1.ui.home.fragment.cinema.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.CinemaItemBinding
import com.net.pvr1.ui.dailogs.OptionDialog
import com.net.pvr1.ui.home.fragment.cinema.response.CinemaResponse
import com.net.pvr1.ui.login.LoginActivity
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show
import com.net.pvr1.utils.toast


class CinemaAdapter(
    private var nowShowingList: List<CinemaResponse.Output.C>,
    private var context: Context,
    private var listener: Direction,
    private var preference: SetPreference,
    private var location: Location,
    private var isLogin: Boolean=false,
) :
    RecyclerView.Adapter<CinemaAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CinemaItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var rowIndex: Boolean = false
    private var showList: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CinemaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //title
                binding.cinemaName.text = this.n
                //type
                binding.multipleCinema.text = this.mc
                //address
                binding.cinemaLocation.text = this.ad
                //Shows
//                binding.tvShows.text = this.sc.toString() + " " + context.getString(R.string.shows)
                //Distance
                binding.tvDistance.text = this.d
                //Image
                Glide.with(context)
                    .load(this.imob)
                    .error(R.drawable.app_icon)
                    .into(binding.cinemaImg)

                if (this.m.isNotEmpty()) {
                    //Call Child Adapter
                    val gridLayout2 =
                        GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                    val comingSoonMovieAdapter = CinemaMovieAdapter(this.m, context, this)
                    binding.llMovieList.layoutManager = gridLayout2
                    binding.llMovieList.adapter = comingSoonMovieAdapter
                } else {
                    binding.llMovieList.hide()
                }

                if (this.l=="true"){
                    binding.cbFav.setImageResource(R.drawable.ic_favourite_theatre)
                }else{
                    binding.cbFav.setImageResource(R.drawable.ic_un_favourite_theatre)
                }

                //ShoW Hide
                binding.imageView164.setOnClickListener {
                    if (showList){
                        showList=false
                        binding.llMovieList.show()
                        binding.imageView164.setImageResource(R.drawable.arrow_up)

                    }else{

                        binding.llMovieList.hide()
                        binding.imageView164.setImageResource(R.drawable.arrow_down)
                        showList=true

                    }
                }


                binding.cbFav.setOnClickListener {
                    if (isLogin) {
                        if (rowIndex){
                            rowIndex=false
                            binding.cbFav.setImageResource(R.drawable.ic_un_favourite_theatre)
                            preference.onPreferenceClick(this,false)

                        }else {
                            rowIndex= true
                            binding.cbFav.setImageResource(R.drawable.ic_favourite_theatre)
                            preference.onPreferenceClick(this,true)
                        }

                    } else {
                        val dialog = OptionDialog(context,
                            R.mipmap.ic_launcher_foreground,
                            R.string.app_name,
                            context.getString(R.string.loginCinema),
                            positiveBtnText = R.string.yes,
                            negativeBtnText = R.string.no,
                            positiveClick = {
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                            },
                            negativeClick = {
                            })
                        dialog.show()
                        context.toast("logout")
                    }

                }

                //Direction
//                binding.llLocation.setOnClickListener {
//                    listener.onDirectionClick(this)
//                }

                //Kilometer
                binding.relativeLayout2.setOnClickListener {
                    location.onCinemaClick(this)
                }
                //Kilometer
                binding.kiloMeter.setOnClickListener {
                    location.onCinemaClick(this)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface Direction {
        fun onDirectionClick(comingSoonItem: CinemaResponse.Output.C)
    }

    interface Location {
        fun onCinemaClick(comingSoonItem: CinemaResponse.Output.C)
    }

    interface SetPreference {
        fun onPreferenceClick(comingSoonItem: CinemaResponse.Output.C, rowIndex: Boolean)
    }

}