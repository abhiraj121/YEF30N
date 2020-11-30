package com.YEF.yefApp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_applied_internships.view.*
import kotlinx.android.synthetic.main.fragment_internship_opportunity.view.*
import java.util.*

class AppliedInternships : Fragment() {

    private val TAG = "checkMe"
    private var adapter: AppliedInternshipAdapter? = null
    private val internshipList = arrayListOf<InternshipModel>()
    private val db by lazy { FirebaseFirestore.getInstance() }
    private var appliedInternships = arrayListOf<String>()
    private var dialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_applied_internships, container, false)

        dialog = SpotsDialog.Builder()
                .setContext(requireContext())
                .setMessage("Loading Applications")
                .setCancelable(false)
                .setTheme(R.style.CustomDialog)
                .build()
                .apply {
                    show()
                }

        getInternshipsFromFirebase(view)

        return view
    }

    private fun getInternshipsFromFirebase(v: View) {
        db.collection("internships")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            if (document.exists()) {
//                                Log.w(TAG, "document" + document.data.toString())
//                                Log.w(TAG, "document" + document.id.toString())
                                internshipList.add(InternshipModel(
                                        document.id,
                                        document.data["title"].toString(),
                                        document.data["type"].toString(),
                                        if (document.data["responsibilities"] == null) {
                                            arrayListOf("")
                                        } else {
                                            document.data["responsibilities"] as ArrayList<String>
                                        },
                                        document.data["opportunities"].toString(),
                                        document.data["skills"].toString(),
                                        if (document.data["conditions"] == null) {
                                            arrayListOf("")
                                        } else {
                                            document.data["conditions"] as ArrayList<String>
                                        },
                                        if (document.data["women"] == null) {
                                            true
                                        } else {
                                            document.data["women"] as Boolean
                                        },
                                        document.data["duration"].toString(),
                                        document.data["perks"].toString()
                                ))
                            }
                        }
                        getAppliedInternships()
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun getAppliedInternships() {
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
        db.collection("users")
                .document(userMail)
                .collection("appliedInternships")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            if (document.exists()) {
                                appliedInternships.add(document["internshipID"].toString())
                            }
                        }
                        filterAppliedInternships()
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun filterAppliedInternships() {
        val finalListOfInternships = arrayListOf<InternshipModel>()
//        finalListOfInternships.addAll(internshipList)
        Log.d(TAG, appliedInternships.toString())
        for (i in internshipList) {
            Log.d(TAG, i.internshipId)
            for (j in appliedInternships) {
                if (i.internshipId == j) {
                    if (internshipList.contains(i)) {
                        finalListOfInternships.add(i)
                    }
                }
            }
        }
        dialog!!.dismiss()
        adapter = AppliedInternshipAdapter(requireContext(), finalListOfInternships)
        if (finalListOfInternships.size==0){
            requireView().noInternshipAppliedLayout.visibility = View.VISIBLE
        }else {
            requireView().numberOfAppliedInternship.visibility = View.VISIBLE
            requireView().appliedInternshipsRv.visibility = View.VISIBLE
            val tempInternshipsApplied = "You've just applied for ${finalListOfInternships.size} internships, explore for new opportunities!"
            requireView().appliedInternshipNumber.text = tempInternshipsApplied
        }
        requireView().appliedInternshipsRv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        requireView().appliedInternshipsRv.adapter = adapter
    }
}