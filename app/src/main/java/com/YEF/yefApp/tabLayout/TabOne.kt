package com.YEF.yefApp.tabLayout

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.YEF.yefApp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_tab1.view.*

class TabOne : Fragment() {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val TAG = "checkMe"
//    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab1, container, false)

//        Log.w(TAG, "${FirebaseAuth.getInstance().currentUser?.email}")

        getDataFromFirebase(view)

//        dialog = SpotsDialog.Builder()
//                .setContext(requireContext())
//                .setMessage("Loading")
//                .setCancelable(false)
//                .setTheme(R.style.CustomDialog)
//                .build()
//                .apply {
//                    show()
//                }

        return view
    }

    private fun getDataFromFirebase(v: View) {
//        Log.w(TAG, "${FirebaseAuth.getInstance().currentUser?.email} reached")
        val userMail = FirebaseAuth.getInstance().currentUser!!.email.toString()
        db.collection("users")
                .document(userMail)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.w(TAG, "${task.result?.data}")

                        val name = task.result!!.data!!["Full Name"].toString()
                        val mail = task.result!!.data!!["Email Address"].toString()
                        val number = task.result!!.data!!["Mobile Number"].toString()
                        val city = task.result!!.data!!["city"].toString()
                        val finalNumber = number.substring(0,3)+"-"+number.substring(3,13)

                        v.nameTv.text = name
                        v.eemail.text = mail
                        v.em2.text = finalNumber
                        v.cityName.text = if (city == "null"){
                            ""
                        }else{
                            city
                        }
//                        dialog!!.dismiss()
                        v.tab1ProgressBar.visibility = View.GONE
                        v.tab1FieldLayout.visibility = View.VISIBLE

                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Reached onFailure "+it.message.toString())
                }
    }

}