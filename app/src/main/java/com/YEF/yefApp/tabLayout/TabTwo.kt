package com.YEF.yefApp.tabLayout

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.YEF.yefApp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_tab2.view.*

class TabTwo : Fragment() {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val TAG = "checkMe"
    private lateinit var alert11: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tab2, container, false)
        getCollegeDetailsFromFirebase(view)
        getSrSchoolDetailsFromFirebase(view)
        getSchoolDetailsFromFirebase(view)

        val builder1 = AlertDialog.Builder(requireContext())
        builder1.setMessage("You haven't completed your profile yet. Please go to EDIT section to make changes.")
        builder1.setCancelable(true)
        builder1.setIcon(android.R.drawable.ic_dialog_alert)
//        builder1.setPositiveButton("OK") { dialog, id ->
////            updateUserProfileData()
//            dialog.cancel()
//        }
//        builder1.setNegativeButton("CANCEL") { dialog, id ->
//            dialog.cancel()
//        }
        alert11 = builder1.create()!!

        return view
    }

    private fun getSchoolDetailsFromFirebase(v: View) {
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("SecSchool")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.data?.isNotEmpty() == true) {
                            Log.d(TAG, task.result!!.data.toString())
                            val cgpa = task.result!!.data!!["cgpa"].toString()
                            val name = task.result!!.data!!["schoolName"].toString()
                            val end = task.result!!.data!!["endYear"].toString()
                            val subjects = task.result!!.data!!["subjects"].toString()
                            val board = task.result!!.data!!["board"].toString()

                            v.schoolBoard.text = board
                            v.schoolCGPA.text = cgpa
                            v.schoolEndYear.text = end
                            v.schoolSubjects.text = subjects
                            v.schoolName.text = name
                        } else {

                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun getSrSchoolDetailsFromFirebase(v: View) {
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
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

                            v.srSchoolCGPA.text = cgpa
                            v.srSchoolBoard.text = board
                            v.srSchoolEndYear.text = end
                            v.srSchoolName.text = name
                            v.srSchoolSubjects.text = subjects
                        } else {

                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun getCollegeDetailsFromFirebase(v: View) {
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
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

                            v.clgCGPA.text = clgCGPA
                            v.clgCourse.text = clgCourse
                            v.clgEndYear.text = clgEnd
                            v.clgStartYear.text = clgStart
                            v.clgSpeciality.text = clgSpecial
                            v.clgName.text = clgName

                            v.tab2FieldLayout.visibility = View.VISIBLE
                            v.tab2ProgressBar.visibility = View.GONE
                        } else {
                            v.tab2FieldLayout.visibility = View.VISIBLE
                            v.tab2FieldLayout.alpha = 0.3F
                            v.tab2ProgressBar.visibility = View.GONE
                            alert11.show()
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