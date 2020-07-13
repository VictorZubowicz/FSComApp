package com.fs.fscomapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.fs.fscomapp.R
import com.fs.fscomapp.auxiliares.ObtenerDisplayValorSegunTipoDeDato
import com.fs.fscomapp.auxiliares.ObtenerIconoSegunTipoDato
import com.fs.fscomapp.auxiliares.tipoDatoClass
import com.fs.fscomapp.entities.PuntoCom


public class fragment_details_tab1 : Fragment() {
    lateinit var v : View
    lateinit var  butCerrar: Button
    lateinit var textNombre: TextView
    lateinit var textDescrip: TextView
    lateinit var textGrupo: TextView
    lateinit var textValor: TextView
    lateinit var imgItem: ImageView

    lateinit var imgValor: ImageView
    lateinit var progressBar: ProgressBar
    lateinit var seekBar_detail_sp: SeekBar

    var indexItem: Int=-1
    var valorItem: Int=-1

    //var list_puntos : MutableList<PuntoCom> = ArrayList<PuntoCom>()
    var itemPuntoCom : PuntoCom = PuntoCom (-1,"","","",-1,0,"",-1, "" )

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

        imgItem=v.findViewById(R.id.img_ic_Item_tab1)
        textNombre= v.findViewById(R.id.txtv_name_item_tab1)
        textDescrip =v.findViewById(R.id.txtv_descrip_item_tab1)
        textGrupo= v.findViewById(R.id.txtv_grupo_item_tab1)
        textValor =v.findViewById(R.id.txtv_valor_item_tab1)

        butCerrar =v.findViewById(R.id.butCerrar_tab1)

        imgValor=v.findViewById(R.id.img_valor_tab1)
        progressBar=v.findViewById(R.id.progressBar_tab1)
        seekBar_detail_sp = v.findViewById((R.id.seekBar_detail_sp))

        imgValor.visibility=ImageView.INVISIBLE
        seekBar_detail_sp.visibility=SeekBar.INVISIBLE
        progressBar.visibility = ProgressBar.INVISIBLE


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

        if (valorItem>0) imgItem.visibility=imgItem.visibility

        valorItem=vM_puntos.getValorListaPuntos(indexItem)

        textValor.setText(ObtenerDisplayValorSegunTipoDeDato (itemPuntoCom,valorItem))
        imgItem.setImageResource(ObtenerIconoSegunTipoDato(itemPuntoCom.tipoDato))

        val observerValoresVmPuntoCom = Observer<MutableList<Int>> {
            valorItem = it.get(indexItem)
         ActualizarValorPuntoCom(valorItem)    //ViewModel carga el Recycler View
        }


        vM_puntos.getValoresLiveData().observe(viewLifecycleOwner, observerValoresVmPuntoCom)




        butCerrar.setOnClickListener {

            val directions = fragment_details_containerDirections.actionFragmentDetailsContainerToFragmentPuntos()
            v.findNavController().navigate(directions)
        }

        seekBar_detail_sp.setProgress(valorItem)
        seekBar_detail_sp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            var valor: Int =0

            override fun onProgressChanged (seekBar: SeekBar, progress: Int, fromUser: Boolean)  {
                //Toast.makeText( context, "seekbar progress: $progress", Toast.LENGTH_SHORT
                //).show()
                valor = progress
                ActualizarValorPuntoCom(valor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                vM_puntos.setValorListaPuntos(indexItem,valor)
            }
        })

    }


    private fun ActualizarValorPuntoCom(valor: Int) {

        textValor.setText(ObtenerDisplayValorSegunTipoDeDato (itemPuntoCom,valor))
        when (itemPuntoCom.tipoDato) {
            tipoDatoClass.ConstTipoDato.typeDigitalInput -> {
                //seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalOuput -> {
                //seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalInputPower -> {
                //seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalOuputFan -> {
                //seekBarSet.visibility = SeekBar.INVISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeDigitalOuputCmp -> {
                //seekBarSet.visibility = SeekBar.INVISIBLE
            }
            //else -> switch_set_val.visibility= Switch.INVISIBLE

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
                imgValor.visibility=ImageView.VISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeAnalogInput, tipoDatoClass.ConstTipoDato.typeAnalogOutput -> {
                progressBar.visibility = ProgressBar.VISIBLE
            }
            tipoDatoClass.ConstTipoDato.typeSetpoint-> {
                seekBar_detail_sp.visibility = SeekBar.VISIBLE
            }
            else -> {
            }

        }
    }


}





