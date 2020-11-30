package com.YEF.yefApp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InternshipAdapter(var context: Context, var list: ArrayList<InternshipModel>) : RecyclerView.Adapter<InternshipAdapter.InternshipViewHolder>() {

    class InternshipViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var internshipImage: ImageView = view.findViewById(R.id.internship_img_view)
        var internshipName: TextView = view.findViewById(R.id.internship_name)
        var internshipOrg: TextView = view.findViewById(R.id.internship_org)
        var internshipType: TextView = view.findViewById(R.id.internshipTypeDetails)
        var internshipStipend: TextView = view.findViewById(R.id.internshipStipendDetails)
        var internshipDuration: TextView = view.findViewById(R.id.internshipDurationDetails)
        var internshipView: View = view.findViewById(R.id.internship_layout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InternshipViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.internship_card, parent, false)
        return InternshipViewHolder(view)
    }

    override fun onBindViewHolder(holder: InternshipViewHolder, position: Int) {
        val currentInternship = list[position]
        holder.internshipName.text = currentInternship.title
        holder.internshipType.text = currentInternship.type
        holder.internshipStipend.text = "Stipend Free"
        holder.internshipDuration.text = currentInternship.duration
        holder.internshipOrg.text = "YEF"
        holder.internshipImage.setImageResource(R.drawable.yef_logo)
        holder.internshipView.setOnClickListener {
            val i = Intent(context, InternshipDetails::class.java)
            i.putExtra("internship_id", currentInternship.internshipId)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int = list.size

}