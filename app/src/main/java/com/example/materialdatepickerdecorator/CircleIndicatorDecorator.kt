package com.example.materialdatepickerdecorator

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.ColorInt
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.DayViewDecorator
import java.util.Calendar
import java.util.TimeZone

internal class CircleIndicatorDecorator : DayViewDecorator() {
    private val today = utcCalendar
    private val indicatorDays: List<Calendar> =
        ArrayList(listOf(addDays(today, 1), addDays(today, 3), addDays(today, -2)))

    private var indicatorDrawables: IndicatorDrawables? = null
    override fun initialize(context: Context) {
        indicatorDrawables = IndicatorDrawables(context)
    }

    override fun getCompoundDrawableTop(
        context: Context, year: Int, month: Int, day: Int, valid: Boolean, selected: Boolean
    ): Drawable? {
        return indicatorDrawables!!.topSpacerDrawable
    }

    override fun getCompoundDrawableBottom(
        context: Context, year: Int, month: Int, day: Int, valid: Boolean, selected: Boolean
    ): Drawable? {
        return selectIndicatorDrawable(year, month, day, valid, selected)
    }

    override fun getContentDescription(
        context: Context,
        year: Int,
        month: Int,
        day: Int,
        valid: Boolean,
        selected: Boolean,
        originalContentDescription: CharSequence?
    ): CharSequence? {
        return if (!valid || !shouldShowIndicator(year, month, day)) {
            originalContentDescription
        } else String.format(
            context.getString(R.string.cat_picker_day_view_decorator_dots_content_description),
            originalContentDescription
        )
    }

    private fun selectIndicatorDrawable(
        year: Int, month: Int, day: Int, valid: Boolean, selected: Boolean
    ): Drawable {
        if (!valid || !shouldShowIndicator(year, month, day)) {
            return indicatorDrawables!!.indicatorDrawableNone
        }
        return if (selected) {
            indicatorDrawables!!.indicatorDrawableSelected
        } else indicatorDrawables!!.indicatorDrawableDefault
    }

    private fun shouldShowIndicator(year: Int, month: Int, day: Int): Boolean {
        for (calendar in indicatorDays) {
            if (calendar[Calendar.YEAR] == year && calendar[Calendar.MONTH] == month && calendar[Calendar.DAY_OF_MONTH] == day) {
                return true
            }
        }
        return false
    }

    private class IndicatorDrawables constructor(context: Context) {
        private val indicatorRadius: Int
        private val indicatorMarginBottom: Int
        val topSpacerDrawable: Drawable
        val indicatorDrawableNone: Drawable
        val indicatorDrawableDefault: Drawable
        val indicatorDrawableSelected: Drawable

        init {
            val resources = context.resources
            indicatorRadius = resources.getDimensionPixelSize(R.dimen.cat_picker_demo_circle_indicator_size)
            indicatorMarginBottom =
                resources.getDimensionPixelOffset(R.dimen.cat_picker_demo_circle_indicator_margin_bottom)
            val topSpacerSize =
                resources.getDimensionPixelSize(R.dimen.cat_picker_demo_circle_indicator_top_spacer_size)
            val indicatorColorDefault = MaterialColors.getColor(
                context, com.google.android.material.R.attr.colorPrimaryVariant, IndicatorDrawables::class.java.simpleName
            )
            val indicatorColorSelected = MaterialColors.getColor(
                context, com.google.android.material.R.attr.colorOnSurface, IndicatorDrawables::class.java.simpleName
            )
            topSpacerDrawable = createSpacerDrawable(topSpacerSize)
            indicatorDrawableNone = createIndicatorDrawable(Color.TRANSPARENT)
            indicatorDrawableDefault = createIndicatorDrawable(indicatorColorDefault)
            indicatorDrawableSelected = createIndicatorDrawable(indicatorColorSelected)
        }

        private fun createSpacerDrawable(size: Int): Drawable {
            val spacer: Drawable = ColorDrawable(Color.TRANSPARENT)
            spacer.setBounds(0, 0, size, size)
            return spacer
        }

        private fun createIndicatorDrawable(@ColorInt color: Int): Drawable {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            shape.setColor(color)
            shape.cornerRadius = indicatorRadius.toFloat()
            val insetDrawable = InsetDrawable(shape, 0, 0, 0, indicatorMarginBottom)
            insetDrawable.setBounds(0, 0, indicatorRadius, indicatorRadius + indicatorMarginBottom)
            return insetDrawable
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        private val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        private fun addDays(source: Calendar, days: Int): Calendar {
            val calendar = utcCalendar
            calendar.time = source.time
            calendar.add(Calendar.DATE, days)
            return calendar
        }

        @JvmField
        val CREATOR: Parcelable.Creator<CircleIndicatorDecorator> =
            object : Parcelable.Creator<CircleIndicatorDecorator> {
                override fun createFromParcel(source: Parcel): CircleIndicatorDecorator {
                    return CircleIndicatorDecorator()
                }

                override fun newArray(size: Int): Array<CircleIndicatorDecorator?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
