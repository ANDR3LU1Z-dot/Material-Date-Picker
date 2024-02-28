package com.example.materialdatepickerdecorator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarButton = findViewById<MaterialButton>(R.id.btn_calendar)

        calendarButton.setOnClickListener {
//            openCalendar()
            val indicatorDecorator = CircleIndicatorDecorator()
            val datePickerMaterial = MaterialDatePicker.Builder.datePicker()
                .setDayViewDecorator(indicatorDecorator)
                .build()

            datePickerMaterial.show(supportFragmentManager, "MaterialDatePicker")
        }

    }
}
