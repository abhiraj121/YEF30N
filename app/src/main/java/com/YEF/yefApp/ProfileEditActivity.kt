package com.YEF.yefApp

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_edit.*


class ProfileEditActivity : AppCompatActivity() {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val TAG = "checkMe"
    private var userMail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        Log.d(TAG, "" + FirebaseAuth.getInstance().currentUser!!.uid)

        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage("Are you sure you want to save these changes ?")
        builder1.setCancelable(true)
        builder1.setIcon(android.R.drawable.ic_dialog_alert)
        builder1.setPositiveButton("OK") { dialog, id ->
            updateUserProfileData()
            dialog.cancel()
        }
        builder1.setNegativeButton("CANCEL") { dialog, id ->
            dialog.cancel()
        }
        val alert11 = builder1.create()!!

        backProfileBtn.setOnClickListener {
            finish()
        }

        updateProfileDetails.setOnClickListener {
//            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#547B87"))
//            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#547B87"))
            alert11.show()
        }

        userMail = FirebaseAuth.getInstance().currentUser!!.email!!

        fillDataToEditText()

    }

    private fun updateUserProfileData() {
        db.collection("users")
                .document(userMail)
                .update(mapOf(
                        "Full Name" to nameEt.text.toString(),
                        "Email Address" to mailEt.text.toString(),
                        "Mobile Number" to numberEt.text.toString(),
                        "city" to cityNameEt.text.toString()
                ))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                    val sk = Snackbar.make(updateProfileLayout, "Profile Updated", Snackbar.LENGTH_SHORT)
                    sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
                    sk.setBackgroundTint(Color.parseColor("#EFF6FF"))
                    sk.setTextColor((Color.parseColor("#1012B3")))
                    sk.show()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("SecSchool")
                .set(mapOf(
                        "cgpa" to schoolCGPAEt.text.toString(),
                        "schoolName" to schoolNameEt.text.toString(),
                        "endYear" to schoolEndYearEt.text.toString(),
                        "subjects" to schoolSubjectsEt.text.toString(),
                        "board" to schoolBoardEt.text.toString()
                ))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("SrSecSchool")
                .set(mapOf(
                        "cgpa" to srSchoolCGPAEt.text.toString(),
                        "schoolName" to srSchoolNameEt.text.toString(),
                        "endYear" to srSchoolEndYearEt.text.toString(),
                        "subjects" to srSchoolSubjectsEt.text.toString(),
                        "board" to srSchoolBoardEt.text.toString()
                ))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("College")
                .set(mapOf(
                        "cgpa" to clgCGPAEt.text.toString(),
                        "collegeName" to clgNameEt.text.toString(),
                        "course" to clgCourseEt.text.toString(),
                        "endYear" to clgEndYearEt.text.toString(),
                        "speciality" to clgSpecialityEt.text.toString(),
                        "startYear" to clgStartYearEt.text.toString()
                ))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }


    }

    private fun fillDataToEditText() {
        Log.w(TAG, "${FirebaseAuth.getInstance().currentUser?.email} reached")
        db.collection("users")
                .document(userMail)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.w(TAG, "${task.result?.data}")
                        if (task.result?.data?.isNotEmpty() == true) {
                            val name = task.result!!.data!!["Full Name"].toString()
                            val mail = task.result!!.data!!["Email Address"].toString()
                            val number = task.result!!.data!!["Mobile Number"].toString()
                            val city = task.result!!.data!!["city"].toString()

                            nameEt.setText(name)
                            mailEt.setText(mail)
                            numberEt.setText(number)
                            cityNameEt.setText(if (city == "null") {
                                ""
                            } else {
                                city
                            })
                        }
                        profileEditProgressBar.visibility = View.GONE
                        profileEditFieldLayout.visibility = View.VISIBLE

                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Reached onFailure " + it.message.toString())
                }

        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("SecSchool")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.data?.isNotEmpty() == true) {
                            val cgpa = task.result!!.data!!["cgpa"].toString()
                            val name = task.result!!.data!!["schoolName"].toString()
                            val end = task.result!!.data!!["endYear"].toString()
                            val subjects = task.result!!.data!!["subjects"].toString()
                            val board = task.result!!.data!!["board"].toString()

                            schoolCGPAEt.setText(cgpa)
                            schoolBoardEt.setText(board)
                            schoolEndYearEt.setText(end)
                            schoolNameEt.setText(name)
                            schoolSubjectsEt.setText(subjects)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }

        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("SrSecSchool")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.data?.isNotEmpty() == true) {
                            val cgpa = task.result!!.data!!["cgpa"].toString()
                            val name = task.result!!.data!!["schoolName"].toString()
                            val end = task.result!!.data!!["endYear"].toString()
                            val subjects = task.result!!.data!!["subjects"].toString()
                            val board = task.result!!.data!!["board"].toString()

                            srSchoolCGPAEt.setText(cgpa)
                            srSchoolBoardEt.setText(board)
                            srSchoolEndYearEt.setText(end)
                            srSchoolNameEt.setText(name)
                            srSchoolSubjectsEt.setText(subjects)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }

        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("College")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.data?.isNotEmpty() == true) {
                            val clgCGPA = task.result!!.data!!["cgpa"].toString()
                            val clgName = task.result!!.data!!["collegeName"].toString()
                            val clgCourse = task.result!!.data!!["course"].toString()
                            val clgEnd = task.result!!.data!!["endYear"].toString()
                            val clgSpecial = task.result!!.data!!["speciality"].toString()
                            val clgStart = task.result!!.data!!["startYear"].toString()

                            clgCGPAEt.setText(clgCGPA)
                            clgCourseEt.setText(clgCourse)
                            clgEndYearEt.setText(clgEnd)
                            clgStartYearEt.setText(clgStart)
                            clgSpecialityEt.setText(clgSpecial)
                            clgNameEt.setText(clgName)
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }
}