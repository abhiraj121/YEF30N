package com.YEF.yefApp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.assessment_ques.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AssessmentQues : AppCompatActivity() {

    private val TAG = "checkMe"
    val db by lazy { FirebaseFirestore.getInstance() }
    private var mAuth: FirebaseAuth? = null
    private var internshipDuration = ""
    private var internshipId = ""
    private var internshipName = ""
    val userMail by lazy { FirebaseAuth.getInstance().currentUser!!.email!! }

    //    val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
    private var dialog: AlertDialog? = null
    private var finalInternshipApply = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.assessment_ques)

        setSupportActionBar(app_bar)

        getProfileDataFromFirebase()

        app_bar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            internshipAssessmentScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                if (toolbarMain == null) {
                    return@setOnScrollChangeListener
                }

                if (!internshipAssessmentScrollView.canScrollVertically(-1)) {
                    // we have reached the top of the list
                    toolbarMain.elevation = 0f
                } else {
                    // we are not at the top yet
                    toolbarMain.elevation = 10f
                }

            }
        }



        mAuth = FirebaseAuth.getInstance()

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Applying")
                .setCancelable(false)
                .setTheme(R.style.CustomDialog)
                .build()

        internshipId = intent.getStringExtra("id")!!

        getInternshipDurationFromFirebase()

        submit.setOnClickListener {
            validateAnswers()
        }
    }

    private fun getInternshipDurationFromFirebase() {
        db.collection("internships")
                .document(internshipId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        internshipName = task.result!!.data!!["title"].toString()
                        internshipDuration = task.result!!.data!!["duration"].toString()
                        val period = "Are you available for $internshipDuration ?"
                        q2.text = period

                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun validateAnswers() {
        if (inputEt.text.isEmpty() || inputEt2.text.isEmpty() || inputEt3.text.isEmpty()) {
            Toast.makeText(this, "The fields cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            dialog!!.show()
            storeAnswers()
        }
    }


    private fun storeAnswers() {
        val user: MutableMap<String, Any> = HashMap()
        val internshipAnswers = arrayListOf<String>()
        internshipAnswers.add(inputEt.text.toString())
        internshipAnswers.add(inputEt2.text.toString())
        internshipAnswers.add(inputEt3.text.toString())
        Log.d(TAG, internshipAnswers.toString())
        user["internshipID"] = internshipId
        user["internshipAnswers"] = internshipAnswers
        user["internshipName"] = internshipName
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
        db.collection("users")
                .document(userMail)
                .collection("appliedInternships")
                .document(internshipName)
                .set(user)
                .addOnSuccessListener {
                    Log.d(TAG, "user answers upload successful")
                    creatingTempVariable()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private fun creatingTempVariable() {
        val user: MutableMap<String, Any> = HashMap()
        user["temp"] = "temp"
        db.collection("applications")
                .document(internshipName)
                .set(user)
                .addOnSuccessListener {
                    Log.d(TAG, "user answers upload successful")
                    addInternshipToApplications()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private fun addInternshipToApplications() {
        val user: MutableMap<String, Any> = HashMap()
        val internshipAnswers = arrayListOf<String>()
        internshipAnswers.add(inputEt.text.toString())
        internshipAnswers.add(inputEt2.text.toString())
        internshipAnswers.add(inputEt3.text.toString())
        user["internshipAnswers"] = internshipAnswers
        user["studentMail"] = userMail
        user["hired"] = "false"
        user["offerLetter"] = "false"
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().time)
        val newDate = timeStamp.subSequence(0, 4).toString() +
                "/" + timeStamp.subSequence(4, 6).toString() +
                "/" + timeStamp.subSequence(6, 8).toString()
        val newTime = timeStamp.subSequence(9, 11).toString() +
                ":" + timeStamp.subSequence(11, 13).toString() +
                ":" + timeStamp.subSequence(13, 15).toString()
        user["appliedDate"] = newDate
        user["appliedTime"] = newTime
        user["studentPhone"] = if (FirebaseAuth.getInstance().currentUser?.phoneNumber != null) {
            FirebaseAuth.getInstance().currentUser!!.phoneNumber!!
        } else {
            ""
        }
        db.collection("applications")
                .document(internshipName)
                .collection("studentsApplied")
                .document(userMail)
                .set(user)
                .addOnSuccessListener {
                    Log.d(TAG, "user answers upload successful")
                    dialog!!.dismiss()
                    addProfileDataToApplications()
//                    makeChangesInLayout()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    private var secSchoolName = ""
    private var secSchoolCGPA = ""
    private var secSchoolEnd = ""
    private var secSchoolSubjects = ""
    private var secSchoolBoard = ""

    private var srSecSchoolName = ""
    private var srSecSchoolCGPA = ""
    private var srSecSchoolEnd = ""
    private var srSecSchoolSubjects = ""
    private var srSecSchoolBoard = ""

    private var collegeName = ""
    private var collegeCGPA = ""
    private var collegeCourse = ""
    private var collegeEnd = ""
    private var collegeStart = ""
    private var collegeSpecial = ""

    private fun getProfileDataFromFirebase() {
        Log.w(TAG, "${FirebaseAuth.getInstance().currentUser?.email} reached")
        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("SecSchool")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.data?.isNotEmpty() == true) {
                            secSchoolCGPA = task.result!!.data!!["cgpa"].toString()
                            secSchoolName = task.result!!.data!!["schoolName"].toString()
                            secSchoolEnd = task.result!!.data!!["endYear"].toString()
                            secSchoolSubjects = task.result!!.data!!["subjects"].toString()
                            secSchoolBoard = task.result!!.data!!["board"].toString()
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
                            srSecSchoolCGPA = task.result!!.data!!["cgpa"].toString()
                            srSecSchoolName = task.result!!.data!!["schoolName"].toString()
                            srSecSchoolEnd = task.result!!.data!!["endYear"].toString()
                            srSecSchoolSubjects = task.result!!.data!!["subjects"].toString()
                            srSecSchoolBoard = task.result!!.data!!["board"].toString()
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
                            collegeCGPA = task.result!!.data!!["cgpa"].toString()
                            collegeName = task.result!!.data!!["collegeName"].toString()
                            collegeCourse = task.result!!.data!!["course"].toString()
                            collegeEnd = task.result!!.data!!["endYear"].toString()
                            collegeSpecial = task.result!!.data!!["speciality"].toString()
                            collegeStart = task.result!!.data!!["startYear"].toString()
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun addProfileDataToApplications() {
        db.collection("applications")
                .document(internshipName)
                .collection("studentsApplied")
                .document(userMail)
                .collection("educationDetails")
                .document("secSchool")
                .set(mapOf(
                        "cgpa" to secSchoolCGPA,
                        "schoolName" to secSchoolName,
                        "endYear" to secSchoolEnd,
                        "subjects" to secSchoolSubjects,
                        "board" to secSchoolBoard
                ))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        db.collection("applications")
                .document(internshipName)
                .collection("studentsApplied")
                .document(userMail)
                .collection("educationDetails")
                .document("SrSecSchool")
                .set(mapOf(
                        "cgpa" to srSecSchoolCGPA,
                        "schoolName" to srSecSchoolName,
                        "endYear" to srSecSchoolEnd,
                        "subjects" to srSecSchoolSubjects,
                        "board" to srSecSchoolBoard
                ))
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

        db.collection("applications")
                .document(internshipName)
                .collection("studentsApplied")
                .document(userMail)
                .collection("educationDetails")
                .document("College")
                .set(mapOf(
                        "cgpa" to collegeCGPA,
                        "collegeName" to collegeName,
                        "course" to collegeCourse,
                        "endYear" to collegeEnd,
                        "speciality" to collegeSpecial,
                        "startYear" to collegeStart
                ))
                .addOnSuccessListener {
                    makeChangesInLayout()
                    Log.d(TAG, "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    private fun makeChangesInLayout() {
        submit.text = "APPLIED"
        submit.setTextColor(Color.WHITE)
        submit.setBackgroundColor(resources.getColor(R.color.red))
        submit.isEnabled = false
        appliedForThisInternship.visibility = View.VISIBLE
        finalInternshipApply = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (finalInternshipApply) {
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }

}
