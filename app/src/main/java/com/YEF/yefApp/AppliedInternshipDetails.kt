package com.YEF.yefApp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.applied_internship_card.*


class AppliedInternshipDetails : AppCompatActivity() {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val TAG = "checkMe"
    private var internshipID = ""
    private var data1 = arrayListOf<String>()
    private var data2 = arrayListOf<String>()
    private var adapter1: MyAdapter? = null
    private var adapter2: MyAdapter? = null
    var dialog: AlertDialog? = null
    private var internshipName = ""
    private var appliedInternshipAnswers = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.applied_internship_card)

        setSupportActionBar(app_bar)

        app_bar.setNavigationOnClickListener {
            finish()
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            appliedInternshipDetailsScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                if (toolbarMain == null) {
                    return@setOnScrollChangeListener;
                }

                if (!appliedInternshipDetailsScrollView.canScrollVertically(-1)) {
                    // we have reached the top of the list
                    toolbarMain.elevation = 0f
                } else {
                    // we are not at the top yet
                    toolbarMain.elevation = 10f
                }

            }
        }


        internshipID = intent.getStringExtra("internship_id")!!

        getInternshipDetailsFromFirebase()

        offerLetter.setOnClickListener {
            val i = Intent(this, OfferLetter::class.java)
            startActivity(i)
        }

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading Details")
                .setCancelable(false)
                .setTheme(R.style.CustomDialog)
                .build()
                .apply {
                    show()
                }

        dialog?.show()

    }

    private fun getInternshipDetailsFromFirebase() {
        db.collection("internships")
                .document(internshipID)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.w(TAG, "document : ${task.result?.data}")

                        data1 = if (task.result!!.data!!["responsibilities"] == null) {
                            arrayListOf("")
                        } else {
                            task.result!!.data!!["responsibilities"] as ArrayList<String>
                        }

                        data2 = if (task.result!!.data!!["conditions"] == null) {
                            arrayListOf("")
                        } else {
                            task.result!!.data!!["conditions"] as ArrayList<String>
                        }
                        internship_name.text = task.result!!.data!!["title"].toString()
                        internshipName = task.result!!.data!!["title"].toString()
                        internship_org.text = "YEF"
                        internship_type.text = task.result!!.data!!["type"].toString()
                        val womenAllowed = if (task.result!!.data!!["women"] == null) {
                            true
                        } else {
                            task.result!!.data!!["women"] as Boolean
                        }
                        if (womenAllowed) {
                            womenJoin.text = "Women wanting to start/restart their career can also apply"
                        }
                        perksTv.text = task.result!!.data!!["perks"].toString()

                        if (data1.isNotEmpty()) {
                            adapter1 = MyAdapter(this, data1)
                            listView1.adapter = adapter1
                            ListUtils.setDynamicHeight(listView1)
                        }

                        if (data2.isNotEmpty()) {
                            adapter2 = MyAdapter(this, data2)
                            listView2.adapter = adapter2
                            ListUtils.setDynamicHeight(listView2)
                        }
                        getAppliedInternshipAnswers()
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun getAppliedInternshipAnswers() {
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
        db.collection("users")
                .document(userMail)
                .collection("appliedInternships")
                .document(internshipName)
                .get()
                .addOnSuccessListener {
                    appliedInternshipAnswers = it.data!!["internshipAnswers"] as ArrayList<String>
                    checkIfHired()
                    Log.d(TAG, appliedInternshipAnswers.toString())
                    answer1.text = appliedInternshipAnswers[0]
                    answer2.text = appliedInternshipAnswers[1]
                    answer3.text = appliedInternshipAnswers[2]
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    private fun checkIfHired() {
        val userMail = FirebaseAuth.getInstance().currentUser!!.email!!
        db.collection("applications")
                .document(internshipName)
                .collection("studentsApplied")
                .document(FirebaseAuth.getInstance().currentUser!!.email.toString())
                .get()
                .addOnSuccessListener {
                    if ((it.data!!["offerLetter"].toString()) == "true") {
                        offerLetter.visibility = View.VISIBLE
                    }
                    when (it.data!!["hired"].toString()) {
                        "true" -> {
                            appliedHiredBtn.visibility = View.VISIBLE
                            appliedHiredBtn.text = "HIRED"
                            appliedHiredBtn.setTextColor(Color.parseColor("#00C100"))
                            appliedHiredBtn.background = ContextCompat.getDrawable(this, R.drawable.applied_internship_hired_bg)
                        }
                        "false" -> {
                            appliedHiredBtn.visibility = View.VISIBLE
                            appliedHiredBtn.text = "APPLIED"
                            appliedHiredBtn.setTextColor(Color.parseColor("#2196F3"))
                            appliedHiredBtn.background = ContextCompat.getDrawable(this, R.drawable.applied_internship_applied_bg)
                        }
                        "rejected" -> {
                            appliedHiredBtn.visibility = View.VISIBLE
                            appliedHiredBtn.text = "REJECTED"
                            appliedHiredBtn.setTextColor(Color.parseColor("#F13333"))
                            appliedHiredBtn.background = ContextCompat.getDrawable(this, R.drawable.applied_internship_rejected_bg)
                        }
                    }
                    dialog!!.dismiss()
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    object ListUtils {
        fun setDynamicHeight(mListView: ListView) {
            val mListAdapter = mListView.adapter
            var height = 0
            val desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.width, View.MeasureSpec.UNSPECIFIED)
            for (i in 0 until mListAdapter.count) {
                val listItem: View = mListAdapter.getView(i, null, mListView)
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                height += listItem.measuredHeight
            }
            val params = mListView.layoutParams
            params.height = 2 * (height + mListView.dividerHeight * (mListAdapter.count - 1))
            mListView.layoutParams = params
            mListView.requestLayout()
        }
    }

    class MyAdapter(private var context: Context, private var data: ArrayList<String>) : BaseAdapter() {
        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val vi = LayoutInflater.from(context).inflate(R.layout.items_card, parent, false)
            val response = vi.findViewById(R.id.respo) as TextView
            response.text = data[position]
            return vi
        }
    }

}