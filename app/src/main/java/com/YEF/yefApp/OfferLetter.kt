package com.YEF.yefApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_offer_letter.*

class OfferLetter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_letter)

        backBtn.setOnClickListener {
            finish()
        }


    }
}