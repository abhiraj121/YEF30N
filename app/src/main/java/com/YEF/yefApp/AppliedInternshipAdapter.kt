package com.YEF.yefApp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppliedInternshipAdapter(var context: Context, var list: ArrayList<InternshipModel>) : RecyclerView.Adapter<AppliedInternshipAdapter.InternshipViewHolder>() {

    class InternshipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var internshipImage: ImageView = view.findViewById(R.id.internship_img_view)
        var internshipName: TextView = view.findViewById(R.id.internship_name)
        var internshipOrg: TextView = view.findViewById(R.id.internship_org)
        var internshipView: View = view.findViewById(R.id.internship_layout)
        var currentLayout: LinearLayout = view.findViewById(R.id.extraInternshipDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InternshipViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.internship_card, parent, false)
        return InternshipViewHolder(view)
    }

    override fun onBindViewHolder(holder: InternshipViewHolder, position: Int) {
        val currentInternship = list[position]
        holder.currentLayout.visibility = View.GONE
        holder.internshipName.text = currentInternship.title
        holder.internshipOrg.text = "YEF"
        holder.internshipImage.setImageResource(R.drawable.yef_logo)
        holder.internshipView.setOnClickListener {
            val i = Intent(context, AppliedInternshipDetails::class.java)
            i.putExtra("internship_id", currentInternship.internshipId)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int = list.size


}