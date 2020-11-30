package com.YEF.yefApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_about_us.view.*
import kotlinx.android.synthetic.main.fragment_contact_us.view.*

class ContactUs : Fragment() {

    val num = "9311698690"
    val rapidNum = "918147352238"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_us, container, false)

        view.phoneIcon.setOnClickListener {
            openPhoneIntent()
        }

        view.phoneTv.setOnClickListener {
            openPhoneIntent()
        }

        view.mailIcon.setOnClickListener {
            openMailIntent()
        }

        view.mailTv.setOnClickListener {
            openMailIntent()
        }

        view.rapidBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), RapidResponse::class.java))
        }

        return view
    }

//    private fun openWhatsappIntent() {
//        val sendIntent = Intent()
////        val username = FirebaseAuth.getInstance().currentUser!!.displayName!!
//        val username = "Abhiraj Sharma"
//        sendIntent.action = Intent.ACTION_SEND
//        sendIntent.setPackage("com.whatsapp")
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! This is $username, I wanna make an enquiry.")
//        sendIntent.putExtra("jid", rapidNum + "@s.whatsapp.net")
//        sendIntent.type = "text/plain"
//        startActivity(sendIntent)
//    }

    private fun openMailIntent() {
        val mail = "contact@yefindia.org"
        val i = Intent()
        i.action = Intent.ACTION_SENDTO
        i.data = Uri.parse("mailto:$mail")
        i.putExtra(Intent.EXTRA_SUBJECT, "YEF Enquiry")
        startActivity(i)
    }

    private fun openPhoneIntent() {
        val i = Intent()
        i.action = Intent.ACTION_DIAL
        i.data = Uri.parse("tel:$num")
        startActivity(i)
    }

}