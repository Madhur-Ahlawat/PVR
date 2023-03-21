package com.net.pvr.utils

import android.widget.EditText
import java.util.regex.Pattern


object InputTextValidator {
    private const val EMAIL_REGEX =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    private const val PASSWORD_REGEX = "^.(?=.{6,})(?=.*[a-z])(?=.*[\\\\d])(?=.*[\\\\S]).*$"
    private const val NUMBER_REGEX = "^[123456789][0-9]{9}?$"

    fun validateEmail(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else Pattern.matches(EMAIL_REGEX, text)
    }

    fun validatePhone(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length == 10
    }

    fun validatePassword(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 8
    }

    fun validatePasswordnew(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 6
    }

    fun validateComment(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 15
    }

    fun validateNumber(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else Pattern.matches(NUMBER_REGEX, text)
    }

    fun validatePhoneNumber(number: String): Boolean {
        val text = number.trim { it <= ' ' }
        if (text.isEmpty()) return false
        val regx = "^(?:[0-9]|\\+91|0)[0-9]+" //[0-9]+
        return text.matches(regx.toRegex()) && (text.length == 10 || text.length == 11 || text.length == 13)
    }

//    fun validateEmail(email: String): Boolean {
//        val text = email.trim { it <= ' ' }
//        return if (text.isEmpty()) false else Pattern.matches(EMAIL_REGEX, text)
//    }

    //    public static boolean validatePassword(EditText editTextPassword,EditText editTextConfirmPassword) {
    //        String password = editTextPassword.getText().toString().trim();
    //        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
    //
    //        if ((password.length() == 0)|| (confirmPassword.length() == 0))
    //            return false;
    //
    //        else if(!password.equals(confirmPassword))
    //            return false;
    //
    //        else
    //            return true;
    //    }
    fun hasText(editText: EditText): Boolean {
        val string = editText.text.toString().trim { it <= ' ' }
        return !(string == null || string.length == 0)
    }

    fun checkFullName(editText: EditText): Boolean {
        val string = editText.text.toString().trim { it <= ' ' }
        return if (string != null && string.isNotEmpty()) {
            val nameArr = string.trim { it <= ' ' }.split(" ").toTypedArray()
            nameArr.size > 1
        } else {
            false
        }
    }

    fun validatePin(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length == 6
    }

    fun validatePCMilesMorePin(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length == 5
    }

    fun validateCard(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 12
    }

    fun validateSixteenDigitsCard(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else text.length >= 16
    }

    fun validatePromoCode(editText: EditText): Boolean {
        val text = editText.text.toString().trim { it <= ' ' }
        return if (text.isEmpty()) false else !(text.length < 6 || text.length > 16)
    }
}