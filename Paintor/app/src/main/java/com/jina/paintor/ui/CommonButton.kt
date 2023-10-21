package com.jina.paintor.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.jina.paintor.R
import com.jina.paintor.databinding.CommonButtonBinding

@RequiresApi(Build.VERSION_CODES.M)
class CommonButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    @ColorInt
    var enableColor: Int

    @ColorInt
    var disableColor: Int

    private val binding: CommonButtonBinding by lazy {
        DataBindingUtil.inflate(
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater,
            R.layout.common_button,
            this,
            false
        )
    }

    init {
        addView(binding.root)
        binding.tvTitle.setOnClickListener { }
        var typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CommonButton, defStyleAttr, 0)
        var title = typedArray.getString(R.styleable.CommonButton_title)
        var titleSize =
            typedArray.getDimensionPixelSize(R.styleable.CommonButton_titleSize, 0).toFloat()
        var titleColor = typedArray.getColor(
            R.styleable.CommonButton_titleColor,
            context.getColor(R.color.white)
        )

        enableColor = typedArray.getColor(
            R.styleable.CommonButton_enableColor,
            context.getColor(R.color.main_color)
        )
        disableColor = typedArray.getColor(
            R.styleable.CommonButton_disableColor,
            context.getColor(R.color.disable_color)
        )
        title?.let {
            binding.tvTitle.text = title
        }
        if (titleSize != 0f) {
            val dbSize = titleSize / resources.displayMetrics.density
            binding.tvTitle.textSize = dbSize
        }

        binding.tvTitle.setBackgroundColor(enableColor)
        binding.tvTitle.setTextColor(titleColor)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.tvTitle.isEnabled = enabled
        binding.tvTitle.setBackgroundColor(if (enabled) enableColor else disableColor)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.tvTitle.setOnClickListener(l)
    }

    override fun setBackgroundColor(color: Int) {
        binding.tvTitle.setBackgroundColor(color)
    }

    fun setText(text: String) {
        binding.tvTitle.text = text
    }
}