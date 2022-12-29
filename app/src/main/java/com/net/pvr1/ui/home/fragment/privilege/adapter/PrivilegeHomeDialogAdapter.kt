package com.net.pvr1.ui.home.fragment.privilege.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.net.pvr1.R
import com.net.pvr1.databinding.PrivilegeHomeItemBinding
import com.net.pvr1.ui.home.fragment.privilege.response.PrivilegeHomeResponse
import com.net.pvr1.utils.hide
import com.net.pvr1.utils.show


class PrivilegeHomeDialogAdapter(
    private var nowShowingList: List<PrivilegeHomeResponse.Output.Pinfo>,
    private var context: Context,
    private var type: Int,
    private var listener: RecycleViewItemClickListener
) :
    RecyclerView.Adapter<PrivilegeHomeDialogAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: PrivilegeHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PrivilegeHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(nowShowingList[position]) {
                //Language  .lng+this.lk
                Glide.with(context)
                    .load(this.pi)
                    .error(R.drawable.app_icon)
                    .into(binding.imageView110)
                if(type==1){
                    binding.textView274.hide()
                    binding.textView275.hide()
                }else{

                    binding.textView274.show()
                    binding.textView275.show()
                }
                //title
                binding.textView274.text=Html.fromHtml(this.ph)
                //desc
                binding.textView275.text=Html.fromHtml(this.psh.replace("Rs.".toRegex(),"₹"))
                //click
                holder.itemView.setOnClickListener {listener.privilegeHomeClick(this)  }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (nowShowingList.isNotEmpty()) nowShowingList.size else 0
    }

    interface RecycleViewItemClickListener {
        fun privilegeHomeClick(comingSoonItem: PrivilegeHomeResponse.Output.Pinfo)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is LinearLayoutManager && itemCount > 0) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePosition = manager.findFirstCompletelyVisibleItemPosition()
                    println("review_position1234--->$visiblePosition")
                    if (visiblePosition > -1) {
//                        View v = llm.findViewByPosition(visiblePosition);
//                        //do something
//                        v.setBackgroundColor(Color.parseColor("#777777"));
                    }
                }
            })
        }
    }

}