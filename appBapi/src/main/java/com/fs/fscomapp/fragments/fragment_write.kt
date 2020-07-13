package com.fs.fscomapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.fs.fscomapp.R
import com.fs.fscomapp.auxiliares.ObtenerDisplayValorSegunTipoDeDato
import com.fs.fscomapp.auxiliares.ObtenerIconoSegunTipoDato
import com.fs.fscomapp.auxiliares.tipoDatoClass
import com.fs.fscomapp.entities.PuntoCom

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_write.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_write : Fragment() {
    lateinit var v : View
    lateinit var  butCerrar: Button
    lateinit var textNombre: TextView
    lateinit var textDescrip: TextView
    lateinit var textGrupo: TextView
    lateinit var textValor: TextView
    lateinit var imgItem: ImageView

    lateinit var switch_set_val: Switch
    lateinit var switch_set_st: Switch
    lateinit var seekBarSet: SeekBar
    lateinit var imgEstado: ImageView

    var indexItem: Int=-1
    var valorItem: Int=-1

    //var list_puntos : MutableList<PuntoCom> = ArrayList<PuntoCom>()
    var itemPuntoCom : PuntoCom = PuntoCom (-1,"","","",0,1, "",-1, ""  )

    private lateinit var vM_puntos: fragment_puntos_vm  //tomo una instancia de puntos
//    private var puntoComDao: puntoComDao? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_details_tab1, container, false)
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        textNombre= v.findViewById(R.id.txtv_name_item_write)
        textValor =v.findViewById(R.id.txtv_valor_set_write)
        imgItem=v.findViewById(R.id.img_ic_Item_write)

        butCerrar =v.findViewById(R.id.butCerrar_write)

        switch_set_val= v.findViewById(R.id.switch_valor_set_write)
        switch_set_st= v.findViewById(R.id.switch_estado_set_write)
        seekBarSet=v.findViewById(R.id.seekBarSet_write)
        imgEstado=v.findViewById(R.id.img_valor_write)

        vM_puntos = ViewModelProvider(requireActivity()).get(fragment_puntos_vm::class.java)

        val observerDetaisPuntosCom = Observer<PuntoCom> {
            itemPuntoCom= it //Acualiza valor
            ActualizarDetallesPuntosCom(it)    //ViewModel carga el Recycler View
        }
        vM_puntos.getvmPuntoCom().observe(viewLifecycleOwner, observerDetaisPuntosCom)
        //Ya esta cargado el observador ahora cargo el PuntoCom con el val.r auxPunto puesto antes

        // se carga PuntoCom LIVEDATA
        indexItem=vM_puntos.vmindexPunto   //Tomo valor Index,
        itemPuntoCom = vM_puntos.getPunto(indexItem)    //Tomo VM data

        //carga view tab1 segun valores
        ActualizarDetallesPuntosCom(itemPuntoCom)   //actualiza fragment con valor de PuntoCom

        if (valorItem>0) switch_set_val.isChecked=true else switch_set_val.isChecked=false
        if (valorItem>0) imgItem.visibility=imgItem.visibility

        valorItem=vM_puntos.getValorListaPuntos(indexItem)

        textValor.setText(ObtenerDisplayValorSegunTipoDeDato (itemPuntoCom,valorItem))
        imgItem.setImageResource(ObtenerIconoSegunTipoDato(itemPuntoCom.tipoDato))

        val observerValoresVmPuntoCom = Observer<MutableList<Int>> {
            valorItem = it.get(indexItem)
            ActualizarValorPuntoCom(valorItem)    //ViewModel carga el Recycler View
        }


        vM_puntos.getValoresLiveData().observe(viewLifecycleOwner, observerValoresVmPuntoCom)


        switch_set_val.setOnCheckedChangeListener { buttonView, isChecked ->

            val valSwitch:Int = if (isChecked==true) 1 else 0
            vM_puntos.setValorListaPuntos(indexItem,valSwitch)
            ActualizarValorPuntoCom(valorItem)  //no deberia hacer falta

            if (isChecked==true) imgEstado.setImageResource(R.drawable.ic_power)
            else imgEstado.visibility= ImageView.INVISIBLE
        }

        switch_set_st.setOnCheckedChangeListener { buttonView, isChecked ->

            val valSwitch:Int = if (isChecked==true) 1 else 0
            vM_puntos.setValorListaPuntos(indexItem,valSwitch)
            ActualizarValorPuntoCom(valorItem)  //no deberia hacer falta

            if (isChecked==true) imgEstado.setImageResource(R.drawable.ic_power)
            else imgEstado.visibility= ImageView.INVISIBLE
        }

        seekBarSet.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            var valor: Int =0

            override fun onProgressChanged (seekBar: SeekBar, progress: Int, fromUser: Boolean)  {
                Toast.makeText( context, "seekbar progress: $progress", Toast.LENGTH_SHORT
                ).show()
                valor = progress
                ActualizarValorPuntoCom(valor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                vM_puntos.setValorListaPuntos(indexItem,valor)
            }
        })


        butCerrar.setOnClickListener {

            val directions = fragment_details_containerDirections.actionFragmentDetailsContainerToFragmentPuntos()
            v.findNavController().navigate(directions)
        }

    }


    private fun ActualizarValorPuntoCom(valor: Int) {

        textValor.setText(ObtenerDisplayValorSegunTipoDeDato (itemPuntoCom,valor))
        when (itemPuntoCom.tipoDato) {
            tipoDatoClass.ConstTipoDato.typeDigitalInput -> {
                seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalOuput -> {
                seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalInputPower -> {
                seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalOuputFan -> {
                seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalOuputCmp -> {
                seekBarSet.visibility = SeekBar.INVISIBLE
            }
            else -> switch_set_val.visibility= Switch.INVISIBLE

        }
    }



    private fun ActualizarDetallesPuntosCom(it: PuntoCom?) {
        textNombre.setText(it?.nombre)
        textDescrip.setText(it?.descripcion)
        textGrupo.setText(it?.grupo.toString())

        when (it?.tipoDato) {
            tipoDatoClass.ConstTipoDato.typeDigitalInput, tipoDatoClass.ConstTipoDato.typeDigitalOuput,
            tipoDatoClass.ConstTipoDato.typeDigitalInputPower, tipoDatoClass.ConstTipoDato.typeDigitalOuputFan,
            tipoDatoClass.ConstTipoDato.typeDigitalOuputCmp -> {
                switch_set_val.visibility=Switch.VISIBLE
                seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeAnalogInput, tipoDatoClass.ConstTipoDato.typeAnalogOutput -> {
                seekBarSet.visibility = SeekBar.VISIBLE
                switch_set_val.visibility=Switch.INVISIBLE
            }
            else -> {
                seekBarSet.visibility = Switch.INVISIBLE
            }

        }
    }


}
