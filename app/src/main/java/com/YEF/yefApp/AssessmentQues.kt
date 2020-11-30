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
import kotlinx.android.synthetic.main.assessment_ques.app_bar
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
    private var dialog: AlertDialog? = null
    private var finalInternshipApply = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.assessment_ques)

        setSupportActionBar(app_bar)

        app_bar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            internshipAssessmentScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                if (toolbarMain == null) {
                    return@setOnScrollChangeListener;
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
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
        val internshipAnswers = arrayListOf<String>()
        internshipAnswers.add(inputEt.text.toString())
        internshipAnswers.add(inputEt2.text.toString())
        internshipAnswers.add(inputEt3.text.toString())
        user["internshipAnswers"] = internshipAnswers
        user["studentMail"] = userMail
        user["hired"] = "false"
        user["offerLetter"] = "false"
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().time)
        user["appliedDate"] = timeStamp
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
                    makeChangesInLayout()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
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
