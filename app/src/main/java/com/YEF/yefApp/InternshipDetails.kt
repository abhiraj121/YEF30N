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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internship_details)

        setSupportActionBar(app_bar)

        app_bar.setNavigationOnClickListener {
            finish()
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            internshipDetailsScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                if (toolbarMain == null) {
                    return@setOnScrollChangeListener;
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
                val i = Intent(this, AssessmentQues::class.java)
                i.putExtra("id", internshipID)
                startActivity(i)
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

}