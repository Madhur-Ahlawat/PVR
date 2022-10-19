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
                binding.tvShows.text = this.sc.toString() + " " + context.getString(R.string.shows)
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
                    val comingSoonMovieAdapter = CinemaChildAdapter(this.m, context, this)
                    binding.llMovieList.layoutManager = gridLayout2
                    binding.llMovieList.adapter = comingSoonMovieAdapter
                } else {
                    binding.llMovieList.hide()
                }


                binding.cbFav.setOnClickListener {
                    if (isLogin) {
                        if (!rowIndex){
                            rowIndex
                            context.toast("true")
                            binding.cbFav.setImageResource(R.drawable.ic_un_favourite_theatre)
                            preference.onPreferenceClick(this,rowIndex)

                        }else {
                            rowIndex= false
                            context.toast("false")
                            binding.cbFav.setImageResource(R.drawable.ic_favourite_theatre)
                            preference.onPreferenceClick(this,rowIndex)
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
                binding.llLocation.setOnClickListener {
                    listener.onDirectionClick(this)
                }

                //Kilometer
                binding.shows.setOnClickListener {
                    location.onLocationClick(this)
                }
                //Kilometer
                binding.kiloMeter.setOnClickListener {
                    location.onLocationClick(this)
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
        fun onLocationClick(comingSoonItem: CinemaResponse.Output.C)
    }

    interface SetPreference {
        fun onPreferenceClick(comingSoonItem: CinemaResponse.Output.C, rowIndex: Boolean)
    }


}