package com.YEF.yefApp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_donations2.*
import java.util.*

class DonationsActivity2 : AppCompatActivity() {

    private var paybtn: Button? = null
    private val PAY_REQUEST = 1
    private var amt = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donations2)

        amt = intent.getStringExtra("amount")!!
        val toDisplayInPay = "pay ₹$amt"
        btnpay.text = toDisplayInPay


        backBtn.setOnClickListener {
            finish()
        }

        cancelPay.setOnClickListener {
            finish()
        }

        paybtn = findViewById(R.id.btnpay)
        paybtn!!.setOnClickListener {
            val name = edtname!!.text.toString()
            val upiId = edtupiid!!.text.toString()
//            val amt = amttxt!!.text.toString()
            val msg = edtmsg!!.text.toString()
            val tnid = edttnid!!.text.toString()
            val refId = edtrefid!!.text.toString()
            if (name.isEmpty() || upiId.isEmpty()) {
                Toast.makeText(this, "Name and Upi Id is necessary", Toast.LENGTH_SHORT).show()
            } else payUsingUpi(name, upiId, amt, msg, tnid, refId)
        }

    }

    private fun payUsingUpi(name: String, upiId: String, amt: String, msg: String, trnId: String, refId: String) {
        val uri = Uri.Builder()
                .scheme("upi").authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", msg)
                .appendQueryParameter("am", amt)
                .appendQueryParameter("tid", trnId)
                .appendQueryParameter("tr", refId)
                .appendQueryParameter("cu", "INR")
                .build()
        val upiIntent = Intent(Intent.ACTION_VIEW)
        upiIntent.data = uri
        val chooser = Intent.createChooser(upiIntent, "Pay")
        if (chooser.resolveActivity(packageManager) != null) {
            startActivityForResult(chooser, PAY_REQUEST)
        } else {
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAY_REQUEST) {
            if (isInternetAvailable(this)) {
                if (data == null) {
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    Toast.makeText(this, "Transaction not complete", Toast.LENGTH_SHORT).show()
                } else {
                    val text = data.getStringExtra("response")
                    val dataList = ArrayList<String?>()
                    dataList.add(text)
                    upiPaymentCheck(text!!)
                }
            }
        }
    }

    private fun upiPaymentCheck(data: String) {
        var paymentCancel = ""
        var status = ""
        val response = data.split("&").toTypedArray()
        for (i in response.indices) {
            val equalStr = response[i].split("=").toTypedArray()
            if (equalStr.size >= 2) {
                if (equalStr[0].toLowerCase(Locale.ROOT) == "Status".toLowerCase(Locale.ROOT)) {
                    status = equalStr[1].toLowerCase(Locale.ROOT)
                }
            } else {
                paymentCancel = "Payment cancelled"
            }
        }
        when {
            status == "success" -> {
                Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show()
            }
            "Payment cancelled" == paymentCancel -> {
                Toast.makeText(this, "Payment Cancelled by User", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo!!.isConnected && networkInfo.isConnectedOrConnecting && networkInfo.isAvailable) {
            return true
        }
        return false
    }

}