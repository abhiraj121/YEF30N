package com.YEF.yefApp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_donations_activty.view.*
import kotlinx.android.synthetic.main.fragment_tab1.view.*

class DonationsFragment : Fragment() {

    private val TAG = "checkMe"
    private val db by lazy { FirebaseFirestore.getInstance() }
    val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.activity_donations_activty, container, false)

        setUserDetailsTop(view)

        view.next_button.setOnClickListener {
            if (view.amountTv.text.isNotEmpty()) {
                if (view.amountTv.text.toString().toInt() > 5) {
                    val i = Intent(requireContext(), DonationsActivity2::class.java)
                    i.putExtra("amount", view.amountTv.text.toString())
                    startActivity(i)
                }else{
                    val sk = Snackbar.make(view.donationsFragmentLayout,"Please select an amount more than 5", Snackbar.LENGTH_SHORT)
                    sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
                    sk.setBackgroundTint(Color.parseColor("#ECEDFF"))
                    sk.setTextColor((Color.parseColor("#4D4D4D")))
                    sk.show()
                }
            }else{
                val sk = Snackbar.make(view.donationsFragmentLayout,"Please select an amount", Snackbar.LENGTH_SHORT)
                sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
                sk.setBackgroundTint(Color.parseColor("#ECEDFF"))
                sk.setTextColor((Color.parseColor("#4D4D4D")))
                sk.show()
            }
        }

        return view
    }

    private fun setUserDetailsTop(view: View) {

//        view.currentUsername.text = auth.currentUser?.displayName
//        view.currentMobile.text = auth.currentUser?.phoneNumber
//        view.currentMail.text = auth.currentUser?.email

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
//                        val city = task.result!!.data!!["city"].toString()
                        val finalNumber = number.substring(3,13)

                        view.currentUsername.text = name
                        view.currentMobile.text = finalNumber
                        view.currentMail.text = mail


                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Reached onFailure "+it.message.toString())
                }



    }

}