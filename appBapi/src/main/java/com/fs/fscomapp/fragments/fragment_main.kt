package com.fs.fscomapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.fs.fscomapp.MainActivity

import com.fs.fscomapp.R
import kotlinx.coroutines.*

object UserGlobal {
    var usuario: String? = null
    var userprior: Int? = -1

    class Prioridad {
        companion object {
            val PRIORIDAD_BAJA = 4
            val PRIORIDAD_MEDIA = 3
            val PRIORIDAD_ALTA = 2
            val ADMINISTRADOR = 1
        }
    }
}

class fragment_main : Fragment() {

    lateinit var v:View
    lateinit var spAddress : Spinner

    lateinit var butLogUsr: Button
    lateinit var txtUserName: TextView
    lateinit var butAdminLog: Button

    lateinit var butShowList: Button

    private val PREF_NAME = "myPreferences"

    var user : String = ""

    lateinit var btnDialog : Button

    companion object {
        fun newInstance() = fragment_main()
    }

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_main, container, false)
        //setHasOptionsMenu(true)
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
        (requireActivity() as MainActivity).supportActionBar!!.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menutoolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        user = sharedPref.getString("USER","default")!!
        Log.d("TAG",user)


    }


    private suspend fun task_procNextListComRQ ( curIndex:Int): Int {
        var newIndex: Int = 0
        //Log.d("Test", "fetchDataFromServerOne()")
        delay(1000)
        newIndex=curIndex+1
        return newIndex
    }

    private suspend fun task_procNewCom(indeCom:Int): Int {
        //Log.d("Test", "fetchDataFromServerTwo()")

        delay(1000)
        return 1
    }

    private suspend fun task_upDateCurComRQ(indeCom:Int): Int {
        //Log.d("Test", "fetchDataFromServerTwo()")

        delay(1000)
        return 1
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        butLogUsr=v.findViewById(R.id.but_log_in)
        txtUserName=v.findViewById(R.id.txt_user_name)
        butShowList=v.findViewById(R.id.but_listaPuntos)
        butAdminLog=v.findViewById(R.id.but_adm_log)

        setHasOptionsMenu(false)

        butLogUsr.setOnClickListener {
            val action =fragment_mainDirections.actionFragmentMainToFragmentLogging()
            v.findNavController().navigate(action)
        }
        butShowList.setOnClickListener {
            val action = fragment_mainDirections.actionFragmentMainToFragmentLogging()
            v.findNavController().navigate(action)
        }
        butAdminLog.setOnClickListener {
            val action = fragment_mainDirections.actionFragmentMainToFragmentAdmLogging()
            v.findNavController().navigate(action)
        }

        if ( UserGlobal.userprior!! >= 0 ) {
            txtUserName.visibility = View.VISIBLE
            val txtCat: String =   "Bienvenido "
            txtUserName.text = txtCat.plus(UserGlobal.usuario) //.plus(" Podemos Ingresar...")
            butShowList.visibility= Button.VISIBLE
            butLogUsr.visibility= Button.INVISIBLE

            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("USER", txtUserName.text as String)
            editor.apply()

        }
        else {
            butShowList.visibility= Button.INVISIBLE
        }

        butShowList.visibility= Button.VISIBLE //SACAR

        if (UserGlobal.userprior == UserGlobal.Prioridad.ADMINISTRADOR) {
            butAdminLog.visibility=Button.VISIBLE
        }
        else {
            butAdminLog.visibility=Button.INVISIBLE
        }

        butShowList.setOnClickListener {
            val action = fragment_mainDirections.actionFragmentMainToFragmentPuntos()
            v.findNavController().navigate(action)
        }


    }

}
