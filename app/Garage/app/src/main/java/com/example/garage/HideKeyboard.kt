package com.example.garage

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Extension function for the Activity class to hide the soft keyboard.
 *
 * This function attempts to hide the keyboard by accessing the current focused view.
 * If no view is currently focused, it creates a new View instance to prevent errors.
 */
fun Activity.hideKeyboard() {
    // Get the current focused view or create a new View instance if no focus exists
    val view = currentFocus ?: View(this)

    // Retrieve the InputMethodManager service from the system
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Request to hide the soft input (keyboard) for the current window token
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}