package com.fs.fscomapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.fs.fscomapp.R
import com.fs.fscomapp.dataBase.puntoComDatabase
import com.fs.fscomapp.dataBase.puntoComDao


class fragment_details_container : Fragment() {

    lateinit var v: View
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    private lateinit var viewModel_puntos: fragment_puntos_vm

    private var db: puntoComDatabase? = null
    private var daoPuntosCom: puntoComDao? = null

    private val titles =
        arrayOf("Panel", "Historial","Atributos")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //db = appDatabasePuntosCom.getAppDatabasePuntosCom(v.context)
        //daoPuntosCom = db?.puntosDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_details_container, container, false)

        tabLayout = v.findViewById(R.id.tab_layout)
        viewPager = v.findViewById(R.id.view_pager)


        return v
    }


    override fun onStart() {
        super.onStart()

        viewPager.setAdapter(createCardAdapter())
        // viewPager.isUserInputEnabled = false
        TabLayoutMediator(tabLayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> tab.text = "General"
                    1 -> tab.text = "Registros"
                    //2 -> tab.text = "Config"
                    else -> tab.text = "undefined"
                }
            }).attach()

    }

    private fun createCardAdapter(): ViewPagerAdapter? {
        return ViewPagerAdapter(requireActivity())
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {

            return when(position){
                0 -> fragment_details_tab1()
                1 -> fragment_details_tab2()
                //2 -> fragment_details_tab3()

                else -> fragment_details_tab1()
            }
        }

        override fun getItemCount(): Int {
            return TAB_COUNT
        }

        companion object {
            private const val TAB_COUNT = 2
        }
    }

}
