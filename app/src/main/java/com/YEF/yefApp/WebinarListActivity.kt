package com.YEF.yefApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.WorkSourceUtil.getNames
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class WebinarListActivity : AppCompatActivity() {
var list: ListView?=null
    var adapter:adapter?=null
    var db:FirebaseFirestore?=null
    var names=ArrayList<String>()
    var dates = ArrayList<String>()
    var urls=ArrayList<String>()
var imgs=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.fragment_webinar_list__list)
        db=FirebaseFirestore.getInstance()
        list=findViewById(R.id.list)
        db!!.collection("Webinars").addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(this,"Error fetching data", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }
            for(doc in value!!){
                doc.getString("Name")?.let{names.add(it)}
                doc.getString("Date")?.let{dates.add(it)}
                doc.getString("pic")?.let{imgs.add(it)}
                doc.getString("url")?.let{urls.add(it)}

            }

            adapter = adapter(this, generateData(names,dates,imgs)!!)
            list?.adapter= adapter
            adapter?.notifyDataSetChanged()
        }
        list!!.setOnItemClickListener{parent,view,position,id->
            val url=urls[position]
            val p= Intent(Intent.ACTION_VIEW)
            p.data= Uri.parse(url)
            startActivity(p)
        }

    }
    private fun generateData(names: MutableList<String>, dates: MutableList<String>, imgs:MutableList<String>): ArrayList<WebinarResponse>? {
        // WebinarResponse webinar = ArrayList<WebinarResponse>();
        var arr= ArrayList<WebinarResponse>()
        for (i in names.indices) {
            var web = WebinarResponse(names[i], dates[i], imgs[i])
            arr.add(web)
        }
        return arr
    }

}
