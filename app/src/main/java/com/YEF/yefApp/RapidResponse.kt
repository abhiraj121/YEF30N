package com.YEF.yefApp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_rapid_response.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class RapidResponse : AppCompatActivity() {

    private val rapidNum = "918147352238"
    val db by lazy { FirebaseFirestore.getInstance() }
    private val TAG = "checkMe"
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapid_response)

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Submitting Application")
                .setCancelable(false)
                .setTheme(R.style.CustomDialog)
                .build()

        backBtn.setOnClickListener {
            finish()
        }

        rapid2Btn.setOnClickListener {
            validateInput()
        }

    }

    private fun validateInput() {
        if (benName.text.toString() == "" ||
                benContact.text.toString()=="" ||
                benAddress.text.toString()=="" ||
                benOccupation.text.toString()=="" ||
                benID.text.toString()=="" ||
                benProblem.text.toString()=="" ||
                benDetails.text.toString()==""){
            val sk = Snackbar.make(rapidResponseLayout,"Please fill all fields", Snackbar.LENGTH_SHORT)
            sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
            sk.setBackgroundTint(Color.parseColor("#ECEDFF"))
            sk.setTextColor((Color.parseColor("#4D4D4D")))
            sk.show()
        }else{
            saveDataToFirebase()
        }
    }

    private fun saveDataToFirebase() {
        dialog!!.show()
        val beneficiaryName = benName.text.toString()
        val beneficiaryContactNumber = benContact.text.toString()
        val beneficiaryAddress = benAddress.text.toString()
        val beneficiaryAadharGovID = benID.text.toString()
        val beneficiaryOccupation = benOccupation.text.toString()
        val beneficiaryProblemsFaced = benProblem.text.toString()
        val beneficiaryVolunteerDetails = benDetails.text.toString()

        val user: MutableMap<String, Any> = HashMap()
        user["beneficiaryName"] = beneficiaryName
        user["beneficiaryNumber"] = beneficiaryContactNumber
        user["beneficiaryAdd"] = beneficiaryAddress
        user["beneficiaryAadhar"] = beneficiaryAadharGovID
        user["beneficiaryWork"] = beneficiaryOccupation
        user["beneficiaryProblems"] = beneficiaryProblemsFaced
        user["beneficiaryVolunteerDetails"] = beneficiaryVolunteerDetails
        user["userName"] = if (FirebaseAuth.getInstance().currentUser?.displayName != null) {
            FirebaseAuth.getInstance().currentUser!!.displayName!!
        } else {
            ""
        }
        user["userMailID"] = if (FirebaseAuth.getInstance().currentUser?.email != null) {
            FirebaseAuth.getInstance().currentUser!!.email!!
        } else {
            ""
        }
        user["userNumber"] = if (FirebaseAuth.getInstance().currentUser?.phoneNumber != null) {
            FirebaseAuth.getInstance().currentUser!!.phoneNumber!!
        } else {
            ""
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().time)
        user["applicationDate"] = timeStamp
        val userMail =  if (FirebaseAuth.getInstance().currentUser?.email != null) {
            FirebaseAuth.getInstance().currentUser!!.email!!
        } else {
            "guest-$beneficiaryName"
        }
        db.collection("rapidResponse")
                .document(userMail)
                .set(user)
                .addOnSuccessListener {
                    Log.d(TAG, "user answers upload successful")
                    dialog!!.dismiss()
                    openWhatsAppIntent(beneficiaryName)

                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

    }

    private fun openWhatsAppIntent(username:String) {
        val sendIntent = Intent()
//        val username = FirebaseAuth.getInstance().currentUser!!.displayName!!
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.setPackage("com.whatsapp")
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! This is $username, I wanna make an enquiry.")
        sendIntent.putExtra("jid", "$rapidNum@s.whatsapp.net")
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }
}