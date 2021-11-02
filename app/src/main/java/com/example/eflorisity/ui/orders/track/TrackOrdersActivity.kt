package com.example.eflorisity.ui.orders.track

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.eflorisity.R
import com.example.eflorisity.ui.orders.track.fragments.*
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TrackOrdersActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private var toTrack:Int = 0
    private lateinit var tlTrack:TabLayout
    private lateinit var vp2Track:ViewPager2

    private lateinit var adaptor:ViewPagerAdapter

    private lateinit var toReceiveFragment: ToReceiveFragment
    private lateinit var toShipFragment:ToShipFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_orders)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = "My Orders"

        tlTrack = findViewById(R.id.tl_track_id)
        vp2Track = findViewById(R.id.vp2_track_id)
        adaptor = ViewPagerAdapter(supportFragmentManager,lifecycle)

        toReceiveFragment = ToReceiveFragment()
        toShipFragment = ToShipFragment()

        adaptor.addFragment(ToPayFragment(),tlTrack.getTabAt(0)!!.text.toString())
        adaptor.addFragment(ToShipFragment(),tlTrack.getTabAt(1)!!.text.toString())
        adaptor.addFragment(ToReceiveFragment(),tlTrack.getTabAt(2)!!.text.toString())
        adaptor.addFragment(ToReviewFragment(),tlTrack.getTabAt(3)!!.text.toString())
        adaptor.addFragment(CancelledFragment(),tlTrack.getTabAt(4)!!.text.toString())
        adaptor.addFragment(ReturnRefundFragment(),tlTrack.getTabAt(5)!!.text.toString())
        vp2Track.adapter = adaptor


        TabLayoutMediator(tlTrack, vp2Track) { tab, position ->
            tab.text = adaptor.getPageTitle(position)
        }.attach()
        toTrack = intent.getIntExtra("toTrack",0)
        tlTrack.getTabAt(toTrack)!!.select()

        tlTrack.addOnTabSelectedListener(this)
    }



    class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle:Lifecycle):FragmentStateAdapter(fragmentManager,lifecycle){
        var fragmentList:ArrayList<Fragment>  = ArrayList()
        var fragmentTitle:ArrayList<String> = ArrayList()

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        fun getPageTitle(position: Int):CharSequence?{
            return fragmentTitle[position]
        }

        fun addFragment(fragment:Fragment,title:String){
            fragmentList.add(fragment)
            fragmentTitle.add(title)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val pos = tab!!.position
        tabSelected(pos)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        val pos = tab!!.position
        tabSelected(pos)
    }

    fun tabSelected(position:Int){
        if (position == 1){
//            toShipFragment.getData()
        }
    }
}