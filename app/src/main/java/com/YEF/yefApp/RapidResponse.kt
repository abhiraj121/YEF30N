package com.YEF.yefApp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_rapid_response.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class RapidResponse : AppCompatActivity() {

    private val rapidNum = "918147352238"
    val db by lazy { FirebaseFirestore.getInstance() }
    private val TAG = "checkMe"
    private var dialog: AlertDialog? = null
    private var mStorageRef: StorageReference? = null
    private var GALLERY_REQUEST1 = 123
    private var GALLERY_REQUEST2 = 456
    private var img1Url = ""
    private var img2Url = ""
    private var img1: Uri? = null
    private var img2: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rapid_response)

        mStorageRef = FirebaseStorage.getInstance().reference

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Submitting Application")
                .setCancelable(false)
                .setTheme(R.style.CustomDialog)
                .build()

        backBtn.setOnClickListener {
            finish()
        }

        attachId1.setOnClickListener {
            openGalleryImagePicker(GALLERY_REQUEST1)
        }

        attachId2.setOnClickListener {
            openGalleryImagePicker(GALLERY_REQUEST2)
        }

        rapid2Btn.setOnClickListener {
            validateInput()
        }

    }

    private fun openGalleryImagePicker(requestCode: Int) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) when (requestCode) {
            GALLERY_REQUEST1 -> {
                val selectedImage = data!!.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    id1Layout.visibility = View.VISIBLE
                    govId1.setImageBitmap(bitmap)
                    img1 = selectedImage
                } catch (e: IOException) {
                    Log.i(TAG, "Some exception $e")
                }
            }
            GALLERY_REQUEST2 -> {
                val selectedImage = data!!.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                    id2Layout.visibility = View.VISIBLE
                    govId2.setImageBitmap(bitmap)
                    img2 = selectedImage
                } catch (e: IOException) {
                    Log.i(TAG, "Some exception $e")
                }
            }
        }
    }

    private fun validateInput() {
        if (benName.text.toString() == "" ||
                benContact.text.toString() == "" ||
                benAddress.text.toString() == "" ||
                benOccupation.text.toString() == "" ||
                img1 == null ||
                benProblem.text.toString() == "" ||
                volunteerName.text.toString() == "" ||
                volunteerNumber.text.toString() == "" ||
                img2 == null) {
            val sk = Snackbar.make(rapidResponseLayout, "Please fill all fields", Snackbar.LENGTH_SHORT)
            sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
            sk.setBackgroundTint(Color.parseColor("#ECEDFF"))
            sk.setTextColor((Color.parseColor("#4D4D4D")))
            sk.show()
        } else {
            dialog!!.show()
            uploadImageToFirebase1(img1!!)
//            uploadImageToFirebase2(img2!!)
//            saveDataToFirebase()
        }
    }

    private fun uploadImageToFirebase1(url: Uri) {
        val imgName = benName.text.toString().replace(" ", "_") + ".jpg"
        val ref1 = mStorageRef!!.child("RapidResponse/BeneficiaryGovID/$imgName")
        ref1.putFile(url)
                .addOnSuccessListener {
                    Log.d(TAG, "File Upload Done")

                    ref1.downloadUrl.addOnCompleteListener { task ->
                        val profileImageUrl = task.result.toString()
                        img1Url = profileImageUrl
                        uploadImageToFirebase2(img2!!)
                        Log.d(TAG, profileImageUrl)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "File Upload Failure")

                }
    }

    private fun uploadImageToFirebase2(url: Uri) {
        val imgName = benName.text.toString().replace(" ", "_") + ".jpg"
        val ref1 = mStorageRef!!.child("RapidResponse/VolunteerGovID/$imgName")
        ref1.putFile(url)
                .addOnSuccessListener {
                    Log.d(TAG, "File Upload Done")

                    ref1.downloadUrl.addOnCompleteListener { task ->
                        val profileImageUrl = task.result.toString()
                        img2Url = profileImageUrl
                        saveDataToFirebase()
                        Log.d(TAG, profileImageUrl)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "File Upload Failure")

                }
    }


    private fun saveDataToFirebase() {
        val beneficiaryName = benName.text.toString()
//        val beneficiaryContactNumber = benContact.text.toString()
//        val beneficiaryAddress = benAddress.text.toString()
////        val beneficiaryAadharGovID = benID.text.toString()
//        val beneficiaryOccupation = benOccupation.text.toString()
//        val beneficiaryProblemsFaced = benProblem.text.toString()
//        val beneficiaryVolunteerName = volunteerName.text.toString()
//        val beneficiaryVolunteerNumber = volunteerNumber.text.toString()
////        val beneficiaryVolunteerGovID = volunteerGovId.text.toString()

        val user: MutableMap<String, Any> = HashMap()
        user["beneficiaryName"] = benName.text.toString()
        user["beneficiaryNumber"] = benContact.text.toString()
        user["beneficiaryAdd"] = benAddress.text.toString()
        user["beneficiaryGovID"] = img1Url
        user["beneficiaryWork"] = benOccupation.text.toString()
        user["beneficiaryProblems"] = benProblem.text.toString()
        user["volunteerName"] = volunteerName.text.toString()
        user["volunteerNumber"] = volunteerNumber.text.toString()
        user["volunteerGovID"] = img2Url
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
        val newDate = timeStamp.subSequence(0, 4).toString() +
                "/" + timeStamp.subSequence(4, 6).toString() +
                "/" + timeStamp.subSequence(6, 8).toString()
        val newTime = timeStamp.subSequence(9, 11).toString() +
                ":" + timeStamp.subSequence(11, 13).toString() +
                ":" + timeStamp.subSequence(13, 15).toString()
        user["applicationDate"] = newDate
        user["applicationTime"] = newTime
        val userMail = if (FirebaseAuth.getInstance().currentUser?.email != null) {
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

    private fun openWhatsAppIntent(username: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.setPackage("com.whatsapp")
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! This is $username, I wanna make an enquiry.")
        sendIntent.putExtra("jid", "$rapidNum@s.whatsapp.net")
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }
}