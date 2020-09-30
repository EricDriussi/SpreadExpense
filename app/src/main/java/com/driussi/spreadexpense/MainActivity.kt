package com.driussi.spreadexpense

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var users: ArrayList<User> = ArrayList()
    private var total: Double = 0.0
    private var isCalculate: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This should become interactive
        val names = arrayOf("Eric", "Dacil", "Selena")

        setUpUsers(names)
        setUpListeners()
    }

    // Temporary method
    private fun setUpUsers(names: Array<String>) {

        users.add(User(names[0], expense1, btn1))
        users.add(User(names[1], expense2, btn2))
        users.add(User(names[2], expense3, btn3))

    }

    private fun setUpListeners() {

        // Ensures 'selected' and isChecked are always on par
        users.forEach { user ->
            user.btn.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                user.selected = isChecked
            })
        }

        // Keyboard enter function
        expenseInput.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    next()

                    return true
                }
                return false
            }
        })

        // Clears input and processes the expense
        next.setOnClickListener {

            next()
        }

        // Restarts whole thing
        reset.setOnClickListener {

            if (isCalculate) {
                hideKeyboard(this@MainActivity)
                result.text = "TOTAL: $total"
                reset.text = "reset"
                isCalculate = false

            } else {
                result.text = ""
                total = 0.0
                users.forEach { user -> user.expense = 0.0 }
                reset.text = "calculate"
                isCalculate = true
            }
        }
    }

    // Checks input validity before executing logic
    private fun next() {

        if (expenseInput.text != null && expenseInput.text.isNotEmpty())
            expenseLogic()
        users.forEach { user -> user.btn.isChecked = false }

        expenseInput.text.clear()
    }

    private fun hideKeyboard(activity: Activity) {

        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        // Finds the currently focused view and grab the window token from it.
        var view = activity.currentFocus

        // If no view currently has focus, create a new one and grab a window token
        if (view == null) {
            view = View(activity)
        }

        // Actual hiding
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun expenseLogic() {

        // Number of users with which to distribute the expense
        var numberOfSelected: Int = 0
        users.forEach { user -> if (user.selected) numberOfSelected++ }

        val input = ((expenseInput.text.toString()).toDouble())

        val expenseToSave: Double = ((input / numberOfSelected) * 100.0) / 100.0

        if (numberOfSelected > 0)
            total += input

        // Assignment of the expense
        users.forEach { user -> if (user.selected) user.expense += expenseToSave }

    }

}