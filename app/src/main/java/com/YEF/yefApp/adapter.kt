package com.YEF.yefApp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import java.security.AccessController.getContext
import java.util.*

class adapter( var activity: Activity, var items: ArrayList<WebinarResponse>): BaseAdapter() {
    private class ViewHolder(row: View?){
        var txtName: TextView? = null
        var txtDate: TextView? = null
        var poster: ImageView?=null
        init{
            this.txtName = row?.findViewById<TextView>(R.id.webinar_name)
            this.txtDate = row?.findViewById<TextView>(R.id.date)
            this.poster=row?.findViewById<ImageView>(R.id.pic)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if(convertView == null){
            val inflater =  activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.fragment_webinar_list_, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        }
        else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        var em = items[position]
        viewHolder.txtName?.text = em.name
        viewHolder.txtDate?.text = em.date
        Picasso.get().load(em.img).into(viewHolder.poster)
      //  viewHolder.poster?.setImageURI(Uri.parse(em.img))
        return view as View
    }
    override fun getItem(i: Int): WebinarResponse{
        return items[i]
    }
    override fun getItemId(i: Int): Long{
        return i.toLong()
    }
    override fun getCount(): Int{
        return items.size
    }
}