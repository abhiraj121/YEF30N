package com.YEF.yefApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_offer_letter.*

class OfferLetter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_letter)

        backBtn.setOnClickListener {
            finish()
        }


        getOfferLetterBtn.setOnClickListener {
            val link = "https://www.google.com/"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
    }
}