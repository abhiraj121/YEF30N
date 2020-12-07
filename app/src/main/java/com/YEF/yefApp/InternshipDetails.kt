package com.YEF.yefApp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_internship_details.*
import kotlinx.android.synthetic.main.fragment_tab2.view.*

class InternshipDetails : AppCompatActivity() {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val TAG = "checkMe"
    private var internshipID = ""
    private var data1 = arrayListOf<String>()
    private var data2 = arrayListOf<String>()
    private var adapter1: MyAdapter? = null
    private var adapter2: MyAdapter? = null
    var dialog: AlertDialog? = null
    private val auth by lazy { FirebaseAuth.getInstance() }
    var userMail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internship_details)

        userMail = FirebaseAuth.getInstance().currentUser?.email.toString()

        setSupportActionBar(app_bar)

        app_bar.setNavigationOnClickListener {
            finish()
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            internshipDetailsScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (toolbarMain == null) {
                    return@setOnScrollChangeListener
                }
                if (!internshipDetailsScrollView.canScrollVertically(-1)) {
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

        next_button.setOnClickListener {
            if (auth.currentUser?.email.isNullOrEmpty()) {
                val sk = Snackbar.make(internshipDetailsLayout, "Please Register or Login before Applying", Snackbar.LENGTH_SHORT)
                sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
                sk.setBackgroundTint(Color.parseColor("#ECEDFF"))
                sk.setTextColor((Color.parseColor("#4D4D4D")))
                sk.show()
            } else {
//                val i = Intent(this, AssessmentQues::class.java)
//                i.putExtra("id", internshipID)
//                startActivity(i)

//                if (GlobalScope.launch {
//                            checkProfileAvailable()
//                        }.isCompleted) {
//                    val i = Intent(this, AssessmentQues::class.java)
//                    i.putExtra("id", internshipID)
//                    startActivity(i)
//                }
//                if (checkProfileAvailable()) {
//                    val i = Intent(this, AssessmentQues::class.java)
//                    i.putExtra("id", internshipID)
//                    startActivity(i)
//                } else {
//                    val sk = Snackbar.make(internshipDetailsLayout, "Please complete your profile before applying", Snackbar.LENGTH_SHORT)
//                    sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
//                    sk.setBackgroundTint(Color.parseColor("#ECEDFF"))
//                    sk.setTextColor((Color.parseColor("#4D4D4D")))
//                    sk.show()
//                }

                checkProfileAvailable(object : MyCallback {
                    override fun onCallback(value: Boolean) {
                        if (value) {
                            val i = Intent(this@InternshipDetails, AssessmentQues::class.java)
                            i.putExtra("id", internshipID)
                            startActivity(i)
                        } else {
                            val sk = Snackbar.make(internshipDetailsLayout, "Please complete your profile before applying", Snackbar.LENGTH_SHORT)
                            sk.animationMode = Snackbar.ANIMATION_MODE_SLIDE
                            sk.setBackgroundTint(Color.parseColor("#ECEDFF"))
                            sk.setTextColor((Color.parseColor("#4D4D4D")))
                            sk.show()
                        }
                    }
                })
            }
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
                        dialog!!.dismiss()

                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message.toString())
                }
    }

    object ListUtils {
        fun setDynamicHeight(mListView: ListView) {
            val mListAdapter = mListView.adapter
            var height = 0
            val desiredWidth = MeasureSpec.makeMeasureSpec(mListView.width, MeasureSpec.UNSPECIFIED)
            for (i in 0 until mListAdapter.count) {
                val listItem: View = mListAdapter.getView(i, null, mListView)
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
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

    private fun checkProfileAvailable(callback: MyCallback) {
        var cgpa = ""
        var name = ""
        var end = ""
        var subjects = ""
        var board = ""
        db.collection("users")
                .document(userMail)
                .collection("educationDetails")
                .document("SecSchool")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.data?.isNotEmpty() == true) {
                            Log.d(TAG, task.result!!.data.toString())
                            cgpa = task.result!!.data!!["cgpa"].toString()
                            name = task.result!!.data!!["schoolName"].toString()
                            end = task.result!!.data!!["endYear"].toString()
                            subjects = task.result!!.data!!["subjects"].toString()
                            board = task.result!!.data!!["board"].toString()
                            //call the callback here
                            if (cgpa != "" || name != "" || end != "" || subjects != "" || board != "") {
                                callback.onCallback(true)
                            } else {
                                callback.onCallback(false)
                            }
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

interface MyCallback {
    fun onCallback(value: Boolean)
}