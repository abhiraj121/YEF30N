package com.YEF.yefApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private var pageradapter: PageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editProfileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }

        backProfileBtn.setOnClickListener {
            finish()
        }

        pageradapter = PageAdapter(supportFragmentManager, tablayout.tabCount)
        viewpager!!.adapter = pageradapter
        viewpager.offscreenPageLimit = 2

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    pageradapter!!.notifyDataSetChanged()
                } else if (tab.position == 1) {
                    pageradapter!!.notifyDataSetChanged()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}

        })

        viewpager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tablayout))

        tablayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpager))
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.tab1 -> pageradapter!!.notifyDataSetChanged()
            R.id.tab2 -> pageradapter!!.notifyDataSetChanged()
        }
    }

}