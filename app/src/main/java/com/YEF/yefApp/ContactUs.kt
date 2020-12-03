package com.YEF.yefApp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        view.locateIcon.setOnClickListener {
            openLocationIntent()
        }

        view.locateTv.setOnClickListener {
            openLocationIntent()
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

    private fun openLocationIntent() {
        val latitude = 28.688768f
        val longitude = 77.1303528f
        val strUri = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Only if initiating from a Broadcast Receiver

        val mapsPackageName = "com.google.android.apps.maps"
        if (isPackageExisted(requireContext(), mapsPackageName)) {
            i.setClassName(mapsPackageName, "com.google.android.maps.MapsActivity")
            i.setPackage(mapsPackageName)
        }
        startActivity(i)
    }

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

    private fun isPackageExisted(context: Context, targetPackage: String): Boolean {
        val pm = context.packageManager
        try {
            val info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }


}