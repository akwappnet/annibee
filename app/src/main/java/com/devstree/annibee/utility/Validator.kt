package com.devstree.annibee.utility

import android.text.TextUtils
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern


/**
 * Created by Dhaval Baldha on 6/1/21
 */
object Validator {
    const val PASSWORD_PATTERN1 = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[\\w~@#\$%^&*+=`|{}:;!.?\"()\\[\\]-](?=\\S+$).{7,}$"
    const val PATTERN = "^[0-9]*\$"
    const val USER_ID_PATTERN = "^[a-z0-9]*\$"
    const val PASSWORD_PATTERN = "^[a-zA-Z0-9]*\$"

    fun isValidPassword(password: String?): Boolean {
        if (password.isNullOrEmpty()) return false
        val matcher = Pattern.compile(PASSWORD_PATTERN1).matcher(password)
        return matcher.matches()
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        if (target == null) return false
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun isEmptyFieldValidate(strField: String): Boolean {
        var isEmptyField = false
        if (strField.isEmpty()) isEmptyField = true
        return isEmptyField
    }

    fun isPasswordValidate(password: String): Boolean {
        var isValid = false
        isValid = password.length >= 8 && !Pattern.compile(PATTERN).matcher(password).matches()
                && Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()
        return isValid
    }

    fun isUserIdValidate(userId: String): Boolean {
        var isValid = false

        isValid = userId.length >= 8 && !Pattern.compile(PATTERN).matcher(userId).matches()
                && Pattern.compile(USER_ID_PATTERN).matcher(userId).matches()
        return isValid
    }

    fun isPhoneNumberValidate(number: String): Boolean {
        var isValid = false
        isValid = number.isNotEmpty() && number.length == 10
        return isValid
    }

    fun setError(edtView: AppCompatEditText, msg: String?) {
        edtView.error = msg
        edtView.requestFocus()
    }
}