package com.jina.paintor.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jina.paintor.R
import com.jina.paintor.data.Location
import com.jina.paintor.databinding.ItemColorBinding
import com.jina.paintor.tripscheudule.ScheduleManager
import com.jina.paintor.utils.TAG
import com.orhanobut.logger.Logger

class ColorAdapter(val context: Context) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    val colorList = context.resources.getIntArray(R.array.color_pallet)
    var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapter.ViewHolder {
        return ViewHolder(
            ItemColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ColorAdapter.ViewHolder, position: Int) {
        holder.binding.viewColor.setBackgroundResource(R.drawable.shape_color_item)
        holder.binding.viewColor.background.setTint(colorList[position])

        // 설정된 아이템이 선택된 상태인지 여부를 판단하여 처리
        if (selectedPosition == position) {
            holder.binding.viewColor.isSelected = true
            holder.binding.ivCheck.visibility = View.VISIBLE
        } else {
            holder.binding.viewColor.isSelected = false
            holder.binding.ivCheck.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            var selectColor:Int? = null
            // 기존에 선택된 아이템 해제
            if (selectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(selectedPosition)
            }

            // 현재 클릭된 아이템이 이미 선택된 아이템이라면 선택 해제
            if (selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                selectColor = null
            } else {
                // 새로운 아이템으로 업데이트
                selectedPosition = position
                notifyItemChanged(selectedPosition)
                Logger.t(TAG.CALENDAR).d("selected color -> ${colorList[selectedPosition]}")
                selectColor = colorList[selectedPosition]

            }
            ScheduleManager.selectedColor.postValue(selectColor)
        }

    }

    override fun getItemCount(): Int {
        return colorList.size
    }
}