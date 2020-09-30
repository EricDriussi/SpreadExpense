package com.driussi.spreadexpense

import android.widget.TextView
import android.widget.ToggleButton
import kotlin.properties.Delegates

class User(
    name: String = "",
    field: TextView,
    var btn: ToggleButton,
    expense: Double = 0.0,
    var selected: Boolean = false
) {

    var expense: Double by Delegates.observable(expense) { _, old, new ->
        field.text = getExpenseString()
    }

    init {
        btn.text = name
        btn.textOn = name
        btn.textOff = name
    }

    private fun getExpenseString(): String {
        return String.format("%.2f", expense)

    }
}