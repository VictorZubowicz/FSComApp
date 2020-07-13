package com.fs.fscomapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fs.fscomapp.MainActivity

import com.fs.fscomapp.R

class fragment_details_tab3 : Fragment() {
    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // IMPORTANTE INCREIBLE PARA QUE NO SUBA LA PANTALLA AL EDITAR HAY Q PONER ESTO
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_details_tab3, container, false)

        return v
    }
}



