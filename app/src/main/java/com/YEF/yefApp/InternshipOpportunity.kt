package com.YEF.yefApp

import android.app.AlertDialog
import android.graphics.Color
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
import kotlinx.android.synthetic.main.fragment_internship_opportunity.view.*

class InternshipOpportunity : Fragment() {

    private val TAG = "checkMe"
    private var adapter: InternshipAdapter? = null
    private val internshipList = arrayListOf<InternshipModel>()
    private val db by lazy { FirebaseFirestore.getInstance() }
    private var appliedInternships = arrayListOf<String>()
    var dialog: AlertDialog? = null
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_internship_opportunity, container, false)

        view.app_bar.navigationIcon!!.setTint(Color.parseColor("#00000000"))
        
        dialog = SpotsDialog.Builder()
                .setContext(requireContext())
                .setMessage("Loading Internships")
                .setCancelable(false)
                .setTheme(R.style.CustomDialog)
                .build()
                .apply {
                    show()
                }

//        view.refreshLayout.setOnRefreshListener {
//            getInternshipsFromFirebase(view)
//            adapter = InternshipAdapter(requireContext(), internshipList)
//            val rv = view.findViewById<RecyclerView>(R.id.internship_rv)!!
//            rv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
//            rv.adapter = adapter
//        }

        getInternshipsFromFirebase(view)
//        adapter = InternshipAdapter(requireContext(), internshipList)
//        view.internship_rv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
//        view.internship_rv.adapter = adapter

        return view
    }

    private fun getInternshipsFromFirebase(v: View) {
        Log.d(TAG, "reached getInternshipsFromFirebase")
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
//                        dialog!!.dismiss()
                        getAppliedInternships()
//                        requireView().refreshLayout.isRefreshing = false
//                        adapter!!.notifyDataSetChanged()

                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun getAppliedInternships() {
        Log.d(TAG, "reached getAppliedInternships")
        val userMail = auth.currentUser?.email
//        val userMail = "g2bLOewpYlv8neiXEIwq"
        if (!userMail.isNullOrEmpty()) {
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
        } else {
            dialog!!.dismiss()
            adapter = InternshipAdapter(requireContext(), internshipList)
            val tempInternshipsAvailable = "Currently ${internshipList.size} new internships are available for you"
            requireView().availableInternshipNumber.text = tempInternshipsAvailable
            requireView().internship_rv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            requireView().internship_rv.adapter = adapter
        }
    }

    private fun filterAppliedInternships() {
        Log.d(TAG, "reached filterAppliedInternships")
        val finalListOfInternships = arrayListOf<InternshipModel>()
        finalListOfInternships.addAll(internshipList)
        Log.d(TAG, appliedInternships.toString())
        for (i in internshipList) {
            Log.d(TAG, i.internshipId)
            for (j in appliedInternships) {
                if (i.internshipId == j) {
                    if (internshipList.contains(i)) {
                        finalListOfInternships.remove(i)
                    }
                }
            }
        }
        dialog!!.dismiss()
        adapter = InternshipAdapter(requireContext(), finalListOfInternships)
        val tempInternshipsAvailable = "Currently ${finalListOfInternships.size} new internships are available for you"
        requireView().availableInternshipNumber.text = tempInternshipsAvailable
        requireView().internship_rv.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        requireView().internship_rv.adapter = adapter
    }

}