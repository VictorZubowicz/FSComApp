package com.fs.fscomapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.fs.fscomapp.R
import com.fs.fscomapp.dataBase.puntoComDao
import com.fs.fscomapp.entities.PuntoCom

class fragment_details_tab2 : Fragment() {
    lateinit var v : View

    lateinit var txtNombre: TextView
    lateinit var txtDescrip: TextView
    lateinit var txtGrupo: TextView
    lateinit var txtTipoDato: TextView
    lateinit var txtFormato: TextView
    lateinit var txtDirmem: TextView
    lateinit var txtUnidad: TextView
    lateinit var txtValorIni: TextView
    lateinit var butEditItem: Button //butEditar_tab2
    lateinit var puntoCom: PuntoCom


    private var puntoComDao: puntoComDao? = null
    private lateinit var viewModel_puntos: fragment_puntos_vm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_details_tab2, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        butEditItem =v.findViewById(R.id.butCerrar_tab1)

        txtNombre =v.findViewById(R.id.txtv_nombre_tab2)
        txtDescrip =v.findViewById(R.id.txtv_descrip_tab2)
        txtUnidad =v.findViewById(R.id.txtv_unidad_tab2)
        txtValorIni =v.findViewById(R.id.txtv_valorIni_tab2)
        txtGrupo =v.findViewById(R.id.txtv_grupo_tab2)
        txtTipoDato =v.findViewById(R.id.txtv_tipodato_tab2)
        txtFormato =v.findViewById(R.id.txtv_formato_tab2)
        txtDirmem =v.findViewById(R.id.txtv_dirmem_tab2)

        //Tomo ViewModel (Memoria compartiza)
        viewModel_puntos = ViewModelProvider(requireActivity()).get(fragment_puntos_vm::class.java)
        if (viewModel_puntos.getListPuntos().isNullOrEmpty() == false) {
            puntoCom = viewModel_puntos.getPunto(viewModel_puntos.vmindexPunto)    //Tomo VM data
        }

        txtNombre.setText(puntoCom.nombre)
        txtDescrip.setText(puntoCom.descripcion)
        txtUnidad.setText(puntoCom.unidad.toString())
        txtValorIni.setText(puntoCom.valorIni.toString())
        txtGrupo.setText(puntoCom.grupo.toString())
        txtTipoDato.setText(puntoCom.tipoDato)
        txtFormato.setText(puntoCom.formato)
        txtDirmem.setText(puntoCom.dirmem.toString())

        butEditItem.setOnClickListener {

            val directions = fragment_details_containerDirections.actionFragmentDetailsContainerToFragmentEdicionPunto()
            v.findNavController().navigate(directions)
        }

    }

}



