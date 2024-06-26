package com.rangganf.storyappearly.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.rangganf.storyappearly.R

class CustomEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var errorIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        // Mendapatkan drawable untuk ikon error dari resources
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_error)!!

        // Menetapkan diri sendiri sebagai listener sentuhan
        setOnTouchListener(this)

        // Menambahkan listener untuk memantau perubahan teks
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak perlu melakukan apa-apa sebelum teks berubah
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Memeriksa panjang teks setiap kali terjadi perubahan
                handleTextChanges(s)
            }

            override fun afterTextChanged(p0: Editable?) {
                // Tidak perlu melakukan apa-apa setelah teks berubah
            }
        })
    }

    private fun handleTextChanges(s: CharSequence?) {
        // Memeriksa panjang teks dan menangani perubahan
        if ((s?.length ?: 0) < 8) {
            // Jika panjang teks kurang dari 8, menampilkan pesan kesalahan dan ikon error
            val errorMessage = resources.getString(R.string.password_error)
            setError(errorMessage, errorIcon)
            setCompoundDrawablesWithIntrinsicBounds(null, null, errorIcon, null)
        } else {
            // Jika panjang teks memenuhi syarat, menghapus pesan kesalahan dan ikon error
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        // Tidak ada tindakan sentuhan yang diimplementasikan saat ini
        return false
    }
}
